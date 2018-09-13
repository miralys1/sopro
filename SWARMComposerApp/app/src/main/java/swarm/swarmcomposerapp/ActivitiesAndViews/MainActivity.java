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



    }

    @Override
    public void notify(boolean successful) {

    }
}
