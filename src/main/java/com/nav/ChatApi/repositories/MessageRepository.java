package com.nav.ChatApi.repositories;

import com.nav.ChatApi.entities.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
    public List<Message> findAllByGroupId(Long groupId);
}
