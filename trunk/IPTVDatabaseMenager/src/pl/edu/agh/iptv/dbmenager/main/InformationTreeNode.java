package pl.edu.agh.iptv.dbmenager.main;

import javax.swing.tree.DefaultMutableTreeNode;

public class InformationTreeNode extends DefaultMutableTreeNode{

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
