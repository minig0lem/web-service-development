package db.project.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class FavoritesDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class FavoritesResponse {
        private List<FavoritesDto.Favorites> locations;

        public FavoritesResponse() {
            this.locations = new ArrayList<>();
        }

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FavoritesChange {
        private String location;
        private boolean favorite;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FavoritesSearch {
        private String location;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Favorites {
        private String location_id;
        private String address;
        private boolean favorite;

    }
}
