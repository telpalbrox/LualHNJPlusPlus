import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

public class HNItem
{
	private int id;
	private String title;
	private int points;
	private String user;
	private int time;
	private String timeAgo;
	private String type;
	private String url;
	private String domain;
	private String content;
	private Vector comments;
	
	public static HNItem fromHashtable(Hashtable table) {
		HNItem hnItem = new HNItem();
		hnItem.setId(((Integer) table.get("id")).intValue());
		hnItem.setTitle((String) table.get("title"));
		hnItem.setUser((String) table.get("user"));
		hnItem.setContent((String) table.get("content"));
		Vector comments = (Vector) table.get("comments");
		if (comments != null) {
			Vector commentItems = new Vector();
			Enumeration commentsEnumeration = comments.elements();
			while(commentsEnumeration.hasMoreElements()) {
				Hashtable item = (Hashtable) commentsEnumeration.nextElement();
				HNItem commentItem = HNItem.fromHashtable(item);
				commentItems.addElement(commentItem);
			}
			hnItem.setComments(commentItems);
		}
		return hnItem;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public void setTimeAgo(String timeAgo) {
		this.timeAgo = timeAgo;
	}
	
	public String toString() {
		return "HNItem{" + getId() + ", " + getTitle() + "}";
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setComments(Vector comments) {
		this.comments = comments;
	}
	
	public Vector getComments() {
		return comments;
	}
}
