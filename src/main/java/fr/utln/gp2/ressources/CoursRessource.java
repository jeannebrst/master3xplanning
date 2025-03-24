package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Cours;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;

import fr.utln.gp2.repositories.CoursRepository;
import fr.utln.gp2.repositories.PersonneRepository;
import fr.utln.gp2.repositories.PromotionRepository;
import fr.utln.gp2.utils.PromotionId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	public Response createCours(Cours cours) {
		List<PromotionId> managedPromotionsIds = new ArrayList<>();
		List<Promotion> managedPromotions = new ArrayList<>();

		for (PromotionId promotionId : cours.getPromosIds()) {
			Promotion managedPromotion = promotionRepository.findById(promotionId);
			if (managedPromotion == null) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity("Promotion avec ID : " + promotionId + " n'existe pas.")
						.build();
			}
			managedPromotions.add(managedPromotion);
			managedPromotionsIds.add(promotionId);
		}

		cours.setPromosIds(managedPromotionsIds);

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
	public Response removeCours(Long id) {
		Cours cours = coursRepository.findById(id);
		if (cours == null) {
			throw new NotFoundException("Cours non trouvé");
		}

		cours.getPromosIds().clear();

		coursRepository.flush();
		boolean deleted = coursRepository.deleteById(id);
		if (!deleted) {
			throw new NotFoundException("Cours non trouvée");
		}
		return Response.status(204).build();
	}
}
