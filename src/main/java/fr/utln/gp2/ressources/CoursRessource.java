package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.repositories.CoursRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

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

    @POST
    @Transactional
    public Response createCours(Cours cours) {
        coursRepository.persist(cours);
        return Response.status(201).entity(cours).build();
    }
}

