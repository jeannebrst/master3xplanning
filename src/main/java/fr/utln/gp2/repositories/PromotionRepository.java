package fr.utln.gp2.repositories;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PromotionRepository implements PanacheRepository<Promotion> {

	public Promotion findByResponsable(Personne responsable) {
		return find("responsable", responsable).firstResult();
	}

	public Promotion findByIdWithCours(Long id) {
		return find("SELECT p FROM Promotion p LEFT JOIN FETCH p.cours WHERE p.id = ?1", id).firstResult();
	}

	public boolean deleteByNom(String nom) {
		return delete("nom", nom) > 0;
	}


}
