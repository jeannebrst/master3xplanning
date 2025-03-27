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
public class Salle {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salle_seq")
    @SequenceGenerator(name = "salle_seq", sequenceName = "salle_id_seq", allocationSize = 10)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(hidden = true)
    private Long salle_id;

    private String nom;

    @OneToMany(mappedBy = "salle", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Cours> cours;

    private int capacite;

    private String description;
}
