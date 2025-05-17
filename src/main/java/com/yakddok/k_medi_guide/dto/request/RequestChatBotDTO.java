package com.yakddok.k_medi_guide.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestChatBotDTO {

    @JsonProperty
    private String input;

}
