package com.example.android.btprongle;

import android.content.Context;
import android.widget.Toast;

import com.example.android.common.logger.Log;

import java.util.Date;

/**
 * PingTest is a runnable class that performs a bunch of pings and logs the round trip times.
 * This is meant to be called with eg. scheduleWithFixedDelay and a set delay.
 */

public class PingTest implements Runnable {

    private Context applicationContext = null;
    private BluetoothService mService = null;
    private int pingType = 0;
    private int round = 10;

    PingTest(Context context, BluetoothService btser, int pt) {
        this.applicationContext = context;
        this.mService = btser;
        this.pingType = pt;
    }

    @Override
    public void run() {
        round--;
        Log.d("PingTest, round " + round + ": ", new Date().toString());

        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(applicationContext.getApplicationContext(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            throw new RuntimeException();
        }

        Frame ping = new Frame(this.pingType);
        long sendTime = System.currentTimeMillis();
        ping.setPayload("sent", sendTime);
        mService.write(ping.toBytes());

        if (round <= 0) {
            // After rounds terminate this pingtester blatantly with a RuntimeException
            Toast.makeText(applicationContext, R.string.pingtest_over, Toast.LENGTH_SHORT).show();
            throw new RuntimeException();
        }
    }
}
