package db.project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostRentalReturnDto {
    private String bike_id;
    private String end_location;

    public PostRentalReturnDto() {
    }

    public PostRentalReturnDto(String bike_id, String end_location) {
        this.bike_id = bike_id;
        this.end_location = end_location;
    }
}
