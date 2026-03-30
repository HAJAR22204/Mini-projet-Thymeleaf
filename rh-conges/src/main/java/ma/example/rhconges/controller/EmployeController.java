package ma.example.rhconges.controller;

import jakarta.validation.Valid;
import ma.example.rhconges.entity.Employe;
import ma.example.rhconges.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmployeController {

    @Autowired
    private EmployeRepository employeRepository;

    @GetMapping("/personnel")
    public String listerAgents(Model model) {
        model.addAttribute("agents", employeRepository.findAll());
        return "personnel";
    }

    @GetMapping("/personnel/nouveau")
    public String formulaireAjout(Employe employe) {
        return "ajouter-employe";
    }

    @PostMapping("/personnel/enregistrer")
    public String enregistrerAgent(@Valid Employe employe, BindingResult result) {
        if (result.hasErrors()) return "ajouter-employe";
        employeRepository.save(employe);
        return "redirect:/personnel";
    }

    @GetMapping("/personnel/modifier/{id}")
    public String formulaireModif(@PathVariable Long id, Model model) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agent introuvable : " + id));
        model.addAttribute("employe", employe);
        return "modifier-employe";
    }

    @PostMapping("/personnel/modifier/{id}")
    public String mettreAJourAgent(@PathVariable Long id, @Valid Employe employe,
                                   BindingResult result) {
        if (result.hasErrors()) { employe.setId(id); return "modifier-employe"; }
        employeRepository.save(employe);
        return "redirect:/personnel";
    }

    @GetMapping("/personnel/supprimer/{id}")
    public String supprimerAgent(@PathVariable Long id) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agent introuvable : " + id));
        employeRepository.delete(employe);
        return "redirect:/personnel";
    }
}