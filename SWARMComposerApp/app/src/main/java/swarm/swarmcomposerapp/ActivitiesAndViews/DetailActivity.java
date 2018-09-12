package swarm.swarmcomposerapp.ActivitiesAndViews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.Model.LocalCache;
import swarm.swarmcomposerapp.R;

public class DetailActivity extends AppCompatActivity implements IResponse {

    private Composition comp;
    private TextView tTitle, tInfo, tLoading;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        progressBar = findViewById(R.id.progressBar3);
        tTitle = findViewById(R.id.text_title);
        tInfo = findViewById(R.id.text_info);
        tLoading = findViewById(R.id.text_loading3);

        Intent intent = getIntent();
        int pos = intent.getIntExtra("COMP_POS", -1);
        try {
            comp = LocalCache.getInstance().getCompAtPos(pos, this);
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            goBackToList(null);
        }
        if(comp != null){
            showLoading(false);
            draw();
        }

    }

    private void draw(){
        //TODO create compView etc
    }

    private void showLoading(boolean activate){
        tLoading.setVisibility(activate ? View.VISIBLE : View.GONE);
        tInfo.setVisibility(activate ? View.GONE : View.VISIBLE);
        tTitle.setVisibility(activate ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(activate ? View.VISIBLE : View.GONE);
    }

    public void goBackToList(View v){
        super.onBackPressed();
    }

    public void sendComposition(View v){
        //TODO create PDF, open share_dialog
    }

    @Override
    public void notify(boolean successful) {
        if(successful){
            draw();
        } else {
            //TODO show error message
            Toast.makeText(getApplicationContext(), "server connection failed when requesting details", Toast.LENGTH_SHORT).show();
        }
        showLoading(false);
    }
}
