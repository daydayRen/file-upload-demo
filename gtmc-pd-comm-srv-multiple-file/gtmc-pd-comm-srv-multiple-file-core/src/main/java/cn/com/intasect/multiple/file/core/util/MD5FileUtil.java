package cn.com.intasect.multiple.file.core.util;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: renyitian
 * @Date: 2024/3/15 15:41
 * @Description:
 */
public class MD5FileUtil {
    public static String getMd5OfFile(MultipartFile file) {

        try {
            // 获取MultipartFile的输入流
            InputStream is = file.getInputStream();
            // 创建MessageDigest实例，这里使用MD5算法
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 创建DigestInputStream
            DigestInputStream dis = new DigestInputStream(is, md);


            byte[] buffer = new byte[1024];
            while (dis.read(buffer) > 0); // Read through the input stream to update the digest

            BigInteger bigInt = new BigInteger(1, md.digest());
            String md5Hex = bigInt.toString(16);

            // 补零至32位
            while (md5Hex.length() < 32) {
                md5Hex = "0" + md5Hex;
            }

            return md5Hex;

        } catch (Exception e) {
            throw new RuntimeException("Error calculating MD5 for file: " + file.getOriginalFilename(), e);
        }
    }
}
