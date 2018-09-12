package swarm.swarmcomposerapp.ActivitiesAndViews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import swarm.swarmcomposerapp.Model.LocalCache;
import swarm.swarmcomposerapp.Model.Service;
import swarm.swarmcomposerapp.R;

public class MainActivity extends AppCompatActivity implements IResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Service[] services = LocalCache.getInstance().getServices(this);
    }

    @Override
    public void notify(Boolean successful) {
        if(successful){
            Service[] services = LocalCache.getInstance().getServices(this);
            Toast.makeText(this, "Success: first service name is: "+
                    services[0].getServiceName(),Toast.LENGTH_LONG).show();

        }
    }
}
