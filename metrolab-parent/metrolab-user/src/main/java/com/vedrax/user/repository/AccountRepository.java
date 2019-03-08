package com.vedrax.user.repository;

import com.vedrax.user.domain.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * The account repository
 *
 * @author remypenchenat
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Find account by email
     *
     * @param email
     * @return
     */
    Optional<Account> findByEmail(String email);
}
