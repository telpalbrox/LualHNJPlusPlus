import com.ms.wfc.app.*;
import com.ms.wfc.core.*;
import com.ms.wfc.ui.*;
import com.ms.wfc.html.*;
import java.util.Vector;
import java.util.Enumeration;
import HNClient;
import HNItem;
import HNItemForm;

/**
 * This class can take a variable number of parameters on the command
 * line. Program execution begins with the main() method. The class
 * constructor is not invoked unless an object of type 'Form1' is
 * created in the main() method.
 */
public class MainHNForm extends Form
{
	private Vector storyHNItems = null;
	public MainHNForm()
	{
		// Required for Visual J++ Form Designer support
		initForm();		

		// TODO: Add any constructor code after initForm call
		postConstruct();
	}
	
	private void postConstruct() {
		statusBar.setText("Fetching stories");
		Thread thread = new Thread(new Runnable() {
			public void run() {
				getInitialStories();
			}
		});
		thread.start();
	}
	
	private void getInitialStories() {
		HNClient hnClient = new HNClient();
		Vector hnItems = hnClient.fetchStories("news");
		System.out.println(hnItems);
		storyHNItems = hnItems;
		invokeAsync(new MethodInvoker(onFinishLoadingStories));
	}
	
	private void onFinishLoadingStories() {
		this.storiesListView.addColumn("Lual HN Win32 J 0.1.0", 500, HorizontalAlignment.LEFT);
		statusBar.setText("Finished fetching stories");
		Enumeration hnItemsEnumeration = storyHNItems.elements();
		while(hnItemsEnumeration.hasMoreElements()) {
			HNItem item = (HNItem) hnItemsEnumeration.nextElement();
			ListItem listItem = new ListItem(item.getTitle());
			this.storiesListView.addItem(listItem);
		}
	}

	/**
	 * Form1 overrides dispose so it can clean up the
	 * component list.
	 */
	public void dispose()
	{
		super.dispose();
		components.dispose();
	}

	private void storiesListView_click(Object source, Event e)
	{
		ListItem[] selectedItems = this.storiesListView.getSelectedItems();
		if (selectedItems.length == 0) {
			return;
		}
		String selectedTitle = selectedItems[0].getText();
		System.out.println(selectedTitle);
		HNItem selectedItem = this.findItemByTitle(selectedTitle);
		if (selectedItem == null) {
			return;
		}
		System.out.println(selectedItem);
		HNItemForm itemForm = new HNItemForm(selectedItem);
		itemForm.setVisible(true);
	}
	
	private HNItem findItemByTitle(String title) {
		Enumeration hnItemsEnumeration = storyHNItems.elements();
		while(hnItemsEnumeration.hasMoreElements()) {
			HNItem item = (HNItem) hnItemsEnumeration.nextElement();
			if (item.getTitle().equals(title)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * NOTE: The following code is required by the Visual J++ form
	 * designer.  It can be modified using the form editor.  Do not
	 * modify it using the code editor.
	 */
	Container components = new Container();
	StatusBar statusBar = new StatusBar();
	ListView storiesListView = new ListView();

	private void initForm()
	{
		this.setText("Lual HN J");
		this.setAutoScaleBaseSize(new Point(5, 13));
		this.setClientSize(new Point(492, 405));

		statusBar.setBackColor(Color.CONTROL);
		statusBar.setLocation(new Point(0, 381));
		statusBar.setSize(new Point(492, 24));
		statusBar.setTabIndex(0);
		statusBar.setText("Status");

		storiesListView.setDock(ControlDock.FILL);
		storiesListView.setSize(new Point(492, 381));
		storiesListView.setTabIndex(1);
		storiesListView.setText("listView1");
		storiesListView.setAutoArrange(true);
		storiesListView.setHeaderStyle(ColumnHeaderStyle.NONCLICKABLE);
		storiesListView.setMultiSelect(false);
		storiesListView.setView(ViewEnum.REPORT);
		storiesListView.addOnDoubleClick(new EventHandler(this.storiesListView_click));

		this.setNewControls(new Control[] {
							storiesListView, 
							statusBar});
	}

	/**
	 * The main entry point for the application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
	public static void main(String args[])
	{
		Application.run(new MainHNForm());
	}
}
