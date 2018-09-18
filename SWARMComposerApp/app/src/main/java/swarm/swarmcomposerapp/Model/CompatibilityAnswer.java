package swarm.swarmcomposerapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompatibilityAnswer {

    @SerializedName("compatibility")
    private boolean isCompatible;
    @SerializedName("suitingFormats")
    List<String> suitingFormats;

    public boolean isCompatible() {
        return isCompatible;
    }

    public List<String> getSuitingFormats() {
        return suitingFormats;
    }

    public List<Alternative> getAlternatives() {
        return alternatives;
    }

    @SerializedName("alternatives")
    List<Alternative> alternatives;


    public CompatibilityAnswer(boolean isCompatible, List<String> suitingFormats,
                               List<Alternative> alternatives) {
        this.isCompatible = isCompatible;
        this.suitingFormats = suitingFormats;
        this.alternatives = alternatives;
    }




    /**
     * Add one alternative to the Node
     * @param alt
     */
    public void addAlternative(Alternative alt){
        alternatives.add(alt);
    }
    /**
     * Add one alternatives to the Node
     * @param alts
     */
    public void addAlternatives(List<Alternative> alts){
        alternatives.addAll(alts);
    }


}
