package fr.utln.gp2.utils;

import java.util.List;
import java.util.stream.Collectors;

import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Personne.Role;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.entites.Salle;
import fr.utln.gp2.entites.UE;

public class DonneesImportees {
    List<Promotion> promotions;
    List<Salle> salles;
    List<Personne> personnes;
    List<UE> ues;
    List<CoursJson> cours;

    
    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }
    public void setSalles(List<Salle> salles) {
        this.salles = salles;
    }
    public void setPersonnes(List<Personne> personnes) {
        this.personnes = personnes;
    }
    public void setUes(List<UE> ues) {
        this.ues = ues;
    }
    public void setCours(List<CoursJson> cours) {
        this.cours = cours;
    }
    public List<Promotion> getPromotions() {
        return promotions;
    }
    public List<Salle> getSalles() {
        return salles;
    }
    public List<Personne> getPersonnes() {
        return personnes;
    }
    public List<UE> getUes() {
        return ues;
    }
    public List<CoursJson> getCours() {
        return cours;
    }

    public List<Personne> getProfs() {
        return personnes.stream()
                .filter(p -> p.getRole() == Role.PROFESSEUR)
                .collect(Collectors.toList());
    }

   
    public List<Personne> getEtudiants() {
        return personnes.stream()
                .filter(p -> p.getRole() == Role.ETUDIANT | p.getRole()==Role.GESTIONNAIRE)
                .collect(Collectors.toList());
   
    
}
}