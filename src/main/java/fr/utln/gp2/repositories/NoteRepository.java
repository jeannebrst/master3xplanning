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
        System.out.println(personneRepository.findByLogin(login).get().getNotes());
        return personneRepository.findByLogin(login).get().getNotes();
    }
}