package salinas.primary.file;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import salinas.primary.file.Constants.*;
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
        
    }

    public boolean isExternalStorageWriteable() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public File makeDataOutputFile(String outputFileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), outputFileName);
        if(!file.mkdir()) {
            Log.e(TAG, "Directory not created.");
        }
        return file;
    }

    private void writeToFile(String outputFileName, String data) {
        File file = makeDataOutputFile(outputFileName);

        try {
            Log.e(TAG, "Attempting to write to file.");
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(data.getBytes(Charset.forName("UTF-8")));
            fileOutputStream.flush();
            fileOutputStream.close();
            Log.e(TAG, "Writing to file complete.");
        } catch(IOException e){
            e.printStackTrace();
            Log.e(TAG, "Could not write to file.");
        }
    }
}
