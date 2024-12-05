package org.project.controllers;

import org.project.services.QwenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@Controller
@RequestMapping("/api")
public class MainController {

    private final QwenService qwenService = new QwenService();

    @PostMapping("/chat")
    @ResponseBody
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        try {
            String answer = qwenService.sendMessage(request.getMessage());
            ChatResponse response = new ChatResponse();
            response.setAnswer(answer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            ChatResponse errorResponse = new ChatResponse();
            errorResponse.setAnswer("Произошла ошибка при отправке сообщения.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ChatResponse> handleException(Exception e) {
        e.printStackTrace();
        ChatResponse errorResponse = new ChatResponse();
        errorResponse.setAnswer("Произошла внутренняя ошибка сервера.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

class ChatRequest {
    private String message;

    // Геттеры и сеттеры
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

class ChatResponse {
    private String answer;

    // Геттеры и сеттеры
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}