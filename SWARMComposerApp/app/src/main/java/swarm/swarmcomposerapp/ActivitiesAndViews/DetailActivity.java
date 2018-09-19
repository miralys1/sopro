package swarm.swarmcomposerapp.ActivitiesAndViews;

import android.app.ActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.Model.LocalCache;
import swarm.swarmcomposerapp.Model.Node;
import swarm.swarmcomposerapp.Model.Service;
import swarm.swarmcomposerapp.R;
import swarm.swarmcomposerapp.Utils.PDFCreator;

/**
 * This activity displays a single composition in detail. The graph is drawn and details are presented as text.
 */
public class DetailActivity extends AppCompatActivity implements IResponse {

    private Composition comp;
    private TextView tTitle, tLoading, tOwner;
    private TextView col1, col2, col3, col4;
    private ProgressBar progressBar;
    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");
    private int position; //(app-)internal id
    private CompositionView compositionView;
    private int listID;
    private LocalCache cache = LocalCache.getInstance();

    private boolean noNodes = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        progressBar = findViewById(R.id.progressBar3);
        tTitle = findViewById(R.id.text_title);
        col1 = findViewById(R.id.text_col1);
        col2 = findViewById(R.id.text_col2);
        col3 = findViewById(R.id.text_col3);
        col4 = findViewById(R.id.text_col4);
        tLoading = findViewById(R.id.text_loading3);
        tOwner = findViewById(R.id.text_owner);

        //retrieve the (app-)internal id of the composition
        Intent intent = getIntent();
        position = intent.getIntExtra("COMP_POSITION", -1);

        compositionView = findViewById(R.id.compositionView3);
        compositionView.setParentActivity(this);

        listID = intent.getIntExtra("LIST_ID", -1);
        Log.i("DETAIL", "called position " + position + " of list " + listID);
        try {
            comp = cache.getCompAtPos(position, this, listID);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            goBackToList(null);
        }
        if (comp != null && comp.getNodeList() != null && comp.getEdgeList() != null) {
            //the needed composition details are not stored in the LocalCache yet; a request has been sent by LocalCache
            showLoading(false);
            tTitle.setText(comp.getName());
            tOwner.setText(comp.getOwner().getFullName());
            col1.setText(getText(R.string.lastupdate) + " " + dateFormat.format(comp.getLastUpdate()));
            compositionView.setComp(comp);
        } else {
            showLoading(true);
        }
    }

    public void onNodeSelected(Node node) {
        String col1text = getText(R.string.lastupdate) + ": ";
        String col2text = "";
        String col3text = "";
        String col4text = "";
        if (node != null) {
            col1text = getText(R.string.service_name) + ": \n" + getText(R.string.service_organisation) + ": \n"
                    + getText(R.string.service_version) + ": \n" + getText(R.string.service_date)
                    + ": \n" + getText(R.string.service_certified) + ": \n" + col1text;

            Service service = node.getSendService();
            col2text += service.getServiceName() + "\n";
            col2text += service.getOrganisation() + "\n";
            col2text += service.getVersion() + "\n";
            col2text += dateFormat.format(service.getDate()) + "\n";
            if (service.getCertified() != null && service.getCertified().equals("true")) {
                col2text += getText(R.string.service_certified_yes) + "\n";
            } else {
                col2text += getText(R.string.service_certified_no) + "\n";
            }
        }
        col2text += dateFormat.format(comp.getLastUpdate());
        col1.setText(col1text);
        col2.setText(col2text);
        col3.setText(col3text);
        col4.setText(col4text);
    }

    /**
     * changes the views to display a loading bar and text
     *
     * @param activate turn loading view on
     */
    private void showLoading(boolean activate) {
        tLoading.setVisibility(activate ? View.VISIBLE : View.GONE);
        col1.setVisibility(activate ? View.GONE : View.VISIBLE);
        col2.setVisibility(activate ? View.GONE : View.VISIBLE);
        col3.setVisibility(activate ? View.GONE : View.VISIBLE);
        col4.setVisibility(activate ? View.GONE : View.VISIBLE);
        tTitle.setVisibility(activate ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(activate ? View.VISIBLE : View.GONE);
        compositionView.setVisibility(activate ? View.GONE : View.VISIBLE);
    }

    public void goBackToList(View v) {
        super.onBackPressed();
    }

    /**
     * creates document via PDFCreator and opens dialog for user to send it
     *
     * @param v
     */
    public void sendComposition(View v) {
        //TODO create PDF, open share_dialog
        if (comp != null) {
            String path = PDFCreator.createPDF(this, this, comp);
            openShareDialog(path);
        }
    }

    private void openShareDialog(String path) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("application/pdf");

        share.putExtra(Intent.EXTRA_STREAM,
                Uri.parse(path));

        startActivity(Intent.createChooser(share, getText(R.string.pdf_share)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //permission to write to storage has been granted
        String path = PDFCreator.createPDF(this, this, comp);
        openShareDialog(path);
    }

    @Override
    public void notify(boolean successful) {
        if (successful && !noNodes && comp != null) {
            //needed composition details are in LocalCache now
            comp = cache.getCompAtPos(position, this, listID);
//            if (comp == null) {
//                //TODO handle fatal event
//                Toast.makeText(getApplicationContext(), getText(R.string.err_text_detail), Toast.LENGTH_SHORT).show();
//                goBackToList(null);
//                return;
//            }

            if (comp.getNodeList() == null || comp.getNodeList().isEmpty()) {
                noNodes = true;
            }

            tTitle.setText(comp.getName());
            tOwner.setText(comp.getOwner().getFullName());
            col1.setText(getText(R.string.lastupdate) + " " + dateFormat.format(comp.getLastUpdate()));
            Log.i("DetailActivity", "View should receive Comp with " + comp.getNodeList().size() + " nodes");
            compositionView.setComp(comp);
        } else {
            if (noNodes) {
                Toast.makeText(getApplicationContext(), "There are no nodes within this composition", Toast.LENGTH_SHORT).show();

            } else {

                //server communication failed
                //TODO show error message
                Toast.makeText(getApplicationContext(), "server connection failed when requesting details", Toast.LENGTH_SHORT).show();
            }
        }
        showLoading(false);
    }


}
