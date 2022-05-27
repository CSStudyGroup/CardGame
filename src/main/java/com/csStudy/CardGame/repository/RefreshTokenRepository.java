package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;


public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
