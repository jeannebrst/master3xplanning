package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Personne;
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
        if (!ue.getIntervenantsLogin().contains(ue.getResponsableLogin())) {
            ue.getIntervenantsLogin().add(ue.getResponsableLogin());
        }
        final Personne responsableUe = personneRepository.findByLogin(ue.getResponsableLogin()).get();
        if (!responsableUe.getRole().equals(Personne.Role.PROFESSEUR)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("La personne (responsable) " + responsableUe.getLogin() +" n'est pas un(e) professeur(e), c'est un(e) " + responsableUe.getRole()).build();
        }
        for (String intervenantLogin : ue.getIntervenantsLogin()) {
            Personne intervenantUe = personneRepository.findByLogin(intervenantLogin).get();
            if (!intervenantUe.getRole().equals(Personne.Role.PROFESSEUR)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("La personne (intervenant) " + intervenantUe.getLogin() +" n'est pas un(e) professeur(e), c'est un(e) " + intervenantUe.getRole()).build();
            }
        }
        ueRepository.persist(ue);
        return Response.status(201).entity(ue).build();
    }
}