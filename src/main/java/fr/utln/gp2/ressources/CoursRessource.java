package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.*;

import fr.utln.gp2.repositories.*;
import fr.utln.gp2.utils.CoursDTO;
import fr.utln.gp2.utils.PromotionId;
import fr.utln.gp2.utils.PromotionId.Type;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Collections;
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

	@Inject
	UERepository ueRepository;

	@Inject
	SalleRepository salleRepository;

	@Inject
	EntityManager entityManager;

	@GET
	public List<Cours> getAllCours() {
		return coursRepository.listAll();
	}

	@GET
	@Path("/{id}")
	public Cours getCoursById(@PathParam("id") Long id) {
		return coursRepository.findByCoursId(id).orElseThrow(() -> new NotFoundException("Cours non trouvé"));
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response updateCours(@PathParam("id") Long id, CoursDTO dto) {
		Cours cours = coursRepository.findById(id);
		if (cours == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		// Récupérer l'UE
		UE ue = entityManager.find(UE.class, dto.ueId);
		if (ue == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("UE non trouvée").build();
		}

		// Récupérer la Salle (si spécifiée)
		Salle salle = null;
		if (dto.salleId != null) {
			salle = salleRepository.findBySalleId(dto.salleId);
			if (salle == null) {
				return Response.status(Response.Status.BAD_REQUEST).entity("Salle non trouvée").build();
			}
		}

		// Récupérer les Promotions
		List<Promotion> promotions = new ArrayList<>();
		for (PromotionId pid : dto.promos) {
			Promotion promo = entityManager.find(Promotion.class, pid);
			if (promo != null) {
				promotions.add(promo);
			} else {
				return Response.status(Response.Status.BAD_REQUEST)
					.entity("Promotion non trouvée : " + pid).build();
			}
		}

		// Mise à jour des champs du Cours
		cours.setUe(ue);
		cours.setSalle(salle);
		System.out.println("Salle du cours modifié : "+salle);
		cours.setPromos(promotions);
		cours.setIntervenantLogin(dto.intervenantLogin);
		cours.setHeureDebut(dto.heureDebut);
		cours.setDuree(dto.duree);
		cours.setJour(dto.jour);
		cours.setType(Cours.stringToTypeC(dto.type));

		// Sauvegarder et renvoyer la réponse
		return Response.ok(CoursDTO.fromCours(cours)).build();
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

		if (cours.getUe() == null || cours.getUe().getNom() == null) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("L'UE associée au cours est nulle ou sans nom.")
					.build();
		}

		final String ueNom = cours.getUe().getNom();

		Optional<UE> optionalUe = ueRepository.findByNom(cours.getUe().getNom());

		UE ue = optionalUe.orElseThrow(() -> new IllegalArgumentException(
				"L'UE avec le nom " + ueNom + " n'existe pas."
		));

		cours.setUe(ue);

		Optional<Salle> optionalSalle = salleRepository.findByNom(cours.getSalle().getNom());

		Salle salle = optionalSalle.orElseThrow(() -> new IllegalArgumentException(
				"La salle avec ce nom n'existe pas."
		));

		cours.setSalle(salle);

		if (ue != null) {
			ue.getCours().add(cours);
		}

		if (!ueRepository.isPersistent(ue)) {
			ueRepository.persist(ue);
		}

		Optional<Personne> intervenantOpt = personneRepository.findByLogin(cours.getIntervenantLogin());
		if (intervenantOpt.isPresent()) {
			Personne intervenant = intervenantOpt.get();
			if (!ue.getIntervenantsLogin().contains(intervenant.getLogin())) {
				return Response.status(Response.Status.BAD_REQUEST)
					.entity("Le professeur " + intervenant.getLogin() +" ne fait pas partie des intervenants de cette UE : " + ue.getIntervenantsLogin().toString()).build();
			}
			for (Promotion promotion : managedPromotions) {
				if (!promotion.getPersonnes().contains(intervenant)) {
					promotion.getPersonnes().add(intervenant);
				}
			}
		}
		for (Promotion promotion : managedPromotions) {
			if (!promotion.getCours().contains(cours)) {
				promotion.getCours().add(cours);
			}
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
	@Path("/by-promo")
	public List<Cours> getCoursByPromotion(@QueryParam("promoId") String id) {
		if (id == null || id.isBlank()) {
			throw new IllegalArgumentException("Le paramètre promoId est obligatoire.");
		}
		
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

	@GET
	@Path("by-intervenant")
	public List<Cours> getCoursByIntervenant(@QueryParam("intervenantLogin") String intervenantLogin) {
		if (intervenantLogin == null || intervenantLogin.isBlank()){
			throw new IllegalArgumentException("Le paramètre intervenantLogin est obligatoire.");
		}
		
		Optional<List<Cours>> listCours = coursRepository.findByIntervenantLogin(intervenantLogin);
		if (listCours.isPresent()) {
			return listCours.get();
		}
		else{
			return Collections.emptyList();
		}
	}
}
