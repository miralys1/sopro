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

/**
 * This activity displays a single composition in detail. The graph is drawn and details are presented as text.
 */
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

        //retrieve the (app-)internal id of the composition
        Intent intent = getIntent();
        int pos = intent.getIntExtra("COMP_POSITION", -1);
        try {
            comp = LocalCache.getInstance().getCompAtPos(pos, this);
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            goBackToList(null);
        }
        if(comp != null){
            //the needed composition details are not stored in the LocalCache yet; a request has been sent by LocalCache
            showLoading(false);
            draw();
        }

    }

    /**
     * initiate graph drawing by CompositionView
     */
    private void draw(){
        //TODO create compView etc
    }

    /**
     * changes the views to display a loading bar and text
     * @param activate turn loading view on
     */
    private void showLoading(boolean activate){
        tLoading.setVisibility(activate ? View.VISIBLE : View.GONE);
        tInfo.setVisibility(activate ? View.GONE : View.VISIBLE);
        tTitle.setVisibility(activate ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(activate ? View.VISIBLE : View.GONE);
    }

    public void goBackToList(View v){
        super.onBackPressed();
    }

    /**
     * creates document via PDFCreator and opens dialog for user to send it
     * @param v
     */
    public void sendComposition(View v){
        //TODO create PDF, open share_dialog
    }

    @Override
    public void notify(boolean successful) {
        if(successful){
            //needed composition details are in LocalCache now
            draw();
        } else {
            //server communication failed
            //TODO show error message
            Toast.makeText(getApplicationContext(), "server connection failed when requesting details", Toast.LENGTH_SHORT).show();
        }
        showLoading(false);
    }
}
