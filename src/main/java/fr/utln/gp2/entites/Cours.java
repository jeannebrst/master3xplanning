package fr.utln.gp2.entites;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.utln.gp2.utils.PromotionId;
import jakarta.persistence.*;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Cours {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cours_seq")
	@SequenceGenerator(name = "cours_seq", sequenceName = "cours_id_seq", allocationSize = 10)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Schema(hidden = true)
	@Column(name = "cours_id")
	private Long coursId;

	//private UE ue;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name = "promotion_cours",
		joinColumns = @JoinColumn(name = "cours_id")
	)
	// @JsonIgnoreProperties("cours")
	// @JsonBackReference
	@Builder.Default
	private List<PromotionId> promosIds = new ArrayList<>();

	private String intervenantLogin;

	//private Salle salle;

	private float heureDebut;

	private float duree;

	private Date jour;

	public enum TypeC {
		CM,
		TD,
		TP
	}
	private TypeC type;

	public Cours(List<PromotionId> promosIds, String intervenantLogin, float heureDebut, float duree, Date jour, TypeC type) {
		if(promosIds!=null){
			this.promosIds = promosIds;
		}
		this.intervenantLogin = intervenantLogin;
		this.heureDebut = heureDebut;
		this.duree = duree;
		this.jour = jour;
		this.type = type;
	}

	@Override
	public String toString(){
		return "Cours [coursId=" + coursId + ", promos=" + promosIds.toString() + ", intervenantLogin=" + intervenantLogin
				+ ", heureDebut=" + heureDebut + ", duree=" + duree + ", jour=" + jour + ", type=" + type + "]";
	}

}