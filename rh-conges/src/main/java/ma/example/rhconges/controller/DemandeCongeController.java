package ma.example.rhconges.controller;

import jakarta.validation.Valid;
import ma.example.rhconges.entity.DemandeConge;
import ma.example.rhconges.entity.TypeConge;
import ma.example.rhconges.repository.DemandeCongeRepository;
import ma.example.rhconges.repository.EmployeRepository;
import ma.example.rhconges.repository.TypeCongeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DemandeCongeController {

    @Autowired
    private DemandeCongeRepository demandeRepository;
    @Autowired
    private EmployeRepository employeRepository;
    @Autowired
    private TypeCongeRepository typeCongeRepository;

    // ── LISTE + FILTRES
    @GetMapping({"/", "/dossiers"})
    public String listerDossiers(
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) String service,
            @RequestParam(required = false) Long typeId,
            Model model) {

        List<DemandeConge> dossiers;

        if (statut != null && !statut.isEmpty()) {
            dossiers = demandeRepository.findByStatut(statut);
        } else if (service != null && !service.isEmpty()) {
            dossiers = demandeRepository.findByEmployeDepartement(service);
        } else if (typeId != null) {
            TypeConge type = typeCongeRepository.findById(typeId).orElse(null);
            dossiers = (type != null)
                    ? demandeRepository.findByTypeConge(type)
                    : demandeRepository.findAll();
        } else {
            dossiers = demandeRepository.findAll();
        }

        model.addAttribute("dossiers", dossiers);
        model.addAttribute("catalogue", typeCongeRepository.findAll());
        model.addAttribute("statutActif", statut);
        model.addAttribute("serviceActif", service);
        model.addAttribute("typeActif", typeId);

        return "dossiers";
    }

    // ── SOUMETTRE .
    @GetMapping("/dossiers/nouveau")
    public String formulaireSoumission(Model model) {
        model.addAttribute("demandeConge", new DemandeConge());
        model.addAttribute("agents", employeRepository.findAll());
        model.addAttribute("catalogue", typeCongeRepository.findAll());
        return "soumettre-conge";
    }

    @PostMapping("/dossiers/soumettre")
    public String soumettreDemande(@Valid DemandeConge demandeConge,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("agents", employeRepository.findAll());
            model.addAttribute("catalogue", typeCongeRepository.findAll());
            return "soumettre-conge";
        }
        demandeConge.setStatut("SOUMIS");
        demandeRepository.save(demandeConge);
        return "redirect:/dossiers";
    }

    // ── MODIFIER 
    @GetMapping("/dossiers/modifier/{id}")
    public String formulaireModif(@PathVariable Long id, Model model) {
        DemandeConge demande = demandeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dossier introuvable : " + id));
        model.addAttribute("demandeConge", demande);
        model.addAttribute("agents", employeRepository.findAll());
        model.addAttribute("catalogue", typeCongeRepository.findAll());
        return "modifier-conge";
    }

    @PostMapping("/dossiers/modifier/{id}")
    public String modifierDemande(@PathVariable Long id,
                                  @Valid DemandeConge demandeConge,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            demandeConge.setId(id);
            model.addAttribute("agents", employeRepository.findAll());
            model.addAttribute("catalogue", typeCongeRepository.findAll());
            return "modifier-conge";
        }
        demandeRepository.save(demandeConge);
        return "redirect:/dossiers";
    }

    // ── SUPPRIMER .
    @GetMapping("/dossiers/supprimer/{id}")
    public String supprimerDemande(@PathVariable Long id) {
        DemandeConge demande = demandeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dossier introuvable : " + id));
        demandeRepository.delete(demande);
        return "redirect:/dossiers";
    }

    // ── VALIDER / REJETER
    @GetMapping("/dossiers/valider/{id}")
    public String validerDemande(@PathVariable Long id) {
        DemandeConge demande = demandeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dossier introuvable : " + id));
        demande.setStatut("VALIDE");
        demandeRepository.save(demande);
        return "redirect:/dossiers";
    }

    @GetMapping("/dossiers/rejeter/{id}")
    public String rejeterDemande(@PathVariable Long id) {
        DemandeConge demande = demandeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dossier introuvable : " + id));
        demande.setStatut("REJETE");
        demandeRepository.save(demande);
        return "redirect:/dossiers";
    }

    //  STATISTIQUES 
    @GetMapping("/statistiques")
    public String afficherStats(Model model) {
        List<DemandeConge> validees = demandeRepository.findByStatut("VALIDE");

        Map<String, Long> absencesParService = validees.stream()
                .collect(Collectors.groupingBy(
                        d -> d.getEmploye().getDepartement(),
                        LinkedHashMap::new,
                        Collectors.summingLong(DemandeConge::getNombreJours)
                ));

        long total      = demandeRepository.count();
        long nbValides  = demandeRepository.countByStatut("VALIDE");
        long nbRejetes  = demandeRepository.countByStatut("REJETE");
        long nbSoumis   = demandeRepository.countByStatut("SOUMIS");
        long taux       = total > 0 ? Math.round((double) nbValides / total * 100) : 0;

        model.addAttribute("absencesParService", absencesParService);
        model.addAttribute("total", total);
        model.addAttribute("nbValides", nbValides);
        model.addAttribute("nbRejetes", nbRejetes);
        model.addAttribute("nbSoumis", nbSoumis);
        model.addAttribute("taux", taux);

        return "statistiques";
    }
}