package com.bg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.bg.dbconnection.DBConnection;

public class ListBoxJDBCComposer  extends SelectorComposer<Window>{

	@Wire
	private Listbox listbox;
	
	@Override
	public void doAfterCompose(Window comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		
		Connection connection = DBConnection.getConnection();
		
		String query = "select * from users";
		
		PreparedStatement stmt = connection.prepareStatement(query);
		
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<String[]> data = new ArrayList<>();
		
		while(rs.next()) {
			String[] records = new String[] {rs.getInt(1)+"", rs.getString(2),
					rs.getDate(3)+"", rs.getString(4), rs.getString(5), 
					rs.getString(6), rs.getString(7)};
			data.add(records);
		}
		
		ListModelList list = new ListModelList(data);
		
		listbox.setModel(list);
	}
}
