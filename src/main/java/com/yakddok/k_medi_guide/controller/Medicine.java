package com.yakddok.k_medi_guide.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Medicine {
    private int id;
    private String name;
    private String description;

    public Medicine(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

}
