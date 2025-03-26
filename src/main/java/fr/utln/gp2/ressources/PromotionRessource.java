package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.repositories.PersonneRepository;
import fr.utln.gp2.repositories.PromotionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/api/v1/promotions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PromotionRessource {

	@Inject
	PromotionRepository promotionRepository;

	@Inject
	PersonneRepository personneRepository;

	@GET
	public List<Promotion> getAllPromotions() {
		return promotionRepository.listAll();
	}

	@GET
	@Path("/{id}/cours")
	public List<Cours> getCoursByPromotion(@PathParam("id") Long id) {
		Promotion promotion = promotionRepository.findByIdWithCours(id);
		if (promotion == null) {
			throw new NotFoundException("Promotion non trouvée");
		}
		return promotion.getCours();
	}

	@POST
	@Transactional
	public Response createPromotion(Promotion promotion) {
		Optional<Personne> responsableOpt = personneRepository.findByLogin(promotion.getResponsableLogin());
		if (responsableOpt.isPresent()) {
			Personne responsable = responsableOpt.get();
			if (!personneRepository.isPersistent(responsable)) {
				responsable = personneRepository.getEntityManager().merge(responsable);
			}
			promotion.getPersonnes().add(responsable);

		}
		promotionRepository.persist(promotion);
		return Response.status(201).entity(promotion).build();
	}

	@DELETE
	@Path("/{nom}/promotion")
	@Transactional
	public Response removePromotion(@PathParam("nom") String nom) {
		boolean deleted = promotionRepository.deleteByNom(nom);
		if (!deleted) {
			throw new NotFoundException("Promotion non trouvée");
		}
		return Response.status(204).build();
	}
}

