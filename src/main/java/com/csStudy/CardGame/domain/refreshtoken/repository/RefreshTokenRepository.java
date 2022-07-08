package com.csStudy.CardGame.domain.refreshtoken.repository;

import com.csStudy.CardGame.domain.refreshtoken.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;


public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
