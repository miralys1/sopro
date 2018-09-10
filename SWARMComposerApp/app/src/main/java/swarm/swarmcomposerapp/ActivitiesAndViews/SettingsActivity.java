package swarm.swarmcomposerapp.ActivitiesAndViews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import swarm.swarmcomposerapp.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void goBackToList(View v){
        super.onBackPressed();
    }

    public void showHelp(View v){
        //TODO implement: Show handbook & credits
    }
}
