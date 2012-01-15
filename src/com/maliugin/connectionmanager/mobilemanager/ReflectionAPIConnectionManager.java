package com.maliugin.connectionmanager.mobilemanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import com.maliugin.connectionmanager.mobilemanager.MobileConnectionManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: maluk
 * Date: 27.12.2011
 * Time: 23:21:57
 * To change this template use File | Settings | File Templates.
 */
public class ReflectionAPIConnectionManager implements MobileConnectionManager {
    private final Context context;

    public ReflectionAPIConnectionManager(Context context) {
        this.context = context;
    }

    public boolean isConnectionEnabled() {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED
                || telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTING;
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
        Method dataConnSwitchmethod;
        Class telephonyManagerClass;
        Object ITelephonyStub;
        Class ITelephonyClass;

        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
        Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
        getITelephonyMethod.setAccessible(true);
        ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
        ITelephonyClass = Class.forName(ITelephonyStub.getClass().getName());

        if (enable) {
            dataConnSwitchmethod = ITelephonyClass
                    .getDeclaredMethod("enableDataConnectivity");
        } else {
            dataConnSwitchmethod = ITelephonyClass
                    .getDeclaredMethod("disableDataConnectivity");
        }
        dataConnSwitchmethod.setAccessible(true);
        dataConnSwitchmethod.invoke(ITelephonyStub);
    }
}
