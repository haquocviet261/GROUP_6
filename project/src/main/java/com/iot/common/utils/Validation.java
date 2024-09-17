package com.iot.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Validation {
    public static final String OK = "Success";
    public static final String FAIL = "Fail";
    private static final String CLIENT_ID = "756081284225-uv9bcn4q0q7uuksosnsn59guqttn0db5.apps.googleusercontent.com";
    public static Claims verifyToken(String idTokenString) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(CLIENT_ID.getBytes());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RS256");
            PublicKey publicKey = keyFactory.generatePublic(spec);

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(idTokenString);

            return claimsJws.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
