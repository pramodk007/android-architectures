package com.mvvm.java.core.base;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.mvvm.java.core.custom.CustomDialog;
import com.mvvm.java.core.utilities.NetworkChangeReceiver;
import com.mvvm.java.core.utilities.NetworkUtilities;

public abstract class BaseMvvmActivity<VB extends ViewBinding>
        extends AppCompatActivity implements BaseMvvmView {

    public VB viewBinding;

    private Dialog progressDialog;
    private BroadcastReceiver networkReceiver;

    public abstract VB getViewBinding();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = getViewBinding();
        setContentView(viewBinding.getRoot());

        setup();
    }


    @Override
    public void setup() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        createNetworkErrorListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterNetworkChanges();
    }

    @Override
    public void showLoading() {
        progressDialog = CustomDialog.showLoadingDialog(this);
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(int resId) {
        showMessage(getString(resId));
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtilities.isNetworkConnected(getApplicationContext());
    }

    @Override
    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = findViewById(android.R.id.content).getRootView();
        }

        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void unregisterNetworkChanges() {
        try {
            unregisterReceiver(networkReceiver);
        } catch (IllegalArgumentException e) {
            Log.d(BaseMvvmActivity.class.getName(), e.getMessage());
        }
    }

    private void createNetworkErrorListener() {
        networkReceiver = new NetworkChangeReceiver();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
}