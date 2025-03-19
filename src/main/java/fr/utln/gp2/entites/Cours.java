package fr.utln.gp2.entites;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Cours {
    @Id
    private UUID id;

    //private UE ue;

    @ManyToMany(mappedBy = "cours")
    private List<Promotion> promos;

    @ManyToMany
    private List<Personne> intervenants;

    //private Salle salle;

    private float heureDebut;

    private float duree;

    private Date jour;

    private enum Type {
        CM,
        TD,
        TP
    }
    private Type type;
}
