package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import fr.utln.gp2.repositories.PersonneRepository;
import fr.utln.gp2.repositories.PromotionRepository;
import fr.utln.gp2.utils.AuthDTO;
import fr.utln.gp2.utils.PromotionId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;

@Path("/api/v1/personnes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PersonneRessource {

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
			Promotion managedPromotion = promotionRepository.findById(promotion.getPromo_id());
			if (managedPromotion == null) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity("Promotion with ID " + promotion.getPromo_id() + " does not exist.")
						.build();
			}
			managedPromotions.add(managedPromotion);
		}

		personne.setPromos(managedPromotions);
		for (Promotion promotion : managedPromotions) {
			promotion.getEtudiants().add(personne);
		}
		String hashMdp = DigestUtils.sha256Hex(personne.getHashMdp());
		personne.setHashMdp(hashMdp);
		String login = personne.getPrenom().toLowerCase().substring(0,1)+personne.getNom().toLowerCase().substring(0,7);
		personne.setLogin(login);
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
	public Response authentificationUtilisateur(AuthDTO auth) {
		String login = auth.getLogin();
		String mdp = auth.getMdp();
		String hashMdp = DigestUtils.sha256Hex(mdp);
		boolean authentifie = personneRepository.authentification(login, hashMdp);
		if (!authentifie) {
			throw new NotFoundException("Erreur auth");
		}
		return Response.status(200).build();

	}

	@DELETE
	@Path("/{login}")
	@Transactional
	public Response removePersonne(@PathParam("login") String login) {
		boolean deleted = personneRepository.deleteByLogin(login);
		if (!deleted) {
			throw new NotFoundException("Personne non trouvée");
		}
		return Response.status(204).build();
	}
}
