package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.repositories.PersonneRepository;
import fr.utln.gp2.repositories.PromotionRepository;
import fr.utln.gp2.utils.AuthDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.codec.digest.DigestUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Path("/api/v1/personnes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PersonneRessource {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(PersonneRessource.class);

	@Inject
	PersonneRepository personneRepository;

	@Inject
	PromotionRepository promotionRepository;

	@GET
	public List<Personne> getAllPersonnes() {
		return personneRepository.listAll();
	}

	@GET
	@Path("/{login}")
	public Personne getPersonneByLogin(@PathParam("login") String login) {
		return personneRepository.findByLogin(login).orElseThrow(()-> new NotFoundException("Personne non trouvée"));
	}

	@POST
	@Transactional
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createPersonne(Personne personne) {
		List<Promotion> managedPromotions = new ArrayList<>();
		for (Promotion promotion : personne.getPromos()) {
			Promotion managedPromotion = promotionRepository.findById(promotion.getPromoId());
			if (managedPromotion == null) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity("Promotion with ID " + promotion.getPromoId() + " does not exist.")
						.build();
			}
			managedPromotions.add(managedPromotion);
		}

		personne.setPromos(managedPromotions);
		for (Promotion promotion : managedPromotions) {
			promotion.getPersonnes().add(personne);
		}
		String hashMdp = DigestUtils.sha256Hex(personne.getHashMdp());
		personne.setHashMdp(hashMdp);
		personne.setMail(personne.getPrenom().toLowerCase()+"."+personne.getNom().toLowerCase()+"@master.com");
		if (personne.getNom().length()<7) {
			String login = personne.getPrenom().toLowerCase().charAt(0)+personne.getNom().toLowerCase();
			personne.setLogin(login);
		} else {
			String login = personne.getPrenom().toLowerCase().charAt(0)+personne.getNom().toLowerCase().substring(0,7);
			personne.setLogin(login);
		}
		if (personneRepository.isPersistent(personne)) {
			personne = personneRepository.getEntityManager().merge(personne);
		} else {
			personneRepository.persist(personne);
		}

		return Response.status(201).entity(personne).build();
	}

	@POST
	@Transactional
	@Path("/auth")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authentification(AuthDTO dto){
		if(personneRepository.authentification(dto.getLogin(), dto.getMdp())) {
			return Response.ok("Authentification réussie").build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED)
					.build();
		}
	}

	@DELETE
	@Path("/{login}")
	@Transactional
	public Response removePersonneByLogin(@PathParam("login") String login) {
		boolean deleted = personneRepository.deleteByLogin(login);
		if (!deleted) {
			throw new NotFoundException("Personne non trouvée");
		}
		return Response.status(204).build();
	}
}