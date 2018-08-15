package com.nilo.dms.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wenzhuo-company on 2018/3/28.
 */
public class JWTUtil {

    private static final String KEY = "m_api";



    private static Key getSecretKey() {
        return new SecretKeySpec(KEY.getBytes(), "AES");
    }

    public static String createTokenJWT(String msg, int afterDay) {

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, afterDay);

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(msg)
                .setIssuedAt(new Date())
                .setExpiration(calendar.getTime())
                .signWith(SignatureAlgorithm.HS256, getSecretKey());

        return jwtBuilder.compact();
    }

    public static String createTokenJWT(String msg, Date date) {

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(msg)
                .setIssuedAt(new Date())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS256, getSecretKey());

        return jwtBuilder.compact();
    }

    public static Claims parseTokenJWT(String token) {
        Claims body = null;
        try {
            body = Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody();
        } catch (Exception e) {
        }
        return body;
    }

    public static boolean isValidTokenJWT(String token) {
        Claims body = parseTokenJWT(token);
        return body == null ? false : true;
    }

}
