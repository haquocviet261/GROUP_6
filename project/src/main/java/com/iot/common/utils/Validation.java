package com.iot.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

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

    public static long calculateDaysLeft(int expiredDate, Date updatedAt) {
        Calendar expirationCal = Calendar.getInstance();
        expirationCal.setTime(updatedAt);
        expirationCal.add(Calendar.DAY_OF_MONTH, expiredDate);

        Date expirationDate = expirationCal.getTime();
        Date currentDate = new Date();

        long diffInMillis = expirationDate.getTime() - currentDate.getTime();

        return diffInMillis / (1000 * 60 * 60 * 24);
    }

    public static Date calculateExpirationDate(int expiredDate, Date updatedAt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(updatedAt);
        calendar.add(Calendar.DAY_OF_MONTH, expiredDate);
        return calendar.getTime();
    }

    public static Date startOfDay(Date specificDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(specificDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date endOfDay(Date specificDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(specificDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

}
