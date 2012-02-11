package com.maliugin.connectionmanager.mobilemanager;

import android.content.Context;
import android.telephony.TelephonyManager;

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
        changeConnection(true);
    }

    public void disableConnection() {
        changeConnection(false);
    }

    public void switchConnection() {
        if (isConnectionEnabled()) {
            disableConnection();
        } else {
            enableConnection();
        }
    }

    private void changeConnection(boolean enable) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
            Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
            getITelephonyMethod.setAccessible(true);
            Object ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
            Class ITelephonyClass = Class.forName(ITelephonyStub.getClass().getName());

            Method dataConnSwitchMethod;
            if (enable) {
                dataConnSwitchMethod = ITelephonyClass
                        .getDeclaredMethod("enableDataConnectivity");
            } else {
                dataConnSwitchMethod = ITelephonyClass
                        .getDeclaredMethod("disableDataConnectivity");
            }
            dataConnSwitchMethod.setAccessible(true);
            dataConnSwitchMethod.invoke(ITelephonyStub);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
