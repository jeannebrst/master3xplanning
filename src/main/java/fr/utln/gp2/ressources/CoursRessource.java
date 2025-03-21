package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Cours;

import fr.utln.gp2.entites.Promotion;

import fr.utln.gp2.repositories.CoursRepository;
import fr.utln.gp2.repositories.PromotionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/cours")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CoursRessource {

    @Inject
    CoursRepository coursRepository;

    @Inject
    PromotionRepository promotionRepository;

    @GET
    public List<Cours> getAllCours() {
        return coursRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public Cours getCoursById(@PathParam("id") UUID id) {
        return coursRepository.findById(id).orElseThrow(() -> new NotFoundException("Cours non trouvé"));
    }
    @POST
    @Transactional
    public Response createCours(Cours cours) {
        List<Promotion> managedPromotions = new ArrayList<>();

        for (Promotion promotion : cours.getPromos()) {
            System.out.println("Promotion: " + promotion + ", promo_id: " + promotion.getPromo_id());
            Promotion managedPromotion = promotionRepository.findById(promotion.getPromo_id());
            if (managedPromotion == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Promotion with ID " + promotion.getPromo_id() + " does not exist.")
                        .build();
            }
            managedPromotions.add(managedPromotion);
        }

        cours.setPromos(managedPromotions);
        if (coursRepository.isPersistent(cours)) {
            cours = coursRepository.getEntityManager().merge(cours);
        } else {
            coursRepository.persist(cours);
        }

        return Response.status(201).entity(cours).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response removeCours(@PathParam("id") UUID id) {
        boolean deleted = coursRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Cours non trouvée");
        }
        return Response.status(204).build();
    }
}

