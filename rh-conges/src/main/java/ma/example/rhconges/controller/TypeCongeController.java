package ma.example.rhconges.controller;

import jakarta.validation.Valid;
import ma.example.rhconges.entity.TypeConge;
import ma.example.rhconges.repository.TypeCongeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class TypeCongeController {

    @Autowired
    private TypeCongeRepository typeCongeRepository;

    @GetMapping("/catalogue")
    public String listerTypes(Model model) {
        model.addAttribute("catalogue", typeCongeRepository.findAll());
        return "catalogue";
    }

    @GetMapping("/catalogue/nouveau")
    public String formulaireAjout(TypeConge typeConge) {
        return "ajouter-type";
    }

    @PostMapping("/catalogue/enregistrer")
    public String enregistrerType(@Valid TypeConge typeConge, BindingResult result) {
        if (result.hasErrors()) return "ajouter-type";
        typeCongeRepository.save(typeConge);
        return "redirect:/catalogue";
    }

    @GetMapping("/catalogue/modifier/{id}")
    public String formulaireModif(@PathVariable Long id, Model model) {
        TypeConge type = typeCongeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Type introuvable : " + id));
        model.addAttribute("typeConge", type);
        return "modifier-type";
    }

    @PostMapping("/catalogue/modifier/{id}")
    public String mettreAJourType(@PathVariable Long id, @Valid TypeConge typeConge,
                                  BindingResult result) {
        if (result.hasErrors()) { typeConge.setId(id); return "modifier-type"; }
        typeCongeRepository.save(typeConge);
        return "redirect:/catalogue";
    }

    @GetMapping("/catalogue/supprimer/{id}")
    public String supprimerType(@PathVariable Long id) {
        TypeConge type = typeCongeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Type introuvable : " + id));
        typeCongeRepository.delete(type);
        return "redirect:/catalogue";
    }
}