package swarm.swarmcomposerapp.Model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Model representation for an GET compositions Request
 *
 */
public class CompositionsAnswer {
    @SerializedName("editable")
    List<Composition> editable;
    @SerializedName("viewable")
    List<Composition> viewable;
    @SerializedName("publicComps")
    List<Composition> publicComps;
    @SerializedName("owns")
    List<Composition> owns;

    public List<Composition> getEditable() {
        return editable;
    }

    public List<Composition> getViewable() {
        viewable.addAll(editable);
        return viewable;
    }

    public List<Composition> getPublicComps() {
        HashSet<Long> ids = new HashSet<>();

        List<Composition> toReturn = new ArrayList<>();


        /*

         */
        for (Composition c : owns) {
           ids.add(c.getId());
        }
        for (Composition c : getViewable()) {
           ids.add(c.getId());
        }

        addCompWithIdNotInSet(publicComps, ids, toReturn);

        return toReturn;
    }

    public List<Composition> getOwns() { return owns; }

    /**
     * Merges all compositions that are seeable for the current user.
     *
     * @return
     */
    public List<Composition> getSeeableComps() { //TODO remove
        HashSet<Long> ids = new HashSet<>();

        List<Composition> toReturn = new ArrayList<>();


        /*
        /Add all Compositions from publicComps to toReturn and remember their id in ids
         */
        for (Composition c : publicComps) {
            ids.add(c.getId());
            toReturn.add(c);
        }


        addCompWithIdNotInSet(editable, ids, toReturn);
        addCompWithIdNotInSet(viewable, ids, toReturn);
        addCompWithIdNotInSet(owns, ids, toReturn);

        return toReturn;
    }

    /**
     * Only adds Compositions from the elementPool that don't
     * have an id in the set to the targetList.
     *
     * @param elementPool
     * @param set
     * @param targetList
     */
    private void addCompWithIdNotInSet(List<Composition> elementPool,
                                       HashSet<Long> set, List<Composition> targetList) {
        for (Composition c : elementPool) {
            if (!set.contains(c.getId())) {
                targetList.add(c);
            }
        }
    }

}
