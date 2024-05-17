package db.project.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRentalHistoryDto {
    private String start_date;
    private String end_date;
}
