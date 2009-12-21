package pl.edu.agh.iptv.dbmenager.usertab;

import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.table.AbstractTableModel;

import pl.edu.agh.iptv.persistence.User;

public class UserTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = -8388963088149738389L;

	private List<User> userList;
	
	private EntityManager em;
	
	private String[] columnNames = { "Id", "Sip", "Credit"};


	@Override
	public String getColumnName(int number) {
		return columnNames[number];
	};
	
	public UserTableModel(List<User> userList, EntityManager em) {
		this.userList = userList;
		this.em = em;
	}
	
	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return userList.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		User user = userList.get(row);
		String value = null;
		switch(column) {
		case 0:
			value = user.getId().toString();
			break;
		case 1:
			value = user.getSip();
			break;
		case 2:
			value = String.valueOf(user.getCredit());
			break;
		}
		
		return value;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		boolean value = true;
		if (columnIndex == 0) {
			value = false;
		}
		return value;
	}

	
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		User user = userList.get(rowIndex);
		String strValue = null;
		if (value instanceof String) {
			strValue = (String) value;
		}
		em.getTransaction().begin();
		em.persist(user);
		switch (columnIndex) {
		case 1:
			user.setSip(strValue);
			break;
		case 2:
			user.setCredit(new Long(strValue));
			break;
		}
		
		em.getTransaction().commit();
	}

}
