package uz.ejavlon.demoapp.service;

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

@Service
public class JwtService {

    @Value("${token.secret.key}")
    private  String secret;
    public String extractUsername(String jwt) {
        return extractClaims(jwt, Claims::getSubject);
    }

    private <T> T extractClaims(String jwt, Function<Claims,T> claimsResolver) {
        final Claims claims = extracAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    private Claims extracAllClaims(String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private Key getSecretKey() {
        byte [] bytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generetToken(UserDetails userDetails){
        return generetToken(new HashMap<>(),userDetails);
    }
    public String generetToken(Map<String,Object> extractClaims,UserDetails userDetails){

        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 604800000))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenExpired(String jwt) {
        return extractClaims(jwt,Claims::getExpiration).before(new Date());
    }
}
