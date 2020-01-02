package com.cn.frame.utils;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * @author Mr.Zheng
 * @date 2014年8月22日 下午1:44:23
 */
public final class RsaUtils {
    private static String RSA = "RSA";
    private static String RSA_ECB = "RSA/ECB/PKCS1Padding";
    private static String PUBLIC_KEY = "MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgFiOgJAO3Dl" +
            "+d1QJsqNQaSuBLf23Tn1jL02XSQmsSZTV9OsIKk0MYXtJLDHKiut/Ry5tSQ4uEOMRYyrhGdY/HCjYbdH" +
            "/yrCp0A6Rhmkrix40INk2VqAaV8ZRsIWdIXsrNsSDCCW3C94o83naWHCqv8AYJi" +
            "/+KwWT64xQvEjbpb7hAgMBAAE=";

    /**
     * 用公钥加密 <br>
     * 每次加密的字节数，不能超过密钥的长度值减去11
     */
    public static String encryptData(String data) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ECB);
            // 编码前设定编码方式及密钥
            cipher.init(Cipher.ENCRYPT_MODE, loadPublicKey());
            // 传入编码数据并返回编码结果
            return Base64.encodeToString(cipher.doFinal(data.getBytes()), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从字符串中加载公钥
     */
    private static PublicKey loadPublicKey() throws Exception {
        try {
            byte[] buffer = Base64.decode(PUBLIC_KEY, Base64.NO_WRAP);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

}