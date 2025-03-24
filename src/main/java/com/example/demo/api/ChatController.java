package com.example.demo.api;

import com.example.demo.entity.Chat;
import com.example.demo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping
    public ResponseEntity<List<Chat>> getChatsByUserId(@RequestParam Long userId) {
        List<Chat> chats = chatService.getChatsByUserId(userId);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/recipient")
    public ResponseEntity<List<Chat>> getChatsByUserIdAndRecipient(
            @RequestParam Long userId,
            @RequestParam String nameRecipient) {
        List<Chat> chats = chatService.getChatsByUserIdAndRecipient(userId, nameRecipient);
        return ResponseEntity.ok(chats);
    }

    @PostMapping
    public ResponseEntity<Chat> createChat(
            @RequestParam Long userId,
            @RequestParam String message,
            @RequestParam String nameRecipient,
            @RequestParam(required = false) String avatarRecipient) {
        Chat chat = chatService.createChat(userId, message, nameRecipient, avatarRecipient);
        return ResponseEntity.ok(chat);
    }
}