/**
 * 
 */
package com.jtrails.sec.jwt.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jtrails.sec.jwt.model.Role;
import com.jtrails.sec.jwt.model.User;

/**
 * @author Lejil
 *
 */

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
	Optional<User> findByRoleName(String roleName);
}