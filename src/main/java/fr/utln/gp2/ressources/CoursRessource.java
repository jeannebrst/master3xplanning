package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Cours;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;

import fr.utln.gp2.repositories.CoursRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("/api/v1/cours")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CoursRessource {

	@Inject
	CoursRepository coursRepository;

	@Inject
	PromotionRepository promotionRepository;

	@Inject
	PersonneRepository personneRepository;

	@GET
	public List<Cours> getAllCours() {
		return coursRepository.listAll();
	}

	@GET
	@Path("/{id}")
	public Cours getCoursById(@PathParam("id") Long id) {
		return coursRepository.findByCoursId(id).orElseThrow(() -> new NotFoundException("Cours non trouvé"));
	}

	@POST
	@Transactional
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCours(Cours cours) {
		List<PromotionId> managedPromotionsIds = new ArrayList<>();
		List<Promotion> managedPromotions = new ArrayList<>();

		for (Promotion promotion : cours.getPromos()) {
			Promotion managedPromotion = promotionRepository.findById(promotion.getPromoId());
			if (managedPromotion == null) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity("Promotion avec ID : " + promotion.getPromoId() + " n'existe pas.")
						.build();
			}
			managedPromotions.add(managedPromotion);
			managedPromotionsIds.add(managedPromotion.getPromoId());
		}
		
		cours.setPromos(managedPromotions);

		Optional<Personne> intervenantOpt = personneRepository.findByLogin(cours.getIntervenantLogin());
		if (intervenantOpt.isPresent()) {
			Personne intervenant = intervenantOpt.get();
			if (!personneRepository.isPersistent(intervenant)) {
				intervenant = personneRepository.getEntityManager().merge(intervenant);
			}
			for (Promotion promotion : managedPromotions) {
				promotion.getPersonnes().add(intervenant);
			}
		}
		for (Promotion promotion : managedPromotions) {
			promotion.getCours().add(cours);
		}
		if (coursRepository.isPersistent(cours)) {
			cours = coursRepository.getEntityManager().merge(cours);
		} else {
			coursRepository.persist(cours);
		}

		return Response.status(201).entity(cours).build();
	}

	@DELETE
	@Transactional
	@Path("/{id}")
	public Response removeCoursById(@PathParam("id") Long id) {
		Cours cours = coursRepository.findById(id);
		if (cours == null) {
			throw new NotFoundException("Cours non trouvé");
		}

		cours.getPromos().clear();

		coursRepository.flush();
		boolean deleted = coursRepository.deleteById(id);
		if (!deleted) {
			throw new NotFoundException("Cours non trouvée");
		}
		return Response.status(204).build();
	}

	@GET
	@Path("?promoId={id}")
	public List<Cours> getCoursByPromotion(@PathParam("id") String id) {

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
		return promotion.getCours();
	}
}
