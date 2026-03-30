package ma.example.rhconges.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class TypeConge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "L'intitulé du type de congé est requis")
    private String libelle;

    @NotNull(message = "Le quota annuel est requis")
    @Min(value = 1, message = "Le quota doit être au moins égal à 1 jour")
    private Integer quotaAnnuel;

    public TypeConge() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public Integer getQuotaAnnuel() { return quotaAnnuel; }
    public void setQuotaAnnuel(Integer quotaAnnuel) { this.quotaAnnuel = quotaAnnuel; }
}