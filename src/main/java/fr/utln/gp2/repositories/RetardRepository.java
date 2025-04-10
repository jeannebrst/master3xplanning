package fr.utln.gp2.repositories;
import fr.utln.gp2.entites.Note;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import fr.utln.gp2.entites.Retard;

import java.util.List;

@ApplicationScoped
public class RetardRepository implements PanacheRepository<Retard>{
    @Inject
    PersonneRepository personneRepository;

    public List<Retard> findByLogin(String login) {
        return personneRepository.findByLogin(login).get().getRetards();
    }

}
