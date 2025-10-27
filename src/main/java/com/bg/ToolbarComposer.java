package com.bg;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Toolbarbutton;

public class ToolbarComposer extends SelectorComposer<Panel>{

	@Wire
	Panelchildren panelchildren;
	
	public void onClick(Event e) {
		if(panelchildren.getFirstChild() instanceof Image) {
			panelchildren.removeChild(panelchildren.getFirstChild());
		}
		Toolbarbutton target=(Toolbarbutton)e.getTarget();
		String label = target.getLabel();
		panelchildren.appendChild(new Panel());
	}
}
