package com.nav.ChatApi.repositories;

import com.nav.ChatApi.entities.Group;
import com.nav.ChatApi.entities.GroupInvite;
import com.nav.ChatApi.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupInviteRepository extends CrudRepository<GroupInvite, Long> {

    public List<GroupInvite> findAllByReceiverEmail(String receiverEmail);

    public Optional<GroupInvite> findByGroupAndSenderAndReceiverEmailAndActive(Group group, User sender, String receiverEmail, boolean active);

    public List<GroupInvite> findBySenderOrReceiverEmail(User sender, String receiverEmail);
}
