package fr.utln.gp2.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import fr.utln.gp2.entites.Cours;
import fr.utln.gp2.entites.Promotion;

public class CoursDTO {
    public Long coursId;
    public Long ueId;
    public Long salleId;
    public List<PromotionId> promos = new ArrayList<>();
    public String intervenantLogin;
    public int heureDebut;
    public int duree;
    public Date jour;
    public String type; // "CM", "TD", "TP"

    // Conversion Cours -> CoursDTO
    public static CoursDTO fromCours(Cours cours) {
        CoursDTO dto = new CoursDTO();
        dto.coursId = cours.getCoursId();
        dto.ueId = cours.getUe() != null ? cours.getUe().getUeId() : null;  // Seulement l'ID de l'UE
        dto.salleId = cours.getSalle().getSalle_id();
        dto.promos = cours.getPromos().stream()
                          .map(Promotion::getPromoId)
                          .collect(Collectors.toList());  // ID des promotions uniquement
        dto.intervenantLogin = cours.getIntervenantLogin();
        dto.heureDebut = cours.getHeureDebut();
        dto.duree = cours.getDuree();
        dto.jour = cours.getJour();
        dto.type = cours.getType().toString();  // Utiliser `name()` si c'est un `enum`
        return dto;
    }
}