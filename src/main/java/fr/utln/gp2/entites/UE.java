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
public class UE {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ue_seq")
    @SequenceGenerator(name = "ue_seq", sequenceName = "ue_id_seq", allocationSize = 10)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(hidden = true)
    private Long ue_id;

    private String nom;

    @ManyToMany(mappedBy = "ue", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Personne> intervenants;

    @OneToMany(mappedBy = "ue", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Notes> notes;

    @OneToMany(mappedBy = "ue", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Cours> cours;

    @OneToOne
    private Personne responsable;

    private int nbHeures;



}
