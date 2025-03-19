package fr.utln.gp2.entites;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
public class Promotion {
    @Id
    private String nom;

    @ManyToMany
    private List<Cours> cours;

    @OneToOne
    @JoinColumn(name = "responsable_id")
    private Personne responsable;
}
