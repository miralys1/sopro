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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.Model.ICache;
import swarm.swarmcomposerapp.Model.LocalCache;
import swarm.swarmcomposerapp.Model.SimpleUser;
import swarm.swarmcomposerapp.R;

/**
 * This activity will be called at the start of the app as the main activity.
 * An overview of all available compositions will be displayed as a RecyclerView.
 */
public class ListActivity extends AppCompatActivity implements IResponse {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Composition> compList = new ArrayList<>();
    private TextView tLoading;
    private ProgressBar progressBar;
    private static final String PREFERENCE_NAME = "app_settings";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private LocalCache cache = LocalCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //load saved preferences such as email and server address
        preferences = getSharedPreferences(PREFERENCE_NAME, 0);
        editor = preferences.edit();
        String address = preferences.getString("SERVERADDRESS", null);
        if(address == null)
            //it's the very first start of the app
            showWelcomeScreen();
        else
            cache.setServerAdress(address);

        String email = preferences.getString("EMAIL", null);
        if(email != null)
            cache.setEmail(email);


        tLoading = findViewById(R.id.text_loading1);
        progressBar = findViewById(R.id.progressBar1);

        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        recycler.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        adapter = new ListAdapter(compList);
        recycler.setAdapter(adapter);

        recycler.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //User clicked on one of the compositions in the list. Intent to open it in DetailActivity.
                Composition comp = compList.get(position);
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                //pass the (app-) internal id of the requested composition to the DetailActivity via intent
                intent.putExtra("COMP_POSITION", position);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    @Override
    protected void onResume(){
        super.onResume();
        //refresh the list with latest data from LocalCache every time the user returns to ListActitvity.
        //also called when the app is started
        updateList();
    }

    /**
     * get the latest data from LocalCache and put it in the RecyclerView
     */
    private void updateList(){
        compList = LocalCache.getInstance().getCompositions(this);
        if(compList == null){
            showLoading(true);
        } else {
            adapter.notifyDataSetChanged();
        }
    }


    private void initList(){
        //TODO remove
        compList = new ArrayList<>();
        compList.add(new Composition(1, "Tolle Komposition", new SimpleUser(1, "Connor", "Tarvos")));
        compList.add(new Composition(2, "Tolle Komposition 2", new SimpleUser(2, "Felix", "Gröner")));
        compList.add(new Composition(3, "Tolle Komposition 3", new SimpleUser(3, "Max", "Mustermann")));
        compList.add(new Composition(4, "Tolle Komposition 4", new SimpleUser(4, "Max", "Mustermann")));

    }

    /**
     * invalidate all cache data and initiate a new server request
     * @param v
     */
    public void reloadList(View v){
        showLoading(true);
        LocalCache.getInstance().hardRefresh(this);
    }

    public void startSettingsActivity(View v){
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * changes the views to display a loading bar and text
     * @param activate turn loading view on
     */
    private void showLoading(boolean activate){
            tLoading.setVisibility(activate ? View.VISIBLE : View.GONE);
            progressBar.setVisibility(activate ? View.VISIBLE : View.GONE);
    }

    @Override
    public void notify(boolean successful) {
        if(successful) {
            //overview data is now available at LocalCache
            compList = LocalCache.getInstance().getCompositions(this);
            adapter.notifyDataSetChanged();
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
    private void showWelcomeScreen(){
        LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
