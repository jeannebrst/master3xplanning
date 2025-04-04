package fr.utln.gp2.repositories;

import fr.utln.gp2.entites.Note;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class NoteRepository implements PanacheRepository<Note> {

    private final PersonneRepository personneRepository;

    @jakarta.inject.Inject
    public NoteRepository(PersonneRepository personneRepository) {
        this.personneRepository = personneRepository;
    }

    public List<Note> findByLogin(String login) {
        return personneRepository.findByLogin(login).get().getNotes();
    }
}