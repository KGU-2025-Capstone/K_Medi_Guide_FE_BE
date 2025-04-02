package com.yakddok.k_medi_guide.service;

import com.yakddok.k_medi_guide.dto.response.ResponsePhamarcyDTO;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpHeaders;

public interface MapService {
    public ResponsePhamarcyDTO getPhamarcy(BigDecimal latitude, BigDecimal longitude);
}
