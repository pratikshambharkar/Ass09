package moviesPkg;

import java.text.ParseException;
import java.util.Scanner;

public class ReadMovies {
	
	@SuppressWarnings("unused")
	static Movie readMovie() throws ParseException {
		Scanner sc1 = new Scanner(System.in);
		String movieId, movieName, movieType,	language, releaseDate, casting, rating, totalBusinessDone;
		System.out.println("\nEnter MovieId");
		movieId = sc1.nextLine();
		System.out.println("Enter movieName");
		movieName = sc1.nextLine();
		System.out.println("Enter movieType separated by '-' ");
		movieType = sc1.nextLine();
		System.out.println("Enter Language");
		language = sc1.nextLine();
		System.out.println("Enter releaseDate (dd/mm/yy)");
		releaseDate = sc1.nextLine();
		System.out.println("Enter Casting separated by '-' ");
		casting = sc1.nextLine();
		System.out.println("Enter Rating ");
		rating = sc1.nextLine();
		System.out.println("Enter totalBusinessDone");
		totalBusinessDone = sc1.nextLine();
		
		Movie movie = new Movie(movieId, movieName, movieType, language, releaseDate, casting, rating, totalBusinessDone);
		return movie;
	}
}
