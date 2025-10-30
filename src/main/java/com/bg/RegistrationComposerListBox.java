package com.bg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.bg.dbconnection.DBConnection;

public class RegistrationComposerListBox extends SelectorComposer<Window> {

	@Wire
	private Textbox name, email, phone, username, password;

	@Wire
	private Datebox dob;

	@Wire
	private Button registerBtn, resetBtn;
	
	@Listen("onClick = #registerBtn")
	public void submitForm() {

		String tempName = name.getValue();

		String tempEmail = email.getValue();

		String tempPhone = phone.getValue();

		String tempUserName = username.getValue();

		String tempPassword = password.getValue();

		Date value = dob.getValue();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String tempDob = sdf.format(value);
		System.out.println(tempDob);
		
		Connection conn = DBConnection.getConnection();
		
		String query = "insert into users(full_name, data_of_birth, email, phone, username, password)"
				+ " values(?,?,?,?,?,?)";
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, tempName);
			
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		Executions.sendRedirect("/listbox.zul");
	}

}
