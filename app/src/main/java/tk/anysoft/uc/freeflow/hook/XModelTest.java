package tk.anysoft.uc.freeflow.hook;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tk.anysoft.uc.freeflow.R;

/**
 * TODO:
 * Created by ycss
 * on 2016/8/30 0030.
 */
public class XModelTest implements IXposedHookLoadPackage {


    private static String TAG = "anysoft";

    public int getIndex(byte b) {
        int i = b;
        if (i < 0)
            return i + 256;
        return i;

    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        //我们可以根据包名去hook对应程序的方法，这里我选择hook我们的测试app
        if (loadPackageParam.packageName.equals("com.UCMobile")) {
            XposedBridge.log("load uc");
            Log.v("anysoft-logs", "load uc");
            //getDeviceId为我们正常情况下获取Imei的方法
          /*  XposedHelpers.findAndHookMethod(TelephonyManager.class, "getDeviceId", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return "已经被HOOK";
                }
                //getSubscriberId为我们正常情况下获取Imsi的方法

            });*/
            /*XposedHelpers.findAndHookMethod("com.uc.browser.business.freeflow.b.a.a.d",
                    loadPackageParam.classLoader,
                    "getOrderId",
                    new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.v("anysoft-logs","orderId  " + param.getResult().toString());

                }
            });
            XposedHelpers.findAndHookMethod("com.uc.browser.business.freeflow.b.a.a.d",
                    loadPackageParam.classLoader,
                    "getOpenId",
                    new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.v("anysoft-logs","openId  " + param.getResult().toString());
                }
            });*/

/*            XposedHelpers.findAndHookMethod("com.uc.browser.business.freeflow.b.a.a.a",
                    loadPackageParam.classLoader,
                    "cWG",
                    new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.v("anysoft-logs","cWG  ");
                    Log.v("anysoft-logs","cWG  " + param.getResult().toString());

                    Class cls = param.thisObject.getClass();
                    Field fields[] = cls.getDeclaredFields();
                    for (int i = 0; i < fields.length; i++) {
                        Log.v("anysoft-logs","======【属性】==args["+i+"]=" + fields[i].getName() + "   " + fields[i].get(param.thisObject).toString() );
                        fields[i].setAccessible(true);
                    }
                    Log.v("anysoft-logs","openId  " + param.getResult().toString());
                }
            });*/

            /*XposedHelpers.findAndHookMethod("com.uc.browser.business.freeflow.proxy.b.b",
                    loadPackageParam.classLoader,
                    "aaN",
                    String.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            Log.v("anysoft-logs","aaN");
                            Class<?> aClass = param.thisObject.getClass();
                            Field oMGField = aClass.getDeclaredField("oMG");
                            oMGField.setAccessible(true);
                            Object oMG = oMGField.get(param.thisObject);//oMG object instance
                            Log.v("anysoft-logs","oMG");

                            Field oMeField  = oMG.getClass().getDeclaredField("oMe");
                            oMeField.setAccessible(true);
                            Log.v("anysoft-logs","OrderId " + oMeField.get(oMG).toString());

                            Field oMfField  = oMG.getClass().getDeclaredField("oMf");
                            oMfField.setAccessible(true);
                            Log.v("anysoft-logs","OpenId " + oMfField.get(oMG).toString());

                            Field oMgField  = oMG.getClass().getDeclaredField("oMg");
                            oMgField.setAccessible(true);
                            Object oMg = oMgField.get(oMG);//oMh object instance
                            Log.v("anysoft-logs","oMg  orderDetail");


                            //------------------dynamic-------------------------
                            Field oMhField  = oMG.getClass().getDeclaredField("oMh");
                            oMhField.setAccessible(true);
                            Object oMh = oMhField.get(oMG);//oMh object instance
                            Log.v("anysoft-logs","oMh  dynamic");



                            Field oLTField  = oMh.getClass().getDeclaredField("oLT");
                            oLTField.setAccessible(true);
                            Log.v("anysoft-logs","OLT 动态密钥 " + oLTField.get(oMh).toString());

                            Field oLVField  = oMh.getClass().getDeclaredField("oLV");
                            oLVField.setAccessible(true);
                            Log.v("anysoft-logs","oLV  " + oLVField.get(oMh).toString());

                            Field oLSField  = oMh.getClass().getDeclaredField("oLS");
                            oLSField.setAccessible(true);
                            Log.v("anysoft-logs","oLS  端口 " + oLSField.get(oMh).toString());

                            Field hoaField  = oMh.getClass().getDeclaredField("hoa");
                            hoaField.setAccessible(true);
                            Log.v("anysoft-logs","hoa 域名 " + hoaField.get(oMh).toString());

                            Field oLWField  = oMh.getClass().getDeclaredField("oLW");
                            oLWField.setAccessible(true);
                            Log.v("anysoft-logs","oLW  " + oLWField.get(oMh).toString());

                            Field oLUField  = oMh.getClass().getDeclaredField("oLU");
                            oLUField.setAccessible(true);
                            Log.v("anysoft-logs","oLU  " + oLUField.getLong(oMh));

                            Field oLPField  = oMh.getClass().getDeclaredField("oLP");
                            oLPField.setAccessible(true);
                            Log.v("anysoft-logs","oLP  " + oLPField.getInt(oMh));

                            Field oLQField  = oMh.getClass().getDeclaredField("oLQ");
                            oLQField.setAccessible(true);
                            Log.v("anysoft-logs","oLQ  " + oLQField.getInt(oMh));

                            Field oLRField  = oMh.getClass().getDeclaredField("oLR");
                            oLRField.setAccessible(true);
                            Log.v("anysoft-logs","oLR  " + oLRField.getInt(oMh));
                            File freeflow = new File("/sdcard/UCDownloads/freeflow.ini");
                            if (!new File(freeflow.getParent()).exists()){
                                freeflow.mkdirs();
                            }
                            if (!freeflow.exists()){
                                freeflow.createNewFile();
                            }
                            IniFileUtils ini = new IniFileUtils(freeflow);
                            //token变动时候更新
                            if (null == ini.get(IniFileUtils.ALI_FISH_TAG,"token") || !ini.get(IniFileUtils.ALI_FISH_TAG,"token").equals(oLTField.get(oMh).toString())){
                                ini.set(IniFileUtils.ALI_FISH_TAG,"orderId",oMeField.get(oMG).toString());
                                ini.set(IniFileUtils.ALI_FISH_TAG,"token",oLTField.get(oMh).toString());
                                ini.set(IniFileUtils.ALI_FISH_TAG,"openId",oMfField.get(oMG).toString());
                                ini.set(IniFileUtils.ALI_FISH_TAG,"proxyIp",hoaField.get(oMh).toString());
                                ini.set(IniFileUtils.ALI_FISH_TAG,"proxyPort",oLSField.get(oMh).toString());
                                ini.set(IniFileUtils.ALI_FISH_TAG,"updateTime",oLUField.getLong(oMh));
                                ini.save();
                                Context context = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
                                Toast.makeText(context, R.string.token_updated_notce,Toast.LENGTH_LONG).show();
                            }else {
                                Log.v("anysoft-logs","no data update");
                            }

                            *//*Field[] fields = aClass.getDeclaredFields();
                            for (int i = 0; i < fields.length; i++) {
                                Log.v("anysoft-logs","======【属性】==args["+i+"]=" + fields[i].getName() );
                                try {
                                    fields[i].setAccessible(true);
                                    if (null !=fields[i].get(param.thisObject))
                                        if (fields[i].getName().equals("oMH")){
                                            Log.v("anysoft-logs", "属性值："+fields[i].get(param.thisObject).toString() );

                                        }else if (fields[i].getName().equals("oMG")){
                                            Class omh = fields[i].get(param.thisObject).getClass();
                                            omh.getDeclaredFields()
                                        }

                                    else {
                                        Log.v("anysoft-logs", "属性值：null" );
                                    }
                                }catch (Throwable throwable){
                                    throwable.printStackTrace();
                                    Log.v("anysoft-logs-error", throwable.getMessage() );

                                }

                            }

                            // aClass.getFields() 获取 public 类型的成员
                            Method[] methods = aClass.getMethods();
                            for (Method mt:
                                    methods) {
                                String mn = mt.getName();
                                Object dv = mt.getDefaultValue();
                                String returnType = mt.getReturnType().getName();
                                Log.v("anysoft-logs","方法名称：" + mn + "    默认值：" + dv + "    返回类型：" + returnType);
                            }*//*
                        }
            });*/
            //宝卡hook
            /*XposedHelpers.findAndHookMethod("com.uc.browser.business.freeflow.proxy.b.c",
                    loadPackageParam.classLoader,
                    "aaN",
                    String.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            Log.v("anysoft-logs","aaN");
                            Class<?> aClass = param.thisObject.getClass();
                            Field oMIField = aClass.getDeclaredField("oMI");
                            oMIField.setAccessible(true);
                            Object oMI = oMIField.get(param.thisObject);//oMG object instance
                            Log.v("anysoft-logs","oMI");

                            Field oNeField  = oMI.getClass().getDeclaredField("oNe");
                            oNeField.setAccessible(true);
                            Log.v("anysoft-logs","ip " + oNeField.get(oMI).toString());

                            Field oCxField  = oMI.getClass().getDeclaredField("oCx");
                            oCxField.setAccessible(true);
                            Log.v("anysoft-logs","password " + oCxField.get(oMI).toString());

                            Field oNdField  = oMI.getClass().getDeclaredField("oNd");
                            oNdField.setAccessible(true);
                            Log.v("anysoft-logs","account " + oNdField.get(oMI).toString());

                            Field portField  = oMI.getClass().getDeclaredField("port");
                            portField.setAccessible(true);
                            Log.v("anysoft-logs","port " + portField.getInt(oMI));

                            IniFileUtils ini = new IniFileUtils();
                            //token变动时候更新
                            if (null == ini.get(IniFileUtils.ALI_ANT_TAG,"account") || !ini.get(IniFileUtils.ALI_ANT_TAG,"account").equals(oNdField.get(oMI).toString())){
                                ini.set(IniFileUtils.ALI_ANT_TAG,"account",oNeField.get(oMI).toString());
                                ini.set(IniFileUtils.ALI_ANT_TAG,"password",oCxField.get(oMI).toString());
                                ini.set(IniFileUtils.ALI_ANT_TAG,"ip",oNdField.get(oMI).toString());
                                ini.set(IniFileUtils.ALI_ANT_TAG,"port",portField.getInt(oMI));
                                ini.save();
                                Context context = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
                                Toast.makeText(context, R.string.token_updated_notce,Toast.LENGTH_LONG).show();
                            }else {
                                Log.v("anysoft-logs","no data update");
                            }

                            *//*Field[] fields = aClass.getDeclaredFields();
                            for (int i = 0; i < fields.length; i++) {
                                Log.v("anysoft-logs","======【属性】==args["+i+"]=" + fields[i].getName() );
                                try {
                                    fields[i].setAccessible(true);
                                    if (null !=fields[i].get(param.thisObject))
                                        if (fields[i].getName().equals("oMH")){
                                            Log.v("anysoft-logs", "属性值："+fields[i].get(param.thisObject).toString() );

                                        }else if (fields[i].getName().equals("oMG")){
                                            Class omh = fields[i].get(param.thisObject).getClass();
                                            omh.getDeclaredFields()
                                        }

                                    else {
                                        Log.v("anysoft-logs", "属性值：null" );
                                    }
                                }catch (Throwable throwable){
                                    throwable.printStackTrace();
                                    Log.v("anysoft-logs-error", throwable.getMessage() );

                                }

                            }

                            // aClass.getFields() 获取 public 类型的成员
                            Method[] methods = aClass.getMethods();
                            for (Method mt:
                                    methods) {
                                String mn = mt.getName();
                                Object dv = mt.getDefaultValue();
                                String returnType = mt.getReturnType().getName();
                                Log.v("anysoft-logs","方法名称：" + mn + "    默认值：" + dv + "    返回类型：" + returnType);
                            }*//*
                        }
            });*/

            XposedHelpers.findAndHookMethod("com.uc.base.secure.EncryptHelper",
                    loadPackageParam.classLoader,
                    "decrypt",
                    byte[].class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);

                            byte[] bytes = (byte[]) param.getResult();
                            String content = new String(bytes, "UTF-8");
                            if (!content.contains("com.autonavi.minimap") && !content.contains("com.youku.phone") && !content.contains("com.UCMobile.love"))
                                return;
                            XposedBridge.log(String.format("%s:%s", TAG, Base64.encodeToString((byte[]) param.getResult(), 0)));

                            // parse data
                            int index = 0 + 1;
                            int length = getIndex(bytes[index]);
                            byte[] data = new byte[length];
                            System.arraycopy(bytes, index + 1, data, 0, length);
                            String orderId = new String(data);

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

                            XposedBridge.log(String.format("%s:  orderId:%s", TAG, orderId));


                            File freeflow = new File(IniFileUtils.FILE_PATH);
                            if (!new File(freeflow.getParent()).exists()) {
                                freeflow.mkdirs();
                            }
                            if (!freeflow.exists()) {
                                freeflow.createNewFile();
                            }
                            IniFileUtils ini = new IniFileUtils(freeflow);
                            //token变动时候更新
                            if (null == ini.get(IniFileUtils.ALI_FISH_TAG, "token") || !ini.get(IniFileUtils.ALI_FISH_TAG, "token").equals(token)) {
                                ini.set(IniFileUtils.ALI_FISH_TAG, "orderId", orderId);
                                ini.set(IniFileUtils.ALI_FISH_TAG, "token", token);
                                ini.set(IniFileUtils.ALI_FISH_TAG, "openId", openId);
                                ini.set(IniFileUtils.ALI_FISH_TAG, "proxyIp", ip);
                                ini.set(IniFileUtils.ALI_FISH_TAG, "proxyPort", port);
                                ini.set(IniFileUtils.ALI_FISH_TAG, "updateTime", Long.valueOf(System.currentTimeMillis() / 1000));
                                ini.save();
                            } else {
                                Log.v("anysoft-logs", "no data update");
                            }


                        }
                    });
        }
    }
}