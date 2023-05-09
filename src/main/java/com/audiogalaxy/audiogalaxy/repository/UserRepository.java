package com.audiogalaxy.audiogalaxy.repository;

import com.audiogalaxy.audiogalaxy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface UserRepository extends JpaRepository<User, Long> {
}
