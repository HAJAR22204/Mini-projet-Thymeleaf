package ma.example.rhconges.repository;

import ma.example.rhconges.entity.DemandeConge;
import ma.example.rhconges.entity.Employe;
import ma.example.rhconges.entity.TypeConge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeCongeRepository extends JpaRepository<DemandeConge, Long> {
    List<DemandeConge> findByStatut(String statut);
    List<DemandeConge> findByEmployeDepartement(String departement);
    List<DemandeConge> findByTypeConge(TypeConge typeConge);
    List<DemandeConge> findByEmploye(Employe employe);
    long countByStatut(String statut);
}