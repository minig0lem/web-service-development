package db.project.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdatePWRequest {
    private String id;
    private String new_password;
}
