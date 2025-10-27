package com.bg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class LoginDatabase extends SelectorComposer<Window>{

	@Wire
	Textbox Username;
	@Wire
	Textbox Password;
	
	@Listen("onClick= #login")
	public void validateUser() {
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","root123");
					PreparedStatement st = con.prepareStatement("select * from users where username=? and password=?");
			st.setString(1,Username.getText());
			st.setString(2,Password.getText());
			ResultSet rs = st.executeQuery();
			
			if(rs.next()){
				alert("Login Successful");
			} else {
				alert("Unauthorised User");
			}
		} catch(Exception e){
			e.printStackTrace();
			alert(e.getMessage());
		}
	}
	@Listen("onClick= #reset")
	public void resetUser(){
		Username.setText("");
		Password.setText("");
	}
}
