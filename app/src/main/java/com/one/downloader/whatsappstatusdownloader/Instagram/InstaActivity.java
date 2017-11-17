package com.one.downloader.whatsappstatusdownloader.Instagram;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.View.OnClickListener;

import com.one.downloader.whatsappstatusdownloader.R;
import com.tapadoo.alerter.Alerter;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

@RuntimePermissions
public class InstaActivity extends AppCompatActivity implements OnClickListener {

    private PulsatorLayout plSwitch;
    private boolean serviceRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insta_fragment);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
       /* MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_app_id));
        initializeAd();*/

        plSwitch = (PulsatorLayout) findViewById(R.id.pl_switch);
        findViewById(R.id.ib_switch).setOnClickListener(this);
        findViewById(R.id.iv_question).setOnClickListener(this);

        serviceRunning = isClipboardServiceRunning(ClipboardService.class);
        if (serviceRunning) {
            plSwitch.start();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_switch:
                handleServiceStartStop();
                break;
            case R.id.iv_question:
                DialogHelper.showHowToDialog(InstaActivity.this);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        InstaActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void startService() {
        serviceRunning = true;
        plSwitch.start();
        startService(new Intent(this, ClipboardService.class));
        showAlert(R.string.message_service_enabled, R.color.md_green_400, R.drawable.ic_check,
                Constants.DURATION_SHORT);
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showRationaleForWriteExternalStorage(final PermissionRequest request) {
        DialogHelper.showWriteExternalStorageRationaleDialog(InstaActivity .this, request);
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showDeniedForWriteExternalStorage() {
        showAlert(R.string.permission_denied_title, R.color.md_orange_50, R.drawable.ic_alert,
                Constants.DURATION_LONG);
    }

    private void showAlert(int title, int color, int icon, int duration) {
        Alerter.create(InstaActivity.this)
                .setTitle(title)
                .setBackgroundColor(color)
                .setIcon(icon)
                .setDuration(duration)
                .show();
    }

    private void stopService() {
        stopService(new Intent(this, ClipboardService.class));
    }

    private boolean isClipboardServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /*private void initializeAd() {
        NativeExpressAdView avNativeAd = (NativeExpressAdView) findViewById(R.id.av_nativeAd);
        avNativeAd.loadAd(new AdRequest.Builder().build());
    }*/

    private void handleServiceStartStop() {
        if (serviceRunning) {
            serviceRunning = false;
            plSwitch.stop();
            stopService();
            showAlert(R.string.message_service_disabled, R.color.md_green_400, R.drawable.ic_check,
                    Constants.DURATION_SHORT);
        } else {
            InstaActivityPermissionsDispatcher.startServiceWithCheck(InstaActivity.this);
        }
    }
}
