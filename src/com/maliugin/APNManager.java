package com.maliugin;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class APNManager {
    public static final Uri APN_TABLE_URI = Uri.parse("content://telephony/carriers");
    public static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
    public static final String DISABLE_PREFIX = "dmmaapnoff";
    private static final String APN_COLUMN = "apn";
    private static final String APN_ID_COLUMN = "_id";

    private final ContentResolver resolver;

    public APNManager(ContentResolver resolver) {
        this.resolver = resolver;
    }

    public String getActiveAPNName() {
        Cursor cur = resolver.query(
                PREFERRED_APN_URI,
                new String[]{APN_COLUMN},
                null,
                null,
                null);
        if (cur.moveToFirst()) {
            String res = cur.getString(0);
            cur.close();
            return res;
        }
        cur.close();
        return "EMPTY";
    }

    public int getActiveAPNId() {
        Cursor cur = resolver.query(
                PREFERRED_APN_URI,
                new String[]{APN_ID_COLUMN},
                null,
                null,
                null);
        if (cur.moveToFirst()) {
            int res = cur.getInt(0);
            cur.close();
            return res;
        }
        cur.close();
        return -1;
    }

    public boolean isActiveAPNEnabled() {
        return !getActiveAPNName().startsWith(DISABLE_PREFIX);
    }

    protected void setActiveAPNName(String name) {
        int activeId = getActiveAPNId();
        ContentValues values = new ContentValues(1);
        values.put(APN_COLUMN, name);
        resolver.update(APN_TABLE_URI, values,  "_id="+activeId, null);
    }

    public void enableActiveAPN() {
        String newAPNName = getActiveAPNName().replace(DISABLE_PREFIX, "");
        setActiveAPNName(newAPNName);
    }

    public void disableActiveAPN() {
        String newAPNName = DISABLE_PREFIX + getActiveAPNName();
        setActiveAPNName(newAPNName);
    }

    public void switchAPNStatus() {
        if (isActiveAPNEnabled()) {
            disableActiveAPN();
        } else {
            enableActiveAPN();
        }
    }
}
