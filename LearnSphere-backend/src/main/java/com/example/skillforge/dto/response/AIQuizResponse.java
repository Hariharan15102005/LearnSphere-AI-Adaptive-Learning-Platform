package com.example.skillforge.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AIQuizResponse {

    private List<AIQuestion> questions;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AIQuestion {
        private String questionText;      // Question text
        private List<String> options;     // MCQ options
        private String correctAnswer;     // Correct answer (text)
        private Integer points;           // Points (default 1)
        private String explanation;       // Optional
        private String difficulty;        // BEGINNER / INTERMEDIATE / ADVANCED
    }
}
