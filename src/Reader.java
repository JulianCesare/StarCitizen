import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class Reader {

	private static File f;
	private static String path = "to/set/path";
	private static BufferedReader in;
	private static ArrayList<GUT> evaluationPrices;

	public static void main(String[] args) {
		try {
			f = new File(path);
			in = new BufferedReader(new FileReader(f));
			String[] text = FileReader();
			evaluationPrices(text);
			
			
			ArrayList<GUT> result = PriceSearch(SearchQuery.PRICEDESC,"ARCCORP","");
			for (int i = 0; i < result.size(); i++) {
				System.out.println(result.get(i).toString());
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @return Übergebene File Zeile für Zeile als String Array
	 * @throws IOException
	 */
	private static String[] FileReader() throws IOException {
		String aktZeile = "";
		String[] readText = new String[0];
		while ((aktZeile = in.readLine()) != null) {
			readText = ArrayPlus(readText);
			readText[readText.length - 1] = aktZeile;
		}
		return readText;
	}

	/**
	 * 
	 * @param array, das verlängert werden soll
	 * @return array um einen Platz verlängert
	 */
	private static String[] ArrayPlus(String[] array) {
		String[] ret = new String[array.length + 1];
		for (int i = 0; i < array.length; i++) {
			ret[i] = array[i];
		}
		return ret;
	}

	/**
	 * Wertet das File aus
	 * 
	 * @param file das ausgewertet werden soll
	 * @return
	 */
	private static void evaluationPrices(String[] file) {
		evaluationPrices = new ArrayList<GUT>();
		for (int i = 4; i < file.length; i++) {
			if (!file[i].contains("--") && !file[i].contains("///") && !file[i].equals("")) {
				if (file[i].charAt(2) < 97) {
					continue;
				}
				if (file[i].equals("none") || file[i].equals("no data")) {
					continue;
				}
				String early = file[i];
				String Product = early.substring(0, early.lastIndexOf(" "));
				double price = Double.parseDouble(early.substring(early.lastIndexOf(" ")));
				evaluationPrices
						.add(new GUT(lastSystem(i, file), lastPlanet(i, file), lastFacility(i, file), Product, price));
			}
		}
	}

	/**
	 * 
	 * @param anfrage  Wie Sortiert werden soll (PRICEASC, PRICEDESC)
	 * @param Location Ort nach dem Sortiert werden soll(entweder System, Planet, Facility)
	 * @param Product Produkt nach dem Sortiert werden soll
	 * @return
	 */
	private static ArrayList<GUT> PriceSearch(SearchQuery anfrage, String Location, String Product) {
		ArrayList<GUT> result = new ArrayList<GUT>();
		if (!Location.equals("") && Product.equals("")) {
			for (int i = 0; i < evaluationPrices.size(); i++) {
				if (evaluationPrices.get(i).getFacilityName().equals(Location)
						|| evaluationPrices.get(i).getPlanet().equals(Location)
						|| evaluationPrices.get(i).getSystem().equals(Location)) {
					result.add(evaluationPrices.get(i));
				}
			}
		} else if (Location.equals("") && !Product.equals("")) {
			for (int i = 0; i < evaluationPrices.size(); i++) {
				if (evaluationPrices.get(i).getProduct().equals(Product)) {
					result.add(evaluationPrices.get(i));
				}
			}
		} else if (!Location.equals("") && !Product.equals("")) {
			for (int i = 0; i < evaluationPrices.size(); i++) {
				if (evaluationPrices.get(i).getProduct().equals(Product)
						&& (evaluationPrices.get(i).getFacilityName().equals(Location)
								|| evaluationPrices.get(i).getPlanet().equals(Location)
								|| evaluationPrices.get(i).getSystem().equals(Location))) {
					result.add(evaluationPrices.get(i));
				}
			}
		} else
			result = evaluationPrices;

		if (anfrage == SearchQuery.PRICEASC) {
			result.sort(Comparator.comparingDouble(GUT::getPrice).reversed());
			return result;
		} else if (anfrage == SearchQuery.PRICEDESC) {
			result.sort(Comparator.comparingDouble(GUT::getPrice));
			return result;
		} else
			return result;

	}

	/**
	 * 
	 * @param aktZeile
	 * @param file
	 * @return Name des zugehörigen Systems
	 */
	private static String lastSystem(int aktZeile, String[] file) {
		int help = aktZeile;
		while (help >= 0 && !file[help].contains("/////")) {
			help--;
		}
		if (help < 0) {
			return "NOT_FOUND";
		}
		while (help >= 0 && file[help].contains("/////")) {
			help--;
		}
		if (help < 0) {
			return "NOT_FOUND";
		}
		return file[help].replace(" SYSTEM", "");
	}

	/**
	 * 
	 * @param aktZeile
	 * @param file
	 * @return Name des zugehörigen Planeten
	 */
	private static String lastPlanet(int aktZeile, String[] file) {
		int help = aktZeile;
		while (help >= 0 && !file[help].contains("---------")) {
			help--;
		}
		if (help < 0) {
			return "NOT_FOUND";
		}

		return file[help + 1];
	}

	/**
	 * 
	 * @param aktZeile
	 * @param file
	 * @return Name der zugehörigen Facility
	 */
	private static String lastFacility(int aktZeile, String[] file) {
		int help = aktZeile;
		while (help >= 0 && !file[help].regionMatches(0, "--", 0, 2)) {
			help--;
		}
		if (help < 0) {
			return "NOT_FOUND";
		}

		return file[help].replace("--", "");
	}
}
