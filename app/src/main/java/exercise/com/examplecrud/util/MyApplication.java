package exercise.com.examplecrud.util;

import android.content.Context;
import android.os.Build;

import android.support.v7.app.AppCompatDelegate;


public class MyApplication  {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static MyApplication mInstance;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


}