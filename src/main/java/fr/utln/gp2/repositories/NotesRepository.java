package fr.utln.gp2.repositories;

import fr.utln.gp2.entites.Personne;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class NotesRepository implements PanacheRepository<Personne> {

    public Optional<Personne> findByLogin(String login) {
        return find("login", login).firstResultOptional();
    }

    public boolean deleteByLogin(String login) {
        return delete("login", login) >0;
    }

}