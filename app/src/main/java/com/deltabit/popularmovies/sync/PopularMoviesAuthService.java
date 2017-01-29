package com.deltabit.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by rigel on 24/01/17.
 */

public class PopularMoviesAuthService extends Service {
    private PopularMoviesAuthenticator mAuthenticator;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAuthenticator = new PopularMoviesAuthenticator(this);
    }
}
