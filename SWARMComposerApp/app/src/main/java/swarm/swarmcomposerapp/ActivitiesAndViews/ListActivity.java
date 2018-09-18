package swarm.swarmcomposerapp.ActivitiesAndViews;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.Model.LocalCache;
import swarm.swarmcomposerapp.Model.SimpleUser;
import swarm.swarmcomposerapp.R;

/**
 * This activity will be called at the start of the app as the main activity.
 * An overview of all available compositions will be displayed as a RecyclerView.
 */
public class ListActivity extends AppCompatActivity implements IResponse {

    private RecyclerView recycler_public, recycler_viewable, recycler_owned;
    private ListAdapter adapter_public, adapter_viewable, adapter_owned;
    private RecyclerView.LayoutManager layoutManager, layoutManager2, layoutManager3;
    private TextView tLoading, tLastUpdate, tOwned, tViewable, tPublic;
    private ProgressBar progressBar;
    private static final String PREFERENCE_NAME = "app_settings";
    private SharedPreferences preferences;
    private LocalCache cache = LocalCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //load saved preferences such as email and server address
        preferences = getSharedPreferences(PREFERENCE_NAME, 0);
        String address = preferences.getString("SERVERADDRESS", null);
        if (address == null) {
            //it's the very first start of the app
            showWelcomeScreen();
        } else {
            cache.setServerAddress(address);
        }

        String email = preferences.getString("EMAIL", null);
        if (email != null)
            cache.setEmail(email);

        tOwned = findViewById(R.id.text_owned);
        tPublic = findViewById(R.id.text_public);
        tViewable = findViewById(R.id.text_viewable);

        tLastUpdate = findViewById(R.id.text_lastupdate);
        tLoading = findViewById(R.id.text_loading1);
        progressBar = findViewById(R.id.progressBar1);

        recycler_owned = findViewById(R.id.recyclerView_owned);
        recycler_public = findViewById(R.id.recyclerView_public);
        recycler_viewable = findViewById(R.id.recyclerView_viewable);

        layoutManager = new LinearLayoutManager(this);
        layoutManager2 = new LinearLayoutManager(this);
        layoutManager3 = new LinearLayoutManager(this);

        recycler_viewable.setLayoutManager(layoutManager);
        recycler_public.setLayoutManager(layoutManager2);
        recycler_owned.setLayoutManager(layoutManager3);

        adapter_owned = new ListAdapter(cache.getCompositions(this, LocalCache.ListIdentifier.OWNED));
        recycler_owned.setAdapter(adapter_owned);
        adapter_public = new ListAdapter(null);
        recycler_public.setAdapter(adapter_public);
        adapter_viewable = new ListAdapter(null);
        recycler_viewable.setAdapter(adapter_viewable);

        addRecyclerTouchListener(recycler_owned, 0);
        addRecyclerTouchListener(recycler_viewable, 1);
        addRecyclerTouchListener(recycler_public, 2);

        Intent start = new Intent(getApplicationContext(),MainActivity.class);
        //startActivity(start);
    }

    private void addRecyclerTouchListener(RecyclerView recyclerView, final int listID){
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //User clicked on one of the compositions in the list. Intent to open it in DetailActivity.
                showLoading(false);
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                //pass the (app-) internal id of the requested composition to the DetailActivity via intent
                intent.putExtra("COMP_POSITION", position);
                intent.putExtra("LIST_ID", listID);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    @Override
    protected void onResume() {
        super.onResume();
        //refresh the list with latest data from LocalCache every time the user returns to ListActitvity.
        //also called when the app is started
        updateList();
    }

    /**
     * get the latest data from LocalCache and put it in the RecyclerView
     */
    private void updateList() {
        ArrayList<Composition> compList;
        compList = LocalCache.getInstance().getCompositions(this, LocalCache.ListIdentifier.PUBLIC);
        if(cache.hasData()) {
            adapter_public.setCompList(compList);
            compList = LocalCache.getInstance().getCompositions(this, LocalCache.ListIdentifier.VIEWABLE);
            tViewable.setVisibility(compList.isEmpty() ? View.GONE : View.VISIBLE);
            adapter_viewable.setCompList(compList);
            compList = LocalCache.getInstance().getCompositions(this, LocalCache.ListIdentifier.OWNED);
            tOwned.setVisibility(compList.isEmpty() ? View.GONE : View.VISIBLE);
            adapter_owned.setCompList(compList);
        } else {
            //there are not compositions in any of the lists.
            showLoading(true);
        }
    }

    /**
     * invalidate all cache data and initiate a new server request
     *
     * @param v
     */
    public void reloadList(View v) {
        showLoading(true);
        LocalCache.getInstance().hardRefresh(this);
    }

    public void startSettingsActivity(View v) {
        showLoading(false);
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * changes the views to display a loading bar and text
     *
     * @param activate turn loading view on
     */
    private void showLoading(boolean activate) {
        tLoading.setVisibility(activate ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(activate ? View.VISIBLE : View.GONE);
    }

    @Override
    public void notify(boolean successful) {
        if (successful) {
            //overview data is now available at LocalCache
            ArrayList<Composition> compList;
            compList = LocalCache.getInstance().getCompositions(this, LocalCache.ListIdentifier.PUBLIC);
            if(compList == null){
                //TODO handle fatal event
                Toast.makeText(getApplicationContext(), getText(R.string.err_text_detail), Toast.LENGTH_SHORT).show();
                return;
            } else {
                adapter_public.setCompList(compList);
                compList = LocalCache.getInstance().getCompositions(this, LocalCache.ListIdentifier.VIEWABLE);
                adapter_viewable.setCompList(compList);
                compList = LocalCache.getInstance().getCompositions(this, LocalCache.ListIdentifier.OWNED);
                adapter_owned.setCompList(compList);
            }
            tLastUpdate.setText(getText(R.string.lastupdate)+": "+cache.getLastUpdate());
        } else {
            //server request failed
            //TODO show error dialog with tips
            Toast.makeText(getApplicationContext(), getText(R.string.err_text_list), Toast.LENGTH_SHORT).show();
        }
        showLoading(false);
    }

    /**
     * Show a welcome message with tips on how to start using the app.
     * Only executed once on the very first start of the app.
     */
    private void showWelcomeScreen() {
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.fragment_dialog, null);
        ((TextView) layout.findViewById(R.id.d_title)).setText(getText(R.string.d_welcome_title));
        ((TextView) layout.findViewById(R.id.d_text)).setText(getText(R.string.d_welcome_text));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setNeutralButton(getText(R.string.d_button_close), null);
        alertDialogBuilder.setPositiveButton(getText(R.string.d_welcome_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startSettingsActivity(null);
            }
        });
        alertDialogBuilder.setView(layout);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
