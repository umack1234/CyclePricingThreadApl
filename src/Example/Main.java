package Example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		JSONParser jsonParser = new JSONParser();
		try (FileReader reader = new FileReader("C:/Example/cycles.json.json")) {
			LinkedHashMap<String, LinkedHashMap<String, Long>> hm1 = new LinkedHashMap<String, LinkedHashMap<String, Long>>();

			Object obj = jsonParser.parse(reader);
			JSONArray cyclePriceList = new JSONArray();
			cyclePriceList.add(obj);
			System.out.println(cyclePriceList);

			hm1 = parseCyclePriceObject((JSONObject) cyclePriceList.get(0));

			String directory = "C:/Example";

			final int FILE_QUEUE_SIZE = 20;
			final int SEARCH_THREADS = 10;

			BlockingQueue<File> queue = new ArrayBlockingQueue<File>(FILE_QUEUE_SIZE);

			FileEnumerationTask enumerator = new FileEnumerationTask(queue, new File(directory));
			new Thread(enumerator).start();

			for (int i = 1; i <= SEARCH_THREADS; i++)
				new Thread(new SearchTask(queue, hm1)).start();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static LinkedHashMap<String, LinkedHashMap<String, Long>> parseCyclePriceObject(JSONObject cyclePricing) {
		LinkedHashMap<String, LinkedHashMap<String, Long>> mainHashMap = new LinkedHashMap<String, LinkedHashMap<String, Long>>();
		LinkedHashMap<String, Long> partsHashMap = new LinkedHashMap<String, Long>();
		LinkedHashMap<String, Long> breaksHashMap = new LinkedHashMap<String, Long>();
		LinkedHashMap<String, Long> seatingHashMap = new LinkedHashMap<String, Long>();
		LinkedHashMap<String, Long> wheelsHashMap = new LinkedHashMap<String, Long>();
		LinkedHashMap<String, Long> chainAssemblyHashMap = new LinkedHashMap<String, Long>();

		JSONObject frameObject = (JSONObject) cyclePricing.get("frame");
		long framePrice = (long) frameObject.get("steelFrame");
		partsHashMap.put("steelFrame", framePrice);
		long framePrice1 = (long) frameObject.get("normalFrame");
		partsHashMap.put("normalFrame", framePrice1);
		mainHashMap.put("frame", partsHashMap);

		JSONObject handleBarWithBrakesObject = (JSONObject) cyclePricing.get("handleBarWithBrakes");
		long brakePrice = (long) handleBarWithBrakesObject.get("normalHandleBar");
		breaksHashMap.put("normalHandleBar", brakePrice);
		long brakePrice1 = (long) handleBarWithBrakesObject.get("sportsHandleBar");
		breaksHashMap.put("sportsHandleBar", brakePrice1);
		mainHashMap.put("handleBarWithBrakes", breaksHashMap);

		JSONObject seatingObject = (JSONObject) cyclePricing.get("seating");
		long seatingPrice = (long) seatingObject.get("customSeating");
		seatingHashMap.put("customSeating", seatingPrice);
		long seatingPrice1 = (long) seatingObject.get("defaultSeating");
		seatingHashMap.put("defaultSeating", seatingPrice1);
		mainHashMap.put("seating", seatingHashMap);

		JSONObject wheelObject = (JSONObject) cyclePricing.get("wheels");
		long wheelPrice = (long) wheelObject.get("tube");
		wheelsHashMap.put("tube", wheelPrice);
		long wheelPrice1 = (long) wheelObject.get("tubeLess");
		wheelsHashMap.put("tubeLess", wheelPrice1);
		mainHashMap.put("wheels", wheelsHashMap);

		JSONObject chainObject = (JSONObject) cyclePricing.get("chainAssembly");
		long chainPrice = (long) chainObject.get("localChain");
		chainAssemblyHashMap.put("localChain", chainPrice);
		long chainPrice1 = (long) chainObject.get("brandedChain");
		chainAssemblyHashMap.put("brandedChain", chainPrice1);
		mainHashMap.put("chainAssembly", chainAssemblyHashMap);

		return mainHashMap;
	}
}
