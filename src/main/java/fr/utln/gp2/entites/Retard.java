package fr.utln.gp2.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Retard {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "retards_seq")
    @SequenceGenerator(name = "retards_seq", sequenceName = "retards_id_seq", allocationSize = 10)
    @Schema(hidden = true)
    private Long retardId;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    @JsonIgnoreProperties({"promos", "ues", "notes", "hashMdp", "nom", "prenom", "mail", "role", "absences", "retards"})
    private Personne etudiant;

    @ManyToOne
    @JsonIgnoreProperties({"notes", "cours","ueId", "intervenantsLogin", "responsableLogin", "nbHeures", "promos", "ue", "intervenantLogin", "absences", "salle", "jour"})
    private Cours cours;

    private Date dateRetard;
    private int dureeRetard;

    public Retard(Personne personne, Cours cours, int duree) {
        this.etudiant = personne;
        this.cours = cours;
        this.dureeRetard = duree;
        this.dateRetard = cours.getJour();
    }

    @Override
    public String toString() {
        return "Retard{" +
                "retardId=" + retardId +
                ", etudiant=" + etudiant.getLogin() +
                ", cours=" + cours.getUe().getNom() +
                ", dateRetard=" + dateRetard +
                ", dureeRetard" + dureeRetard +
                '}';
    }
}