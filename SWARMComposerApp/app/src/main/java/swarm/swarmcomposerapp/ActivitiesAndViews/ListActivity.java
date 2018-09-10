package swarm.swarmcomposerapp.ActivitiesAndViews;

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

public class ListActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Composition> compList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //TODO get List from Cache onResume()
        initList();
        

        //TODO if Chache indicates waiting time (server request initiated) show loading screen

        //TODO when data is ready (onResponse) inflate RecyclerView

        //TODO listen for touches, initiate Intent to open DetailActivity

        //TODO create Intent open SettingsActivity




        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        recycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        adapter = new ListAdapter(compList);
        recycler.setAdapter(adapter);

        recycler.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Composition comp = compList.get(position);
                Toast.makeText(getApplicationContext(), comp.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    private void initList(){
        compList = new ArrayList<>();
        compList.add(new Composition(1, "Tolle Komposition", new SimpleUser(1, "Connor", "Tarvos")));
        compList.add(new Composition(2, "Tolle Komposition 2", new SimpleUser(1, "Felix", "Gröner")));
        compList.add(new Composition(3, "Tolle Komposition 3", new SimpleUser(1, "Max", "Mustermann")));
        compList.add(new Composition(4, "Tolle Komposition 4", new SimpleUser(1, "Mustermann", "Max")));

    }
}
