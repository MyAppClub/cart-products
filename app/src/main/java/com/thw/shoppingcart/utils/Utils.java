package com.thw.shoppingcart.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/*
    Place to keep all common utilities
 */
public class Utils {

    private static final String TAG = "Utils";

    private static ProgressDialog mProgressDialog;

    public static void showToast(final Activity activity, final String message, final int duration) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, duration).show();
            }
        });
    }

    public static void showProgressDialog(final Activity activity, final String message) {

        if (activity == null || activity.isFinishing()) {
            Log.e(TAG, "prodgressDialog: activity null/finishing");
            return;
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return;
        }

        Handler mainHandler = new Handler(activity.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = ProgressDialog.show(activity, "",
                        message, true);
            }
        });
    }

    public static void hideProgressDialog(final Activity activity) {

        if (activity == null || activity.isFinishing()) {
            Log.e(TAG, "prodgressDialog: activity null/finishing");
            return;
        }

        Handler mainHandler = new Handler(activity.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null) mProgressDialog.cancel();
            }
        });
    }

}
