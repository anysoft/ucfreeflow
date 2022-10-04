package tk.anysoft.freeflow;

import org.junit.Test;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    public static String rQ(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        if (str.indexOf("://") < 0) {
            str = "http://" + str;
        }
        try {
            return new URL(str).getHost();
        } catch (Throwable th) {
            return "";
        }
    }

    @Test
    public void test() throws NoSuchAlgorithmException {
        String orderId= "1582199711111286561";
        String key = "9ac4932dc101da8ae14a38ab1ba59c9c";
        String url = "http://baidu.comp";
        char[] cQe = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        String str2 = "" + "|" + "" + "|" + rQ("");
        MessageDigest instance = MessageDigest.getInstance("MD5");
        instance.reset();
        instance.update(str2.getBytes());
        byte[] digest = instance.digest();
        String str = "";
        StringBuilder sb = new StringBuilder();
        for (byte b2 : digest) {
            char c = cQe[(b2 & 240) >> 4];
            char c2 = cQe[b2 & 15];
            sb.append(c);
            sb.append(c2).append(str);
        }


        System.out.println(sb.toString().toLowerCase());
    }
}