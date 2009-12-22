package pl.edu.agh.iptv.view.chat.data;

public class Group implements Comparable<Group> {

	private String groupName;

	public Group(String name) {
		this.groupName = name;
	}

	@Override
	public int compareTo(Group o) {
		// TODO Auto-generated method stub
		if (this.groupName.equals(((Group) o).getGroupName()))
			return 0;
		else
			return -1;
	}

	public String getGroupName() {
		return this.groupName;
	}

}