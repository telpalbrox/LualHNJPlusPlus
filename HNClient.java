import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import JSONParser;
import HTTPClient;
import HTTPResponse;
import HNItem;

public class HNClient
{
	private static final String BASE_URL = "http://192.168.50.204:8080/https://api.hackerwebapp.com";
	private final HTTPClient httpClient = new HTTPClient();
	private final Hashtable defaultHeaders = new Hashtable();
	
	public HNClient() {
		defaultHeaders.put("x-requested-with", "HTTPClient Lual / 0.1");
	}
	
	public Vector fetchStories(String page) {
		HTTPResponse response = httpClient.sendRequest(BASE_URL + "/" + page, "GET", defaultHeaders, null);
		String json = response.getBody();
		Vector parsedStories = JSONParser.parseArray(json);
		
		Vector hnItems = new Vector();
		Enumeration enumeration = parsedStories.elements();
		while(enumeration.hasMoreElements()) {
			Hashtable item = (Hashtable) enumeration.nextElement();
			hnItems.addElement(HNItem.fromHashtable(item));
		}
		
		return hnItems;
	}
	
	public HNItem fetchItem(int id) {
		HTTPResponse response = httpClient.sendRequest(BASE_URL + "/item/" + id, "GET", defaultHeaders, null);
		String json = response.getBody();
		Hashtable parsedItem = JSONParser.parseObject(json);
		return HNItem.fromHashtable(parsedItem);
	}
}
