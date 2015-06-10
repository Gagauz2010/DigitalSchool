package ru.spsu.fmf.digitalschool;

import android.app.Application;

import com.parse.Parse;
import com.parse.PushService;

public class ParsePushApplication extends Application {
    @Override
    public void onCreate(){
        Parse.initialize(getApplicationContext(), getString(R.string.applicationKey), getString(R.string.serverKey));
        PushService.setDefaultPushCallback(getBaseContext(), LogIn.class);
    }
}