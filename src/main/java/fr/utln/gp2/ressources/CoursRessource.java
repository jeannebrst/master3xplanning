package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Cours;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;

import fr.utln.gp2.entites.UE;
import fr.utln.gp2.repositories.CoursRepository;
import fr.utln.gp2.repositories.PersonneRepository;
import fr.utln.gp2.repositories.PromotionRepository;
import fr.utln.gp2.repositories.UERepository;
import fr.utln.gp2.utils.PromotionId;
import fr.utln.gp2.utils.PromotionId.Type;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

		if (cours.getUes() == null || cours.getUes().getNom() == null) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("L'UE associée au cours est nulle ou sans nom.")
					.build();
		}

		final String ueNom = cours.getUes().getNom();

		// Récupérer l'UE dans la base de données en utilisant le nom
		Optional<UE> optionalUe = ueRepository.findByNom(cours.getUes().getNom());

		// Vérification si l'UE existe, sinon retour d'une erreur
		UE ue = optionalUe.orElseThrow(() -> new IllegalArgumentException(
				"L'UE avec le nom " + ueNom + " n'existe pas."
		));

		// Associer l'UE au cours
		cours.setUes(ue);

		if (ue != null && !ue.getCours().contains(cours)) {
			ue.getCours().add(cours); // Ajouter le cours à la liste des cours de l'UE
		}

		// Vérification et persistance de l'UE
		if (!ueRepository.isPersistent(ue)) {
			ueRepository.persist(ue);
		}

		Optional<Personne> intervenantOpt = personneRepository.findByLogin(cours.getIntervenantLogin());
		if (intervenantOpt.isPresent()) {
			Personne intervenant = intervenantOpt.get();
			if (!personneRepository.isPersistent(intervenant)) {
				intervenant = personneRepository.getEntityManager().merge(intervenant);
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
