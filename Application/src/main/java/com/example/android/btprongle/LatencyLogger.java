package com.example.android.btprongle;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * LatencyLogger handles all logging to external storage.
 */

class LatencyLogger {

    private Date now = new Date();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    private String format = sdf.format(now);

    private File logFile = null;

    public LatencyLogger() {

        File path = new File(Environment.getExternalStorageDirectory(), Constants.LOG_FOLDER);
        if (!path.exists()) {
            path.mkdirs();
        }

        this.logFile = new File (path, "log "+ format +".txt");
    }


    void writeString(String handle, long value) {
        try {
            long now = System.currentTimeMillis() / 1000;

            FileOutputStream out = new FileOutputStream(logFile, true);

            out.write((now + "\t" + handle + "\t" + value + "\n").getBytes());
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
