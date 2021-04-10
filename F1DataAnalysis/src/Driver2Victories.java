import java.util.HashMap;
import java.util.Vector;

public class Driver2Victories {
	private HashMap <String, Vector <Race>> map;
	private Driver2Victories () {
		this.map = new HashMap <String, Vector <Race>> ();
	}
	public Driver2Victories (Vector <Race> allraces) {
		this ();
		int size = allraces.size ();
		for (int i = 0; i < size; i++) {
			storeVictory (allraces.elementAt(i));
		}
	}
	private void storeVictory (Race race) {
		String driver = race.getWinner();
		if (!this.map.containsKey(driver)) {
			Vector <Race> races = new Vector <Race> ();
			races.addElement(race);
			this.map.put (driver, races);
		}
		else {
			Vector <Race> races = this.map.get (driver);
			races.addElement(race);
			this.map.put (driver, races);
		}
	}
	public Vector <Race> getVictories (String driver) {
		return this.map.get (driver);
	}
	public String toString () {
		Object [] o = this.map.keySet().toArray();
		String str = "";
		for (int i = 0; i < o.length; i++) {
			Vector <Race> getraces = this.map.get ((String) o[i]);
			int getracessize = getraces.size();
			str += (String)o[i] + " (" + getracessize + ") -> \n" + Race.toStringCompact(getraces) + "\n";
		}
		return str;
	}
}
