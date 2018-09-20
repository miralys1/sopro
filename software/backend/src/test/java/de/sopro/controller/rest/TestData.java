package de.sopro.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.sopro.model.Composition;
import de.sopro.model.CompositionEdge;
import de.sopro.model.CompositionNode;
import de.sopro.model.Format;
import de.sopro.model.Service;
import de.sopro.model.Tag;
import de.sopro.model.User.User;
import de.sopro.repository.CompositionEdgeRepository;
import de.sopro.repository.CompositionNodeRepository;
import de.sopro.repository.CompositionRepository;
import de.sopro.repository.FormatRepository;
import de.sopro.repository.ServiceRepository;
import de.sopro.repository.TagRepository;
import de.sopro.repository.UserRepository;

@Component
public class TestData {

  private Tag tag1;
  private Tag tag2;
  private Tag tag3;
  private Tag tag4;
  private Tag tag5;
  private Format ifc;
  private Format bcf;
  private Format dwg;
  private Service tpModeller;
  private Service dModeller;
  private CompositionNode node1;
  private CompositionNode node2;
  private CompositionEdge edge;
  private Composition comp;
  private User lukas;
  private User jim;
  private User liSi;

  @Autowired
  private CompositionRepository compRepo;
  @Autowired
  private UserRepository userRepo;
  @Autowired
  private CompositionNodeRepository nodeRepo;
  @Autowired
  private CompositionEdgeRepository edgeRepo;
  @Autowired
  private ServiceRepository serviceRepo;
  @Autowired
  private TagRepository tagRepo;
  @Autowired
  private FormatRepository formatRepo;
  private Format gbxml;

  public void setUp() {

    lukas = new User("Lukas", "der Lokomotivfuehrer", "lukas@lummerland", "Herr", true);
    jim = new User("Jim", "Knopf", "jim@lummerland.de", "Herr", false);
    liSi = new User("Li", "Si", "lisi@ping.cn", "Prinzessin", false);
    lukas.setPassword("emma");
    jim.setPassword("molly");
    liSi.setPassword("drachenstadt");
    
    userRepo.save(lukas);
    userRepo.save(jim);
    userRepo.save(liSi);
    
    tag1 = tagRepo.save(new Tag("3D"));
    tag2 = tagRepo.save(new Tag("Modeller"));
    tag3 = tagRepo.save(new Tag("Visualisierung"));
    tag4 = tagRepo.save(new Tag("Modellierung"));
    tag5 = tagRepo.save(new Tag("IFC"));
    List<Tag> tags = new ArrayList<>();
    List<Tag> tags2 = new ArrayList<>();
    tags.add(tag1);
    tags.add(tag2);
    tags.add(tag3);
    tags.add(tag4);
    tags2.add(tag1);
    tags2.add(tag2);
    tags2.add(tag5);
    ifc = formatRepo.save(new Format("IFCTest", "2x0", "strict"));
    bcf = formatRepo.save(new Format("BCFTest", "1.0", "strict"));
    dwg = formatRepo.save(new Format("DWGTest", "5", "flexible"));
    gbxml = formatRepo.save(new Format("gbXMLTest", "2", "strict"));
    List<Format> formatIn1 = new ArrayList<>();
    List<Format> formatIn2 = new ArrayList<>();
    List<Format> formatOut1 = new ArrayList<>();
    List<Format> formatOut2 = new ArrayList<>();
    formatIn1.add(ifc);
    formatIn1.add(bcf);
    formatOut1.add(ifc);
    formatOut1.add(dwg);
    formatIn2.add(ifc);
    formatIn2.add(gbxml);
    formatOut2.add(ifc);
    formatOut2.add(dwg);
    tpModeller = serviceRepo
        .save(new Service("TP Modeller", "1.0", tags, "TP", 1534943388, "TP_Modeller_10.png", true, formatIn1, formatOut1));

    dModeller = serviceRepo
        .save(new Service("3D-Modeller", "3", tags2, "IGD", 1531573788, "IGD_Modeller.png", true, formatIn2, formatOut2));
    node1 = nodeRepo.save(new CompositionNode(5, 5, tpModeller));
    node2 = nodeRepo.save(new CompositionNode(10, 10, dModeller));
    edge = edgeRepo.save(new CompositionEdge(node1, node2));
    List<CompositionNode> nodes = new ArrayList<>();
    nodes.add(node1);
    nodes.add(node2);
    List<CompositionEdge> edges = new ArrayList<CompositionEdge>();
    edges.add(edge);
    comp = compRepo.save(new Composition(lukas, "Testkomposition", false, nodes, edges ));
    lukas.getOwnsComp().add(comp);
    lukas = userRepo.save(lukas);

  }
  
  public void shutDown() {
    
    compRepo.delete(comp);
    userRepo.delete(lukas);
    userRepo.delete(jim);
    userRepo.delete(liSi);
    serviceRepo.delete(tpModeller);
    serviceRepo.delete(dModeller);
    formatRepo.delete(ifc);
    formatRepo.delete(bcf);
    formatRepo.delete(dwg);
    formatRepo.delete(gbxml);
    tagRepo.delete(tag1);
    tagRepo.delete(tag2);
    tagRepo.delete(tag3);
    tagRepo.delete(tag4);
    tagRepo.delete(tag5);
    
  }

  public Tag getTag1() {
    return tag1;
  }

  public Tag getTag2() {
    return tag2;
  }

  public Tag getTag3() {
    return tag3;
  }

  public Tag getTag4() {
    return tag4;
  }

  public Tag getTag5() {
    return tag5;
  }

  public Format getIfc() {
    return ifc;
  }

  public Format getBcf() {
    return bcf;
  }

  public Format getDwg() {
    return dwg;
  }

  public Service getTpModeller() {
    return tpModeller;
  }

  public Service getdModeller() {
    return dModeller;
  }

  public CompositionNode getNode1() {
    return node1;
  }

  public CompositionNode getNode2() {
    return node2;
  }

  public CompositionEdge getEdge() {
    return edge;
  }

  public Composition getComp() {
    return comp;
  }

  public User getLukas() {
    return lukas;
  }

  public User getJim() {
    return jim;
  }

  public User getLiSi() {
    return liSi;
  }

  

  public Format getGbxml() {
    return gbxml;
  }
  
  

}
