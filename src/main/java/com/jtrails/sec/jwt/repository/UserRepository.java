/**
 * 
 */
package com.jtrails.sec.jwt.repository;

import java.util.Optional;

/**
 * @author Lejil
 *
 */
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jtrails.sec.jwt.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}