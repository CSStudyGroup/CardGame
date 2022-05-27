package com.csStudy.CardGame.service;

import com.csStudy.CardGame.dto.RefreshTokenDto;
import com.csStudy.CardGame.mapper.RefreshTokenMapper;
import com.csStudy.CardGame.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenMapper refreshTokenMapper;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository,
                                   RefreshTokenMapper refreshTokenMapper) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenMapper = refreshTokenMapper;
    }

    @Override
    public void save(RefreshTokenDto refreshTokenDto) {
        refreshTokenRepository.save(refreshTokenMapper.toEntity(refreshTokenDto));
    }

    @Override
    public Optional<RefreshTokenDto> findById(String id) {
        return refreshTokenRepository.findById(id)
                .map(refreshTokenMapper::toDto);
    }

    @Override
    public void deleteById(String id) {
        refreshTokenRepository.deleteById(id);
    }

}
