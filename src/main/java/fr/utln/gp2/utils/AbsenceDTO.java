package fr.utln.gp2.utils;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AbsenceDTO {
    private String login;
    private Long coursId;
    public AbsenceDTO() {
    }
}
