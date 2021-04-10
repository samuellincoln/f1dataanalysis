import java.util.StringTokenizer;
import java.util.Vector;

public class F1Data {
	public enum Orderby {
		NUMOFLAPS
		, LONGESTRACES
		//, TIGHTESTWINS
	}
	private Orderby o;
	public String getOrderby () {
		return this.o.toString();
	}
	private Vector <Season> seasons;
	private Vector <Race> orderedraces;
	//private Vector <Race> orderedracesbydifffrom2ndto1st;
	private Driver2Victories d2v;

	public F1Data (Vector <Season> seasons, Orderby o) {
		this.seasons = seasons;
		Vector <Race> allraces = allraces ();
		d2v = new Driver2Victories (allraces);
		this.o = o;
		if (this.o == Orderby.NUMOFLAPS) {
			this.orderedraces = orderByNumberOfLaps (allraces, o);
		}
		else if (this.o == Orderby.LONGESTRACES) {
			this.orderedraces = orderByTime (allraces, o, true, true);
		}
		else //if TIGHTESTWINS
			this.orderedraces/*bydifffrom2ndto1st*/ = orderByTime (allraces, o, false, false);
		System.out.print ("");
	}
	public String d2vToString () {
		return this.d2v.toString();
	}
	private String toString (boolean infull) {
		String str = "";
		int size = seasons.size();
		for (int i = 0; i < size; i++) {
			str += //(!infull)? 
						this.seasons.elementAt(i).toString ()
					//: this.seasons.elementAt(i).toStringInFull ()
				;
		}
		return str;
	}
	public String toString () {
		return this.toString (false);
	}
	public String toStringInFull () {
		return this.toString (true);
	}
	public String toStringOrdered (boolean reverse) {
		String str = "";
		int size = this.orderedraces.size();
		int count = 1;
		//if (!reverse) {
			for (int i = 0; i < size; i++) {
				String compl = //isracetimeornumoflaps?
						this.orderedraces.elementAt(i).toString()
						//: this.orderedracesbydifffrom2ndto1st.elementAt(i).toString()
						;
				str += count + " :: " + compl + "\n";
				count++;
			}
		//}
		/*else {
			for (int i = size - 1; i >= 0; i--) {
				String compl = isracetime? 
						this.orderedraces.elementAt(i).toString()
						: this.orderedracesbydifffrom2ndto1st.elementAt(i).toString();
				str += count + " :: " + compl + "\n";
				count++;
			}
		}*/
		return str;
	}
	private Vector <Race> allraces () {
		Vector <Race> allraces = new Vector <Race> ();
		int size = this.seasons.size();
		for (int i = 0; i < size; i++) {
			allraces.addAll (this.seasons.elementAt(i).getAllraces());
		}
		return allraces;
	}
	private static boolean leftAndRightTimeEqual (String left, String right) {
		StringTokenizer st1 = new StringTokenizer (left, ":.");
		StringTokenizer st2 = new StringTokenizer (right, ":.");
		if (st1.countTokens() != st2.countTokens()) {
			return false;
		}
		else {
			while (st1.hasMoreTokens() /*&& st2.hasMoreTokens()*/) {
				int nt1 = Integer.parseInt (st1.nextToken());
				int nt2 = Integer.parseInt (st2.nextToken());
				if (nt1 != nt2) {
					return false;
				}
			}
			return true;
		}
		//return !(leftTimeBiggerThanRightTime (left, right) || leftTimeBiggerThanRightTime (right, left));
	}
	protected static boolean isANumber (String str) {
		int size = str.length();
		boolean aux = true;
		for (int i = 0; i < size; i++) {
			int ascii = (int) str.charAt(i);
			if (!(ascii >= 48 && ascii <= 57)) {
				aux = false;
			}
		}
		return aux;
	}
	private static boolean timeIsANumber (StringTokenizer time) {
		boolean isanumber = true;
		while (time.hasMoreTokens()) {
			if (!isANumber (time.nextToken())) {
				isanumber = false;
			}
		}
		return isanumber;
	}
	private static boolean leftTimeBiggerThanRightTime (String left, String right) {
		StringTokenizer st1 = new StringTokenizer (left, ":.");
		StringTokenizer st2 = new StringTokenizer (right, ":.");
		/*if (!timeIsANumber (st1) || !timeIsANumber (st2)) {
			return false;
		}
		else*/if (left.equals("0")) {
			return false;
		}
		else if (st1.countTokens() > st2.countTokens()) {
			return true;
		}
		else if (st1.countTokens() < st2.countTokens()) {
			return false;
		}
		else {
			while (st1.hasMoreTokens() /*&& st2.hasMoreTokens()*/) {
				int nt1 = Integer.parseInt (st1.nextToken());
				int nt2 = Integer.parseInt (st2.nextToken());
				if (nt1 > nt2) {
					System.out.println (left + " bigger than " + right);
					return true;
				}
				else if (nt1 < nt2) {
					System.out.println (left + " NOT bigger than " + right);
					return false;
				}
				else {
					//continue
				}
			}
			return false;
		}
	}

	private static Race longestOrShortestTime (Vector <Race> allraces, Orderby o, boolean isracetime, boolean islongest) {
		Race retrace = new Race (!islongest);
		int size = allraces.size();
		String flag = islongest? "0" : "10.59.59.999";
		for (int i = 0; i < size; i++) {
			Race currentrace = allraces.elementAt(i);
			String strtime;
			//if (isracetime)
				strtime = currentrace.getTimeOfRace();
			//else
			//	strtime = currentrace.getTimeOfDriver(2 /**The second place of each driver will dictate what was the most tightest win*/);
			String minormax = strtime;
			if (!minormax.contains ("Lap")) {
				if (/*islongest && */leftTimeBiggerThanRightTime (minormax, flag)) {
					flag = minormax;
					retrace = currentrace;
				}
				/**else if (!islongest && leftTimeBiggerThanRightTime (flag, minormax)) {
					flag = minormax;
					retrace = currentrace;
				}*/
			}
			/*else {
				if (!islongest) {
					flag = minormax;
					retrace = currentrace;
				}
				else {
					//NOTHING TO DO
				}
			}*/
		}
		//System.out.println (isracetime? retrace.getTimeOfRace() : retrace.getTimeOfDriver(2));
		return retrace;
	}
	private static Race raceWithBiggerNumberOfLaps (Vector <Race> allraces, Orderby o) {
		Race retrace = new Race (false);
		int size = allraces.size();
		int flag = 0;
		for (int i = 0; i < size; i++) {
			Race currentrace = allraces.elementAt(i);
			String strlaps = currentrace.getLaps();
			int max = (!strlaps.equals("null"))? Integer.parseInt (strlaps) : 0;
			//The line above is a result of a mistake on the data shown at the website of F1
			if (max > flag) {
				flag = max;
				retrace = currentrace;
			}
		}
		return retrace;
	}
	private static Vector <Race> racesWithRemovedRaceWithNLaps (int n, Vector <Race> allraces, Orderby o) {
		int size = allraces.size();
		int flag = 0;
		for (int i = 0; i < size; i++) {
			String strlaps = allraces.elementAt(i).getLaps();
			int laps = (!strlaps.equals("null"))? Integer.parseInt (strlaps) : 0;
				//The line above is a result of a mistake on the data shown at the website of F1
			if (laps == n) {
				allraces.removeElementAt(i);
				break;
			}
		}
		return allraces;
	}
	private static Vector <Race> racesWithRemovedRaceWithNTime (String time, Vector <Race> allraces, Orderby o, boolean istimerace) {
		int size = allraces.size();
		int flag = 0;
		for (int i = 0; i < size; i++) {
			String strtime = 
				//istimerace? 
						allraces.elementAt(i).getTimeOfRace() 
						//: allraces.elementAt(i).getTimeOfDriver(2)
			;
			//int laps = (!strlaps.equals("null"))? Integer.parseInt (strlaps) : 0;
				//The line above is a result of a mistake on the data shown at the website of F1
			if (leftAndRightTimeEqual (strtime, time)) {
				allraces.removeElementAt(i);
				break;
			}
		}
		return allraces;
	}
	private static Vector <Race> orderByNumberOfLaps (Vector <Race> allraces, Orderby o) {
		Vector <Race> newallraces = new Vector <Race> (allraces);
		Vector <Race> retallraces = new Vector <Race> ();
		int size = newallraces.size();
		for (int i = 0; i < size; i++) {
			if (!newallraces.isEmpty()) {
				Race biggerlapsrace = raceWithBiggerNumberOfLaps (newallraces, o);
				retallraces.addElement (biggerlapsrace);
				newallraces = racesWithRemovedRaceWithNLaps (Integer.parseInt(biggerlapsrace.getLaps()), newallraces, o);
			}
		}
		return retallraces;
	}
	private static Vector <Race> orderByTime (Vector <Race> allraces, Orderby o, boolean isracetime, boolean islongest) {
		Vector <Race> newallraces = new Vector <Race> (allraces);
		Vector <Race> retallraces = new Vector <Race> ();
		int size = newallraces.size();
		for (int i = 0; i < size; i++) {
			if (!newallraces.isEmpty()) {
				Race longestorshortesttime = longestOrShortestTime (newallraces, o, isracetime, islongest);
				retallraces.addElement (longestorshortesttime);
				newallraces = racesWithRemovedRaceWithNTime (longestorshortesttime.getTimeOfRace(), newallraces, o, isracetime);
			}
		}
		return retallraces;
	}
}
