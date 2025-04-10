package fr.utln.gp2.repositories;

import fr.utln.gp2.entites.Salle;
import fr.utln.gp2.entites.UE;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class SalleRepository implements PanacheRepository<Salle> {
    public Salle findBySalleId(Long id) {
        return find("salleId", id).firstResult();
    }
    public Optional<Salle> findByNom(String nom) {
        return find("nom", nom).firstResultOptional();
    }

    public boolean deleteByNom(String nom) {
        return delete("nom", nom) > 0;
    }

}
