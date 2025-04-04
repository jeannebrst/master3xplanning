package fr.utln.gp2.utils;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class NoteDTO {
    private String login;
    private String ue;
    private float note;
    private Date date;
    public NoteDTO() {
        // Obligatoire pour Jackson
    }
}
