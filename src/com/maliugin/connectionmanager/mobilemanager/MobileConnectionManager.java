package com.maliugin.connectionmanager.mobilemanager;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: maluk
 * Date: 27.12.2011
 * Time: 23:18:20
 * To change this template use File | Settings | File Templates.
 */
public interface MobileConnectionManager {
    boolean isConnectionEnabled();

    void enableConnection();

    void disableConnection();

    void switchConnection();
}
