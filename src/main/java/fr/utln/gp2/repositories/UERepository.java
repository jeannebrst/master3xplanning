package fr.utln.gp2.repositories;

import fr.utln.gp2.entites.UE;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class UERepository implements PanacheRepository<UE> {

    public Optional<UE> findByNom(String nom) {
        return find("nom", nom).firstResultOptional();
    }
}
