package com.securityVideoProject.security.config;

import com.securityVideoProject.security.business.JwtService;
import com.securityVideoProject.security.dataAccess.abstracts.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Jwt implementasyonunda ilk uygulanan şey JwtAuthFilter'dır ve Jwt Token'imizin olup olmadığını kontrol eder
@Component
@RequiredArgsConstructor
//Class içinde final ve NonNull olan değişkenleri parametre olarak alan bir constructor oluşturur
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization"); //Bir çağrı yaptığımızda Jwt kimlik doğrulama jetonunun başlığın içine aktarmamız gerekir
        final String jwtToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwtToken); // to do extract the userEmail from JWT token - JWT jetonundan userEmail'i çıkarmak için yapılacaklar
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) { //doğrulama boş olduğunda kullanıcının henüz kimliğinin doğrulanmadığı, kullanıcının henüz bağlanmadığı anlamına gelir
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            var isTokenValid = tokenRepository.findByToken(jwtToken)
                    .map(t -> !t.isExpired() && !t.isRevoked()) //boolean ile esliyoruz
                    .orElse(false);
            if (jwtService.isTokenValid(jwtToken, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, //kimlik bilgilerimiz şuanlık olmadığı için null
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request, response);
        }

    }
}
