package fr.utln.gp2.utils;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class PromotionId implements Serializable {
	public enum Type {
		LICENCE,
		MASTER,
		IUT,
		LICENCE_PROFESSIONNELLE,
		DOCTORAT;

		public static Type fromString(String str) {
            for (Type t : Type.values()) {
                if (t.name().equalsIgnoreCase(str)) {
                    return t;
                }
            }
            throw new IllegalArgumentException("Type inconnu : " + str);
        }
	}
	private Type type;

	private int annee;

	private String categorie;

	public PromotionId(Type type, int annee, String categorie) {
		this.type = type;
		this.annee = annee;
		this.categorie = categorie;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PromotionId that)) return false;
		return annee == that.annee && type == that.type && Objects.equals(categorie, that.categorie);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, annee, categorie);
	}

	@Override
	public String toString() {
		return "" + type + annee + categorie;
	}
}
