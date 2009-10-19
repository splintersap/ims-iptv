package pl.edu.agh.iptv.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import pl.edu.agh.iptv.servlet.facade.MessageCreator;
import pl.edu.agh.iptv.servlet.persistence.Category;
import pl.edu.agh.iptv.servlet.persistence.Movie;
import pl.edu.agh.iptv.servlet.persistence.MovieComment;
import pl.edu.agh.iptv.servlet.persistence.MovieRating;
import pl.edu.agh.iptv.servlet.persistence.User;

@javax.servlet.sip.annotation.SipServlet
public class VideoServlet extends SipServlet {

	@PersistenceContext(unitName = "PU")
	protected EntityManager em;

	@Resource
	private UserTransaction utx;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1055741883702121144L;

	private final String ACK_RECEIVED = "ackReceived";
	/**
	 * Session attribute in which we store the number to guess
	 */

	private final String NUMBER_TO_GUESS = "number";

	/**
	 * Session is accepted - select the number to guess
	 */

	static String moviesDirectoryLocation = "C:\\Movies";

	static final String vlcLocation = "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe";

	String sip;

	int a = 0;

	/**
	 * @inheritDoc
	 */
	protected void doMessage(SipServletRequest sipServletRequest)
			throws ServletException, IOException {
		a++;
		log("inside doMessage method 123, a = " + a);

		// start streaming

		sipServletRequest.createResponse(200).send();

		String content = new String(sipServletRequest.getRawContent());
		String moviePath = getMoviePath(content);
		log("Movie path = " + moviePath);
		// String sip = sipServletRequest.getRequestURI().toString();
		runStreamer(sipServletRequest.getInitialRemoteAddr(), moviePath);
	}

	private void fillDatabase() {
		User coco = new User("coco@ericsson.com");
		User maciek = new User("maciek@ericsson.com");
		User alice = new User("alice@ericsson.com");

		Movie forrest = new Movie("Forrest Gump", "C:/Movies/forrest.avi");
		forrest
				.setDescription("Forrest Gump is a 1994 American comedy-drama film"
						+ " based on the 1986 novel of the same name by Winston Groo"
						+ "m. The film, directed by Robert Zemeckis, stars Tom Hanks"
						+ ", Robin Wright Penn, and Gary Sinise. The film tells the "
						+ "story of Forrest Gump's journey through life meeting hist"
						+ "orical figures, influencing popular culture, and experien"
						+ "cing firsthand historic events of the late 20th century w"
						+ "hile being largely unaware of their significance, owing t"
						+ "o his somewhat below average intelligence. The film diffe"
						+ "rs substantially from Winston Groom's novel on which it w"
						+ "as based. Filming took place in late 1993, mainly in Geor"
						+ "gia and North and South Carolina. Extensive visual effect"
						+ "s were used to incorporate the protagonist into archived "
						+ "footage as well as for developing other scenes. An extens"
						+ "ive soundtrack was featured in the film, and its commerci"
						+ "al release made it one of the top selling albums of all t"
						+ "ime. Released in the United States on July 6, 1994, Forre"
						+ "st Gump was well received by critics and became a commerc"
						+ "ial success as the top grossing film in North America rel"
						+ "eased that year. The film ended up earning over $677 mill"
						+ "ion worldwide during its theatrical run. The film garnere"
						+ "d multiple awards and nominations, including Academy Awar"
						+ "ds, Golden Globe Awards, People's Choice Awards, and Youn"
						+ "g Artist Awards, among others. Since the film's release, "
						+ "varying interpretations have been made of the film's prot"
						+ "agonist and its political symbolism. In 1996, a themed re"
						+ "staurant opened based on the film, and has since expanded"
						+ " to multiple locations worldwide. Although a screenplay w"
						+ "as developed on Groom's second novel, as of 2009, no sequ"
						+ "el has been officially greenlit.");

		forrest.setCategory(Category.Drama);
		forrest.setDirector("Robert Zemeckis");

		Movie shawshank = new Movie("The shawshank redemption",
				"C:/Movies/shawshank.avi");
		shawshank
				.setDescription("The Shawshank Redemption is a 1994 American dram"
						+ "a film written and directed by Frank Darabont, loosely based on t"
						+ "he Stephen King novella Rita Hayworth and Shawshank Redemption. T"
						+ "he film stars Tim Robbins as Andrew \"Andy\" Dufresne and Morgan "
						+ "Freeman as Ellis Boyd \"Red\" Redding.The film portrays Andy spen"
						+ "ding nearly two decades in Shawshank State Prison, a fictional pe"
						+ "nitentiary in Maine and his friendship with Red, a fellow inmate."
						+ " Despite a lukewarm box office reception that was barely enough t"
						+ "o cover its budget, The Shawshank Redemption received favorable r"
						+ "eviews from critics and has since enjoyed a remarkable life on ca"
						+ "ble television, VHS, DVD and Blu-ray.");
		shawshank.setCategory(Category.Drama);
		shawshank.setDirector("Frank Darabont");

		Movie sstory = new Movie("The Straight Story", "C:/Movies/sstory.avi");
		sstory
				.setDescription("The Straight Story is a 1999 film directed by David"
						+ " Lynch. It is based on the true story of Alvin Straight's journey"
						+ " across Iowa and Wisconsin on a lawnmower. The film was edi"
						+ "ted and produced by Mary Sweeney, Lynch's longtime partner "
						+ "and co-worker. She co-wrote the script with John E. Roach. "
						+ "The title of the film is a double entendre: it refers to Al"
						+ "vin's surname, but also refers to the story\'s seemingly st"
						+ "raightforward nature, as compared to the complex, interweav"
						+ "ing, mysterious plots of Lynch\'s other films.");
		
		sstory.setCategory(Category.Drama);
		sstory.setDirector("David Lynch");
		
		MovieRating movieRating1 = new MovieRating(coco, sstory, 4);
		MovieRating movieRating2 = new MovieRating(coco, shawshank, 5);
		MovieRating movieRating3 = new MovieRating(maciek, shawshank, 3);
		MovieRating movieRating4 = new MovieRating(alice, shawshank, 5);
		
		MovieComment comment = new MovieComment("Very good movie", maciek, forrest);
		MovieComment comment2 = new MovieComment("I like it very much", alice, forrest);
		
		try {
			utx.begin();
			em.persist(coco);
			em.persist(alice);
			em.persist(maciek);
			
			em.persist(forrest);
			em.persist(sstory);
			em.persist(shawshank);
			
			em.persist(movieRating1);
			em.persist(movieRating2);
			em.persist(movieRating3);
			em.persist(movieRating4);
			
			em.persist(comment);
			em.persist(comment2);

			utx.commit();
		} catch (Exception e) {

			
			e.printStackTrace();
		} 
		
		
	}

	@SuppressWarnings("unchecked")
	private String getMoviePath(String content) {

		Query query = em.createQuery("FROM Movie WHERE title = '" + content
				+ "'");
		List<Movie> movieList = query.getResultList();
		Movie movie = movieList.get(0);
		return movie.getMoviePath();
	}

	/**
	 * @inheritDoc
	 */
	protected void doAck(SipServletRequest sipServletRequest)
			throws ServletException, IOException {

		a++;
		log("inside doAck method, a = " + a);
		SipSession session = sipServletRequest.getSession(true);
		// Ensure we do not process the ACK twice for the dialog
		Object ackAlreadyProcessed = session.getAttribute(ACK_RECEIVED);
		log(sipServletRequest.getInitialRemoteAddr());
		if (ackAlreadyProcessed == null) {
			log("wysylam wiadomosc");
			session.setAttribute(ACK_RECEIVED, true);
			// yields a value between 0 (inclusive) and 11 (exclusive) so [0,
			// 10]
			session.setAttribute(NUMBER_TO_GUESS, 8);

			SipServletRequest message = session.createRequest("MESSAGE");

			try {
				// message.setContent(getMovies().getBytes(), "movies/list");
				message.setContent(getMovies(), "text/movie-list");

			} catch (Exception e) {
				log(e.getMessage());
			}
			message.send();
			log("wiadomosc wyslana");
		}
	}

	/**
	 * @inheritDoc
	 */
	protected void doBye(SipServletRequest sipServletRequest)
			throws ServletException, IOException {
		log("inside doBye method");
		sipServletRequest.getSession().invalidate();
	}

	/**
	 * @inheritDoc
	 */
	protected void doInvite(SipServletRequest sipServletRequest)
			throws ServletException, IOException {
		a++;
		log("inside doInvite method, a = " + a);
		
		log("Zaczynamy");
		fillDatabase();
		log("Konczymy");

		try {

			log("SIP = " + sipServletRequest.getFrom().getURI());
			log("XML: " + MessageCreator.getMessage(em, utx, sipServletRequest.getFrom().getURI().toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		log("Po XMLu");
		
		SipServletResponse response = sipServletRequest.createResponse(200);
		response.send();
	}

	@SuppressWarnings("unchecked")
	private String getMovies() {

		StringBuilder moviesStringBuilder = new StringBuilder();

		Query query = em.createQuery("FROM Movie");
		List<Movie> movieList = query.getResultList();
		Iterator<Movie> movieIterator = movieList.iterator();
		while (movieIterator.hasNext()) {
			Movie movie = movieIterator.next();
			moviesStringBuilder.append(movie.getTitle() + "\n");
		}

		log("Filmy: " + moviesStringBuilder.toString());
		
		
		return moviesStringBuilder.toString();
	}

	private void runStreamer(String ip, String movieLocation)
			throws IOException {
		// File movieLocation = new File(moviesDirectoryLocation + "\\" +
		// movie);
		String vlcCommandString = "\"" + vlcLocation + "\" \"" + movieLocation
				+ "\" --sout=udp/ts://" + ip + ":1234";

		log(vlcCommandString);

		Runtime runtime = Runtime.getRuntime();

		runtime.exec(vlcCommandString);
	}

}
