package fr.utln.gp2.repositories;

import fr.utln.gp2.entites.Personne;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonneRepository implements PanacheRepository<Personne>{

	public Personne findByLogin(String login) {
		return find("login", login).firstResult();
	}
}