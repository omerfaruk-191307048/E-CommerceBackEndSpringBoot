package com.securityVideoProject.security.business;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//Jwt tokeninin olup olmadığını JwtAuthFilter'de kontrol ettikten sonra yapmamız gereken bu sınıfı çağırıp kullanıcının zaten veritabanımızda olup olmadığını kontrol etmek
@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T>claimsResolver) {
        final Claims claims=extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims) //to set our claims
                .setSubject(userDetails.getUsername()) //to set our subject
                .setIssuedAt(new Date(System.currentTimeMillis())) //son kullanma tarihini hesaplamamıza ya da jetonun hala geçerli olup olmadığını kontrol etmemize yardımcı olur
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) //token'in ne kadar geçerli olduğu ayarı-24 saat artı 1000 milisaniye geçerli olacak
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) //hangi alogoritma ile kullanmak istiyorsak
                .compact(); //jetonu oluşturup geri döner
    }
    public boolean isTokenValid(String jwtToken, UserDetails userDetails){ //kullanıcı ayrıntılarına eşit olup olmadığını kontrol ediyoruz
        final  String username = extractUsername(jwtToken);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    private Claims extractAllClaims(String jwtToken){
        return Jwts
                .parserBuilder() //patopars'a jetonu iletmek için
                .setSigningKey(getSignInKey()) //Token oluşturmaya veya kodunu çözmeye çalıştığımzda imzalama anahtarını kullanacağız. JWT'nin imza kısmını oluşturmak için kullanılır, jwt'yi gönderenin iddia ettiği kişi olduğunu doğrulamak için kullanılır
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

