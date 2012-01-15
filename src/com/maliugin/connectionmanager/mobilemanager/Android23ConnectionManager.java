package com.maliugin.connectionmanager.mobilemanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import com.maliugin.connectionmanager.mobilemanager.MobileConnectionManager;

import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: maluk
 * Date: 28.12.2011
 * Time: 20:06:22
 * To change this template use File | Settings | File Templates.
 */
public class Android23ConnectionManager implements MobileConnectionManager {
    private final Context context;

    public Android23ConnectionManager(Context context) {
        this.context = context;
    }

    public boolean isConnectionEnabled() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        try {
            Class cmClazz = cm.getClass();
            Method localMethod = cmClazz.getMethod("getMobileDataEnabled");
            return ((Boolean) localMethod.invoke(cm)).booleanValue();
        } catch (Exception ex) {
            return true;
        }
    }

    public void enableConnection() {
        try {
            changeConnection(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disableConnection() {
        try {
            changeConnection(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchConnection() {
        if (isConnectionEnabled()) {
            disableConnection();
        } else {
            enableConnection();
        }
    }

    private void changeConnection(boolean enable) throws Exception {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        Class cmClass = cm.getClass();
        Method localMethod = cmClass.getMethod("setMobileDataEnabled", Boolean.TYPE);
        localMethod.invoke(cm, enable);
    }
}
