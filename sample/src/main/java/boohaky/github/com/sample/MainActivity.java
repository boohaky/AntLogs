package boohaky.github.com.sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import boohaky.github.com.antlogs.log.LogConfiguration;
import boohaky.github.com.antlogs.log.LogInstance;

public class MainActivity extends AppCompatActivity {

    private static final LogInstance LOG_INSTANCE = new LogInstance(getConfiguration());
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG_INSTANCE.d(this, "");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LOG_INSTANCE.d(TAG, "click click click");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static LogConfiguration getConfiguration() {
        final LogConfiguration.Builder builder = new LogConfiguration.Builder();
        builder.setLogDirectory("AntLogs/MainActivity");
        builder.setCreationSchedule(LogConfiguration.FileScheduleCreation.EVERY_HOUR);
        builder.setFileLimitSize(1024);
        return builder.build();
    }

}
