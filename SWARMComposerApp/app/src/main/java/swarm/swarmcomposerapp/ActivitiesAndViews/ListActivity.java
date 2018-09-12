package swarm.swarmcomposerapp.ActivitiesAndViews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.Model.ICache;
import swarm.swarmcomposerapp.Model.LocalCache;
import swarm.swarmcomposerapp.Model.SimpleUser;
import swarm.swarmcomposerapp.R;

public class ListActivity extends AppCompatActivity implements IResponse {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Composition> compList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initList();

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
                intent.putExtra("COMP_POSITION", position);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        Intent intent =  new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onResume(){
        super.onResume();
        compList = LocalCache.getInstance().getCompositions(this);
        initList();
        if(compList == null){
            //TODO show loading screen
            Toast.makeText(getApplicationContext(), "loading...", Toast.LENGTH_SHORT).show();

        } else {
            adapter.notifyDataSetChanged();
        }
    }

    //called by LocalCache when server data has arrived
    public void onResponse(ArrayList<Composition> compList){
            this.compList = compList;
            adapter.notifyDataSetChanged();
    }

    //called by LocalCache when server connection has failed
    public void onFailure(){
        //TODO show error message
        Toast.makeText(getApplicationContext(), "server connection failed", Toast.LENGTH_SHORT).show();
    }

    private void initList(){
        compList = new ArrayList<>();
        compList.add(new Composition(1, "Tolle Komposition", new SimpleUser(1, "Connor", "Tarvos")));
        compList.add(new Composition(2, "Tolle Komposition 2", new SimpleUser(2, "Felix", "Gröner")));
        compList.add(new Composition(3, "Tolle Komposition 3", new SimpleUser(3, "Max", "Mustermann")));
        compList.add(new Composition(4, "Tolle Komposition 4", new SimpleUser(4, "Max", "Mustermann")));

    }

    public void reloadList(View v){
        LocalCache.getInstance().hardRefresh(this);
    }

    public void startSettingsActivity(View v){
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void notify(boolean successful) {

    }
}
