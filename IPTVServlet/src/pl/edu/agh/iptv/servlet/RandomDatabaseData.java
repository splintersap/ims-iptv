package pl.edu.agh.iptv.servlet;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import pl.edu.agh.iptv.persistence.Category;
import pl.edu.agh.iptv.persistence.MediaType;
import pl.edu.agh.iptv.persistence.Movie;
import pl.edu.agh.iptv.persistence.Quality;
import pl.edu.agh.iptv.persistence.User;
import pl.edu.agh.iptv.telnet.AbstractTelnetWorker;
import pl.edu.agh.iptv.telnet.MulticastTelnetClient;
import pl.edu.agh.iptv.telnet.RemovingTelnetClient;
import pl.edu.agh.iptv.telnet.VodTelnetClient;

public class RandomDatabaseData {

	private static int RTSP_PORT = 5554;

	@SuppressWarnings("unchecked")
	public static void fillDatabase(EntityManager em, UserTransaction utx) {

		// return when there are already movies in database
		try {
			utx.begin();

			Query query = em.createQuery("FROM Movie");
			List<Movie> movieList = query.getResultList();
			utx.commit();
			if (movieList.size() > 1) {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		delAllMoviesFromTelnet();
		
		User coco = new User("sip:coco@ericsson.com");
		User maciek = new User("sip:maciek@ericsson.com");
		User alice = new User("sip:alice@ericsson.com");

		Movie up = new Movie("Up", "C:/Movies/Up.mov");
		up
				.setDescription("Carl Fredricksen, a shy and quiet young boy, meets" +
						" an energetic and outgoing bucktoothed barefooted tomboy nam" +
						"ed Ellie, discovering they share the same interest in explor" +
						"ation as their hero, the then 24-year-old famed explorer Char" +
						"les F. Muntz. Ellie expresses her desire to move her clubhou" +
						"se to Paradise Falls in South America, a promise she makes Carl" +
						" keep. Carl and Ellie wed and grow old together in the old ho" +
						"use where they first met while making a living as a toy balloon" +
						" vendor and a zookeeper respectively. Unable to have children" +
						", they also try to save up for the trip to Paradise Falls but " +
						"other financial obligations arise. Just as they seem to finally" +
						" be able to take their trip, Ellie dies of old age, leaving Carl" +
						" living alone in their home as a sour recluse with nothing to li" +
						"ve for and missing his wife terribly. As the years pass, the cit" +
						"y grows around Carl's house with construction as Carl refuses t" +
						"o move. After a tussle with a construction worker over Carl's bro" +
						"ken mailbox, the court orders Carl to move into Shady Oaks Retire" +
						"ment Home. Carl comes up with a scheme to keep his promise to Ell" +
						"ie, and uses his old professional supplies to create a makeshift " +
						"airship using tens of thousands of helium balloons that lift his hous" +
						"e off its foundations. Russell, a Wilderness Explorer trying to earn hi" +
						"s final merit badge for \"Assisting the Elderly\", has stowed away " +
						"on the porch after being sent on a snipe hunt by Carl the day before.");

		up.setCategory(Category.Animation);
		up.setDirector("Pete Docter");
		up.addMoviePayment(500, Quality.LOW);
		up.addMoviePayment(600, Quality.MEDIUM);
		up.addMoviePayment(800, Quality.HIGH);
		up.addMovieComment("Very good movie", maciek);
		up.addMovieComment("I like it very much", alice);
		up.setMediaType(MediaType.VOD);
		addVodMovieToTelnet(up);

		Movie terminatorSalvation = new Movie("Terminator salvation",
				"C:/Movies/Terminator_salvation.avi");
		terminatorSalvation
				.setDescription("It's the early stages of the war between man and machines." +
						" And the savior John Conner is doing everything he can to make sure " +
						"man survives. He thinks he's found a way to ensure that, and he al" +
						"so learns that the machines have targeted the man who's suppose to f" +
						"ather him, Kyle Reese, so he tries to find him. One of Conner's soldi" +
						"ers meets a man named Marcus Wright who has no knowledge of Judgment" +
						" Day. When they reach the base they discover Wright is part machine a" +
						"nd suspect that he was sent by Skynet but Wright claims that's not true");
		terminatorSalvation.setCategory(Category.Action);
		terminatorSalvation.setDirector("Joseph McGinty Nichol ");
		terminatorSalvation.addMoviePayment(300, Quality.LOW);
		terminatorSalvation.addMoviePayment(400, Quality.MEDIUM);
		terminatorSalvation.addMoviePayment(500, Quality.HIGH);
		terminatorSalvation.addMovieRating(coco, 4);
		terminatorSalvation.addMovieRating(maciek, 3);
		terminatorSalvation.addMovieRating(alice, 2);
		terminatorSalvation.addMovieComment("First comment", coco);
		terminatorSalvation.addMovieComment("Second comment", coco);
		terminatorSalvation.addMovieComment("Another comment", alice);
		terminatorSalvation.addMovieComment("Tratatatatatata", coco);
		terminatorSalvation.setMediaType(MediaType.VOD);
		addVodMovieToTelnet(terminatorSalvation);

		Movie movie2012 = new Movie("2012",
				"C:/Movies/2012.mov");
		movie2012
				.setDescription("In 2009, American geologist Adrian Helmsley (Chiwetel Ejiofor) travels" +
						" to India to meet his friend Satnam, who has discovered that neutrinos from a mas" +
						"sive solar flare have penetrated the Earth and are causing the temperature of " +
						"its core to increase rapidly. Adrian returns to Washington D.C. to inform White" +
						" House Chief of Staff Carl Anheuser (Oliver Platt) and US President Thomas Wils" +
						"on (Danny Glover) that this will instigate a chain of events that will bring ab" +
						"out the end of the world. At the 36th G8 summit in 2010, other heads of state an" +
						"d heads of government are made aware of the situation. They collaborate to begi" +
						"n a secret project intended to ensure the continuity of human life, strategical" +
						"ly choosing 400,000 people for admission on a series of gigantic arks to be cons" +
						"tructed in the Himalayas. To help fund the venture, additional individuals are al" +
						"lowed to purchase tickets for one billion euros apiece.");

		movie2012.setCategory(Category.Drama);
		movie2012.setDirector("Roland Emmerich");
		movie2012.addMoviePayment(400, Quality.LOW);
		movie2012.addMoviePayment(500, Quality.MEDIUM);
		movie2012.addMoviePayment(600, Quality.HIGH);
		movie2012.addMovieRating(coco, 4);
		movie2012.addMovieRating(maciek, 5);
		movie2012.setMediaType(MediaType.VOD);
		addVodMovieToTelnet(movie2012);

		Movie basterds = new Movie("Inglourious Basterds",
				"C:/Movies/Inglourious_basterds.mov");
		basterds.setDirector("Quentin Tarantino");

		basterds
				.setDescription("In Nazi occupied France, young Jewish refugee Shosanna" +
						" Dreyfus witnesses the slaughter of her family by Colonel Hans L" +
						"anda. Narrowly escaping with her life, she plots her revenge sev" +
						"eral years later when German war hero Fredrick Zoller takes a rapi" +
						"d interest in her and arranges an illustrious movie premiere at the" +
						" theater she now runs. With the promise of every major Nazi office" +
						"r in attendance, the event catches the attention of the \"Basterds\"" +
						", a group of Jewish-American guerilla soldiers led by the ruthless Lt" +
						". Aldo Raine. As the relentless executioners advance and the conspirin" +
						"g young girl's plans are set in motion, their paths will cross for a fa" +
						"teful evening that will shake the very annals of history.");

		basterds.addMoviePayment(400, Quality.LOW);
		basterds.addMoviePayment(500, Quality.MEDIUM);
		basterds.addMoviePayment(600, Quality.HIGH);
		basterds.addMovieRating(maciek, 5);
		basterds.setCategory(Category.Drama);
		basterds.setMediaType(MediaType.VOD);
		
		addVodMovieToTelnet(basterds);
		
		Movie avatar = new Movie("Avatar",
				"C:/Movies/Avatar.mov");
		avatar.setDescription("Avatar is the story of an ex-Marine who finds himself " +
				"thrust into hostilities on an alien planet filled with exotic life form" +
				"s. As an Avatar, a human mind in an alien body, he finds himself torn be" +
				"tween two worlds, in a desperate fight for his own survival and that o" +
				"f the indigenous people. More than ten years in the making, Avatar marks " +
				"Cameron's return to feature directing since helming 1997's Titanic, the " +
				"highest grossing film of all time and winner of eleven Oscarsé including " +
				"Best Picture. WETA Digital, renowned for its work in The Lord of the Rings " +
				"Trilogy and King Kong, will incorporate new intuitive CGI technologies to tr" +
				"ansform the environments and characters into photorealistic 3D imagery that wi" +
				"ll transport the audience into the alien world rich with imaginative vistas, crea" +
				"tures and characters.");
		avatar.setDirector("James Cameron");
		avatar.addMoviePayment(400, Quality.LOW);
		avatar.addMoviePayment(500, Quality.MEDIUM);
		avatar.addMoviePayment(600, Quality.HIGH);
		avatar.addMovieRating(coco, 5);
		avatar.addMovieRating(alice, 5);
		avatar.addMovieComment("Coming soon", coco);
		avatar.setCategory(Category.Sci_Fi);
		avatar.setMediaType(MediaType.VOD);

		addVodMovieToTelnet(avatar);
		
		Movie aliceInWonderland =  new Movie("Alice in Wonderland", "C:/Movies/Alice_in_wonderland.mov");
		
		aliceInWonderland.setDescription("Tim Burton (Edward Scissorhands, Beetlejuice)" +
				" attempts to work his gothic magic over one of the best loved stories of" +
				" all time... Lewis Carroll's 'Alice In Wonderland' and 'Through the Lookin" +
				"g Glass and What Alice Found There', which first told the story of a you" +
				"ng girl, who after following a rabbit down a hole, is transported to a s" +
				"trange world. Whilst the disney version of the tale relied on sweetness and" +
				" light to delight the audience, Burton fans can be sure to look out for t" +
				"he same cartoon traits as earlier masterpiece 'The Nightmare Before Christmas'.");
		
		aliceInWonderland.setDirector("Tim Burton");
		aliceInWonderland.setCategory(Category.Fantasy);
		aliceInWonderland.addMoviePayment(400, Quality.LOW);
		aliceInWonderland.addMoviePayment(500, Quality.MEDIUM);
		aliceInWonderland.addMoviePayment(600, Quality.HIGH);
		aliceInWonderland.setMediaType(MediaType.VOD);

		addVodMovieToTelnet(aliceInWonderland);
		
		
		Movie xman =  new Movie("X-Men Origins: Wolverine", "C:/Movies/x-man.mov");
		xman.setDescription("Two mutant brothers, Logan and Victor, born 200 years ago, suffer" +
				" childhood trauma and have only each other to depend on. Basically, they're f" +
				"ighters and killers, living from war to war through U.S. history. In modern t" +
				"imes, a U.S. colonel, Stryker, recruits them and other mutants as commandos. " +
				"Logan quits and becomes a logger, falling in love with a local teacher. When " +
				"Logan refuses to rejoin Stryker's crew, the colonel sends the murderous Victo" +
				"r. Logan now wants revenge.");
		xman.setDirector("Gavin Hood");
		xman.setCategory(Category.Action);
		xman.addMoviePayment(400, Quality.LOW);
		xman.addMoviePayment(600, Quality.MEDIUM);
		xman.addMoviePayment(700, Quality.HIGH);
		xman.setMediaType(MediaType.VOD);
		addVodMovieToTelnet(xman);
		
		Movie aljazera = new Movie("Aljazera", "mms://stream.onet.pl/media.wsx?/live/aljazeera");
		aljazera.setCategory(Category.Documentary);
		aljazera.setDirector("director");
		aljazera.setMediaType(MediaType.BROADCAST);
		aljazera.addMoviePayment(400, Quality.LOW);
		aljazera.addMoviePayment(600, Quality.MEDIUM);
		aljazera.addMoviePayment(700, Quality.HIGH);
		addBroadcastToTelnet(aljazera);
		
		try {
			utx.begin();

			em.persist(coco);
			em.persist(alice);
			em.persist(maciek);

			em.persist(up);
			em.persist(movie2012);
			em.persist(terminatorSalvation);
			em.persist(basterds);
			em.persist(avatar);
			em.persist(aliceInWonderland);
			em.persist(xman);
			em.persist(aljazera);

			coco.addOrderedMovie(movie2012, Quality.LOW);
			coco.addOrderedMovie(up, Quality.MEDIUM);
			coco.addOrderedMovie(xman, Quality.MEDIUM);
			alice.addOrderedMovie(up, Quality.HIGH);
			alice.addOrderedMovie(aliceInWonderland, Quality.HIGH);

			utx.commit();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private static void addBroadcastToTelnet(Movie aljazera) {
		AbstractTelnetWorker telnet = null;

		telnet = new MulticastTelnetClient(aljazera.getMoviePath(), "239.255.12.42");
		aljazera.setMovieUrl("rtp://@239.255.12.42:5004");

		System.out.println("Starting telnet");
		telnet.start();
		try {
			telnet.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void delAllMoviesFromTelnet() {
		AbstractTelnetWorker telnet = null;

		telnet = new RemovingTelnetClient("all");
		telnet.start();
		try {
			telnet.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	private static void addVodMovieToTelnet(Movie movie) {
		AbstractTelnetWorker telnet = null;

		telnet = new VodTelnetClient(movie.getMoviePath());
		String address = getIpAddress();
		movie.setMovieUrl("rtsp://" + address + ":" + RTSP_PORT + "/"
				+ telnet.getUuid().toString());

		movie.setUuid(telnet.getUuid().toString());
		System.out.println("Starting telnet");
		telnet.start();
		try {
			telnet.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String getIpAddress() {
		String address = null;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			address = addr.getHostAddress();
		} catch (UnknownHostException e) {
		}
		return address;
	}

}
