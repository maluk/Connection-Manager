package com.maliugin.connectionmanager.widget;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.maliugin.connectionmanager.R;

/**
 * Created by IntelliJ IDEA.
 * User: maluk
 * Date: 29.12.2011
 * Time: 22:53:10
 * To change this template use File | Settings | File Templates.
 */
public class PreferencesWidgetAtivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        //jkjlkjkl
    }

}
