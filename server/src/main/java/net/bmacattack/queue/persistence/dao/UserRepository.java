package net.bmacattack.queue.persistence.dao;

import net.bmacattack.queue.persistence.model.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    @Cacheable("getUserByUsername")
    User getUserByUsername(String username);

    @Override
    @CacheEvict(value = "getUserByUsername", key = "#p0.getUsername")
    void delete(User user);

    @Override
    @CacheEvict(value = "getUserByUsername", key = "#p0.getUsername")
    User save(User entity);
}
