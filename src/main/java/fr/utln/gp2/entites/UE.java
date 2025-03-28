package fr.utln.gp2.entites;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Builder
public class UE {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ue_seq")
    @SequenceGenerator(name = "ue_seq", sequenceName = "ue_id_seq", allocationSize = 10)
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    @Schema(hidden = true)
    @Column(name = "ue_id")
    private Long ueId;

    private String nom;

    @OneToMany(mappedBy = "ues", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Notes> notes = new ArrayList<>();

    @OneToMany(mappedBy = "ues", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    @JsonIgnoreProperties({"ues"})
    @JsonBackReference
    private List<Cours> cours = new ArrayList<>();

    private String responsableLogin;

    private int nbHeures;

    @Override
    public String toString() {
        return "UE{" +
                "ueId=" + ueId +
                ", nom='" + nom + '\'' +
                ", notes=" + notes +
                ", cours=" + cours +
                ", responsableLogin='" + responsableLogin + '\'' +
                ", nbHeures=" + nbHeures +
                '}';
    }
}
