package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.*;
import fr.utln.gp2.repositories.AbsenceRepository;
import fr.utln.gp2.repositories.CoursRepository;
import fr.utln.gp2.repositories.PersonneRepository;
import fr.utln.gp2.repositories.UERepository;
import fr.utln.gp2.utils.AbsenceDTO;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Path("/api/v1/absence")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AbsenceRessource {

    @Inject
    PersonneRepository personneRepository;

    @Inject
    UERepository ueRepository;

    @Inject
    AbsenceRepository absenceRepository;
    @Inject
    CoursRepository coursRepository;

    @GET
    public List<Absence> getAllAbsence() {
        return absenceRepository.listAll();
    }

    @GET
    @Path("/{login}")
    public Response getAbsenceByLogin(@PathParam("login") String login) {
        // Vérifier si l'étudiant existe
        Optional<Personne> etudiantOpt = personneRepository.findByLogin(login);
        if (etudiantOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Étudiant avec le login " + login + " introuvable.")
                    .build();
        }

        List<Absence> absences = absenceRepository.findByLogin(login);
        return Response.ok(absences).build();
    }

    @POST
    @Transactional
    public Response createAbsence(AbsenceDTO absence) {
        // Vérifier si l'étudiant existe
        Optional<Personne> etudiantOpt = personneRepository.findByLogin(absence.getLogin());
        if (etudiantOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Étudiant avec le login " + absence.getLogin() + " introuvable.")
                    .build();
        }
        // Vérifier si le cours existe
        Optional<Cours> coursOpt = coursRepository.findByCoursId(absence.getCoursId());
        if (coursOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cours avec l'ID " + absence.getCoursId() + " introuvable.")
                    .build();
        }


        Absence absencePersist = new Absence(etudiantOpt.get(), coursOpt.get());
        absenceRepository.persist(absencePersist);
        return Response.status(Response.Status.CREATED)
                .entity("Absence ajoutée avec succès pour l'étudiant " + absence.getLogin())
                .build();
    }



}
