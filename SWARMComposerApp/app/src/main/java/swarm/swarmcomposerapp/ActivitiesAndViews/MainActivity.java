package swarm.swarmcomposerapp.ActivitiesAndViews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import swarm.swarmcomposerapp.Model.Composition;
import swarm.swarmcomposerapp.Model.Edge;
import swarm.swarmcomposerapp.Model.LocalCache;
import swarm.swarmcomposerapp.Model.Node;
import swarm.swarmcomposerapp.Model.Service;
import swarm.swarmcomposerapp.Model.SimpleUser;
import swarm.swarmcomposerapp.R;

public class MainActivity extends AppCompatActivity implements IResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Node> nList = new ArrayList<>();
        Node n1 = new Node(0,20,1);
        Node n2 = new Node(30,40000,2);
        Node n3 = new Node(40, 50, 4);
        nList.add(n1);
        nList.add(n2);
        nList.add(n3);
        List<Edge> eList = new ArrayList<>();
        Edge e1 = new Edge(n1,n2,null);
        Edge e2 = new Edge(n1, n3,null);
        eList.add(e1);
        eList.add(e2);


        Composition c2 = new Composition(2,"testi",new SimpleUser(1,"Karl", "Karlson")
        ,eList,nList);

        CompositionView compi = findViewById(R.id.compositionView);
        compi.setComp(c2);
        Toast.makeText(getApplicationContext(),"comp should be added now",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void notify(boolean successful) {

    }
}
