package swarm.swarmcomposerapp.Model;

import java.util.List;

public class Service {

    private boolean isFlexible;
    private String serviceName;
    private String version;
    private String organisation;
    private String picture;
    private long id;
    private long date;
    private List<String> tags;

    private List<Service> in;
    private List<Service> out;
}
