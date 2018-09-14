package de.sopro.model.send;

import java.util.List;

public class CompLists{
    private Iterable<SimpleComp> editable;
    private Iterable<SimpleComp> viewable;
    private Iterable<SimpleComp> publicComps;
    private Iterable<SimpleComp> owns;

    public CompLists(Iterable<SimpleComp> owns, Iterable<SimpleComp> editable, Iterable<SimpleComp> viewable, Iterable<SimpleComp> publicComps){
        this.editable = editable;
        this.viewable = viewable;
        this.publicComps = publicComps;
        this.owns = owns;
    }

    public Iterable<SimpleComp> getEditable(){
        return editable;
    }

    public Iterable<SimpleComp> getviewable(){
        return viewable;
    }

    public Iterable<SimpleComp> getpublicComps(){
        return publicComps;
    }

    public Iterable<SimpleComp>  getOwns(){
        return owns;
    }
}