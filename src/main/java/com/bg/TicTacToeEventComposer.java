package com.bg;

	import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
	import org.zkoss.zk.ui.event.Event;
	import org.zkoss.zk.ui.select.SelectorComposer;
	import org.zkoss.zk.ui.select.annotation.Listen;
	import org.zkoss.zk.ui.select.annotation.Wire;
	import org.zkoss.zk.ui.util.Clients;
	import org.zkoss.zul.Button;
	import org.zkoss.zul.Label;
	import org.zkoss.zul.Window;
	import org.zkoss.zk.ui.util.DesktopActivationListener;

	public class TicTacToeEventComposer extends SelectorComposer<Window> implements DesktopActivationListener {
	    private TicTacToe game = TicTacToe.getInstance();
	    private String playerSymbol;
	    private Thread updater;
	    
	    @Wire
	    private Label status;

	    @Wire
	    private Button cell0, cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8;

	    private Button[] cells;

	    @Override
	    public void doAfterCompose(Window comp) throws Exception {
	        super.doAfterCompose(comp);
	        Executions.getCurrent().getDesktop().enableServerPush(true);
	        cells = new Button[]{cell0, cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8};

	        int role = game.addPlayer();
	        if (role == 1) playerSymbol = "X";
	        else if (role == 2) playerSymbol = "O";
	        else playerSymbol = "Spectator";

	        status.setValue("You are " + playerSymbol);

	        startUpdater();
	    }

	    private void startUpdater() {
	        updater = new Thread(() -> {
	            while (Executions.getCurrent().getDesktop().isAlive()) {
	                try {
	                    Executions.schedule((Desktop) Executions.getCurrent(), e -> updateBoard(), null);
	                    Thread.sleep(1000);
	                } catch (InterruptedException ex) {
	                    break;
	                }
	            }
	        });
	        updater.start();
	    }

	    private void updateBoard() {
	        String[] b = game.getBoard();
	        for (int i = 0; i < cells.length; i++) {
	            cells[i].setLabel(b[i]);
	        }

	        if (game.isGameOver()) {
	            Clients.showNotification("Game Over!");
	        } else {
	            status.setValue("Turn: " + game.getCurrentPlayer());
	        }
	    }

	    @Listen("onClick = button")
	    public void onCellClick(Event event) {
	        if (playerSymbol.equals("Spectator")) {
	            Clients.showNotification("Spectators can't play!");
	            return;
	        }

	        Button clicked = (Button) event.getTarget();
	        int index = Integer.parseInt(clicked.getId().replace("cell", ""));

	        if (game.makeMove(index, playerSymbol)) {
	            updateBoard();
	        } else {
	            Clients.showNotification("Invalid Move!");
	        }
	    }

	    @Override
	    public void didActivate(org.zkoss.zk.ui.Desktop desktop) {}

	    public void didDeactivate(org.zkoss.zk.ui.Desktop desktop) {
	        game.removePlayer();
	        updater.interrupt();
	    }

		@Override
		public void willPassivate(Desktop arg0) {
			// TODO Auto-generated method stub
			
		}
	}

