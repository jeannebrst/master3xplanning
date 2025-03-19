package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Personne;
import jakarta.enterprise.context.ApplicationScoped;
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
    private static List<Personne> personnes = new ArrayList<>();

    @GET
    public List<Personne> getAllPersonnes() {
        return personnes;
    }

    @GET
    @Path("/{login}")
    public Personne getPersonneByLogin(@PathParam("login") String login) {
        return personnes.stream()
                .filter(b -> b.getLogin().equals(login))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Personne not found"));
    }

    @POST
    public Response createPersonne(Personne personne) {
        personnes.add(personne);
        return Response.status(201).entity(personne).build();
    }

    @DELETE
    public Response removePersonne(String login){
        Personne personne = getPersonneByLogin(login);
        personnes.remove(personne);
        return Response.status(204).build();
    }
}
