package pl.edu.agh.iptv.dbmenager.main;

import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.table.AbstractTableModel;

import pl.edu.agh.iptv.persistence.Category;
import pl.edu.agh.iptv.persistence.Movie;

public class DBTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -5338903864691224720L;

	private List<Movie> movieList;

	EntityManager em;

	String[] columnNames = { "Id", "Title", "Category", "Director",
			"Description", "Movie Url", "UUID", "Movie path", "Media type",
			"Record by" };

	DBTableModel(List<Movie> movieList, EntityManager em) {
		this.movieList = movieList;
		this.em = em;
	}

	public List<Movie> getMovieList() {
		return movieList;
	}

	public void addMovie(Movie movie) {
		movieList.add(movie);
		fireTableDataChanged();
	}

	public void removeMovie(int index) {
		System.out.println("Removieng movie");
		Movie movie = movieList.get(index);
		movieList.remove(index);
		em.getTransaction().begin();
		em.persist(movie);
		em.remove(movie);

		em.getTransaction().commit();
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int number) {
		return columnNames[number];
	};

	@Override
	public int getColumnCount() {
		return 10;
	}

	@Override
	public int getRowCount() {
		return movieList.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		String value = null;
		Movie movie = movieList.get(arg0);
		try {
			switch (arg1) {
			case 0:
				value = movie.getId().toString();
				break;
			case 1:
				value = movie.getTitle();
				break;
			case 2:
				value = movie.getCategory().name();
				break;
			case 3:
				value = movie.getDirector();
				break;
			case 4:
				value = movie.getDescription();
				break;
			case 5:
				value = movie.getMovieUrl();
				break;
			case 6:
				value = movie.getUuid();
				break;
			case 7:
				value = movie.getMoviePath();
				break;
			case 8:
				value = movie.getMediaType().name();
				break;
			case 9:
				value = movie.getRecordingUser().getSip();
				break;

			}
		} catch (NullPointerException e) {
			value = null;
		}

		return value;
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		Movie movie = movieList.get(rowIndex);

		em.getTransaction().begin();
		em.persist(movie);

		if (columnIndex == 2) {
			Category category = (Category) value;
			movie.setCategory(category);
		}
		String strValue = null;
		if (value instanceof String) {
			strValue = (String) value;
		}

		switch (columnIndex) {
		case 1:
			movie.setTitle(strValue);
			break;
		case 2:
			break;
		case 3:
			movie.setDirector(strValue);
			break;
		case 4:
			movie.setDescription(strValue);
			break;
		case 5:
			movie.setMovieUrl(strValue);
			break;
		case 7:
			movie.setMoviePath(strValue);
			break;
		}

		em.getTransaction().commit();

	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		boolean value = true;
		if (columnIndex == 0) {
			value = false;
		}
		return value;
	}

}
