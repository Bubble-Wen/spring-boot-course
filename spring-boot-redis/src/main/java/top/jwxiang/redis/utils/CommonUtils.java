package top.jwxiang.utils;

public class CommonUtils {

    /**
     * 校验手机号格式
     */
    public static boolean checkPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return false;
        }
        return phone.matches("^1[3-9]\\d{9}$");
    }

    /**
     * 生成6位短信验证码
     */
    public static int generateCode() {
        return (int) ((Math.random() * 9000) + 1000);
    }
}
