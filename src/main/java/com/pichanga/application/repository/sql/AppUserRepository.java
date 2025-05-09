package com.pichanga.application.repository.sql;

import com.pichanga.application.entity.sql.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE AppUser a SET a.enabled = TRUE WHERE a.email = :email")
    void enableAppUser(@Param("email") String email);
}
