package fr.utln.gp2.repositories;

import fr.utln.gp2.entites.Cours;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CoursRepository implements PanacheRepository<Cours>{
}