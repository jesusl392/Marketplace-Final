package com.example.MarcketPlaceUniversitario.repository;

import com.example.MarcketPlaceUniversitario.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.leido = true WHERE m.sender.id = :senderId AND m.receiver.id = :receiverId AND m.leido = false")
    void marcarLeidos(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
}
