/**
 * 
 * Klasse om de gegevens te corrigeren
 * 
 * 
 * @author Mark Nijboer
 * @version 25.9.2015
 * 
 */

package weerstation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class CorrectData {
	private static HashMap<String, ArrayList<HashMap<String, String>>> dataStorage = new HashMap<String, ArrayList<HashMap<String, String>>>();
	private static final int MAX_COUNTER = 30;
	
	
	
	/**
	 * 
	 * Deze methode zorgt voor de correctie van de data. Sommige data is niet volledig en soms foutief. Dat probleem wordt hierin aangepakt.
	 * 
	 * @param data		De data in de vorm van een HashMap met soms foutieve of lege waarden
	 * @return data		De gecorrigeerde data
	 */
	public static synchronized HashMap<String, String> correct(HashMap<String, String> data) {
		
		String stationnr = "0";
		ArrayList<String> missingValue = new ArrayList<String>();
		for(Entry<String, String> entry : data.entrySet()) {
			
		    String key = entry.getKey();
		    String value = entry.getValue();
		    
			if(key == "stn") {
				stationnr = value;
			}
		    
		    if(value == "") {
		    	missingValue.add(key);
		    }

		}
		
		ArrayList<HashMap<String, String>> list = dataStorage.get(stationnr);
		
		if(list != null) {
			if(list.size() == MAX_COUNTER) {
				
				for(String key : missingValue) {
					
					int count = 0;
					double total = 0;
					
					for(HashMap<String, String> map : list) {
						total = total + Double.parseDouble(map.get(key));
						count++;
					}
					
					Double value = (double) Math.round((total / count) * 10) / 10;
					
					data.put(key, value.toString());
				}
				
				double totalTemp = 0;
				int countTemp = 0;
				for(HashMap<String, String> map : list) {
					totalTemp = totalTemp + Double.parseDouble(map.get("TEMP"));
					countTemp++;
				}
				Double value = (double) Math.round((totalTemp / countTemp) * 10) / 10;
				Double margin = value * 0.20;
				
				if(Double.parseDouble(data.get("TEMP")) > value + margin || Double.parseDouble(data.get("TEMP")) < value - margin) {
					data.put("TEMP", value.toString());
				}
				
				
				list.add(data);
				list.remove(0);
				list.trimToSize();
			} else {
				
				for(String key : missingValue) {
					
					int count = 0;
					double total = 0;
					
					for(HashMap<String, String> map : list) {
						total = total + Double.parseDouble(map.get(key));
						count++;
					}
					
					Double value = (double) Math.round((total / count) * 10) / 10;
					
					data.put(key, value.toString());
				}
				
				double totalTemp = 0;
				int countTemp = 0;
				for(HashMap<String, String> map : list) {
					totalTemp = totalTemp + Double.parseDouble(map.get("TEMP"));
					countTemp++;
				}
				Double value = (double) Math.round((totalTemp / countTemp) * 10) / 10;
				Double margin = value * 0.20;
				
				if(Double.parseDouble(data.get("TEMP")) > value + margin || Double.parseDouble(data.get("TEMP")) < value - margin) {
					data.put("TEMP", value.toString());
				}
				
				list.add(data);
			}
		} else {
			
			for(String key : missingValue) {
				data.put(key, "0.0");
			}
			
			list = new ArrayList<HashMap<String, String>>();
			list.add(data);
		}
		
		dataStorage.put(stationnr, list);
		
		
		return data;
	}
}
