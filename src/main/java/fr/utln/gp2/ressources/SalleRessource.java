package fr.utln.gp2.ressources;

import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Salle;
import fr.utln.gp2.repositories.SalleRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

@Path("/api/v1/salles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SalleRessource {

    @Inject
    SalleRepository salleRepository;

    @GET
    public List<Salle> getAllSalles() {
        return salleRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public Salle getSalleById(@PathParam("id") Long id) {
        return salleRepository.findBySalleId(id);
    }

    @POST
    @Transactional
    public Response createSalle(Salle salle) {
        try {
            // Tentative de persistance de la salle
            salleRepository.persist(salle);

            // Si la persistance réussit, on retourne une réponse HTTP 201 (Created) avec la salle
            return Response.status(Response.Status.CREATED).entity(salle).build();
        } catch (ConstraintViolationException e) {
            // En cas de violation de contrainte (par exemple, doublon), retourner une erreur HTTP 400 (Bad Request)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erreur de contrainte : " + e.getMessage())
                    .build();
        } catch (Exception e) {
            // En cas d'erreur générale, retourner une erreur HTTP 500 (Internal Server Error)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur interne : " + e.getMessage())
                    .build();
        }
    }


    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteSalle(@PathParam("id") Long id) {
        boolean deleted = salleRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Cours non trouvée");
        }
        return Response.status(204).build();
    }
}


