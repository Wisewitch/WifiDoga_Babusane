package com.e.wifidoldozat_bga;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
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

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    TextView textView_WIFI;
    BottomNavigationView bottom_navigation_view_WIFINav;
    MediaPlayer mediaPlayer_on, mediaPlayer_off, mediaPlayer;
    WifiManager wifiManager;
    WifiInfo wifiInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        bottom_navigation_view_WIFINav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.WIFIOn:
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            textView_WIFI.setText("Nincs ngedélye a wifi állapot változtatására (Android 10+)");
                            Intent panel = new Intent(Settings.Panel.ACTION_WIFI);
                            startActivityForResult(panel, 0);
                            return true;
                        }
                        wifiManager.setWifiEnabled(true);
                        textView_WIFI.setText("Wifi bekapcsolva");
                        ProcessBuilder mediaplayer;
                        mediaPlayer_on.start();
                        break;
                    case R.id.WIFIoff:
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            textView_WIFI.setText("Nincs ngedélye a wifi állapot változtatására (Android 10+)");
                            Intent panel = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                            startActivityForResult(panel, 0);
                            return true;
                        }
                        wifiManager.setWifiEnabled(false);
                        textView_WIFI.setText("Wifi kikapcsolva");
                        mediaPlayer_off.start();
                        break;
                    case R.id.WIFIInfo:
                        ConnectivityManager connManager =
                                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                        if (netInfo.isConnected()) { // ha csatlakozik
                          int ip_int = wifiInfo.getIpAddress();
                          String ip = Formatter.formatIpAddress(ip_int);

                            textView_WIFI.setText("IP cím: " + ip);
                        }else {   // ha nem csatlakozik
                            textView_WIFI.setText("Nem csatlakozik Wifi hálózathoz ");
                        }
                        mediaPlayer.start();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
             if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING ||
                     wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED
             )
             {
                 textView_WIFI.setText("Wifi bekapcsolva");
            }
             else if(wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING ||
                     wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED
             ){
                 textView_WIFI.setText("Wifi kikapcsolva");
             }
        }


    }

    /*/ @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }*/

    public void init() {
        textView_WIFI = findViewById(R.id.textView_WIFI);
        bottom_navigation_view_WIFINav = findViewById(R.id.bottom_navigation_view_WIFINav);
        mediaPlayer_on = MediaPlayer.create(this,R.raw.wifi_on);
        mediaPlayer_off = MediaPlayer.create(this,R.raw.wifi_off);
        mediaPlayer = MediaPlayer.create(this,R.raw.wifi_info);

        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
    }

}