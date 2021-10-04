package api;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

public class Artikelstammdaten {
	String artikelnummer;
	String artikelbezeichnung;
	String ean_stueck;
	String ean_gebinde;
	String menge_im_gebinde;
	String ean_umkarton;
	String menge_im_umkarton;
	Integer artikel_hoehe;
	Integer artikel_laenge;
	Integer artikel_breite;
	Integer artikel_gewicht;
	Lagerplatz[] lagerplatz;
	Integer max_pro_box;

	protected class Lagerplatz {
		String lagerplatz;
		Integer lagerplatzbestand;
	}

	/**
	 * Deserializing a JSON-String to an Array of Artikelstammdaten
	 * 
	 * @param jsonstring String containing Artikelstammdaten serialized as JSON
	 * @param useGSON    Use GSON to serialize, false = manual coded serializing
	 *                   (not working)
	 * @return Array of Artikelstammdaten
	 */
	public static Artikelstammdaten[] toArtikelstammdaten(String jsonstring, boolean useGSON) {

		if (useGSON) {
			return new Gson().fromJson(jsonstring, Artikelstammdaten[].class);
		} else {
			// not working, manual serializing
			JSONArray array = new JSONArray(jsonstring);
			int temp = array.length();
			Artikelstammdaten[] artikelstammdatenarray = new Artikelstammdaten[temp];
			System.out.println(temp);
			System.out.println(artikelstammdatenarray.length);
			JSONObject jsonObject;

			for (int i = 0; i < temp; i++) {
				jsonObject = array.getJSONObject(i);
				Artikelstammdaten asd = new Artikelstammdaten();

				System.out.println(i);
				System.out.println(jsonObject.toString());

				System.out.println(jsonObject.getString("artikelnummer"));
				asd.artikelnummer = jsonObject.getString("artikelnummer");
				System.out.println(asd.artikelnummer);
				asd.artikelbezeichnung = jsonObject.getString("artikelbezeichnung");
				asd.ean_stueck = jsonObject.getString("ean_stueck");
				asd.ean_gebinde = jsonObject.getString("ean_gebinde");
				asd.menge_im_gebinde = jsonObject.getString("menge_im_gebinde");
				asd.ean_umkarton = jsonObject.getString("ean_umkarton");
				asd.menge_im_umkarton = jsonObject.getString("menge_im_umkarton");
				asd.artikel_hoehe = jsonObject.getInt("artikel_hoehe");
				asd.artikel_laenge = jsonObject.getInt("artikel_laenge");
				asd.artikel_breite = jsonObject.getInt("artikel_breite");
				asd.artikel_gewicht = jsonObject.getInt("artikel_gewicht");
				asd.max_pro_box = jsonObject.getInt("max_pro_box");
				System.out.println(asd.max_pro_box);

				JSONArray array2 = jsonObject.getJSONArray("lagerplatz");
				asd.lagerplatz = new Lagerplatz[array2.length()];
				System.out.println("lagerplatz length" + array2.length());

				for (int i2 = 0; i2 < array2.length(); i2++) {
					JSONObject jsonObject2 = array2.getJSONObject(i2);
					asd.lagerplatz[i2].lagerplatz = jsonObject2.getString("lagerplatz");
					System.out.println(asd.lagerplatz[i2].lagerplatz);
					asd.lagerplatz[i2].lagerplatzbestand = jsonObject2.getInt("lagerplatzbestand");
				}
			}

			return artikelstammdatenarray;
		}

	}

	/**
	 * Creates a Json-String from this Class
	 * 
	 * @return Json String
	 */
	public String ToJSONString() {
		return new Gson().toJson(this);
	}

	/**
	 * Checks object for NULL values, throws custom NullPointerException
	 */
	public void CheckForNull() {
		String message = "";
		message += requireNonNull(artikelbezeichnung, "\"artikelbezeichnung\" is NULL\n");
		message += requireNonNull(ean_stueck, "\"ean_stueck\" is NULL\n");
		message += requireNonNull(ean_gebinde, "\"ean_gebinde\" is NULL\n");
		message += requireNonNull(menge_im_gebinde, "\"menge_im_gebinde\" is NULL\n");
		message += requireNonNull(ean_umkarton, "\"ean_umkarton\" is NULL\n");
		message += requireNonNull(menge_im_umkarton, "\"menge_im_umkarton\" is NULL\n");
		message += requireNonNull(artikel_hoehe, "\"artikel_hoehe\" is NULL\n");
		message += requireNonNull(artikel_laenge, "\"artikel_laenge\" is NULL\n");
		message += requireNonNull(artikel_breite, "\"artikel_breite\" is NULL\n");
		message += requireNonNull(artikel_gewicht, "\"artikel_gewicht\" is NULL\n");
		message += requireNonNull(lagerplatz, "\"lagerplatz\" is NULL\n");
		if (lagerplatz.length == 0)
			message += "\"lagerplatz\" is EMPTY\n";
		else
			for (Lagerplatz lagerplatztemp : lagerplatz) {
				message += requireNonNull(lagerplatztemp.lagerplatz, "\"lagerplatz(Nr)\" is NULL\n");
				message += requireNonNull(lagerplatztemp.lagerplatzbestand, "\"lagerplatzbestand\" is NULL\n");
			}
		message += requireNonNull(max_pro_box, "\"max_pro_box\" is NULL\n");

		if (!message.equals("")) {
			// message += "---Please check JSON-Construction on Client---";
			throw new NullPointerException(message);
		}

	}

	private static <T> String requireNonNull(T obj, String message) {
		if (obj == null)
			return message;
		return "";
	}

	/**
	 * Checks Object for harmable code for SQL-Injection in String-fields
	 * 
	 * @throws Exception If any field contained any characters which may lead to a
	 *                   SQL-Injection
	 */
	public void CheckForSQLInjectPaddern() throws Exception {
		String[] test = new String[] { "=", "\"", "{", "/", "}" };
		containsAny(artikelnummer, test);
		containsAny(artikelbezeichnung, test);
		containsAny(ean_stueck, test);
		containsAny(ean_gebinde, test);
		containsAny(menge_im_gebinde, test);
		containsAny(ean_umkarton, test);
		containsAny(menge_im_umkarton, test);
		for (Lagerplatz lagerplatztemp : lagerplatz) {
			containsAny(lagerplatztemp.lagerplatz, test);
		}
	}

	/*
	 * private static void containsAny(String str, char[] searchChars) throws
	 * Exception {
	 * 
	 * for (int i = 0; i < str.length(); i++) { char ch = str.charAt(i); for (int j
	 * = 0; j < searchChars.length; j++) { if (searchChars[j] == ch) { throw new
	 * Exception("Contains malicious character: "+ ch); } } }
	 * 
	 * }
	 */

	/**
	 * Checks if the String contains any character in the given set of characters.
	 * 
	 * @param str           The String to check
	 * @param searchStrings Array of Strings to search for within the String
	 * @throws Exception if any of the SearchStrings have been found
	 */
	private static void containsAny(String str, String[] searchStrings) throws Exception {
		for (int j = 0; j < searchStrings.length; j++) {
			if (str.contains(searchStrings[j])) {
				throw new Exception("Contains character: " + searchStrings[j]);
			}
		}

	}
}

//TODO json should be sent as object{}, not as array[], for secure transmission
// need to be clarified with Customer

/*
 * example json
 * 
 * 
 * [{ "artikelnummer": "145880", "artikelbezeichnung": "Erdbeer-Traum 420g",
 * "ean_stueck": "2000145880008", "ean_gebinde": "", "menge_im_gebinde": "",
 * "ean_umkarton": "", "menge_im_umkarton": "", "artikel_hoehe": 116,
 * "artikel_laenge": 75, "artikel_breite": 75, "artikel_gewicht": 670,
 * "lagerplatz": [ { "lagerplatz": "P001", "lagerplatzbestand": 933 }, {
 * "lagerplatz": "P002", "lagerplatzbestand": 500 } ], "max_pro_box": 48 }, {
 * "artikelnummer": "177960", "artikelbezeichnung": "Himbeer-Traum",
 * "ean_stueck": "2000177960006", "ean_gebinde": "", "menge_im_gebinde": "",
 * "ean_umkarton": "", "menge_im_umkarton": "", "artikel_hoehe": 116,
 * "artikel_laenge": 75, "artikel_breite": 75, "artikel_gewicht": 690,
 * "lagerplatz": [ { "lagerplatz": "P004", "lagerplatzbestand": 960 } ],
 * "max_pro_box": 48 }]
 * 
 */
