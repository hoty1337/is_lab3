package my.hoty.lab3xx.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha512PasswordEncoder implements PasswordEncoder {
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b)); // Преобразуем в hex
        }
        return sb.toString();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(rawPassword.toString().getBytes());

            return bytesToHex(digest);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 algorithm not supported", e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
