package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.repositories.PromotionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/api/v1/promotions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PromotionRessource {

	@Inject
	PromotionRepository promotionRepository;

	@GET
	public List<Promotion> getAllPromotions() {
		return promotionRepository.listAll();
	}

	@GET
	@Path("/{id}/promotion/cours")
	public List<Cours> getCoursByPromotion(@PathParam("id") Long id) {
		Promotion promotion = promotionRepository.findByIdWithCours(id);
		if (promotion == null) {
			throw new NotFoundException("Promotion non trouvée");
		}
		return promotion.cours;
	}

	@POST
	@Transactional
	public Response createPromotion(Promotion promotion) {
		promotionRepository.persist(promotion);
		return Response.status(201).entity(promotion).build();
	}
}

