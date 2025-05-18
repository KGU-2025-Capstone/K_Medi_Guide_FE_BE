package com.yakddok.k_medi_guide.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class ResponseChatBotDTO {
    @JsonProperty("response_type")
    private String responseType;

    @JsonProperty("next")
    private String next;

    @JsonProperty("message")
    private String message;

    @JsonProperty("addMessage")
    private String addMessage;

    @JsonProperty("error")
    private String error;

    @JsonProperty("medicine_candidates")
    private List<candidate> candidates;

    @Getter
    @Setter
    public static class candidate {
        @JsonProperty("itemName")
        private String name;
        @JsonProperty("name_ko")
        private String nameKo;
    }
}
