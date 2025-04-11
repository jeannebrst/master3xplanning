package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.*;
import fr.utln.gp2.repositories.*;
import fr.utln.gp2.utils.RetardDTO;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import java.util.List;
import java.util.Optional;

@Path("/api/v1/retards")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RetardRessource {

    @Inject
    PersonneRepository personneRepository;

    @Inject
    UERepository ueRepository;

    @Inject
    RetardRepository retardRepository;
    @Inject
    CoursRepository coursRepository;

    @GET
    public List<Retard> getAllRetard() {
        return retardRepository.listAll();
    }

    @GET
    @Path("/{login}")
    public Response getRetardByLogin(@PathParam("login") String login) {
        // Vérifier si l'étudiant existe
        Optional<Personne> etudiantOpt = personneRepository.findByLogin(login);
        if (etudiantOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Étudiant avec le login " + login + " introuvable.")
                    .build();
        }

        List<Retard> absences = retardRepository.findByLogin(login);
        return Response.ok(absences).build();
    }

    @POST
    @Transactional
    public Response createRetard(RetardDTO retard) {
        // Vérifier si l'étudiant existe
        Optional<Personne> etudiantOpt = personneRepository.findByLogin(retard.getLogin());
        if (etudiantOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Étudiant avec le login " + retard.getLogin() + " introuvable.")
                    .build();
        }
        // Vérifier si le cours existe
        Optional<Cours> coursOpt = coursRepository.findByCoursId(retard.getCoursId());
        if (coursOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cours avec l'ID " + retard.getCoursId() + " introuvable.")
                    .build();
        }
        Retard retardPersist = new Retard(etudiantOpt.get(), coursOpt.get(), retard.getDuree());
        retardRepository.persist(retardPersist);
        return Response.status(Response.Status.CREATED)
                .entity("Retard ajoutée avec succès pour l'étudiant " + retard.getLogin())
                .build();
    }



}
