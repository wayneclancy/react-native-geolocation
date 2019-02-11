package co.uk.hive.reactnativegeolocation.geofence;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class PersistentLog {private static final String TAG = "PersistentLogger";
    private static final String LOG_FILENAME = "geofence_log.txt";
    private static final DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG);

    public static void log(String s) {
        Log.d(TAG, s);
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File logFile = new File(downloadsDir, LOG_FILENAME);

        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            FileWriter writer = new FileWriter(logFile, true);
            writer
                    .append('[')
                    .append(DATE_FORMAT.format(new Date()))
                    .append("] ")
                    .append(s)
                    .append('\n');
            writer.flush();
        } catch (IOException e) {
            if (e.getMessage().contains("Permission denied")) {
                Log.e(TAG, "Cannot log, did you enable the Storage permission in app settings?");
            } else {
                e.printStackTrace();
            }
        }
    }
}
