package com.yakddok.k_medi_guide.service.impl;

import com.yakddok.k_medi_guide.dto.request.RequestPharmacyDTO;
import com.yakddok.k_medi_guide.dto.response.ResponsePharmacyDTO;
import com.yakddok.k_medi_guide.service.MapService;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleMapServiceImpl implements MapService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.map.client-secret}")
    private String googleMapClientSecret;

    private final String requestURL =
            "https://places.googleapis.com/v1/places:searchNearby";


    /**
     * api 를 통해 인근 약국 목록을 받아옵니다. 1키로 반경 15개 고정
     *
     * @return ResponsePhamarcyDTO (반환된 약국정보들 )
     */
    @Override
    public ResponsePharmacyDTO getPharmacy(BigDecimal latitude, BigDecimal longitude) {
        HttpHeaders headers = createHeaders();
        RequestPharmacyDTO requestDTO = new RequestPharmacyDTO();

        RequestPharmacyDTO.Center center = new RequestPharmacyDTO().new Center();
        center.setLatitude(latitude);
        center.setLongitude(longitude);

        RequestPharmacyDTO.LocationRestriction locationRestriction =
                new RequestPharmacyDTO().new LocationRestriction();
        RequestPharmacyDTO.Circle circle = new RequestPharmacyDTO().new Circle();

        circle.setCenter(center);
        locationRestriction.setCircle(circle);
        List<String> list = new ArrayList<>();
        list.add("pharmacy");
        requestDTO.setIncludedTypes(list);
        requestDTO.setLocationRestriction(locationRestriction);


        HttpEntity<RequestPharmacyDTO> entity = new HttpEntity<>(requestDTO, headers);

        ResponseEntity<ResponsePharmacyDTO> response = restTemplate.exchange(
                requestURL,
                HttpMethod.POST,
                entity,
                ResponsePharmacyDTO.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }

        return null;
    }

    /**
     * api 요청 시 필요한 헤더를 만듭니다.
     * 필수값 : secret key
     *
     * @return HttpHeaders (만들어진 헤더)
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-Goog-Api-Key", googleMapClientSecret);
        headers.set("X-Goog-FieldMask", "places.displayName,places.formattedAddress,places.internationalPhoneNumber,places.addressComponents,places.currentOpeningHours,places.location");
        return headers;
    }

    /**
     * api 를 통해 Static Map 을 받아옵니다
     */
    @Override
    public String getStaticMap(BigDecimal latitude, BigDecimal longitude){
        String url =
            "https://maps.googleapis.com/maps/api/staticmap?center=" +
                latitude + "," + longitude +"&zoom=16&size=600x300&markers=color:red%7C" + latitude+","+longitude+"&key=" + googleMapClientSecret;

        RestTemplate restTemplate = new RestTemplate();
        byte[] imageBytes = restTemplate.getForObject(url, byte[].class);

        // Base64로 인코딩하고 data URL 형식으로 반환
        String base64 = Base64.getEncoder().encodeToString(imageBytes);
        return "data:image/png;base64," + base64;
    }
}