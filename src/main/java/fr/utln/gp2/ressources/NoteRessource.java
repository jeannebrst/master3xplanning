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

@Path("/api/v1/notes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NoteRessource {

    @Inject
    NoteRepository noteRepository;

    @Inject
    PersonneRepository personneRepository;

    @Inject
    UERepository ueRepository;

    @GET
    public List<Note> getAllNotes() {
        return noteRepository.listAll();
    }


    /**
     * GET - Récupérer toutes les notes d'un étudiant via son login
     */
    @GET
    @Path("/{login}")
    public Response getNotesByLogin(@PathParam("login") String login) {
        // Vérifier si l'étudiant existe
        Optional<Personne> etudiantOpt = personneRepository.findByLogin(login);
        if (etudiantOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Étudiant avec le login " + login + " introuvable.")
                    .build();
        }

        List<Note> notes = noteRepository.findByLogin(login);
        return Response.ok(notes).build();
    }

    /**
     * POST - Ajouter une note à un étudiant
     */
    @POST
    @Transactional
    public Response createNote(NoteDTO note) {
        // Vérifier si l'étudiant existe
        Optional<Personne> etudiantOpt = personneRepository.findByLogin(note.getLogin());
        if (etudiantOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Étudiant avec le login " + note.getLogin() + " introuvable.")
                    .build();
        }

        // Vérifier si l'UE existe
        Optional<UE> ueOpt = ueRepository.findByNom(note.getUe());
        if (ueOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("UE " + ueOpt.get().getNom() + " introuvable.")
                    .build();
        }
        Note notePersist = new Note(etudiantOpt.get(), ueOpt.get(), note.getNote(), note.getDate());
        noteRepository.persist(notePersist);
        return Response.status(Response.Status.CREATED)
                .entity("Note ajoutée avec succès pour l'étudiant " + note.getLogin())
                .build();
    }

    /**
     * DELETE - Supprimer une note par son ID
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteNote(@PathParam("id") Long id) {
        boolean deleted = noteRepository.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Note avec l'ID " + id + " introuvable.")
                    .build();
        }
        return Response.noContent().build();
    }

}
