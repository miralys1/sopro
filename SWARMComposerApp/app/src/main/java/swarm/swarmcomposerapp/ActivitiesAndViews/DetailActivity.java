package swarm.swarmcomposerapp.ActivitiesAndViews;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.Model.Edge;
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
    private TextView tTitle, tLoading, tOwner, tLastUpdated;
    private TextView col1, col2, col3, col4, col5;
    private LinearLayout serviceInfo;
    private ScrollView scrollView;
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
        col5 = findViewById(R.id.text_col5);
        scrollView = findViewById(R.id.edge_list);
        tLoading = findViewById(R.id.text_loading3);
        tOwner = findViewById(R.id.text_owner);
        serviceInfo = findViewById(R.id.serviceInfo);
        tLastUpdated = findViewById(R.id.lastUpdate);

        //retrieve the (app-)internal id of the composition
        Intent intent = getIntent();
        position = intent.getIntExtra("COMP_POSITION", -1);

        compositionView = findViewById(R.id.compositionView3);
        compositionView.setParentActivity(this);

        listID = intent.getIntExtra("LIST_ID", -1);
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
            tLastUpdated.setText(getText(R.string.lastupdate)+": "+dateFormat.format(comp.getLastUpdate()));
            setEdgeList();
        } else {
            showLoading(true);
        }
    }

    public void onNodeSelected(Node node){
        String col1text = "";
        String col2text = "";
        if(node != null) {
            col1text = getText(R.string.service_name) + ": \n" + getText(R.string.service_organisation) + ": \n"
                            +getText(R.string.service_version) + ": \n"+getText(R.string.service_date)
                            + ": \n"+getText(R.string.service_certified) + ": \n";

            Service service = node.getSendService();
            col2text +=  service.getServiceName()+"\n";
            col2text +=  service.getOrganisation()+"\n";
            col2text +=  service.getVersion()+"\n";
            col2text +=  dateFormat.format(service.getDate()*1000)+"\n";
            if(service.getCertified() != null && service.getCertified().equals("true")){
                col2text +=  getText(R.string.service_certified_yes)+"\n";
            } else {
                col2text += getText(R.string.service_certified_no) + "\n";
            }
        }
        col1.setText(col1text);
        col2.setText(col2text);

        scrollView.setVisibility((node == null) ? View.VISIBLE : View.GONE);
        serviceInfo.setVisibility((node == null) ? View.GONE : View.VISIBLE);
    }

    private void setEdgeList(){
        String col3text = "";
        String col4text = "";
        String col5text = "";

        for(Edge e : comp.getEdgeList()){
            col3text += e.getOut().getSendService().getServiceName()+"\n";
            col5text += e.getIn().getSendService().getServiceName()+"\n";
            if(e.getCompatibility().isCompatible()){
                //compatible
                col4text+="<font color='"+getColor(R.color.compatibility_green)+"'>"+getText(R.string.ic_compatible)+"</font><br>";
            } else if (e.getCompatibility().getAlternatives().isEmpty()){
                //incompatible
                col4text+="<font color='"+getColor(R.color.compatibility_red)+"'>"+getText(R.string.ic_incompatible)+"</font><br>";
            } else {
                //alternative
                col4text+="<font color='"+getColor(R.color.compatibility_yellow)+"'>"+getText(R.string.ic_alternative)+"</font><br>";
            }
        }
        col3.setText(col3text);
        col4.setText(Html.fromHtml(col4text));
        col5.setText(col5text);
        scrollView.setVisibility(View.VISIBLE);
        serviceInfo.setVisibility(View.GONE);
    }

    /**
     * changes the views to display a loading bar and text
     *
     * @param activate turn loading view on
     */
    private void showLoading(boolean activate) {
        tLoading.setVisibility(activate ? View.VISIBLE : View.GONE);
        serviceInfo.setVisibility(View.GONE);
        col1.setVisibility(activate ? View.GONE : View.VISIBLE);
        col2.setVisibility(activate ? View.GONE : View.VISIBLE);
        scrollView.setVisibility(activate ? View.GONE : View.VISIBLE);
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
        if(comp != null) {
            String path = PDFCreator.createPDF(this, this, comp);
            if(path == null){
                Toast.makeText(getApplicationContext(), getText(R.string.err_text_filesave), Toast.LENGTH_LONG).show();
            } else {
                Log.i("PDF", "saved to "+path);
                openShareDialog(path);
            }
        }
    }

    private void openShareDialog(String path){
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
        if(path == null){
            Toast.makeText(getApplicationContext(), getText(R.string.err_text_filesave), Toast.LENGTH_LONG).show();
        } else {
            Log.i("PDF", "saved to "+path);
            openShareDialog(path);
        }
    }

    @Override
    public void notify(boolean successful) {
        if (successful && !noNodes && comp != null) {
            //needed composition details are in LocalCache now
            comp = cache.getCompAtPos(position, this, listID);

            if (comp.getNodeList() == null || comp.getNodeList().isEmpty()) {
                noNodes = true;
            }

            tTitle.setText(comp.getName());
            tOwner.setText(comp.getOwner().getFullName());
            tLastUpdated.setText(getText(R.string.lastupdate)+": "+dateFormat.format(comp.getLastUpdate()));
            col1.setText(getText(R.string.lastupdate) + " " + dateFormat.format(comp.getLastUpdate()));
            Log.i("DetailActivity", "View should receive Comp with " + comp.getNodeList().size() + " nodes");
            compositionView.setComp(comp);
            setEdgeList();
        } else {
            if (noNodes) {
                Toast.makeText(getApplicationContext(), "There are no nodes within this composition", Toast.LENGTH_SHORT).show();

            } else {
                //TODO show error message
                Toast.makeText(getApplicationContext(), "server connection failed when requesting details", Toast.LENGTH_SHORT).show();
            }
        }
        showLoading(false);
    }
}
