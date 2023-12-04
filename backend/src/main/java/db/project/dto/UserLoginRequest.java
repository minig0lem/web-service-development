package db.project.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserLoginRequest {
    String userId;
    String password;
}