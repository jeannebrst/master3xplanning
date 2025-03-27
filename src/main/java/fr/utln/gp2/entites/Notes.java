package fr.utln.gp2.entites;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notes_seq")
    @SequenceGenerator(name = "notes_seq", sequenceName = "notes_id_seq", allocationSize = 10)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(hidden = true)
    private Long notes_id;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Personne etudiant;

    @ManyToOne
    private UE ues;

    private float note;

    private Date date;
}
