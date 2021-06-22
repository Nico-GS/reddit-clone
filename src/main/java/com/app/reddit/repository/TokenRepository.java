package com.app.reddit.repository;

import com.app.reddit.models.AccountVerificationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<AccountVerificationToken, Long> {

    Optional<AccountVerificationToken> findByToken(String token);

}
