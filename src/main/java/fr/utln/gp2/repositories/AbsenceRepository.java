package fr.utln.gp2.repositories;
import fr.utln.gp2.entites.Note;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import fr.utln.gp2.entites.Absence;

import java.util.List;

@ApplicationScoped
public class AbsenceRepository implements PanacheRepository<Absence>{
    @Inject
    PersonneRepository personneRepository;

    public List<Absence> findByLogin(String login) {
        return personneRepository.findByLogin(login).get().getAbsences();
    }

}
