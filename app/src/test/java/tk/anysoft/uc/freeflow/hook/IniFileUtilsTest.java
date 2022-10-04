package tk.anysoft.uc.freeflow.hook;

import junit.framework.TestCase;

import org.junit.Test;

import java.io.File;

public class IniFileUtilsTest extends TestCase {

    @Test
    public void test(){

        IniFileUtils ini = new IniFileUtils(new File("E:/a.ini"));
        //token变动时候更新

            ini.set(IniFileUtils.ALI_FISH_TAG,"orderId","a");
            ini.save();

    }
}