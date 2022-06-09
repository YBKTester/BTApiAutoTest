package com.bitmart.utils;



import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


/**
 * @Desc SignUtils
 * @Author admin
 * @DateTime 2022/5/26  5:06 下午
 */
public class SignUtils {

    public static Integer getRandom() {
        Random random = new Random();
        return random.nextInt(99999999);
    }

    public static String getSortUrl(Map<String, Object> params, String salt) {
        Set set = params.keySet();
        Object[] str = set.toArray();
        Arrays.sort(str);
        Collection<String> keys = params.keySet();
        List<String> list = new ArrayList<>(keys);
        Collections.sort(list);
//        System.out.println(list.toString());
        String url = "";
        for (String s : list) {
            if (!(s.equals("timestamp") || s.equals("sign"))) {
                url = url + s + "=" + params.get(s).toString() + "&";
            }
        }
//        System.out.println(url.substring(0,url.length()-1));
        return url.substring(0, url.length() - 1).concat(params.get("timestamp").toString()).concat(salt);
    }

    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }
}













