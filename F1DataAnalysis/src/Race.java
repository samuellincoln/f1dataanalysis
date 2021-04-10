import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

public class Race {
	private String country;
	private String circuit;
	private String date;
	private String team;
	private String winner;
	private String laps;
	private String time;
	//private RaceDetails racedetails;
	private boolean generateracedetails;
	private String timeof2nd;
	public Race (String country, String circuit, String date, String winner, String team, String laps, String time, String linkcountry, Document doc) {
		this.setCountry(country);
		this.setCircuit(circuit);
		this.setDate(date);
		this.setWinner(winner);
		this.setTeam (team);
		this.setTime (time);
		this.setLaps(laps);
		this.generateracedetails = true;
		String link = linkOfRaceDetails (this.getYear(), linkcountry);
		//this.racedetails = new RaceDetails (link, this.getYear());
		//this.timeof2nd = country.equals ("0")? "" : this.racedetails.getTime(2);
		System.out.print ("");
	}
	public Race (boolean min) {
		this ("0", "0", min? "10.59.59.999" : "0", "0", "0", "0", "0", "0", null);
	}
	private String linkOfRaceDetails (String year, String linkfull) {
		//https://www.formula1.com/en/results.html/1950/races/94/great-britain/race-result.html
		//String year = this.getYear();
		//String countrylink = this.countryLinkInFull (true, href, year);
		return
			"https://www.formula1.com/en/results.html/"
			+ year
			+ "/races/"
			+ linkfull + "/race-result.html"
		;
	}
	private String getYear () {
		return this.getYear(this.date);
	}
	public String getYear (String date) {
		Object [] str = date.split (" ");
		if (str.length < 2) {
			System.out.print ("");
		}
		String [] splits = date.split (" ");
		return splits [splits.length - 1];
	}
	public String toString () {
		return "Race (" + this.country + ", " + this.date + ", " + this.winner + ", " + this.team + ", " + this.laps + ", " + this.time + ", TIME OF 2ND: " + this.timeof2nd + ")";
	}
	/*public String toStringInFull () {
		return 
			"RACE (" + this.country + ", " + this.date + this.laps + ", " + this.time + "):\n\n"
		 +	"[\n"
		 +	((this.generateracedetails)? this.racedetails.toString() : "")
		 +	"]\n"
		;
	}*/
	private String toStringCompact () {
		StringTokenizer st = new StringTokenizer (this.date);
		String lasttoken = "";
		while (st.hasMoreTokens ()) {
			lasttoken = st.nextToken();
		}
		return this.country + "-" + lasttoken;
	}
	public static String toStringCompact (Vector <Race> races) {
		int size = races.size();
		String str = "[\n";
		for (int i = 0 ; i < size; i++) {
			str += races.elementAt(i).toStringCompact();
			if (i < size - 1) {
				str += "\n, ";
			}
		}
		return str + "\n]";
	}
	private static void assertAllElementsAreOfTag (String tag, Elements els) {
		int size = els.size();
		for (int i = 0; i < size; i++) {
			if (!els.get(i).text().equals (tag)) {
				try {
					throw new Exception ("Tag not expected");
				} catch (Exception e) {}
			}
		}
	}
	private static String winnerName (Elements els) {
		String str = "";
		int size = els.size();
		for (int i = 1 ; i <= size; i++) {
			str += els.get(i).text() + " ";
		}
		return str;
	}
/**
	<tr> 
	 <td class="limiter"></td> 
	 <td class="dark bold"> 
	 	<a href="/en/results.html/1950/races/94/great-britain/race-result.html" data-ajax-url="/content/fom-website/en/results/jcr:content/resultsarchive.html/1950/races/94/great-britain/race-result.html" class="dark bold ArchiveLink"> Great Britain 
	 	</a> 
	 </td> 
	 <td class="dark hide-for-mobile">13 May 1950</td> 
	 <td class="dark bold"> 
	 	<span class="hide-for-tablet">Nino</span> 
	 	<span class="hide-for-mobile">Farina</span> 
	 	<span class="uppercase hide-for-desktop">FAR</span> </td> 
	 <td class="semi-bold uppercase ">Alfa Romeo</td> 
	 <td class="bold hide-for-mobile">70</td> 
	 <td class="dark bold hide-for-tablet">2:13:23.600</td> 
	 <td class="limiter"></td> 
	</tr>
* */
	private static String countryLinkInFull (boolean full, String href, String year) {
		String str = href.substring (28, href.indexOf ("/race-result.html"));
		String str2 = str.substring (str.indexOf ("/") + 1);
		System.out.print ("");
		if (!full)
			return str2;
		else 
			return str;
		//<a href="/en/results.html/1950/races/94/great-britain/race-result.html" data-ajax-url="/content/fom-website/en/results/jcr:content/resultsarchive.html/1950/races/94/great-britain/race-result.html" class="dark bold ArchiveLink"> Great Britain </a>
	}
	public static Race mountRace (Element el, String year, Document doc) {
		if (!el.text().equals ("tr")) {
			try {
				throw new Exception ("Not expected tag name");
			} catch (Exception e) {}
		}
		Elements tds = el.getAllElements();
		assertAllElementsAreOfTag ("td", tds);
		String country = ((Element)tds.get(3)).text();
		String countrylink = countryLinkInFull (true, ((Element)tds.get(3)).attr("href"), year);
		String date = ((Element)tds.get(4)).text();
		String winnername = ((Element)tds.get(5)).text();
		String team = ((Element)tds.get(9)).text();
		String laps = ((Element)tds.get(10)).text();
		String time = ((Element)tds.get(11)).text();
		//(String country, String circuit, String date, String winner, String team, String laps)
		return new Race (country, "", date, winnername, team, laps, time, countrylink, doc);
	}
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCircuit() {
		return circuit;
	}
	public void setCircuit(String circuit) {
		this.circuit = circuit;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public String getLaps() {
		return laps;
	}
	public void setLaps(String laps) {
		this.laps = laps;
	}
	public String getTimeOfRace () {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	/*public String getTimeOfDriver (int p) {
		return this.racedetails.getTime (p);
	}*/
}
