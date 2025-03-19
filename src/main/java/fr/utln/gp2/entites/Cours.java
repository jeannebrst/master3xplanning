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

    @ManyToMany(mappedBy = "cours")
    private List<Promotion> promos;
}
