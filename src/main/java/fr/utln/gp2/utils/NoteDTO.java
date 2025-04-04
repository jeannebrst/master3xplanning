package fr.utln.gp2.utils;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NoteDTO {
    private String login;
    private String ue;
    private float note;
    private Date date;
}
