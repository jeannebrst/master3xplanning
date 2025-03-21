package fr.utln.gp2.repositories;

import fr.utln.gp2.entites.Cours;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CoursRepository implements PanacheRepository<Cours>{
	public Optional<Cours> findById(UUID id) {
		return find("id", id).firstResultOptional();
	}

	public boolean deleteById(UUID id) {
		return delete("id", id) > 0;
	}
}