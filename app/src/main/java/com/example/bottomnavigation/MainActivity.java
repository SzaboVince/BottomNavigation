package com.example.bottomnavigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView textViewInfo;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.wifi_on){
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                        textViewInfo.setText("Android 10-től az alkalmazások nem kapcsolhatják be a wifit.");
                        Intent intent=new Intent(Settings.Panel.ACTION_WIFI);
                        startActivityForResult(intent,0);
                    }
                    else{
                        wifiManager.setWifiEnabled(true);
                        textViewInfo.setText("Wifi bekapcsolva");
                    }
                } else if (item.getItemId()==R.id.wifi_off) {
                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                        textViewInfo.setText("Android 10-től az alkalmazások nem kapcsolhatják ki a wifit.");
                        Intent intent=new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                        startActivityForResult(intent,1);
                    }
                    else{
                        wifiManager.setWifiEnabled(false);
                        textViewInfo.setText("Wifi kikapcsolva");
                    }
                }
                else{
                    ConnectivityManager connectivityManager=(ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (networkInfo.isConnected()){
                        int ip_number=wifiInfo.getIpAddress();
                        String ip= Formatter.formatIpAddress(ip_number);
                        textViewInfo.setText("IP: "+ip);
                    }
                    else{
                        textViewInfo.setText("Nincs wifi kapcsolat");
                    }

                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==0){
            if (wifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLED||wifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLING){
                textViewInfo.setText("Wifi bekapcsolva");
            } else if (wifiManager.getWifiState()==WifiManager.WIFI_STATE_DISABLED||wifiManager.getWifiState()==WifiManager.WIFI_STATE_DISABLING){
                textViewInfo.setText("Wifi kikapcsolva");
            }
        }
        if (requestCode==1){
            if (wifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLED||wifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLING){
                textViewInfo.setText("Wifi bekapcsolva");
            } else if (wifiManager.getWifiState()==WifiManager.WIFI_STATE_DISABLED||wifiManager.getWifiState()==WifiManager.WIFI_STATE_DISABLING){
                textViewInfo.setText("Wifi kikapcsolva");
            }
        }

    }

    public void init(){
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        textViewInfo=findViewById(R.id.textviewinfo);
        wifiManager=(WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiInfo=wifiManager.getConnectionInfo();
    }
}