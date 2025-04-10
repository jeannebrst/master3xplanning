package fr.utln.gp2.entites;

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
public class Salle {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salle_seq")
	@SequenceGenerator(name = "salle_seq", sequenceName = "salle_id_seq", allocationSize = 10)
	@Schema(hidden = true)
	@Column(name = "salle_id")
	private Long salleId;

	private String nom;

	@OneToMany(mappedBy = "salle", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@Builder.Default
	@JsonIgnoreProperties({"salle"})
	private List<Cours> cours = new ArrayList<>();

	private int capacite;

	private String description;

	public Salle(String nom, List<Cours> cours, int capacite, String description) {
		this.nom = nom;
		if(cours!=null){
			this.cours = cours;
		}
		this.capacite = capacite;
		this.description = description;
	}

	@Override
	public String toString() {
		List<Long> coursIds = new ArrayList<>();
		for (Cours c : cours){
			coursIds.add(c.getCoursId());
		}

		return "Salle [nom=" + nom + ", cours=" + coursIds + ", capacite=" + capacite
				+ ", description=" + description + "]";
	}

	
}