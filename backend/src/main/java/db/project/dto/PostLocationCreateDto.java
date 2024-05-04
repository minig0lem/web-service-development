package db.project.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostLocationCreateDto {
    private String location_id;
    private String address;
    private String latitude;
    private String longitude;
}
