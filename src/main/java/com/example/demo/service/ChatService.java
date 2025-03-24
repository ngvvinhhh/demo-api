package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Chat;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Chat> getChatsByUserId(Long userId) {
        Account user = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return chatRepository.findByUserOrderByCreate_atAsc(user);
    }

    public List<Chat> getChatsByUserIdAndRecipient(Long userId, String nameRecipient) {
        Account user = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return chatRepository.findByUserAndNameRecipientOrderByCreate_atAsc(user, nameRecipient);
    }

    public Chat createChat(Long userId, String message, String nameRecipient, String avatarRecipient) {
        Account user = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Chat chat = new Chat();
        chat.setUser(user);
        chat.setMessage(message);
        chat.setNameRecipient(nameRecipient);
        chat.setAvatarRecipient(avatarRecipient);
        chat.setCreate_at(LocalDateTime.now());

        return chatRepository.save(chat);
    }
}