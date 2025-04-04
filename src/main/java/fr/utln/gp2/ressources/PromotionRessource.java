package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.repositories.PersonneRepository;
import fr.utln.gp2.repositories.PromotionRepository;
import fr.utln.gp2.utils.PromotionId;
import fr.utln.gp2.utils.PromotionId.Type;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	@Path("{id}")
	public Promotion getPromotionById(@PathParam("id") String id) {

		// Expression régulière pour séparer les parties
		Pattern pattern = Pattern.compile("([A-Z]+)([0-9]+)([A-Za-z]+)");
		Matcher matcher = pattern.matcher(id);

		PromotionId promoId;
		if (matcher.matches()){
			promoId = new PromotionId(
				Type.fromString(matcher.group(1)),
				Integer.parseInt(matcher.group(2)),
				matcher.group(3)
			);
		} else {
			throw new IllegalArgumentException("Format invalide : " + id);
		}
		Promotion promotion = promotionRepository.findById(promoId);
		if (promotion == null) {
			throw new NotFoundException("Promotion non trouvée");
		}
		return promotion;
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
			if (!responsable.getRole().equals(Personne.Role.PROFESSEUR)) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity("La personne (responsable) " + responsable.getLogin() +" n'est pas un(e) professeur(e), c'est un(e) " + responsable.getRole()).build();
			}
			if (!promotion.getPersonnes().contains(responsable)) {
				promotion.getPersonnes().add(responsable);
			}
			if (!responsable.getPromos().contains(promotion)) {
				responsable.getPromos().add(promotion);
			}
		}
		promotionRepository.persist(promotion);
		return Response.status(201).entity(promotion).build();
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public Response removePromotion(@PathParam("id") String id) {
		Pattern pattern = Pattern.compile("([A-Z]+)([0-9]+)([A-Za-z]+)");
		Matcher matcher = pattern.matcher(id);
		PromotionId promoId;
		if (matcher.matches()){
			promoId = new PromotionId(
					PromotionId.Type.fromString(matcher.group(1)),
					Integer.parseInt(matcher.group(2)),
					matcher.group(3)
			);
		} else {
			throw new IllegalArgumentException("Format invalide : " + id);
		}
		boolean deleted = promotionRepository.deleteById(promoId);
		if (!deleted) {
			throw new NotFoundException("Promotion non trouvée");
		}
		return Response.status(204).build();
	}
}

