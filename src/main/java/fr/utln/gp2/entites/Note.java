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
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notes_seq")
    @SequenceGenerator(name = "notes_seq", sequenceName = "notes_id_seq", allocationSize = 10)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(hidden = true)
    private Long noteId;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    @JsonIgnoreProperties({"promos", "ues", "notes", "hashMdp", "nom", "prenom", "mail", "role"})
    private Personne etudiant;

    @ManyToOne
    @JsonIgnoreProperties({"notes", "cours","ueId", "intervenantsLogin", "responsableLogin", "nbHeures"})
    private UE ue;

    private float note;

    private Date date;

    public Note(Personne personne, UE ue, float note, Date date) {
        this.etudiant = personne;
        this.ue = ue;
        this.note = note;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteId=" + noteId +
                ", etudiant=" + etudiant +
                ", ue=" + ue.getNom() +
                ", note=" + note +
                ", date=" + date +
                '}';
    }
}
