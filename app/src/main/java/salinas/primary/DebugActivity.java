package salinas.primary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.app.Activity;

import salinas.primary.data.UserParameterString;
import salinas.primary.randomdata.RandomDataService;

/**
 * Created by Jose Salinas on 4/13/2017.
 */

public class DebugActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        Intent randomDataIntent = new Intent(this.getBaseContext(), RandomDataService.class);
        randomDataIntent.setData(Uri.parse((new UserParameterString(true, true, true, true, true, true)).getData()));

        this.startService(randomDataIntent);
    }
}
