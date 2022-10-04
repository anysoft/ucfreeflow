package tk.anysoft.uc.freeflow.hook;

import org.junit.Test;

import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.Assert.*;

public class XModelTestTest {
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

    /**
     * [AliFish]
     * orderId=1585428424847145869
     * token=f4ae08bf5d8bd3c65435f1cb3bed4beb
     * openId=7e9c6f78d0367fb9ca51362af1843bb2
     * proxyIp=14.116.199.192
     * proxyPort=8871
     * updateTime=1585939533124
     *
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void test() throws NoSuchAlgorithmException {
        String orderId = "1585428424847145869";
        String key = "f4ae08bf5d8bd3c65435f1cb3bed4beb";
        String url = "http://120.79.194.113:11081";
        char[] cQe = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        String str2 = orderId + "|" + key + "|" + rQ(url);
        MessageDigest instance = MessageDigest.getInstance("MD5");
        instance.reset();
        instance.update(str2.getBytes());
        byte[] digest = instance.digest();
        String str = "";
        System.out.println("md5:" + new BigInteger(digest).toString(16));
        StringBuilder sb = new StringBuilder();
        for (byte b2 : digest) {
            char c = cQe[(b2 & 240) >> 4];
            char c2 = cQe[b2 & 15];
            sb.append(c);
            sb.append(c2).append(str);
        }


        System.out.println(sb.toString().toLowerCase());
    }

    public int getIndex(byte b) {
        int i = b;
        if (i < 0)
            return i + 256;
        return i;

    }

    @Test
    public void base() {
        String base = "ChMxNjU1ODEyMzMyNTA1MTE4MjEyEiA3ZTljNmY3OGQwMzY3ZmI5Y2E1MTM2MmFmMTg0M2JiMhpJCgMyNjgSLemYv+mHjOaWh+WoseWNoS1VQ+a1j+iniOWZqDQwR+WumuWQkea1gemHj+WMhRgAIICAgBQo6O/cqpswMJio8aelMCKNAwgDEJKH9xMYACINMTQuMTE2LjE5OS4xMioEODg4NDIgNGEwNTEyMDBlMzgyZWYyYjc2NTM4MmQ4YzYwNzJhYjQ47tqSk50wQrsCY29tLlVDTW9iaWxlfGNvbS5hdXRvbmF2aS5taW5pbWFwfGNvbS55b3VrdS5waG9uZXxjb20udWN3ZWIuaXBob25lLmRldnxjb20uVUNNb2JpbGUubG92ZXxjb20udWN3ZWIuaXBob25lLmxvd3ZlcnNpb258Y29tLnVjYnJvd3Nlci5pcGhvbmUuY258Y29tLnVjd2ViLmlwaG9uZXxjb20udWNicm93c2VyLmlwaG9uZS53czV8Y29tLnVjYnJvd3Nlci5pcGhvbmV8Y29tLnVjd2ViLmlwaG9uZS5lbnRlcnByaXNlfGNvbS5VQ01vYmlsZS55dW5vc3xjb20uVUNNb2JpbGUuZ3JlZXxjb20uVUNNb2JpbGUueWR8Y29tLmdvbGQuc2pofGNvbS5wcC5hc3Npc3RhbnR8SgbkvJjphbc=";
        byte[] bytes = Base64.getDecoder().decode(base);
        System.out.println(new BigInteger(1, bytes).toString(16));
        System.out.println(new String(bytes));

        // a 13=19
        // 31363535383132333332353035313138323132   // id 1655812332505118212
        // 12 20
        // 3765396336663738643033363766623963613531333632616631383433626232 // openid
        // 1a 49=73
        // 0a03323638122de998bfe9878ce69687e5a8b1e58da12d5543e6b58fe8a788e599a8343047e5ae9ae59091e6b581e9878fe58c851800208080801428e8efdcaa9b303098a8f1a7a530228d030803109287f7131800
        // 22 0d=13
        // 31342e3131362e3139392e3132   //ip
        // 2a 04
        // 38383834     // port
        // 32 20=32
        // 3461303531323030653338326566326237363533383264386336303732616234   //token
        // 38eeda92939d3042bb02

        // 636f6d2e55434d6f62696c657c636f6d2e6175746f6e6176692e6d696e696d61707c636f6d2e796f756b752e70686f6e657c636f6d2e75637765622e6970686f6e652e6465767c636f6d2e55434d6f62696c652e6c6f76657c636f6d2e75637765622e6970686f6e652e6c6f7776657273696f6e7c636f6d2e756362726f777365722e6970686f6e652e636e7c636f6d2e75637765622e6970686f6e657c636f6d2e756362726f777365722e6970686f6e652e7773357c636f6d2e756362726f777365722e6970686f6e657c636f6d2e75637765622e6970686f6e652e656e74657270726973657c636f6d2e55434d6f62696c652e79756e6f737c636f6d2e55434d6f62696c652e677265657c636f6d2e55434d6f62696c652e79647c636f6d2e676f6c642e736a687c636f6d2e70702e617373697374616e747c
        // com.UCMobile|com.autonavi.minimap|com.youku.phone|com.ucweb.iphone.dev|com.UCMobile.love|com.ucweb.iphone.lowversion|com.ucbrowser.iphone.cn|com.ucweb.iphone|com.ucbrowser.iphone.ws5|com.ucbrowser.iphone|com.ucweb.iphone.enterprise|com.UCMobile.yunos|com.UCMobile.gree|com.UCMobile.yd|com.gold.sjh|com.pp.assistant|
        // 4a 06 e4bc98e985b7 //优酷


        int index = 0 + 1;
        int length = getIndex(bytes[index]);
        byte[] data = new byte[length];
        System.arraycopy(bytes, index + 1, data, 0, length);
        String id = new String(data);

        index = index + length + 2;
        length = getIndex(bytes[index]);
        data = new byte[length];
        System.arraycopy(bytes, index + 1, data, 0, length);
        String openId = new String(data);

        index = index + length + 2;
        length = getIndex(bytes[index]);

        //        data = new byte[length];
        //        System.arraycopy(bytes,index+2,data,0,length);
        //        String openId = new String(data);


        index = index + length + 2;
//        length = getIndex(bytes[index]);


        index = index + 2;
        length = getIndex(bytes[index]);

        index = index + length + 2;
        length = getIndex(bytes[index]);
        data = new byte[length];
        System.arraycopy(bytes, index + 1, data, 0, length);
        String ip = new String(data);

        index = index + length + 2;
        length = getIndex(bytes[index]);
        data = new byte[length];
        System.arraycopy(bytes, index + 1, data, 0, length);
        String port = new String(data);

        index = index + length + 2;
        length = getIndex(bytes[index]);
        data = new byte[length];
        System.arraycopy(bytes, index + 1, data, 0, length);
        String token = new String(data);


        System.out.println(id);
        System.out.println(openId);
        System.out.println(openId);
        System.out.println(ip);
        System.out.println(port);
        System.out.println(token);


        System.out.println(new BigInteger(1, "1655812332505118212".getBytes()).toString(16));
        System.out.println(new BigInteger(1, "7e9c6f78d0367fb9ca51362af1843bb2".getBytes()).toString(16));
        System.out.println(new BigInteger(1, "14.116.199.12".getBytes()).toString(16));
        System.out.println(new BigInteger(1, "8884".getBytes()).toString(16));
        System.out.println(new BigInteger(1, "4a051200e382ef2b765382d8c6072ab4".getBytes()).toString(16));


    }
}