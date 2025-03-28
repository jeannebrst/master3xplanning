package fr.utln.gp2.repositories;

import fr.utln.gp2.entites.Cours;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CoursRepository implements PanacheRepository<Cours>{

	public Optional<Cours> findByCoursId(Long id) {
		return find("id", id).firstResultOptional();
	}

	public boolean deleteById(Long id) {
		return delete("id", id) > 0;
	}

	public Optional<List<Cours>> findByIntervenantLogin(String intervenantLogin) {
		return Optional.ofNullable(find("intervenantLogin", intervenantLogin).list());
	}
}