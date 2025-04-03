package fr.utln.gp2.repositories;

import fr.utln.gp2.entites.Salle;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SalleRepository implements PanacheRepository<Salle> {
    public Salle findBySalleId(Long id) {
        return find("salle_id", id).firstResult();
    }
    public Salle findByNom(String nom) {
        return find("nom", nom).firstResult();
    }

    public boolean deleteByNom(String nom) {
        return delete("nom", nom) > 0;
    }

}
