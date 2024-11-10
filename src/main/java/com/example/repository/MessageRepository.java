package com.example.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

//interacts with the database and allows users to execute CRUD operations 
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findByMessageId(int id);
    List<Message> findByPostedBy(int postedBy);
}
