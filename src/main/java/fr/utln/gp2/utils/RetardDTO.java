package fr.utln.gp2.utils;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class RetardDTO {
    private String login;
    private Long coursId;
    private int duree;
    public RetardDTO() {
    }
}
