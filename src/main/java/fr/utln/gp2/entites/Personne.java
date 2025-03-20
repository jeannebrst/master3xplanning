package fr.utln.gp2.entites;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Personne {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "personne_seq", sequenceName = "personne_id_seq", allocationSize = 10)
	private Long id;

	@Column(name = "login", nullable = false)
	private String login;

	@Column(name = "mdp", nullable = false)
	private String hashMdp;

	private String nom;

	private String prenom;

	private String mail;

	private enum Role {
		PROFESSEUR,
		ETUDIANT,
		SECRETARIAT ,
		GESTIONNAIRE
	}
	private Role role;
}