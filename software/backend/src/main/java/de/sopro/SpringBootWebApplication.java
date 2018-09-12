package de.sopro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import de.sopro.model.Composition;
import de.sopro.model.CompositionEdge;
import de.sopro.model.CompositionNode;
import de.sopro.model.Format;
import de.sopro.model.Service;
import de.sopro.model.Tag;
import de.sopro.model.User;
import de.sopro.model.send.Edge;
import de.sopro.repository.CompositionEdgeRepository;
import de.sopro.repository.CompositionNodeRepository;
import de.sopro.repository.CompositionRepository;
import de.sopro.repository.FormatRepository;
import de.sopro.repository.ServiceRepository;
import de.sopro.repository.TagRepository;
import de.sopro.repository.UserRepository;

@SpringBootApplication
public class SpringBootWebApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	UserRepository userRepo;

	@Autowired
	ServiceRepository serviceRepo;

	@Autowired
	TagRepository tagRepo;

	@Autowired
	FormatRepository formatRepo;

	@Autowired
	CompositionNodeRepository compNodeRepo;

	@Autowired
	CompositionEdgeRepository compEdgeRepo;

	@Autowired
	CompositionRepository compRepo;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringBootWebApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		// Add code that should be run when the application starts
		// (e. g., populate empty repositories with example data)

		if (userRepo.count() == 0) {

			// Add person with single hosted events and no visited events
			User dark = new User("drake", "dunkel", "d@d.de", "Dr.", true);
			User dum = new User("dummi", "dumm", "du@d.com", "Prof.", false);

			userRepo.save(dark);
			userRepo.save(dum);

			String[] ts = { "3D", "Modeller", "Visualisierung", "Modellierung" };
			List<Tag> tags = createTagList(ts);

			String[][] fis = { { "IFC", "2x0", "strict" }, { "BCF", "1.0", "strict" } };
			List<Format> formatIn = createFormatList(fis);

			String[][] fos = { { "IFC", "2x0", "strict" }, { "DWG", "5", "strict" } };
			List<Format> formatOut = createFormatList(fos);

			Service s1 = new Service("TP Modeller", "1.0", tags, "TP", 153443388, "TP_Modeller_10.png", formatIn,
					formatOut);

			serviceRepo.save(s1);

			String[] ts2 = { "3D", "Modeller", "IFC" };
			tags = createTagList(ts2);

			String[][] fis2 = { { "IFC", "2x0", "strict" }, { "gbXML", "2", "strict" } };
			formatIn = createFormatList(fis2);

			String[][] fos2 = { { "IFC", "2x0", "strict" }, { "DWG", "5", "flexible" } };
			formatOut = createFormatList(fos2);

			Service s2 = new Service("3D-Modeller", "3", tags, "IGD", 1531573788, "IGD_Modeller.png", formatIn,
					formatOut);

			serviceRepo.save(s2);

			CompositionNode n1 = new CompositionNode(5, 5, s1);
			CompositionNode n2 = new CompositionNode(50, 50, s2);
			compNodeRepo.save(n1);
			compNodeRepo.save(n2);

			CompositionEdge e = new CompositionEdge(n1, n2);
			compEdgeRepo.save(e);

			List<CompositionNode> nodes = new ArrayList<>();
			nodes.add(n1);
			nodes.add(n2);

			List<CompositionEdge> edges = new ArrayList<>();
			edges.add(e);

			Composition c = new Composition(dark, "MyComp", true, nodes, edges);
			compRepo.save(c);

			List<Composition> owns = dark.getOwnsComp();
			owns.add(c);
			dark.setOwnsComp(owns);
		}
	}

	private List<Format> createFormatList(String[][] fis) {
		List<Format> formats = new ArrayList<>();
		for (String[] s : fis) {
			Format f = new Format(s[0], s[1], s[2]);
			Format existing = formatRepo.findOneByTypeAndVersion(s[0], s[1]);
			if (existing == null) {
				formatRepo.save(f);
			} else {
				f.setId(existing.getId());
			}
			formats.add(f);
		}
		return formats;
	}

	private List<Tag> createTagList(String[] ts) {
		List<Tag> tags = new ArrayList<>();
		for (String s : ts) {
			Tag t = new Tag(s);
			tagRepo.save(t);
			tags.add(t);
		}
		return tags;
	}
}
