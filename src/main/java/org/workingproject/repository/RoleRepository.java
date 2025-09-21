package org.workingproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workingproject.entity.Role;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRoleName(String roleName);
}
