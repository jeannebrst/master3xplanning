package fr.utln.gp2.entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.*;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import fr.utln.gp2.utils.PromotionId;
import fr.utln.gp2.utils.PromotionId.Type;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
public class Promotion {
	private static final HttpClient client = HttpClient.newHttpClient();
	private static final Logger logger = LoggerFactory.getLogger(Promotion.class);

	@EmbeddedId
	private PromotionId promoId;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "promotion_cours",
			joinColumns = {
					@JoinColumn(name = "type_promotion", referencedColumnName = "type"),
					@JoinColumn(name = "annee_promotion", referencedColumnName = "annee"),
					@JoinColumn(name = "categorie_promotion", referencedColumnName = "categorie")
			},
			inverseJoinColumns = @JoinColumn(name = "cours_id")
	)
	@Column(name = "cours")
	// @JsonManagedReference
	// @JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@JsonIgnoreProperties({"promos"})
	// @Schema(hidden = true)
	// @Builder.Default
	private List<Cours> cours = new ArrayList<>();


	private String responsableLogin;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "promotion_personnes",
			joinColumns = {
					@JoinColumn(name = "type_promotion", referencedColumnName = "type"),
					@JoinColumn(name = "annee_promotion", referencedColumnName = "annee"),
					@JoinColumn(name = "categorie_promotion", referencedColumnName = "categorie")
			},
			inverseJoinColumns = @JoinColumn(name = "personne_id")
	)
	@Column(name = "personnes")
	// @JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Schema(hidden = true)
	@JsonIgnoreProperties({"personneId","hashMdp","nom","prenom","mail","promos"})
	private List<Personne> personnes = new ArrayList<>();

	protected Promotion() {}

	public Promotion(Type type, int annee, String categorie, List<Cours> cours, String responsable_login, List<Personne> personnes) {
		this.promoId = new PromotionId(type, annee, categorie);
		if(cours != null){
			this.cours = cours;
		}
		this.responsableLogin = responsable_login;
		if(personnes != null){
			this.personnes = personnes;
		}
	}

	@Override
	public String toString(){
		List<Long> coursIds = new ArrayList<>();
		for (Cours c : cours){
			coursIds.add(c.getCoursId());
		}

		List<String> personnesIds = new ArrayList<>();
		for (Personne p : personnes){
			personnesIds.add(p.getLogin());
		}

		return "Promotion [promoId=" + promoId.toString() + ", cours=" + coursIds + ", responsableLogin=" + responsableLogin
				+ ", personnes=" + personnesIds + "]";
	}
}