package com.example.asus.chatty.sample.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.asus.chatty.R;
import com.example.asus.chatty.sample.groupchannel.GroupChannelActivity;
import com.example.asus.chatty.sample.utils.PreferenceUtils;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;


public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //แปะ toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);

        // เข้าหน้า group channel
        findViewById(R.id.linear_layout_group_channels).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GroupChannelActivity.class);
                startActivity(intent);
            }
        });

        //กด disconnect
        findViewById(R.id.button_disconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disconnect
                disconnect();
            }
        });
    }

    //disconnect
    private void disconnect() {
        SendBird.unregisterPushTokenAllForCurrentUser(new SendBird.UnregisterPushTokenHandler() {
            @Override
            public void onUnregistered(SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();
                }
                ConnectionManager.logout(new SendBird.DisconnectHandler() {
                    @Override
                    public void onDisconnected() {
                        PreferenceUtils.setConnected(false);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    //แสดง icon menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //เข้าหน้า setting
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_main:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }
}
