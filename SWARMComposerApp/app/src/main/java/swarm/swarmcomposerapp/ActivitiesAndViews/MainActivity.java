package swarm.swarmcomposerapp.ActivitiesAndViews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import swarm.swarmcomposerapp.Model.CompatibilityAnswer;
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
        Service test = new Service("test","1.0","Super","igd_modeller.png",2,2,null,null,null);

        Node n1 = new Node(10,10,test);
        Node n2 = new Node(300,300,test);
        Node n3 = new Node(400,400,test);
        Edge e1 = new Edge(n1,n2,new CompatibilityAnswer(true,null,null));
        Edge e3 = new Edge(n1,n3,new CompatibilityAnswer(false,null,null));
        final ArrayList<Edge> edges2 = new ArrayList<>();
        edges2.add(e1);
        edges2.add(e3);
        ArrayList<Node> nodis = new ArrayList<>();
        nodis.add(n1);
        nodis.add(n2);
        nodis.add(n3);

        Composition c1 = new Composition(0, "tolle Komposition",new SimpleUser(1,"Harald","Derp"),edges2,nodis);


        CompositionView compi = findViewById(R.id.compositionView);
        compi.setComp(c1);
        Toast.makeText(getApplicationContext(),"comp should be added now",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void notify(boolean successful) {
        if (successful) {
            Service[] services = LocalCache.getInstance().getServices(this);
            Toast.makeText(this, "Success: first service name is: " +
                    services[0].getServiceName(), Toast.LENGTH_LONG).show();

        }
    }
}