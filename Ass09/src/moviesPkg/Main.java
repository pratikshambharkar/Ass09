package moviesPkg;

import databasePkg.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
	private static List<Movie> movies;

	public Main() {
		movies = new ArrayList<Movie>();
	}

	// 1.Read data from movieDetails File
	List<Movie> populateMovies(File file) throws FileNotFoundException, ParseException {
		Scanner sc = new Scanner(file);

		while (sc.hasNextLine()) {
			String data[] = sc.nextLine().split(",");
			String movieId = data[0];
			String movieName = data[1];
			String movieType = data[2];
			String language = data[3];
			String releaseDate = data[4];
			String casting = data[5];
			String rating = data[6];
			String totalBusinessDone = data[7];

			// You can see what data is getting read
			// System.out.println(movieId+" - "+movieName+" - "+movieType+" - "+language+" -
			// "+releaseDate+" - "+casting+" - "+rating+" - "+totalBusinessDone);

			Movie movie = new Movie(movieId, movieName, movieType, language, releaseDate, casting, rating,
					totalBusinessDone);
			movies.add(movie);
		}

		sc.close();
		return movies;
	}

	// 2.Store all movie details into database
	Boolean allAllMoviesInDb(List<Movie> movies) throws ClassNotFoundException, SQLException {

		String query = "insert into movies values(?,?,?,?,?,?,?,?)";
		Database dbConnection = new Database();
		try {
			for (Movie movie : movies) {
				PreparedStatement pstmt = Database.con.prepareStatement(query);
				pstmt.setInt(1, movie.getMovieId());
				pstmt.setString(2, movie.getMovieName());
				pstmt.setString(3, String.join("-", movie.getMovieType()));
				pstmt.setString(4, movie.getLanguage());
				pstmt.setDate(5, movie.getReleaseDate());
				pstmt.setString(6, String.join("-", movie.getCasting()));
				pstmt.setDouble(7, movie.getRating());
				pstmt.setDouble(8, movie.getTotalBusinessDone());

				pstmt.executeUpdate();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			dbConnection.close();
		}
	}

	// 3.Add new movie in the list
	void addMovie(Movie movie, List<Movie> movies) {
		movies.add(movie);
		System.out.println("\nMovie added successfully");
		String movieDetails = movies.get(movies.size() - 1).toString();// getting last added movie
		System.out.println(movieDetails);
	}

	// 4.Serialize movie data in the provided file in the given project directory
	void serializeMovies(List<Movie> movies, String fileName) throws IOException {
		FileOutputStream file = new FileOutputStream(fileName);
		try (ObjectOutputStream oos = new ObjectOutputStream(file)) {
			oos.writeObject(movies);
			System.out.println("\nSerialization Successful....\n");
		}
	}

	// 5.Deserialize movies from given files
	List<Movie> deserializeMovie(String filename) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(filename);
		try (ObjectInputStream ois = new ObjectInputStream(fis)) {
			@SuppressWarnings("unchecked")
			List<Movie> movies = (ArrayList<Movie>) ois.readObject();
			return movies;
		}
	}

	// getting movies according to user queries.
	List<Movie> getMoviesFromDb(String query) throws SQLException, ClassNotFoundException, ParseException {
		Database dbConnection = new Database();
		List<Movie> movies = new ArrayList<Movie>();

		ResultSet rs = Database.st.executeQuery(query);

		while (rs.next()) {
			String movieId, movieName, movieType, language, releaseDate, casting, rating, totalBusinessDone;
			movieId = rs.getString(1);
			movieName = rs.getString(2);
			movieType = rs.getString(3);
			language = rs.getString(4);
			releaseDate = rs.getDate(5).toString();// Unparseable date: "2019-02-14"
			String arr[] = releaseDate.split("-");
			releaseDate = arr[2] + "/" + arr[1] + "/" + arr[0];// "14/02/2019"
			casting = rs.getString(6);
			rating = rs.getString(7);
			totalBusinessDone = rs.getString(8);

			Movie movie = new Movie(movieId, movieName, movieType, language, releaseDate, casting, rating,
					totalBusinessDone);
			movies.add(movie);
		}
		dbConnection.close();
		return movies;
	}

	// 6.Find the Movies released in entered year
	List<Movie> getMoviesRealeasedInYear(int year) throws ClassNotFoundException, SQLException, ParseException {
		String query = "select * from movies where EXTRACT(YEAR FROM releaseDate) ='" + year + "'";
		List<Movie> movies = getMoviesFromDb(query);
		return movies;
	}

	// 7.Find the list of movies by actor
	List<Movie> getMoviesByActor(String actorNames) throws SQLException, ParseException, ClassNotFoundException {
		String query = "select * from movies where casting like '%" + actorNames + "%'";
		List<Movie> movies = getMoviesFromDb(query);
		return movies;
	}

	void update(String query) throws ClassNotFoundException, SQLException {
		Database dbConnection = new Database();
		Database.st.executeUpdate(query);
		dbConnection.close();
		System.out.println("1 row updated ");
	}

	// 8.Update Movie Rating
	void updateRatings(int movieId, double rating) throws ClassNotFoundException, SQLException {
		String query = "update movies set rating =" + rating + " where movie_id= " + movieId;
		update(query);
	}

	// 9.Update Business Done by Movie
	void updateBusiness(int movieId, double amount) throws ClassNotFoundException, SQLException {
		String query = "update movies set totalBusinessDone =" + amount + " where movie_id= " + movieId;
		update(query);
	}

	// 10.Find the set of movies as per the review comments done business more than
	// entered amount descending order of amount
	Set<Movie> businessDone(double amount) throws ClassNotFoundException, SQLException, ParseException {
		String query = "select * from movies " + "where totalBusinessDone > " + amount
				+ " order by totalBusinessDone DESC";
		List<Movie> lsmovies = getMoviesFromDb(query);

		Set<Movie> movies = new LinkedHashSet<>();// preserves insertion order
		movies.addAll(lsmovies);
		return movies;
	}

	static void displayMovies(Collection<Movie> movies) {
		for (Movie movie : movies) {
			System.out.println(movie.toString());
		}
	}

	public static void main(String[] args) throws Exception {
		String fileName = System.getProperty("user.dir") + "\\src\\serializeMovies.txt";
		;
		String pathname = "C:\\Users\\prati\\eclipse-workspace\\Ass09\\movieDetails.txt";

		File file = new File(pathname);
		Scanner sc = new Scanner(System.in);
		Main method = new Main();

		while (true) {
			System.out.println("1.Read data from movieDetails File");
			System.out.println("2.Store all movie details into database");
			System.out.println("3.Add new movie in the list");
			System.out.println("4.Serialize movie data in the file");
			System.out.println("5.Deserialize movies  from given files");
			System.out.println("6.Find the Movies realeased in entered year");
			System.out.println("7.Find the list of movies by actor");
			System.out.println("8.Update Movie Rating");
			System.out.println("9.Update Business Done by Movie");
			System.out.println("10.Find per business amount");
			System.out.println("\nEnter your choice : ");
			int choice = Integer.parseInt(sc.nextLine());

			switch (choice) {
			case 1:
				movies = method.populateMovies(file);
				displayMovies(movies);
				break;
			case 2:
				boolean db = method.allAllMoviesInDb(movies);
				System.out.println(db);
				break;
			case 3:
				Movie movie = ReadMovies.readMovie();
				method.addMovie(movie, movies);
				break;
			case 4:
				method.serializeMovies(movies, fileName);
				break;
			case 5:
				List<Movie> deserializedMovies = method.deserializeMovie(fileName);
				System.out.println("\n Deserialized Movies are:-\n");
				displayMovies(deserializedMovies);
				break;
			case 6:
				System.out.println("Enter year ");
				int year = Integer.parseInt(sc.nextLine());
				List<Movie> moviesByYear = method.getMoviesRealeasedInYear(year);
				displayMovies(moviesByYear);
				break;
			case 7:
				System.out.println("Enter Actor name ");
				String actorNames = sc.nextLine().trim();
				List<Movie> moviesByActor = method.getMoviesByActor(actorNames);
				displayMovies(moviesByActor);
				break;
			case 8:
				List<Movie> moviesFromDb = method.getMoviesFromDb("select * from movies");
				displayMovies(moviesFromDb);
				System.out.println("Enter movie id to update rating");
				int movieId = Integer.parseInt(sc.nextLine());
				System.out.println("Enter new rating ");
				double rating = Double.parseDouble(sc.nextLine());
				method.updateRatings(movieId, rating);
				break;
			case 9:
				List<Movie> moviesFromDb1 = method.getMoviesFromDb("select * from movies");
				displayMovies(moviesFromDb1);
				System.out.println("Enter movie id to update business");
				int movieId1 = Integer.parseInt(sc.nextLine());
				System.out.println("Enter new amount ");
				double amount = Double.parseDouble(sc.nextLine());
				method.updateBusiness(movieId1, amount);
				break;
			case 10:
				System.out.println("Enter amount ");
				double amount1 = Double.parseDouble(sc.nextLine());
				Set<Movie> movieByBuissness = method.businessDone(amount1);
				displayMovies(movieByBuissness);
			}

			System.out.println("Do you wanna continue....Y/N");
			char ch = sc.nextLine().toLowerCase().charAt(0);
			if (ch == 'n')
				break;
		}
		sc.close();
	}
	
}
