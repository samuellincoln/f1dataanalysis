import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang.StringEscapeUtils;

public class BiggestNumberOfLaps {
	private static final int FIRSTSEASON = 1950;
	private static final int LASTSEASON = 2020;
	private static Vector <Race> mountRaces (Document doc, String year) {
		Vector <Race> allraces = new Vector <Race> ();
		Elements els = doc.getElementsByTag ("tr");
		int size = els.size();
		for (int i = 1; i < size; i++) {
			Race race = Race.mountRace (els.get(i), year, doc);
			allraces.addElement(race);
		}
		return allraces;
	}
	public static void main (String [] args) {
		Vector <Season> seasons = new Vector <Season> ();
		try {
			for (int i = FIRSTSEASON; i <= LASTSEASON; i++) {
				URL u = new URL ("https://www.formula1.com/en/results.html/" + i + "/races.html");
				InputStream inputstream = u.openStream();
				InputStreamReader streamreader = new InputStreamReader (inputstream);
				BufferedReader br = new BufferedReader (streamreader);
				String l = br.readLine();
				String str = "";
				System.out.println (l);
				while (l != null) {
					l = br.readLine();
					str += l + "\n";
				}
				br.close();
				Document doc = Jsoup.parse (str);
				Vector <Race> races = mountRaces (doc, "" + i);
				Season s = new Season (i + "", races);
				seasons.addElement(s);
			}
			F1Data f1data = new F1Data (seasons, F1Data.Orderby.LONGESTRACES);
			BufferedWriter bw = new BufferedWriter (new FileWriter (new File (
					"/home/samuel/Software/eclipse/java-2020-03/eclipse"
					+ "/eclipse-jse-workspace/F1DataAnalysis/src/F1AllData.txt")));
			bw.write (f1data.toString()
						+ "\n\nORDERED BY " + f1data.getOrderby() + " \n\n"
					+ f1data.toStringOrdered (true)
						+ "\n\nALL RACE WINNERS:\n\n" + f1data.d2vToString()
						//+ "\n\nALL RACE RESULTS IN FULL:\n\n" + f1data.toStringInFull()
					 )
			;
			bw.close();
			System.out.print ("");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
