package fr.utln.gp2.repositories;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.utils.PromotionId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PromotionRepository implements PanacheRepositoryBase<Promotion, PromotionId> {

	public Promotion findByResponsable(Personne responsable) {
		return find("responsable", responsable).firstResult();
	}

	public Promotion findByIdWithCours(PromotionId id) {
		return find("promoId", id).firstResult();
	}

	public boolean deleteByNom(String nom) {
		return delete("nom", nom) > 0;
	}
}