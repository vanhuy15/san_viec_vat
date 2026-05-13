package com.viecvat.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String getMd5(String input) {
        try {
        	// 1. Chọn thuật toán MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            // 2. Băm chuỗi đầu vào (input) thành mảng byte
            md.update(input.getBytes());
            byte[] digest = md.digest(); // Kết quả là một mảng các byte loằng ngoằng
            
            // 3. Chuyển mảng byte thành chuỗi Hex (Hệ 16) để con người đọc được
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                // %02x: In ra 2 ký tự Hex.
                // b & 0xff: Phép toán Bitwise để xử lý số âm trong Java (chuyển về số dương)
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString(); // Trả về chuỗi mã hóa
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}