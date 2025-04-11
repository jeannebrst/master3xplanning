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
public class Absence {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "absences_seq")
    @SequenceGenerator(name = "absences_seq", sequenceName = "absences_id_seq", allocationSize = 10)
    @Schema(hidden = true)
    private Long absenceId;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    @JsonIgnoreProperties({"promos", "ues", "notes", "hashMdp", "nom", "prenom", "mail", "role", "absences"})
    private Personne etudiant;

    @ManyToOne
    @JsonIgnoreProperties({"notes", "cours","ueId", "intervenantsLogin", "responsableLogin", "nbHeures", "promos", "ue", "intervenantLogin", "absences", "salle", "jour"})
    private Cours cours;

    private Date dateAbsence;

    public Absence(Personne personne, Cours cours) {
        this.etudiant = personne;
        this.cours = cours;
        this.dateAbsence = cours.getJour();
    }

    @Override
    public String toString() {
        return "Absence{" +
                "absenceId=" + absenceId +
                ", etudiant=" + etudiant.getLogin() +
                ", cours=" + cours.getUe().getNom() +
                ", dateAbsence=" + dateAbsence +
                '}';
    }
}