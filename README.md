# Mini Projet Spring Boot — Système de Gestion des Congés

## Contexte

Ce mini projet a été réalisé dans le cadre du module Web/JEE. Il s'inspire du TP de gestion des utilisateurs (CRUD simple avec Spring Boot et Thymeleaf) et applique les mêmes concepts à un thème métier plus riche : la gestion des demandes de congés du personnel.

---

## Technologies utilisées

- Java 25 / Spring Boot 3.5.13
- Spring Data JPA / Hibernate 6.6
- Thymeleaf 3.1
- MySQL (via phpMyAdmin)
- Maven
- IntelliJ IDEA

---

## Architecture du projet

Le projet suit une architecture en couches classique MVC :

- entity : modélisation des données (Employe, TypeConge, DemandeConge)
- repository : accès aux données via JpaRepository
- controller : logique métier et routage HTTP
- templates : vues HTML rendues côté serveur avec Thymeleaf

Package racine : ma.example.rhconges
Base de données : rh_conges (MySQL)

---

## Modèle de données

Trois entités liées entre elles :

<img width="1444" height="585" alt="image" src="https://github.com/user-attachments/assets/9fce6597-21b8-4e8a-a094-af6e255405aa" />

Employe (id, nom, departement, dateEmbauche)
TypeConge (id, libelle, quotaAnnuel)
DemandeConge (id, dateDebut, dateFin, statut, motif, employe_id, type_conge_id)

Les relations sont de type ManyToOne : plusieurs demandes peuvent référencer le même employé et le même type de congé. Hibernate génère automatiquement les clés étrangères et les tables au démarrage grâce à ddl-auto=update.

<img width="961" height="638" alt="image" src="https://github.com/user-attachments/assets/89919abf-2e75-497b-8041-81b1b154603b" />


---

## Fonctionnalités réalisées

CRUD complet sur trois entités :
- Gestion des agents (ajout, modification, suppression)
- Gestion du catalogue des types de congé avec quota annuel
- Soumission et gestion des dossiers de congé

Workflow de validation :
- Toute demande est créée avec le statut SOUMIS
- Un responsable peut la passer à VALIDE ou REJETE via un simple lien
- Le formulaire de modification permet aussi de changer le statut manuellement

Filtrage des dossiers :
- Par statut (SOUMIS / VALIDE / REJETE)
- Par service (département de l'employé)
- Par type de congé (liste déroulante)

Statistiques :
- Nombre total de dossiers, validés, rejetés, en cours
- Taux de validation calculé dynamiquement
- Jours d'absence consommés par service (uniquement sur les dossiers VALIDE)

---

## Résultats obtenus

Après insertion des données de test, l'application affiche correctement :

Page principale (localhost:8080) :

<img width="1920" height="1080" alt="Capture d’écran (1729)" src="https://github.com/user-attachments/assets/085d0e21-be27-494a-a3dd-d51f736bb18c" />

- 8 dossiers listés avec toutes les colonnes (agent, service, type, dates, durée calculée, motif, statut)
- Les dossiers SOUMIS affichent les boutons Valider et Rejeter
- Les dossiers VALIDE et REJETE affichent uniquement Modifier et Supprimer
- La durée en jours est calculée automatiquement côté Java (ChronoUnit.DAYS)

Page Personnel (localhost:8080/personnel) :

<img width="1920" height="1080" alt="Capture d’écran (1730)" src="https://github.com/user-attachments/assets/d74375c3-78e4-4308-a971-338a1354e711" />

- 5 agents enregistrés avec leur service et date d'entrée en poste
- CRUD fonctionnel avec validation des champs obligatoires

Page Catalogue (localhost:8080/catalogue) :

<img width="1920" height="1080" alt="Capture d’écran (1731)" src="https://github.com/user-attachments/assets/7ae35376-f0b2-4515-85cd-4ea6080a4252" />

- 5 types de congé avec leur quota annuel respectif
- CRUD fonctionnel

Page Statistiques (localhost:8080/statistiques) :

<img width="1920" height="1080" alt="Capture d’écran (1732)" src="https://github.com/user-attachments/assets/528c1267-63a0-40b4-b559-0d51f284d960" />

- Total : 8 dossiers
- Validés : 3, Rejetés : 2, En cours : 3
- Taux de validation : 38%
- Jours par service : Ressources Humaines (6j), Direction (3j), Comptabilité (3j)

---

## Points notables

- La durée des congés est calculée dynamiquement via une méthode getNombreJours() dans l'entité, sans colonne supplémentaire en base
- Le filtrage est géré par des méthodes dérivées dans le repository (findByStatut, findByEmployeDepartement, findByTypeConge) sans écrire de requêtes SQL manuelles
- La validation des formulaires utilise les annotations jakarta.validation (@NotBlank, @NotNull, @Min) avec affichage des erreurs directement dans les vues Thymeleaf
- Les statuts sont gérés comme des chaînes simples (SOUMIS, VALIDE, REJETE) ce qui simplifie le filtrage et l'affichage

---
