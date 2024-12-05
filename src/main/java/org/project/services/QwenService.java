package org.project.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

@PropertySource("properties.yml")
public class QwenService {

    private static final String API_URL = "${qwen.api.url}"; // Замените на реальный URL API Qwen
    private static final String API_KEY = "${qwen.api.url}"; // Ваш API ключ для доступа к API Qwen

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public String sendMessage(String message) throws IOException {
        HttpPost httpPost = new HttpPost(API_URL);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + API_KEY);

        // Создаем тело запроса
        String jsonRequest = objectMapper.writeValueAsString(new RequestBody(message));
        httpPost.setEntity(new StringEntity(jsonRequest));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                ResponseBody responseBody = objectMapper.readValue(result, ResponseBody.class);
                return responseBody.getAnswer(); // Предполагается, что ответ содержит поле "answer"
            }
        }
        return null;
    }

    // Класс для сериализации тела запроса
    static class RequestBody {
        private final String prompt;

        public RequestBody(String prompt) {
            this.prompt = prompt;
        }

        public String getPrompt() {
            return prompt;
        }
    }

    // Класс для десериализации тела ответа
    static class ResponseBody {
        private String answer;

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}
