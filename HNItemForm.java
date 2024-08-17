import com.ms.wfc.app.*;
import com.ms.wfc.core.*;
import com.ms.wfc.ui.*;
import com.ms.wfc.html.*;
import java.util.Vector;
import java.util.Enumeration;
import HNItem;
import HNClient;

/**
 * This class can take a variable number of parameters on the command
 * line. Program execution begins with the main() method. The class
 * constructor is not invoked unless an object of type 'HNItemForm'
 * created in the main() method.
 */
public class HNItemForm extends Form
{
	HNItem formHNItem = null;
	TreeNode rootCommentNode = null;
	
	public HNItemForm(HNItem sourceItem)
	{
		super();

		// Required for Visual J++ Form Designer support
		initForm();
		
		this.setText(sourceItem.getTitle());

		// TODO: Add any constructor code after initForm call
		this.postConstruct(sourceItem);
	}
	
	private void postConstruct(final HNItem sourceItem) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				fetchItem(sourceItem.getId());
				buildTreeNodeComments();
			}
		});
		thread.start();
	}
	
	private void fetchItem(int id) {
		HNClient hnClient = new HNClient();
		this.formHNItem = hnClient.fetchItem(id);
	}
	
	private void buildTreeNodeComments() {
		TreeNode root = new TreeNode(this.formHNItem.getTitle());
		Vector comments = formHNItem.getComments();
		addCommentsToNode(comments, root);
		this.rootCommentNode = root;
		this.invokeAsync(new MethodInvoker(this.onFinishBuildingTreeNode));
	}
	
	private void addCommentsToNode(Vector comments, TreeNode node) {
		Enumeration commentsEnumeration = comments.elements();
		while(commentsEnumeration.hasMoreElements()) {
			HNItem comment = (HNItem) commentsEnumeration.nextElement();
			TreeNode commentNode = new TreeNode(comment.getContent());
			addCommentsToNode(comment.getComments(), commentNode);
			node.addNode(commentNode);
		}
	}
	
	private void onFinishBuildingTreeNode() {
		if (this.rootCommentNode == null) {
			System.err.println("root commenet node null");
			return;
		}
		this.rootCommentNode.expand();
		this.commentsTreeView.addNode(this.rootCommentNode);
	}

	/**
	 * HNItemForm overrides dispose so it can clean up the
	 * component list.
	 */
	public void dispose()
	{
		super.dispose();
		components.dispose();
	}

	/**
	 * NOTE: The following code is required by the Visual J++ form
	 * designer.  It can be modified using the form editor.  Do not
	 * modify it using the code editor.
	 */
	Container components = new Container();
	TreeView commentsTreeView = new TreeView();

	private void initForm()
	{
		this.setText("HNItemForm");
		this.setAutoScaleBaseSize(new Point(5, 13));
		this.setClientSize(new Point(492, 384));

		commentsTreeView.setDock(ControlDock.FILL);
		commentsTreeView.setSize(new Point(492, 384));
		commentsTreeView.setTabIndex(0);
		commentsTreeView.setText("treeView1");
		commentsTreeView.setIndent(19);

		this.setNewControls(new Control[] {
							commentsTreeView});
	}

}
