package com.bg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

public class StudentComposer extends SelectorComposer<Window> {

    @Wire
    private Listbox studentList;

    @Override
    public void doAfterCompose(Window window) throws Exception {
        super.doAfterCompose(window);
        loadStudents();
    }

    private void loadStudents() {
        String url = "jdbc:mysql://localhost:3306/Studentdb";
        String user = "root";
        String password = "root123";

        String query = "SELECT id, name, department FROM students";

        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");

            
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    Listitem item = new Listitem();
                    item.appendChild(new Listcell(String.valueOf(rs.getInt("id"))));
                    item.appendChild(new Listcell(rs.getString("name")));
                    item.appendChild(new Listcell(rs.getString("department")));
                    studentList.appendChild(item);
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
