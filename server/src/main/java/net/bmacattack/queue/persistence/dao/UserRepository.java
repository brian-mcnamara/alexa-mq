package net.bmacattack.queue.persistence.dao;

import net.bmacattack.queue.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByUsername(String username);

    @Override
    void delete(User user);
}
