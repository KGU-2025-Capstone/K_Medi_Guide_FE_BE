package com.yakddok.k_medi_guide.dto.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPharmarcyDTO {
    private List<String> includedTypes;
    private int maxResultCount = 15;
    private LocationRestriction locationRestriction;

    @Getter
    @Setter
    public class LocationRestriction {
        private Circle circle;
    }

    @Getter
    @Setter
    public class Circle {
        private Center center;
        private double radius = 1000;
    }

    @Getter
    @Setter
    public class Center{
        private BigDecimal latitude;
        private BigDecimal longitude;
    }
}


