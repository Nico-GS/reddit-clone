package com.app.reddit.security;

import com.app.reddit.exception.ActivationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

import static io.jsonwebtoken.Jwts.parserBuilder;

@Service
public class JWTProvider {

    private KeyStore keystore;

    @PostConstruct
    public void init() {
        try {
            keystore = KeyStore.getInstance("JKS");
            InputStream resourceStream = getClass().getResourceAsStream("/keystore.jks");
            keystore.load(resourceStream, "rootroot".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new ActivationException("Exception occurred while loading keystore");
        }
    }

    public String generateToken(Authentication authentication) {
        org.springframework.security.core.userdetails.User princ = (User) authentication.getPrincipal();
        return Jwts.builder().setSubject(princ.getUsername()).signWith(getPrivKey()).compact();
    }

    public boolean validateToken (String token) {
        parserBuilder().setSigningKey(getPubKey()).build().parseClaimsJws(token);
        return true;
    }

    // modifier alias / refaire keystore
    private PrivateKey getPrivKey () {
        try {
            return (PrivateKey) keystore.getKey("alias", "rootroot".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new ActivationException("Exception occurred while retrieving public key");
        }
    }

    private PublicKey getPubKey () {
        try {
            return keystore.getCertificate("vox").getPublicKey();
        } catch(KeyStoreException e) {
            throw new ActivationException("Exception occurred retrieving public key");
        }
    }

    public String getNameFromJWT(String token) {
        Claims claims = parserBuilder()
                .setSigningKey(getPubKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
