package com.ironmountain.imtest.repositories;

import com.ironmountain.imtest.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsernameIgnoreCase(String username);
    Optional<User> findUserByUsernameIgnoreCaseAndIdIsNot(String username, Integer id);
}
