package fr.utln.gp2.repositories;

import fr.utln.gp2.entites.Note;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class NoteRepository implements PanacheRepository<Note> {

    @Inject
    PersonneRepository personneRepository;

    public List<Note> findByLogin(String login) {
        return personneRepository.findByLogin(login)
                .map(p -> {
                    // force l'initialisation des notes si besoin
                    p.getNotes().size();
                    return p.getNotes();
                })
                .orElse(Collections.emptyList());
    }
}