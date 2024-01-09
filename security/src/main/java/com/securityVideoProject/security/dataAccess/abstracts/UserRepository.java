package com.securityVideoProject.security.dataAccess.abstracts;

import com.securityVideoProject.security.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findUserByFirstnameContains(String name);
    List<User> findUserByFirstname(String name);
    List<User> findByCreatedAtBetween(Date start, Date end);
}
