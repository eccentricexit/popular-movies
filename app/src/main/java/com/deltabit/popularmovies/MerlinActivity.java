package com.deltabit.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

/**
 * Created by rigel on 28/02/17.
 */

public abstract class MerlinActivity extends AppCompatActivity implements Connectable,Disconnectable {

    private Merlin merlin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        merlin = createMerlin();
    }

    private void registerConnectable(Connectable connectable) {
        merlin.registerConnectable(connectable);
    }

    private void registerDisconnectable(Disconnectable disconnectable) {
        merlin.registerDisconnectable(disconnectable);
    }

    @Override
    protected void onStart() {
        super.onStart();
        merlin.bind();
        registerConnectable(this);
        registerDisconnectable(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        merlin.unbind();
    }


    private Merlin createMerlin() {
        return new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .build(this);
    }
}