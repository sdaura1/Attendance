package com.example.shaheed.attendance;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    SignInButton gsignInButton;
    private GoogleApiClient googleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    GoogleSignInAccount googleSignInAccount;
    WifiConfiguration wifiConfiguration;
    final String networkSSID = "FlexiSAF Edusoft Ltd";
    final String networkPass = "safadmin$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        gsignInButton = findViewById(R.id.signInBtn);
        // googleSignInAccount = result.getSignInAccount();

/*
        wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = "\"" + networkSSID + "\"";

        wifiConfiguration.preSharedKey = "\"" + networkPass + "\"";

        WifiManager wifiManager = (WifiManager) getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(wifiConfiguration);

        List<WifiConfiguration> wifiConfigurationList = wifiManager.getConfiguredNetworks();
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        for (WifiConfiguration i : wifiConfigurationList) {
            if ((i.SSID != null) && (i.SSID.equals("\"" + networkSSID + "\""))) {
                if (!(wifiInfo.getSSID().equals(networkSSID))) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    Toast.makeText(getApplicationContext(),
                            "Connected!", Toast.LENGTH_SHORT).show();
                    wifiManager.reconnect();

                    break;
                }
            }
            */
            gsignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail().build();

                    googleApiClient = new GoogleApiClient.Builder(SignInActivity.this)
                            .enableAutoManage(SignInActivity.this,
                                    SignInActivity.this)
                            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                            .build();


                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);

                    /*
                    Intent mIntent = new Intent(SignInActivity.this,
                            MainActivity.class);
                    mIntent.putExtra("name", );
                    startActivity(mIntent);
                    */
                }
            });
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount googleSignInAccount = result.getSignInAccount();

            Intent mIntent = new Intent(SignInActivity.this, MainActivity.class);
            mIntent.putExtra("name", googleSignInAccount.getDisplayName());
            startActivity(mIntent);
        }
    }

        @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}