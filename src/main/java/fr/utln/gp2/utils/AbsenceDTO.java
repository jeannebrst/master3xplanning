package fr.utln.gp2.utils;
import fr.utln.gp2.entites.Cours;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class AbsenceDTO {
    private String login;
    private Long coursId;
    public AbsenceDTO() {
    }
}
