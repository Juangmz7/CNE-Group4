package com.cne_project.cne_project.service;


import com.cne_project.cne_project.utils.TokenPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.keys.private.base64}")
    private String privateKeyBase64;

    @Value("${jwt.keys.public.path}")
    private String publicKeyPath;

    // Hold the actual keys
    private PrivateKey privateKey;
    @Getter
    private PublicKey publicKey;

    // Convert Resource -> Key on startup
    @PostConstruct
    public void initKeys() throws Exception {
        this.privateKey = parsePrivateKey(privateKeyBase64);
        this.publicKey = readPublicKey(publicKeyPath);
    }

    private PrivateKey parsePrivateKey(String base64Key) throws Exception {
        String cleanKey = base64Key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", ""); // Remove newlines/spaces

        byte[] encoded = Base64.getDecoder().decode(cleanKey);
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(encoded));
    }

    private PublicKey readPublicKey(String path) throws Exception {
        String key = new String(Files.readAllBytes(Paths.get(path)))
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] encoded = Base64.getDecoder().decode(key);
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encoded));
    }

    public String generateToken(TokenPayload payload) {
        Map<String, Object> claims = new HashMap<>();

        long JWT_EXPIRATION = 1000 * 60 * 30;
        return Jwts.builder()
                .header().keyId("user-key-id").and()
                .claims(claims)
                .subject(payload.getUserId())
                .signWith(privateKey, Jwts.SIG.RS256)
                .issuedAt(new Date(System.currentTimeMillis()))
                // 30 minutes for token expiration
                .expiration( new Date(System.currentTimeMillis() + JWT_EXPIRATION) )
                .compact();
    }

    public TokenPayload extractPayload(String token) {

        String userId =  extractClaim(token, Claims::getSubject);

        return new TokenPayload(
                userId
        );
    }

    public boolean validateToken(String token, TokenPayload payload) {
        boolean validUsername = extractPayload(token).getUserId()
                .equals(payload.getUserId());

        // If the token has expired
        boolean tokenExpired = isTokenExpired(token);

        return validUsername && !tokenExpired;
    }

    public String extractAuthToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public Duration tokenTtl(String token) {
        return Duration.between(
                new Date(System.currentTimeMillis()).toInstant(),
                extractExpiration(token).toInstant()
        );
    }

    /**
     * Extracts from the token the claim indicated in claimResolver
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }


    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token) // If signature is fake, this throws an Exception
                .getPayload();
    }

    // Verifies token expiration
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}