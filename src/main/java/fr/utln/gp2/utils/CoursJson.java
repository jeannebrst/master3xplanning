package fr.utln.gp2.utils;

import java.util.List;

public class CoursJson {
    private String ue;
    private List<String> promotions;
    private String intervenantLogin;
    private int heureDebut;
    private int duree;
    private String jour;
    private String type;
    private String salle;

    // Getters et setters
    public String getUe() {
        return ue;
    }

    public void setUe(String ue) {
        this.ue = ue;
    }

    public List<String> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<String> promotions) {
        this.promotions = promotions;
    }

    public String getIntervenantLogin() {
        return intervenantLogin;
    }

    public void setIntervenantLogin(String intervenantLogin) {
        this.intervenantLogin = intervenantLogin;
    }

    public int getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(int heureDebut) {
        this.heureDebut = heureDebut;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }
}