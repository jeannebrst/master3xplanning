package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.repositories.CoursRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/api/v1/cours")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CoursRessource {

    @Inject
    CoursRepository coursRepository;

    @GET
    public List<Cours> getAllCours() {
        return coursRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public Cours getCoursById(@PathParam("id") UUID id) {
        return coursRepository.findById(id).orElseThrow(() -> new NotFoundException("Cours non trouvé"));
    }
    @POST
    @Transactional
    public Response createCours(Cours cours) {
        coursRepository.persist(cours);
        return Response.status(201).entity(cours).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response removeCours(@PathParam("id") UUID id) {
        boolean deleted = coursRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Cours non trouvée");
        }
        return Response.status(204).build();
    }
}

