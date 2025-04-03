package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Note;
import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.UE;
import fr.utln.gp2.repositories.NoteRepository;
import fr.utln.gp2.repositories.PersonneRepository;
import fr.utln.gp2.repositories.UERepository;
import fr.utln.gp2.utils.NoteDTO;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Path("/notes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NoteRessource {

    @Inject
    NoteRepository notesRepository;

    @Inject
    PersonneRepository personneRepository;

    @Inject
    UERepository ueRepository;

    /**
     * GET - Récupérer toutes les notes d'un étudiant via son login
     */
    @GET
    @Path("/{login}")
    public Response getNotesByLogin(@PathParam("login") String login) {
        List<Note> notes = notesRepository.findByLogin(login);
        if (notes.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Aucune note trouvée pour l'étudiant avec le login : " + login)
                    .build();
        }
        return Response.ok(notes).build();
    }

    /**
     * POST - Ajouter une note à un étudiant
     */
    @POST
    @Transactional
    public Response addNote(NoteDTO request) {
        // Vérifier si l'étudiant existe
        Optional<Personne> etudiantOpt = personneRepository.find("login", request.login).firstResultOptional();
        if (etudiantOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Étudiant avec le login " + request.login + " introuvable.")
                    .build();
        }

        // Vérifier si l'UE existe
        Optional<UE> ueOpt = ueRepository.findByIdOptional(request.ueId);
        if (ueOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("UE avec l'ID " + request.ueId + " introuvable.")
                    .build();
        }

        Note note = new Note(etudiantOpt.get(), ueOpt.get(), request.getNote(), request.getDate());

        notesRepository.persist(note);
        return Response.status(Response.Status.CREATED)
                .entity(note)
                .build();
    }

    /**
     * DELETE - Supprimer une note par son ID
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteNote(@PathParam("id") Long id) {
        boolean deleted = notesRepository.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Note avec l'ID " + id + " introuvable.")
                    .build();
        }
        return Response.noContent().build();
    }

}
