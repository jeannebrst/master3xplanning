package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Personne;
import fr.utln.gp2.entites.Promotion;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

@Path("/api/v1/promotions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PromotionRessource {
    private static List<Promotion> promotions = new ArrayList<>();

    @GET
    public List<Promotion> getAllPromotions() {
        return promotions;
    }
}

