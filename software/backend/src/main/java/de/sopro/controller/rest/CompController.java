package de.sopro.controller.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.sopro.model.Composition;
import de.sopro.model.CompositionEdge;
import de.sopro.model.CompositionNode;
import de.sopro.model.User;
import de.sopro.model.send.CompLists;
import de.sopro.model.send.DetailComp;
import de.sopro.model.send.SendService;
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
    @RequestMapping(value="/compositions", method=RequestMethod.GET)
    public ResponseEntity<CompLists> getCompositions(@RequestHeader(value="id", defaultValue="0") long userId){
        

        Optional<User> userOp = userRepo.findById(userId);
        // if user is logged in, editable, viewable and public composition are shown
        if(userOp.isPresent()){
            User user = userOp.get();
            return new ResponseEntity<CompLists>(new CompLists(
                convertListToSimple(user.getEditable(), userId),
                convertListToSimple(user.getViewable(), userId),
                convertListToSimple(compRepo.findAll(), userId) ), HttpStatus.OK);
            
        }
        System.out.println("not found");
        // if user is not logged in, only public compositions are viewable, none editable
        return new ResponseEntity<CompLists>(new CompLists(new ArrayList<>(), new ArrayList<>(), 
            convertListToSimple(compRepo.findAll(), userId)), HttpStatus.OK);

    }


    @RequestMapping(value="/compositions/{id}", method=RequestMethod.GET)
    public ResponseEntity<DetailComp> getCompositionDetail(@PathVariable long id,@RequestHeader(value="id", defaultValue="0") long userID){
        Optional<Composition> opComp = compRepo.findById(id);
        if(opComp.isPresent()){
            Composition comp = opComp.get();
            if(isUserViewer(userID, comp)){
                return new ResponseEntity<>(comp.createDetailComp(userID), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // @RequestMapping(value="/compositions", method=RequestMethod.POST)
    // public ResponseEntity<Void> createComposition(@RequestBody DetailComp comp, @RequestHeader(value="id", defaultValue="0") long userID){
    //     Optional<User> opUser = userRepo.findById(userID);
    //     if(!opUser.isPresent()){
    //         return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    //     }
    //     System.out.println(comp.getId());
    //     if(comp == null || compRepo.findById(comp.getId()).isPresent()){
    //         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //     }


    //     Composition saveComp = comp.createComposition(opUser.get());

    //     compRepo.save(saveComp);
        

    //     return new ResponseEntity<>(HttpStatus.CREATED);
    // }

    // @RequestMapping(value="/compositions/{id}",method=RequestMethod.PUT)
    // public ResponseEntity<Void> editComposition(@RequestBody DetailComp dComp, @RequestHeader(value="id", defaultValue="0") long userID ){
    //     Optional<Composition> opComp = compRepo.findById(dComp.getId());
    //     if(!opComp.isPresent()){
    //         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //     }

    //     Optional<User> opUser = userRepo.findById(userID);
    //     Composition composition = opComp.get();
	// 	if(!opUser.isPresent() || !isViewerEditor(opUser.get(), composition)){
    //         return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    //     }

    //     for (CompositionNode node : dComp.getNodes()) {
    //         nodeRepo.save(node);
    //     }

    //     Composition comp = dComp.createComposition(composition.getOwner());
    //     for(CompositionEdge e : comp.getEdges()){
    //         edgeRepo.save(e);
    //     }
    //     compRepo.save(comp);

        
    
    //     return new ResponseEntity<>(HttpStatus.OK);
    // }

    /////////////////
    // Helper Code //
    /////////////////

    private boolean isViewerEditor(User user, Composition composition) {
        return user.equals(composition.getOwner()) || composition.getEditors().contains(user);
    }

    private boolean isUserViewer(long userID, Composition comp) {
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

    private List<SimpleComp> convertListToSimple(Iterable<Composition> comps, long userID) {
        List<SimpleComp> simpleComps = new ArrayList<>();
        for (Composition comp : comps) {
            simpleComps.add(comp.createSimpleComp(userID));
        }
        return simpleComps;
    }
}