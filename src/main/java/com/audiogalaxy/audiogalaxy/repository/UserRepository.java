package com.audiogalaxy.audiogalaxy.repository;

import com.audiogalaxy.audiogalaxy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
