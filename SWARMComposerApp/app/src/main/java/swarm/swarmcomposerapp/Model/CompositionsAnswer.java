package swarm.swarmcomposerapp.Model;


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

    /**
     * @return concatenation of editable and viewable compositions
     */
    public List<Composition> getViewable() {
        boolean b = true;
        for (Composition c : editable) {
            b = true;
            for (Composition d : viewable) {
                if(c.getId() == d.getId())
                    b = false;
            }
            if(b)
                viewable.add(c);
        }
        return viewable;
    }

    /**
     * filters the list of public compositions to only show those which are not owned by or shared with the current user
     * @return the filtered list of public compositions
     */
    public List<Composition> getPublicComps() {
        HashSet<Long> ids = new HashSet<>();

        List<Composition> toReturn = new ArrayList<>();

        //collect the IDs of viewable and owned comps
        for (Composition c : owns) {
           ids.add(c.getId());
        }
        for (Composition c : getViewable()) {
           ids.add(c.getId());
        }

        //remove comps which are already displayed in another list
        addCompWithIdNotInSet(publicComps, ids, toReturn);

        return toReturn;
    }

    public List<Composition> getOwns() { return owns; }

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
