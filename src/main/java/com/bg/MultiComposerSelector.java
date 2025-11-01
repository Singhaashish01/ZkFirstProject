package com.bg;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

public class MultiComposerSelector extends SelectorComposer<Window> implements EventListener<Event>{

	private Window comp;
	
	@Wire
	Button btn1,btn2, btn3, btn4, b1, b2, b3,b4; 
	@Override
	public void doAfterCompose(Window comp) throws Exception {
		// TODO Auto-generated method stub
		
		super.doAfterCompose(comp);
		btn2.addEventListener(Events.ON_CLICK, this);
		btn3.addEventListener(Events.ON_CLICK, null);
				new java.util.EventListener() {
				};
				
		
	}
	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
