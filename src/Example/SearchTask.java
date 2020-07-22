package Example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class SearchTask implements Runnable {
	private LinkedHashMap<String, LinkedHashMap<String, Long>> mainHashMap;
	private BlockingQueue<File> queue;

	public SearchTask(BlockingQueue<File> queue, LinkedHashMap<String, LinkedHashMap<String, Long>> mainHashMap) {
		this.queue = queue;
		this.mainHashMap = mainHashMap;
	}

	public void run() {
		try {
			boolean done = false;
			while (!done) {
				File file = queue.take();
				if (file == FileEnumerationTask.DUMMY) {
					queue.put(file);
					done = true;
				} else
					search(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void search(File file) throws IOException, ParseException {
		Scanner in = new Scanner(new FileInputStream(file));

		while (in.hasNextLine()) {
			long totalPrice = 0;
			String line = in.nextLine();
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(line);
			String frameValue = (String) jsonObject.get("frame");
			LinkedHashMap<String, Long> frameMap = mainHashMap.get("frame");
			long framePrice = frameMap.get(frameValue);
			System.out.println("Frame price is : " + framePrice);
			totalPrice = totalPrice + framePrice;
			String breakValue = (String) jsonObject.get("handleBarWithBrakes");
			LinkedHashMap<String, Long> breakMap = mainHashMap.get("handleBarWithBrakes");
			long breakPrice = breakMap.get(breakValue);
			System.out.println("break price is : " + breakPrice);
			totalPrice = totalPrice + breakPrice;
			String seatingValue = (String) jsonObject.get("seating");
			LinkedHashMap<String, Long> seatingMap = mainHashMap.get("seating");
			long seatingPrice = seatingMap.get(seatingValue);
			System.out.println("seat price is : " + seatingPrice);
			totalPrice = totalPrice + seatingPrice;
			String wheelsValue = (String) jsonObject.get("wheels");
			LinkedHashMap<String, Long> wheelsMap = mainHashMap.get("wheels");
			long wheelPrice = wheelsMap.get(wheelsValue);
			System.out.println("wheels price is : " + wheelPrice);
			totalPrice = totalPrice + wheelPrice;
			String chainAssemblyalue = (String) jsonObject.get("chainAssembly");
			LinkedHashMap<String, Long> chainAssemblyMap = mainHashMap.get("chainAssembly");
			long chainAssemblyPrice = chainAssemblyMap.get(chainAssemblyalue);
			System.out.println("chain assembly price is : " + chainAssemblyPrice);
			totalPrice = totalPrice + chainAssemblyPrice;
			System.out.println("Total Price of the cycle : " + totalPrice);

		}
		in.close();
	}

}