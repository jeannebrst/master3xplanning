package fr.utln.gp2.utils;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NoteDTO {
        public String login;
        public Long ueId;
        public float note;
        public Date date;
}
