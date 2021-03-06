package salinas.primary.file;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;

import salinas.primary.file.Constants.*;
import salinas.primary.sensors.Constants.*;
/**
 * Created by Jose Salinas on 4/16/2017.
 */

public class FileService extends IntentService {

    public static final String TAG = "File Service";

    public FileService() {
        super("File Service");
    }

    public FileService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(isExternalStorageWriteable()) {
            requestDataUpdates();

            Runnable updateFile = new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        try {
                            Log.i(TAG, "Running file output thread");
                            Thread.sleep(3*1000);
                            writeToFile(Constants.OUTPUT_FILE_NAME, data);
                        } catch (InterruptedException e) {
                            Log.e(TAG, "Thread interrupted.");
                            e.printStackTrace();
                        } catch(IOException e) {
                            Log.e(TAG, "File not found.");
                            e.printStackTrace();
                        }
                    }
                }
            };
            updateFile.run();
        } else {
            Log.e(TAG, "External Storage is not available");
            return;
        }
    }

    private String data = "";

    DataBroadcastReceiver dataBroadcastReceiver;

    private void requestDataUpdates() {
        dataBroadcastReceiver = new DataBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(salinas.primary.sensors.Constants.BROADCAST_SENSOR_DATA);
        registerReceiver(dataBroadcastReceiver, intentFilter);
    }

    protected class DataBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            data = intent.getStringExtra(salinas.primary.sensors.Constants.BASIC_SENSOR_DATA_STATUS);
            Log.i(TAG, "Updating data: " + data);
        }
    }

    private boolean isExternalStorageWriteable() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private File makeDataOutputFile(String outputFileName) throws IOException{
        File path  = new File(this.getExternalFilesDir(null), "output");
        if(!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path, outputFileName);
        file.createNewFile();
//        File file = new File(Environment.getExternalStorageDirectory(), outputFileName);
        Log.i(TAG, file.toString());

//        if(!file.exists()) {
//            Log.e(TAG, "File does not exist, attempting to create file.");
//            new File(file.getParent()).mkdirs();
//            file.createNewFile();
//        }
//
//        Log.i(TAG, "File found. Returning file.");
        return file;
    }

    private void writeToFile(String outputFileName, String data) throws IOException{

        File file = makeDataOutputFile(outputFileName);
        FileWriter fileWriter = new FileWriter(file);
//        FileOutputStream fileOutputStream = new FileOutputStream(file, true);

        try {
            Log.e(TAG, "Attempting to write to file.");
//            fileOutputStream.write((data + "\n").getBytes(Charset.forName("UTF-8")));
            fileWriter.append(data + "\n");
            Log.i(TAG, "Writing to file complete.");
        } catch(IOException e){
            e.printStackTrace();
            Log.i(TAG, "Could not write to file. " + e.getMessage());
        } finally {
            fileWriter.flush();
            fileWriter.close();
//            fileOutputStream.flush();
//            fileOutputStream.close();
        }
    }

    @Override
    public void onDestroy() {
        if(dataBroadcastReceiver != null) {
            unregisterReceiver(dataBroadcastReceiver);
        }
        super.onDestroy();
    }
}
