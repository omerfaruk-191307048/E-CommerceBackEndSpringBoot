package com.securityVideoProject.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securityVideoProject.security.business.JwtService;
import com.securityVideoProject.security.dataAccess.abstracts.TokenRepository;
import com.securityVideoProject.security.dataAccess.abstracts.UserRepository;
import com.securityVideoProject.security.entities.token.Token;
import com.securityVideoProject.security.auth.enums.TokenType;
import com.securityVideoProject.security.auth.enums.Role;
import com.securityVideoProject.security.entities.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) { //bir kullanıcı oluşturup onu veri tabanına kaydetmemize ve oluşturulan jetonu geri göndermemizi sağlar
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) { //kimlik doğrulama
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        //var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    private void revokeAllUserTokens(User user) { //User parametresini tüm token'ları, mevcut ve geçerli kullanıcı token'larını vt'dan çekmek için kullanıyoruz
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) { //Token'ı kaydediyoruz
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }


    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION); //Bir çağrı yaptığımızda Jwt kimlik doğrulama jetonunun başlığın içine aktarmamız gerekir
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken); // to do extract the userEmail from JWT token - JWT jetonundan userEmail'i çıkarmak için yapılacaklar
        if (userEmail != null){ //doğrulama boş olduğunda kullanıcının henüz kimliğinin doğrulanmadığı, kullanıcının henüz bağlanmadığı anlamına gelir
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) { //Yenileme jetonunu saklamamız gerekir, böylece yenileme jetonunu değiştirmemize gerek kalmaz. Aksi takdirde erişim jetonunu her yenilediğimizde, yenileme jetonunu da yenileriz, ancak yenileme jetonunun aynı kalması gerekir
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
