package com.bg;

import java.util.Collection;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

public class IDSpaceComposer extends SelectorComposer<Window>{

	@Wire
	Button b2;
	
	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		b2.setLabel("CLICK");
		Button bb = (Button)b2.getFellow("b3");
		Collection<Component> fellowList = b2.getFellows();
		for(Component c : fellowList) {
			System.out.println(c.getClass()+",id="+c.getId());
		}
	//	bb.setLabel("Priyanshu");
		bb.getSpaceOwner();
	}
}
