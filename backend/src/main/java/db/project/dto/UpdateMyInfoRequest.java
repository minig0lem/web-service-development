package db.project.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateMyInfoRequest {
    private String password;
    private String email;
    private String phone_number;
}
