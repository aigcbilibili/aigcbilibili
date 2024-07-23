package ljl.bilibili.chat.ppt;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64; // Assuming Apache Commons Codec is used

public class ApiAuthAlgorithm {

    /**
     * Gets the signature for the given appId and secret.
     *
     * @param appId  The appId used as a key for the signature.
     * @param secret The secret key used for the signature.
     * @param ts     The timestamp.
     * @return The generated signature.
     */
    public String getSignature(String appId, String secret, long ts) {
        try {
            String auth = md5(appId + ts);
            return hmacSHA1Encrypt(auth, secret);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            // Log the exception for debugging
            e.printStackTrace();
            return null;
        }
    }

    /**
     * HMAC SHA1 encryption.
     *
     * @param encryptText The text to be encrypted.
     * @param encryptKey  The encryption key.
     * @return The encrypted string.
     * @throws NoSuchAlgorithmException If the algorithm is not available.
     * @throws InvalidKeyException      If the key is invalid.
     */
    private String hmacSHA1Encrypt(String encryptText, String encryptKey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec keySpec = new SecretKeySpec(
                encryptKey.getBytes(StandardCharsets.UTF_8), "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(keySpec);
        byte[] result = mac.doFinal(encryptText.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeBase64String(result);
    }

    /**
     * Generates MD5 hash of the given text.
     *
     * @param text The text to be hashed.
     * @return The MD5 hash of the text.
     * @throws NoSuchAlgorithmException If the MD5 algorithm is not available.
     */
    private String md5(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(text.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
