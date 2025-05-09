package com.yakddok.k_medi_guide.service.impl;

import com.yakddok.k_medi_guide.dto.PharmacyDTO;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PharmacyService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    public List<PharmacyDTO> searchNearbyPharmacies(double latitude, double longitude) {
        List<PharmacyDTO> pharmacies = new ArrayList<>();

        try {
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(apiKey)
                    .build();

            LatLng location = new LatLng(latitude, longitude);
            PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, location)
                    .radius(5000) // 5km 반경
                    .type(PlaceType.PHARMACY)
                    .await();

            for (PlacesSearchResult result : response.results) {
                PharmacyDTO pharmacy = new PharmacyDTO();
                pharmacy.setName(result.name);
                pharmacy.setAddress(result.vicinity);
                pharmacy.setLatitude(result.geometry.location.lat);
                pharmacy.setLongitude(result.geometry.location.lng);

                // 상세 정보 가져오기
                var details = PlacesApi.placeDetails(context, result.placeId)
                        .fields(com.google.maps.model.PlaceDetailsRequest.Field.PHONE_NUMBER,
                                com.google.maps.model.PlaceDetailsRequest.Field.OPENING_HOURS,
                                com.google.maps.model.PlaceDetailsRequest.Field.OPENING_HOURS_WEEKDAY_TEXT)
                        .await();

                pharmacy.setPhoneNumber(details.phoneNumber);
                if (details.openingHours != null) {
                    pharmacy.setOpen(details.openingHours.openNow);
                    pharmacy.setOpeningHours(String.join(", ", details.openingHours.weekdayText));
                }

                pharmacies.add(pharmacy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pharmacies;
    }
}