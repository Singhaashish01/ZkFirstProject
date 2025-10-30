package com.bg;

import java.util.Arrays;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

public class TicTacToe extends SelectorComposer<Window> {

//	@Wire
//	Button b1, b2, b3, b4, b5, b6, b7, b8, b9;
//	Boolean flag = true;
//
//	@Listen("onClick = button")
//	public void gameOn(Event e) {
//		Button b = (Button) e.getTarget();
//
//		b.setLabel(flag ? "X" : "0");
//		flag = !flag;
//		b.setDisabled(true);
//		if (b1.getLabel() == b2.getLabel() && b1.getLabel() == b3.getLabel() && b1.getLabel() != ""
//				|| b4.getLabel() == b5.getLabel() && b4.getLabel() == b6.getLabel() && b4.getLabel() != ""
//				|| b7.getLabel() == b8.getLabel() && b7.getLabel() == b9.getLabel() && b7.getLabel() != ""
//				|| b1.getLabel() == b4.getLabel() && b1.getLabel() == b7.getLabel() && b1.getLabel() != ""
//				|| b2.getLabel() == b5.getLabel() && b2.getLabel() == b8.getLabel() && b2.getLabel() != ""
//				|| b3.getLabel() == b6.getLabel() && b3.getLabel() == b9.getLabel() && b3.getLabel() != ""
//				|| b1.getLabel() == b5.getLabel() && b1.getLabel() == b9.getLabel() && b1.getLabel() != ""
//				|| b3.getLabel() == b5.getLabel() && b3.getLabel() == b7.getLabel() && b3.getLabel() != "")
//			alert(b.getLabel() + " Won the game");
//	}
	
	
	private static TicTacToe instance = new TicTacToe();

    private String[] board = new String[9];
    private String currentPlayer = "X";
    private int players = 0;
    private boolean gameOver = false;

    private TicTacToe() {
        Arrays.fill(board, "");
    }

    public static TicTacToe getInstance() {
        return instance;
    }

    public synchronized void reset() {
        Arrays.fill(board, "");
        currentPlayer = "X";
        gameOver = false;
    }

    public synchronized boolean makeMove(int index, String symbol) {
        if (gameOver || !board[index].equals("") || !symbol.equals(currentPlayer))
            return false;

        board[index] = symbol;
        if (checkWin(symbol)) {
            gameOver = true;
        } else {
            currentPlayer = currentPlayer.equals("X") ? "O" : "X";
        }
        return true;
    }

    private boolean checkWin(String s) {
        int[][] winPositions = {
                {0,1,2},{3,4,5},{6,7,8},
                {0,3,6},{1,4,7},{2,5,8},
                {0,4,8},{2,4,6}
        };
        for (int[] pos : winPositions)
            if (board[pos[0]].equals(s) && board[pos[1]].equals(s) && board[pos[2]].equals(s))
                return true;
        return false;
    }

    public String[] getBoard() { return board; }
    public String getCurrentPlayer() { return currentPlayer; }
    public boolean isGameOver() { return gameOver; }

    public synchronized int addPlayer() {
        if (players < 2) {
            players++;
            return players;
        }
        return 3; // spectator
    }

    public synchronized void removePlayer() {
        if (players > 0) players--;
    }
	
}
