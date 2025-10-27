package com.bg;

import java.util.ArrayList;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

public class ListBoxComposer extends SelectorComposer<Window>{

	@Wire
	Listbox lb1,lb2;
	
	@Override
	public void doAfterCompose(Window comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
	
	
		String[][] fruits = new String[][] { 
				{"Apple", "150"},
				{"Mango", "80"},
				{"Pine Apple", "60"},
				{"Guava", "50"}
		};

	
	ListModelArray<String> lma = new ListModelArray(fruits);
	
	ArrayList<String[]> fruitList= new ArrayList<>();
	fruitList.add(new String[] {"Cherry"});
	ListModelList lml = new ListModelList(fruitList);
	lb1.setModel(lml);
	lb2.setModel(lma);
	
	}
	
}
