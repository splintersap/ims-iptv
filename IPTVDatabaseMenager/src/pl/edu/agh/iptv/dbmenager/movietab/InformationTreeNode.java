package pl.edu.agh.iptv.dbmenager.movietab;

import javax.swing.tree.DefaultMutableTreeNode;

public class InformationTreeNode extends DefaultMutableTreeNode{

	private static final long serialVersionUID = -3426530917706827205L;
	
	public Object object;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public InformationTreeNode(Object arg0) {
		super(arg0);
	}
	
}
