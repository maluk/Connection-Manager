package com.maliugin.connectionmanager.mobilemanager;

import android.content.Context;
import android.net.ConnectivityManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: maluk
 * Date: 28.12.2011
 * Time: 20:06:22
 * To change this template use File | Settings | File Templates.
 */
public class Android23ConnectionManager implements MobileConnectionManager {
    private static final String CONNECTIVITY_SERVICE = "connectivity";
    private final Context context;

    public Android23ConnectionManager(Context context) {
        this.context = context;
    }

    public boolean isConnectionEnabled() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        Class cmClazz = cm.getClass();
        try {
            Method localMethod = cmClazz.getMethod("getMobileDataEnabled");
            return ((Boolean) localMethod.invoke(cm)).booleanValue();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
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
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        Class cmClass = cm.getClass();
        try {
            Method localMethod = cmClass.getMethod("setMobileDataEnabled", Boolean.TYPE);
            localMethod.invoke(cm, enable);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
