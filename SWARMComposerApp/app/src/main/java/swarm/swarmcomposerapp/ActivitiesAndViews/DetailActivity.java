package swarm.swarmcomposerapp.ActivitiesAndViews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import swarm.swarmcomposerapp.R;

public class DetailActivity extends AppCompatActivity implements IResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }


    public void goBackToList(View v){
        super.onBackPressed();
    }

    public void sendComposition(View v){
        //TODO create PDF, open share_dialog
    }

    @Override
    public void notify(boolean successful) {

    }
}
