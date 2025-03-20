package fr.utln.gp2.utils;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class PromotionId implements Serializable {
    private enum Type {
        LICENCE,
        MASTER,
        IUT,
        LICENCE_PROFESSIONNELLE,
        DOCTORAT
    }
    private Type type;

    private int annee;

    private String categorie;
}
