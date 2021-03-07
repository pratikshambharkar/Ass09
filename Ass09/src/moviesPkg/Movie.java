/*
 * 1.Create Class Movies with following data members
	Class Movie (all members should be private)
	Int movieId, 
	String movieName, 
	List<String> movieType, 
	String language, 
	Date releaseDate, 
	List<String> casting
	Double rating
	Double totalBusinessDone
 */

package moviesPkg;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@SuppressWarnings("serial")
public class Movie implements Serializable{
	private int movieId;
	private String movieName;
	private List<String> movieType;
	private String language;
	private Date releaseDate;
	private List<String> casting;
	private double rating;
	private double totalBusinessDone;
	
	//constructor
	Movie(String movieId, String movieName, String movieType, String language, String releaseDate,
		  String casting, String rating, String totalBusinessDone) throws ParseException 
	{
		this.setMovieId(movieId);
		this.setMovieName(movieName);
		this.setMovieType(movieType);
		this.setLanguage(language);
		this.setReleaseDate(releaseDate);
		this.setCasting(casting);
		this.setRating(rating);
		this.setTotalBusinessDone(totalBusinessDone);
	}

	private void setMovieId(String movieId) {
		this.movieId = Integer.parseInt(movieId.trim());
	}

	private void setMovieName(String movieName) {
		this.movieName = movieName.toLowerCase();
	}

	private void setMovieType(String movieType) {
		List<String> data = new ArrayList<String>();
		
		if(movieType.contains("-")) { 	// if contain multiple movieTypes
			String arr[] = movieType.toLowerCase().split("-");
			for (String string : arr)
				data.add(string.trim());
			this.movieType = data;
		}
		else {
			data.add(movieType.toLowerCase().trim());
			this.movieType = data;
		}
	}

	private void setLanguage(String language) {
		this.language = language.toLowerCase();
	}


	private void setReleaseDate(String releaseDate) throws ParseException{
		java.util.Date utilDate = new SimpleDateFormat("dd/MM/yyyy").parse(releaseDate);
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		this.releaseDate = sqlDate;
	}


	private void setCasting(String casting) {
		List<String> data = new ArrayList<String>();
		
		if(casting.contains("-")) { 	// if contain multiple movieTypes
			String arr[] = casting.toLowerCase().trim().split("-");
			for (String string : arr)
				data.add(string.trim());
			this.casting = data;
		}
		else {
			data.add(casting.trim());
			this.casting = data;
		}
	}


	private void setRating(String rating) {
		this.rating = Double.parseDouble(rating);
	}


	private void setTotalBusinessDone(String totalBusinessDone) {
		this.totalBusinessDone = Double.parseDouble(totalBusinessDone);;
	}

	public int getMovieId() {
		return movieId;
	}

	public String getMovieName() {
		return movieName;
	}


	public List<String> getMovieType() {
		return movieType;
	}


	public String getLanguage() {
		return language;
	}


	public Date getReleaseDate() {
		return releaseDate;
	}


	public List<String> getCasting() {
		return casting;
	}


	public double getRating() {
		return rating;
	}


	public double getTotalBusinessDone() {
		return totalBusinessDone;
	}


	@Override
	public String toString() {
		return "Movies [movieId=" + movieId + ", movieName=" + movieName + ", movieType=" + movieType + ", language="
				+ language + ", releaseDate=" + releaseDate + ", casting=" + casting + ", rating=" + rating
				+ ", totalBusinessDone=" + totalBusinessDone + "]";
	}
	

}
