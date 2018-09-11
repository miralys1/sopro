package de.sopro.controller.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.sopro.model.Composition;
import de.sopro.model.CompositionEdge;
import de.sopro.model.CompositionNode;
import de.sopro.model.User;
import de.sopro.model.send.DetailComp;
import de.sopro.model.send.SimpleComp;
import de.sopro.repository.CompositionEdgeRepository;
import de.sopro.repository.CompositionNodeRepository;
import de.sopro.repository.CompositionRepository;
import de.sopro.repository.UserRepository;

@RestController
public class CompController{
    
    @Autowired
    private CompositionRepository compRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CompositionNodeRepository nodeRepo;
    @Autowired
    private CompositionEdgeRepository edgeRepo;

    // TODO actual way to identificate the user


    /**
     * Allows to get all the compositions that are either viewable or editable for the user.
     * @param userId the userId of the user requesting the list
     * @return a Object that contains two lists of compositions. One for editing and one for viewing
     */
    @RequestMapping(value="compositions", method=RequestMethod.GET)
    public ResponseEntity<CompLists> getCompositions(long userId){
        
        Optional<User> userOp = userRepo.findById(userId);
        // if user is logged in, editable, viewable and public composition are shown
        if(userOp.isPresent()){
            User user = userOp.get();

            return new ResponseEntity<CompLists>(new CompLists(convertListToSimple(user.getEditable(), userId),                 convertListToSimple(user.getViewable(), userId),
                convertListToSimple(compRepo.findByIsPublic(true), userId) ), HttpStatus.OK);
            
        }
        
        // if user is not logged in, only public compositions are viewable, none editable
        return new ResponseEntity<CompLists>(new CompLists(new ArrayList<>(), new ArrayList<>(), 
            convertListToSimple(compRepo.findByIsPublic(true), userId)), HttpStatus.OK);

    }


    @RequestMapping(value="/compositions/{id}", method=RequestMethod.GET)
    public ResponseEntity<DetailComp> getCompositionDetail(@PathVariable long id, long userID){
        Optional<Composition> opComp = compRepo.findById(id);
        if(opComp.isPresent()){
            Composition comp = opComp.get();
            if(isUserAuthorized(userID, comp)){
                return new ResponseEntity<>(comp.createDetailComp(userID), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value="/compositions", method=RequestMethod.POST)
    public ResponseEntity<Void> createComposition(@RequestBody DetailComp comp, long userID){
        Optional<User> opUser = userRepo.findById(userID);
        if(comp == null || !opUser.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        for(CompositionNode n : comp.getNodes()){
            nodeRepo.save(n);
        }

        Composition saveComp = comp.createComposition(opUser.get());

        for(CompositionEdge e : saveComp.getEdges()){
            edgeRepo.save(e);
        }

        compRepo.save(saveComp);
        

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /////////////////
    // Helper Code //
    /////////////////

    private boolean isUserAuthorized(long userID, Composition comp) {
        if(comp.isPublic()){
            return true;
        }
        Optional<User> opUser = userRepo.findById(userID);
        if(!opUser.isPresent()){
            return false;
        }
        User user = opUser.get();
        return comp.getOwner().equals(user) || comp.getViewers().contains(user) || comp.getEditors().contains(user);
    }

    private List<SimpleComp> convertListToSimple(List<Composition> comps, long userID) {
        List<SimpleComp> simpleComps = new ArrayList<>();
        for (Composition comp : comps) {
            simpleComps.add(comp.createSimpleComp(userID));
        }
        return simpleComps;
    }


    private class CompLists{
        private List<SimpleComp> editable;
        private List<SimpleComp> viewable;
        private Iterable<SimpleComp> publicComps;

        public CompLists(List<SimpleComp> editable,List<SimpleComp> viewable, Iterable<SimpleComp> publicComps){
            this.editable = editable;
            this.viewable = viewable;
            this.publicComps = publicComps;
        }
    }
}