package com.example.skillforge.service.impl;

import com.example.skillforge.dto.request.AIQuizGenerationRequest;
import com.example.skillforge.dto.response.AIQuizResponse;
import com.example.skillforge.service.AIQuizGeneratorService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AIQuizGeneratorServiceImpl implements AIQuizGeneratorService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Active verified Gemini models supporting generateContent with fallback
    private static final String[] CANDIDATE_MODELS = {
            "gemini-3.6-flash",
            "gemini-3-flash-preview",
            "gemini-3.1-flash-lite",
            "gemini-3.8-flash"
    };

    @Override
    public AIQuizResponse generateQuestions(AIQuizGenerationRequest request) {
        // keep this for backward compatibility if other code calls generateQuestions
        return generateQuiz(request);
    }

    // This must match the interface signature exactly.
    @Override
    public AIQuizResponse generateQuiz(AIQuizGenerationRequest req) {
        String prompt = buildSafePrompt(req);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(25, java.util.concurrent.TimeUnit.SECONDS)
                .build();

        String payload = "{\"contents\":[{\"parts\":[{\"text\":\"" +
                prompt.replace("\"", "\\\"") +
                "\"}]}]}";

        RequestBody body = RequestBody.create(
                payload,
                MediaType.parse("application/json")
        );

        Exception lastException = null;

        for (String modelName : CANDIDATE_MODELS) {
            try {
                String modelUrl = "https://generativelanguage.googleapis.com/v1beta/models/" + modelName + ":generateContent";
                HttpUrl url = HttpUrl.parse(modelUrl)
                        .newBuilder()
                        .addQueryParameter("key", geminiApiKey)
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();

                if (response.body() == null) {
                    log.warn("Gemini model {} returned null body. Trying next candidate...", modelName);
                    continue;
                }

                String responseBody = response.body().string();
                log.info("Gemini API Response from [{}]: {}", modelName, responseBody);

                if (!response.isSuccessful() && (response.code() == 503 || response.code() == 429 || response.code() == 404)) {
                    log.warn("Gemini model {} returned HTTP {}. Trying next candidate...", modelName, response.code());
                    continue;
                }

                return parseGeminiResponse(responseBody);

            } catch (Exception e) {
                log.warn("Gemini model {} failed with: {}. Trying next candidate...", modelName, e.getMessage());
                lastException = e;
            }
        }

        log.error("All candidate Gemini models failed for AI Quiz generation.");
        throw new RuntimeException("Failed to generate quiz from Gemini: " + (lastException != null ? lastException.getMessage() : "All models unavailable"), lastException);
    }

    private String buildSafePrompt(AIQuizGenerationRequest req) {
        // Ensure this matches the fields your request DTO actually exposes (e.g. getNumberOfQuestions)
        int count = 1;
        try {
            // try a few common getter names for backward compatibility
            if (req.getNumberOfQuestions() != 0) count = req.getNumberOfQuestions();
        } catch (Throwable ignored) {
            try {
                if (req.getNumberOfQuestions() != 0) count = req.getNumberOfQuestions();
            } catch (Throwable ignored2) {
                // fallback default 5
                count = 5;
            }
        }

        String difficulty = "BEGINNER";
        try {
            difficulty = Optional.ofNullable(req.getDifficulty()).orElse("BEGINNER");
        } catch (Throwable ignored) {}

        String topic = Optional.ofNullable(req.getTopicName()).orElse("General Programming");

        return
                "Generate " + count + " MCQ questions strictly in VALID JSON.\n" +
                        "NO text outside JSON. NO explanations outside JSON.\n\n" +
                        "RETURN EXACT JSON ONLY:\n" +
                        "{\n" +
                        "  \"questions\": [\n" +
                        "    {\n" +
                        "      \"questionText\": \"\",\n" +
                        "      \"options\": [\"A\", \"B\", \"C\", \"D\"],\n" +
                        "      \"correctAnswer\": \"\",\n" +
                        "      \"points\": 1,\n" +
                        "      \"explanation\": \"\",\n" +
                        "      \"difficulty\": \"" + difficulty + "\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}\n\n" +
                        "Topic: " + topic + "\n";
    }

    /**
     * Robustly parse responses coming from the Gemini / generativelanguage API.
     * Handles:
     *  - responses containing an \"error\" object
     *  - responses with candidates -> content -> parts -> text
     *  - text wrapped in markdown fences (```json ...```), single/backticks, or extra leading text
     */
    private AIQuizResponse parseGeminiResponse(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);

            // 1) If the API returned an error object, surface it with a clear message.
            if (root.has("error")) {
                JsonNode err = root.path("error");
                String msg = err.has("message") ? err.path("message").asText() : err.toString();
                log.error("Gemini returned error node: {}", msg);
                throw new RuntimeException("Gemini API error: " + msg);
            }

            // 2) Find text inside candidates -> [0] -> content -> parts -> [0] -> text
            JsonNode candidates = root.path("candidates");
            if (candidates.isMissingNode() || !candidates.isArray() || candidates.size() == 0) {
                // Some responses use top-level 'content' or different shapes; try a few fallbacks:
                JsonNode topContent = root.at("/content/parts/0/text");
                if (!topContent.isMissingNode() && topContent.isTextual()) {
                    String raw = topContent.asText();
                    String stripped = stripCodeFences(raw);
                    return objectMapper.readValue(stripped, AIQuizResponse.class);
                }

                log.error("No candidates found and no fallback content. Root: {}", root.toString());
                throw new RuntimeException("Gemini returned no candidates/content");
            }

            JsonNode first = candidates.get(0);
            JsonNode content = first.path("content");
            JsonNode parts = content.path("parts");
            if (parts.isMissingNode() || !parts.isArray() || parts.size() == 0) {
                log.error("Candidates present but no content.parts text. Candidates: {}", candidates.toString());
                throw new RuntimeException("Gemini returned no content parts");
            }

            JsonNode textNode = parts.get(0).path("text");
            if (textNode.isMissingNode() || !textNode.isTextual()) {
                log.error("Expected text node missing in parts[0].");
                throw new RuntimeException("Gemini returned empty text part");
            }

            String extracted = textNode.asText();
            String cleaned = stripCodeFences(extracted);

            // The model sometimes prepends text before the JSON; find first '{'
            int firstBrace = cleaned.indexOf('{');
            if (firstBrace > 0) {
                cleaned = cleaned.substring(firstBrace);
            }

            // Now parse into your DTO
            return objectMapper.readValue(cleaned, AIQuizResponse.class);

        } catch (com.fasterxml.jackson.core.JsonProcessingException jpe) {
            log.error("Failed to parse Gemini JSON into AIQuizResponse", jpe);
            throw new RuntimeException("Invalid JSON returned by Gemini: " + jpe.getMessage(), jpe);
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            log.error("Unexpected error parsing Gemini response", e);
            throw new RuntimeException("Unexpected parse error: " + e.getMessage(), e);
        }
    }

    /**
     * Remove markdown fences and any triple-backtick code fences commonly returned by models.
     * Examples it handles:
     *  ```json\n{ ... }\n```
     *  `{"questions": [...]}` (single-backtick)
     *  Leading/trailing whitespace
     */
    private String stripCodeFences(String s) {
        if (s == null) return "";
        String r = s.trim();

        // Remove triple backtick fences ```json ... ```
        if (r.startsWith("```")) {
            // drop leading ```... and trailing ```
            int start = r.indexOf("\n");
            if (start >= 0) {
                r = r.substring(start + 1);
            } else {
                r = r.substring(3);
            }
            // if there's trailing ```
            int endFence = r.lastIndexOf("```");
            if (endFence >= 0) {
                r = r.substring(0, endFence);
            }
            return r.trim();
        }

        // Remove single backticks
        if (r.startsWith("`") && r.endsWith("`")) {
            r = r.substring(1, r.length() - 1);
            return r.trim();
        }

        // If it starts with something like "```json\n{...}\n```" but not exactly,
        // try to remove any leading ```json or ``` and trailing ```
        if (r.contains("```")) {
            int firstFence = r.indexOf("```");
            int lastFence = r.lastIndexOf("```");
            if (firstFence != lastFence && lastFence > firstFence) {
                r = r.substring(firstFence + 3, lastFence);
                return r.trim();
            }
        }

        return r;
    }
}
