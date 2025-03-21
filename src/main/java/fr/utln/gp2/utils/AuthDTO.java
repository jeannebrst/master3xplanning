package fr.utln.gp2.utils;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AuthDTO {
    public String login;
    public String mdp;
}