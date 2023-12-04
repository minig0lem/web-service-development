package db.project.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PostMapLocationInfoDto {
    private BigDecimal latitude;
    private BigDecimal longitude;
}