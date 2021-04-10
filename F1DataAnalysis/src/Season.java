import java.util.Vector;

public class Season {
	private Vector <Race> allraces;
	private String year;
	public Season (String year, Vector <Race> allraces) {
		this.year = year;
		this.allraces = allraces;
	}
	public String toString () {
		String str = "Season of " + this.year + ":\n";
		int size = allraces.size();
		for (int i = 0; i < size; i++) {
			str += "	" + allraces.elementAt(i).toString() + "\n";
		}
		return str;
	}
	/*public String toStringInFull () {
		String str = "SEASON OF " + this.year + ":\n";
		int size = allraces.size();
		for (int i = 0; i < size; i++) {
			str += allraces.elementAt(i).toStringInFull() + "\n";
		}
		return str;
	}*/
	public Vector <Race> getAllraces() {
		return allraces;
	}
	public void setAllraces(Vector <Race> allraces) {
		this.allraces = allraces;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
}