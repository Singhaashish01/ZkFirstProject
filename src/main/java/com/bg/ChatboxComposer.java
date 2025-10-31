package com.bg;



import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

public class ChatboxComposer extends SelectorComposer<Window>{
	
	@Wire
	Vbox vbox;
	
	@Wire
	Textbox uName, sendMessage;
	
	@Override
	public void doAfterCompose(Window comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		String host = java.net.InetAddress.getLocalHost().getHostName();
		uName.setValue(host);
		EventQueue<Event> eQue = EventQueues.lookup("chat", EventQueues.APPLICATION, true);
		eQue.subscribe(new EventListener<Event>() {
			
			@Override
			public void onEvent(Event e) throws Exception {
				Label l = new Label(e.getData().toString());
				l.setParent(vbox);
			}
		});
	}
	
//	@Subscribe(value="queue", scope = EventQueues.APPLICATION)
//	public void receive(Event e1) {
//		Object data = e1.getData();
//		alert((String)data);
//	}
	
	@Listen("onClick=#b1")
	public void send() {
		String text = uName.getValue()+":"+sendMessage.getValue();
		EventQueue<Event> EQue = EventQueues.lookup("chat", EventQueues.APPLICATION, true);
		Event e = new Event("onChat", null, text);
		EQue.publish(e);
	}

}
