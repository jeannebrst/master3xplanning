package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.repositories.PersonneRepository;
import fr.utln.gp2.utils.AuthDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/api/v1/personnes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PersonneRessource {

	@Inject
	PersonneRepository personneRepository;

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
		String hashMdp = DigestUtils.sha256Hex(personne.getHashMdp());
		personne.setHashMdp(hashMdp);
		personneRepository.persist(personne);
		return Response.status(201).entity(personne).build();
	}

	@POST
	@Transactional
	@Path("/auth")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authentification(AuthDTO dto) {
		String hashMdp = DigestUtils.sha256Hex(dto.getMdp());

		if(personneRepository.authentification(dto.getLogin(), hashMdp)) {
			return Response.ok("Authentification réussie").build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED)
					.build();
		}
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
