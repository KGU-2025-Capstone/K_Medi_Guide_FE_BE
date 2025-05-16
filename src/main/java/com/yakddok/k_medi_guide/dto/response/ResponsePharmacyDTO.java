package com.yakddok.k_medi_guide.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResponsePharmacyDTO {

    @JsonProperty("places")
    private List<Place> places;

    @Getter
    @Setter
    public static class Place {
        @JsonProperty("displayName")
        private DisplayName displayName;
        @JsonProperty("formattedAddress")
        private String formattedAddress;
        @JsonProperty("internationalPhoneNumber")
        private String internationalPhoneNumber;
        @JsonProperty("location")
        private Location location;
        @JsonProperty("currentOpeningHours")
        private CurrentOpeningHours currentOpeningHours;
    }

    @Getter
    @Setter
    public static class DisplayName {
        @JsonProperty("text")
        private String text;
    }
    @Getter
    @Setter
    public static class Location {
        @JsonProperty("latitude")
        private BigDecimal latitude;
        @JsonProperty("longitude")
        private BigDecimal longitude;
    }

    @Getter
    @Setter
    public static class CurrentOpeningHours {
        @JsonProperty("openNow")
        private boolean openNow;
        @JsonProperty("weekdayDescriptions")
        private List<String> weekdayDescriptions;
    }
}
