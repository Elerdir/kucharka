package cz.niderle.server.auth.dto;

import cz.niderle.server.auth.model.UserEntity;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    private Long id;

    private String username;

    private String password;

    public static Users fromEntity(UserEntity user) {
        return Users.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
