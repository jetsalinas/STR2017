package salinas.primary.file;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

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
}
