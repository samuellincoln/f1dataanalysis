import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RaceDetails {
	private String site;
	//private Vector <String> drivers;
	//private Vector <String> times;
	private Vector <String> vecrdet;
	public RaceDetails (String site, String year) {
		this.site = site;
		Document doc = this.genDoc ();
		this.vecrdet = this.mountVectorRaceDetails (doc, year);
		System.out.print ("");
	}
	public String getTime (int place) {
		String element = vecrdet.elementAt(place - 1);
		if (element.contains (" lap")) {
			return "1LapOrMore";
		}
		else if (element.contains (" SHC ")) {
			return getTime (place + 1);
		}
		String [] splits = element.split(" ");
		String time = "+NOT-FOUNDs";
		for (int i = 0; i < splits.length; i++) {
			if (splits [i].contains("+") && splits [i].contains("s")) {
				time = splits [i];
			}
		}
		String rettime = time.substring(1, time.length() - 1);
		return rettime;
	}
	public String toString () {
		String str = "";
		int size = this.vecrdet.size();
		for (int i = 0; i < size; i++) {
			str += "	" + this.vecrdet.elementAt(i) + "\n";
		}
		return str;
	}
	private Document genDoc () {
		try {
			URL u = new URL (this.site);
			InputStream inputstream = u.openStream();
			InputStreamReader streamreader = new InputStreamReader (inputstream);
			BufferedReader br = new BufferedReader (streamreader);
			String l = br.readLine();
			String str = "";
			System.out.println(l);
			while (l != null) {
				l = br.readLine();
				str += l + "\n";
			}
			br.close();
			Document doc = Jsoup.parse (str);
			return doc;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private Vector <String> mountVectorRaceDetails (Document doc, String year) {
		Vector <String> strss = new Vector <String> ();
		//Vector <String> drivers = new Vector <String> ();
		Elements els = doc.getElementsByTag ("tr");
		int size = els.size();
		for (int i = 1; i < 4 && i < size/*size*/; i++) {
			strss.addElement (els.get(i).text());
			System.out.print ("");
		}
		return strss;
	}
	/*private void mountRaceDetails (Element el) {
		URL u = new URL (this.site);
		InputStream inputstream = u.openStream();
		InputStreamReader streamreader = new InputStreamReader (inputstream);
		BufferedReader br = new BufferedReader (streamreader);
		String l = br.readLine();
		String str = "";
		System.out.println(l);
		while (l != null) {
			l = br.readLine();
			str += l + "\n";
		}
		br.close();
		Document doc = Jsoup.parse (str);
		if (!el.text().equals ("tr")) {
			try {
				throw new Exception ("Not expected tag name");
			} catch (Exception e) {}
		}
		Elements tds = el.getAllElements();
		//assertAllElementsAreOfTag ("td", tds);
		String country = ((Element)tds.get(3)).text();
		String countrylink = countryLinkInFull (true, ((Element)tds.get(3)).attr("href"), year);
		String date = ((Element)tds.get(4)).text();
		String winnername = ((Element)tds.get(5)).text();
		String team = ((Element)tds.get(9)).text();
		String laps = ((Element)tds.get(10)).text();
		String time = ((Element)tds.get(11)).text();
		//(String country, String circuit, String date, String winner, String team, String laps)
		return new Race (country, "", date, winnername, team, laps, time, countrylink);
	}*/
}