package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.repositories.PersonneRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

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
	public Response createPersonne(Personne personne) {
		personneRepository.persist(personne);
		return Response.status(201).entity(personne).build();
	}

	@DELETE
	@Path("/{login}")
	@Transactional  // Nécessaire pour modifier la base
	public Response removePersonne(@PathParam("login") String login) {
		boolean deleted = personneRepository.deleteByLogin(login);
		if (!deleted) {
			throw new NotFoundException("Personne non trouvée");
		}
		return Response.status(204).build();
	}
}
