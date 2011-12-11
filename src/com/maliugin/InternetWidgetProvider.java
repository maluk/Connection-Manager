package com.maliugin;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

public class InternetWidgetProvider extends AppWidgetProvider {
    public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
    public static final int ENABLED_ICON = R.drawable.box_green;
    public static final int DISABLED_ICON = R.drawable.box_red;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onEnabled(context);
        RemoteViews views = createWidgetView(context);
        Intent active = new Intent(context, InternetWidgetProvider.class);
        active.setAction(ACTION_WIDGET_RECEIVER);
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
        views.setOnClickPendingIntent(R.id.widget_button, actionPendingIntent);
        setStatusImg(createAPNManager(context), views);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (ACTION_WIDGET_RECEIVER.equals(action)) {
            APNManager apnManager = createAPNManager(context);
            showMessage(context, apnManager.isActiveAPNEnabled());
            apnManager.switchAPNStatus();
            RemoteViews views = createWidgetView(context);
            setStatusImg(apnManager, views);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int[] widgetIds = manager.getAppWidgetIds(new ComponentName(context, InternetWidgetProvider.class));
            manager.updateAppWidget(widgetIds, views);
        }
        super.onReceive(context, intent);
    }

    protected void showMessage(Context context, boolean APNEnabled) {
        int message = APNEnabled ? R.string.internet_disabled : R.string.internet_enabled;
        Toast toastMessage = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toastMessage.show();
    }

    protected RemoteViews createWidgetView(Context context) {
        return new RemoteViews(context.getPackageName(), R.layout.main);
    }

    protected APNManager createAPNManager(Context context) {
        return new APNManager(context.getContentResolver());
    }

    protected void setStatusImg(APNManager apnManager, RemoteViews views) {
        int buttonImg = apnManager.isActiveAPNEnabled() ? ENABLED_ICON : DISABLED_ICON;
        views.setImageViewResource(R.id.widget_button, buttonImg);
    }
}
