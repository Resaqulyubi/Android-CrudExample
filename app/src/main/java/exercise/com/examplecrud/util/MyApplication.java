package exercise.com.examplecrud.util;

import android.content.Context;
import android.os.Build;

import android.support.v7.app.AppCompatDelegate;

import com.orm.SugarApp;

public class MyApplication extends SugarApp {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static MyApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;



    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


}