package hr.algebra.iisProject.service;

import hr.algebra.iisProject.model.RefreshToken;
import hr.algebra.iisProject.repository.RefreshTokenRepository;
import hr.algebra.iisProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {


    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userRepository.findByUsername(username))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(24 * 60 * 60 * 1000)) // 24 sata
                .build();
        saveOrUpdateToken(refreshToken);
        return refreshToken;
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }

    public void saveOrUpdateToken(RefreshToken refreshToken) {
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserInfo_Id(refreshToken.getUserInfo().getId());
        if (existingToken.isPresent()) {
            RefreshToken token = existingToken.get();
            token.setToken(refreshToken.getToken());
            token.setExpiryDate(refreshToken.getExpiryDate());
            refreshTokenRepository.save(token);
        } else {
            refreshTokenRepository.save(refreshToken);
        }
    }


}
