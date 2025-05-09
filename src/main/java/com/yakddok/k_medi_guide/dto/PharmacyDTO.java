package com.yakddok.k_medi_guide.dto;

import lombok.Data;

@Data
public class PharmacyDTO {
    private String name;
    private String address;
    private String phoneNumber;
    private boolean isOpen;
    private String openingHours;
    private double latitude;
    private double longitude;
}