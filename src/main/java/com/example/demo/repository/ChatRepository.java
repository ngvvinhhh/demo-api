package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    // Sử dụng @Query để viết truy vấn JPQL
    @Query("SELECT c FROM Chat c WHERE c.user = :user ORDER BY c.create_at ASC")
    List<Chat> findByUserOrderByCreate_atAsc(@Param("user") Account user);

    // Sử dụng @Query cho phương thức còn lại
    @Query("SELECT c FROM Chat c WHERE c.user = :user AND c.nameRecipient = :nameRecipient ORDER BY c.create_at ASC")
    List<Chat> findByUserAndNameRecipientOrderByCreate_atAsc(@Param("user") Account user, @Param("nameRecipient") String nameRecipient);
}
