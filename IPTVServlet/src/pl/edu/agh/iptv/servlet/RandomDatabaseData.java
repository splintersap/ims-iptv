package pl.edu.agh.iptv.servlet;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import pl.edu.agh.iptv.persistence.Category;
import pl.edu.agh.iptv.persistence.Movie;
import pl.edu.agh.iptv.persistence.Quality;
import pl.edu.agh.iptv.persistence.User;

public class RandomDatabaseData {
	@SuppressWarnings("unchecked")
	public static void fillDatabase(EntityManager em, UserTransaction utx) {
		
		// return when there are alredy movies in database
		try {
			utx.begin();
			
			Query query = em.createQuery("FROM Movie");
			List<Movie> movieList = query.getResultList();			
			utx.commit();
			if(movieList.size() > 1) {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		User coco = new User("sip:coco@ericsson.com");
		User maciek = new User("sip:maciek@ericsson.com");
		User alice = new User("sip:alice@ericsson.com");

		Movie forrest = new Movie("Forrest Gump", "C:/Movies/Shining.avi");
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
		forrest.addMoviePayment(500, Quality.LOW);
		forrest.addMoviePayment(600, Quality.MEDIUM);
		forrest.addMoviePayment(800, Quality.HIGH);
		forrest.addMovieComment("Very good movie", maciek);
		forrest.addMovieComment("I like it very much", alice);
		
		Movie shawshank = new Movie("The shawshank redemption",
				"C:/Movies/MVI_1165.avi");
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
		shawshank.addMoviePayment(300, Quality.LOW);
		shawshank.addMoviePayment(400, Quality.MEDIUM);
		shawshank.addMoviePayment(500, Quality.HIGH);
		shawshank.addMovieRating(coco, 4);
		shawshank.addMovieRating(maciek, 3);
		shawshank.addMovieRating(alice, 2);
		shawshank.addMovieComment("First comment", coco);
		shawshank.addMovieComment("Second comment", coco);
		shawshank.addMovieComment("Another comment", alice);
		shawshank.addMovieComment("Tratatatatatata", coco);
		
		Movie sstory = new Movie("The Straight Story", "C:/Movies/WyznaniaGejszy.2.LekPL.avi");
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
		sstory.addMoviePayment(400, Quality.LOW);
		sstory.addMoviePayment(500, Quality.MEDIUM);
		sstory.addMoviePayment(600, Quality.HIGH);
		sstory.addMovieRating(coco, 4);
		sstory.addMovieRating(maciek, 5);
		
		try {
			utx.begin();
			
			em.persist(coco);
			em.persist(alice);
			em.persist(maciek);
			
			em.persist(forrest);
			em.persist(sstory);
			em.persist(shawshank);
			
			coco.addOrderedMovie(sstory, Quality.LOW);
			coco.addOrderedMovie(forrest, Quality.MEDIUM);
			alice.addOrderedMovie(forrest, Quality.HIGH);
			
			utx.commit();
		} catch (Exception e) {

			
			e.printStackTrace();
		} 
		
		
	}
}
