package ma.example.rhconges.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
public class DemandeConge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date de début est requise")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est requise")
    private LocalDate dateFin;

    // SOUMIS | VALIDE | REJETE
    private String statut = "SOUMIS";

    private String motif;

    @ManyToOne
    @NotNull(message = "Veuillez sélectionner un agent")
    private Employe employe;

    @ManyToOne
    @NotNull(message = "Veuillez sélectionner un type de congé")
    private TypeConge typeConge;

    public DemandeConge() {}

    public long getNombreJours() {
        if (dateDebut != null && dateFin != null)
            return ChronoUnit.DAYS.between(dateDebut, dateFin) + 1;
        return 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }
    public TypeConge getTypeConge() { return typeConge; }
    public void setTypeConge(TypeConge typeConge) { this.typeConge = typeConge; }
}