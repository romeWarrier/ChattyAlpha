package com.example.asus.chatty.utils;

import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

public class ConnectionManager {
    public static void login(String userId, final SendBird.ConnectHandler handler){
        SendBird.connect(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (handler != null){
                    handler.onConnected(user, e);
                }
            }
        });
    }

    public void logout(final SendBird.DisconnectHandler handler){
        SendBird.disconnect(new SendBird.DisconnectHandler() {
            @Override
            public void onDisconnected() {
                if (handler != null){
                    handler.onDisconnected();
                }
            }
        });
    }


}
