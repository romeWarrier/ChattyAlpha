package com.example.asus.chatty;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.asus.chatty.utils.ConnectionManager;
import com.example.asus.chatty.utils.PreferenceUtils;
import com.example.asus.chatty.utils.PushUtils;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

public class LoginActivity extends AppCompatActivity {

    private CoordinatorLayout mLoginLayout;
    private TextInputEditText mUserIdConnectEditText, mUserNicknameEditText;
    private Button mConnectButton;
    private ContentLoadingProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginLayout = (CoordinatorLayout) findViewById(R.id.layout_login);

        mUserIdConnectEditText = (TextInputEditText) findViewById(R.id.edittext_login_user_id);
        mUserNicknameEditText = (TextInputEditText) findViewById(R.id.edittext_login_user_nickname);

        mUserIdConnectEditText.setText(PreferenceUtils.getUserId());
        mUserNicknameEditText.setText(PreferenceUtils.getNickname());

        mConnectButton = (Button) findViewById(R.id.button_login_connect);
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = mUserIdConnectEditText.getText().toString();
                // remove all space
                userId = userId.replaceAll("\\s","");

                String userNickname = mUserNicknameEditText.getText().toString();

                PreferenceUtils.setUserId(userId);
                PreferenceUtils.setNickname(userNickname);

                connectToSendBird(userId, userNickname);
            }
        });

        mUserNicknameEditText.setSelectAllOnFocus(true);
        mUserNicknameEditText.setSelectAllOnFocus(true);

        // A loading indicator
        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar_login);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(PreferenceUtils.getConnected()){
            connectToSendBird(PreferenceUtils.getUserId(),PreferenceUtils.getNickname());
        }
    }

    private void connectToSendBird(final String userId, final String userNickname) {
        showProgressBar(true);
        mConnectButton.setEnabled(false);

        ConnectionManager.login(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                showProgressBar(false);

                if (e != null) {
                    Toast.makeText(
                            LoginActivity.this,"" + e.getCode() + ": " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    // Show login failure snackbar
                    showSnackbar("Login To SendBird failed");
                    mConnectButton.setEnabled(true);
                    PreferenceUtils.setConnected(false);
                    return;
                }

                PreferenceUtils.setNickname(user.getNickname());
                PreferenceUtils.setProfileUrl(user.getProfileUrl());
                PreferenceUtils.setConnected(true);

                //Update the user's nickname
                updateCurrentUserInfo(userNickname);
                updateCurrentUserPushToken();

                // Proceed to MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateCurrentUserPushToken(){
        PushUtils.registerPushTokenForCurrentUser(LoginActivity.this, null);
    }

    public void updateCurrentUserInfo(final String userNickname){
        SendBird.updateCurrentUserInfo(userNickname, null, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                if(e != null) {
                    //error
                    Toast.makeText(
                            LoginActivity.this, "" + e.getCode() + ":" + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    // Show update failed snackbar
                    showSnackbar("Update user nickname failed");

                    return;
                }
                PreferenceUtils.setNickname(userNickname);
            }
        });
    }

    // Displays a snackbar from the bottom of the screen
    private void showSnackbar(String text) {
        Snackbar snackbar = Snackbar.make(mLoginLayout, text, Snackbar.LENGTH_SHORT);

        snackbar.show();
    }

    private  void showProgressBar(boolean show){
        if(show){
            mProgressBar.show();
        }else{
            mProgressBar.hide();
        }
    }
}
