package com.yakddok.k_medi_guide.service;

import com.yakddok.k_medi_guide.dto.response.ResponsePharmacyDTO;
import java.math.BigDecimal;

public interface MapService {
    public ResponsePharmacyDTO getPharmacy(BigDecimal latitude, BigDecimal longitude);
    public String getStaticMap(BigDecimal latitude, BigDecimal longitude);
}
