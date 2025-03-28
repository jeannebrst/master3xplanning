package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.UE;
import fr.utln.gp2.repositories.PersonneRepository;
import fr.utln.gp2.repositories.UERepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;

@Path("/api/v1/ues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class UERessource {

    @Inject
    PersonneRepository personneRepository;

    @Inject
    UERepository ueRepository;

    @GET
    public List<UE> getAllUE() {
        return ueRepository.listAll();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUE(UE ue) {
        ueRepository.persist(ue);
        return Response.status(201).entity(ue).build();
    }
}