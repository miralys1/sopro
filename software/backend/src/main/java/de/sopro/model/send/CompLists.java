package de.sopro.model.send;

import java.util.List;

public class CompLists{
    private List<SimpleComp> editable;
    private List<SimpleComp> viewable;
    private Iterable<SimpleComp> publicComps;

    public CompLists(List<SimpleComp> editable,List<SimpleComp> viewable, Iterable<SimpleComp> publicComps){
        this.editable = editable;
        this.viewable = viewable;
        this.publicComps = publicComps;
    }

    public List<SimpleComp> getEditable(){
        return editable;
    }

    public List<SimpleComp> getviewable(){
        return viewable;
    }

    public Iterable<SimpleComp> getpublicComps(){
        return publicComps;
    }

    // public void setEditable(List<SimpleComp> editable){
    //     this.editable = editable;
    // }

    // public void setViewable(List<SimpleComp> viewable){
    //     this.viewable = viewable;
    // }

    // public void setPublicComps(List<SimpleComp> publicComps){
    //     this.publicComps = publicComps;
    // }
}