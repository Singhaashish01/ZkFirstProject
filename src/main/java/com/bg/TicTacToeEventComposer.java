package com.bg;

import java.io.Serializable;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * Tic-Tac-Toe using ZK EventQueue (shared game across sessions).
 */
public class TicTacToeEventComposer extends SelectorComposer<Window> {

    private static final long serialVersionUID = 1L;

    @Wire
    private Button cell0, cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8;
    @Wire 
    private Label status;
    
    @Wire
    private Textbox txtBoxOTP;
    
    @Wire
    private Button btnOtp;
    
    
    private static boolean xTurn = true;
    private static boolean gameOver = false;
    private static final String[] board = new String[9];
 // === OTP and Player Management ===
    private static String sharedOTP = null;
    private static int playerCount = 0;
    private boolean isVerified = false;

    // Shared application-level queue
    private static EventQueue<Event> gameQueue;

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        // Lookup (create if missing) a shared queue visible to all sessions
        gameQueue = EventQueues.lookup("tictactoeQueue", EventQueues.APPLICATION, true);

        // Subscribe: listen for moves or resets published by any instance
        gameQueue.subscribe(event -> {
            try {
                if ("onMove".equals(event.getName())) {
                    Move move = (Move) event.getData();
                    applyMove(move.index, String.valueOf(move.symbol));
                } else if ("onReset".equals(event.getName())) {
                    resetBoardUI();
                }
            } catch (Exception ex) {
                Clients.showNotification("Queue error: " + ex.getMessage(), "error", null, "middle_center", 3000);
            }
        });

        // Initialize board
        resetBoardUI();
    }

    // === Button Clicks ===
    @Listen("onClick = button")
    public void onCellClick(Event event) {
        if (gameOver) return;

        Object target = event.getTarget();
        if (!(target instanceof Button)) return;
        Button btn = (Button) target;
        String id = btn.getId();

        // Handle reset
        if ("resetBtn".equals(id)) {
            publishReset();
            return;
        }

        // Handle cell buttons
        if (id == null || !id.startsWith("cell")) return;
        int index;
        try {
            index = Integer.parseInt(id.substring(4));
        } catch (NumberFormatException e) {
            return;
        }

        if (board[index] != null) return;

        String symbol = xTurn ? "X" : "O";
        publishMove(index, symbol.charAt(0));  // publish to queue
    }

    // === Queue publishing ===
    private void publishMove(int index, char symbol) {
        if (gameQueue != null) {
            gameQueue.publish(new Event("onMove", null, new Move(index, symbol)));
        } else {
            applyMove(index, String.valueOf(symbol));
        }
    }

    private void publishReset() {
        if (gameQueue != null) {
            gameQueue.publish(new Event("onReset", null, null));
        } else {
            resetBoardUI();
        }
    }

    
    private void applyMove(int index, String symbol) {
        Button btn = getButton(index);
        if (btn == null) return;
        if (!btn.getLabel().isEmpty()) return; 

        btn.setLabel(symbol);
        btn.setStyle("color: " + ("X".equals(symbol) ? "#196d2;" : "#e53935;"));
        board[index] = symbol;

        if (checkWinner(symbol)) {
            status.setValue("🎉 Player " + symbol + " wins!");
            gameOver = true;
            disableRemainingButtons();
            return;
        }

        if (isDraw()) {
            status.setValue("🤝 It's a draw!");
            gameOver = true;
            disableRemainingButtons();
            return;
        }

        xTurn = !xTurn;
        status.setValue("Player " + (xTurn ? "X" : "O") + "'s turn");
    }

    // === Reset ===
    @Listen("onClick = #resetBtn")
    public void onRestart() {
        publishReset();
    }

    private void resetBoardUI() {
        for (int i = 0; i < 9; i++) {
            board[i] = null;
            Button b = getButton(i);
            if (b != null) {
                b.setLabel("");
                b.setDisabled(false);
                b.setStyle("color: black;");
            }
        }
        xTurn = true;
        gameOver = false;
        status.setValue("Player X's turn");
    }

    // === Helpers ===
    private boolean checkWinner(String player) {
        int[][] wins = {
            {0,1,2},{3,4,5},{6,7,8},
            {0,3,6},{1,4,7},{2,5,8},
            {0,4,8},{2,4,6}
        };
        for (int[] w : wins) {
            if (player.equals(board[w[0]]) &&
                player.equals(board[w[1]]) &&
                player.equals(board[w[2]])) {
                return true;
            }
        }
        return false;
    }

    private boolean isDraw() {
        for (String s : board) if (s == null) return false;
        return true;
    }

    private void disableRemainingButtons() {
        for (int i = 0; i < 9; i++) {
            Button b = getButton(i);
            if (b != null) b.setDisabled(true);
        }
    }

    private Button getButton(int index) {
        switch (index) {
            case 0: return cell0;
            case 1: return cell1;
            case 2: return cell2;
            case 3: return cell3;
            case 4: return cell4;
            case 5: return cell5;
            case 6: return cell6;
            case 7: return cell7;
            case 8: return cell8;
            default: return null;
        }
    }
    
    
    @Listen("onClick = #btnOtp")
    public void onOtpSubmit() {
        String enterOtp = txtBoxOTP.getValue().trim();
        if (enterOtp.isEmpty()) {
            Clients.showNotification("Please enter OTP", "warning", null, "middle_center", 2000);
            return;
        }

        // If already 2 players are verified
        if (playerCount >= 2 && !isVerified) {
            Clients.showNotification("Game room is full! Try later.", "error", null, "middle_center", 3000);
            return;
        }

        // First player sets the shared OTP
        if (sharedOTP == null) {
            sharedOTP = enterOtp;
            isVerified = true;
            playerCount++;
            Clients.showNotification("OTP accepted! Waiting for another player...", "info", null, "middle_center", 2500);
            disableGameBoard();
            return;
        }

        // Second player enters OTP
        if (sharedOTP.equals(enterOtp)) {
            isVerified = true;
            playerCount++;
            Clients.showNotification("✅ Both players verified! Game starting...", "info", null, "middle_center", 2500);
            enableGameBoard();
            publishReset(); // Start a fresh game
        } else {
            Clients.showNotification("❌ Invalid OTP! Please enter correct one.", "error", null, "middle_center", 3000);
        }
    }

    // Disable board until OTP verified
    private void disableGameBoard() {
        for (int i = 0; i < 9; i++) {
            Button b = getButton(i);
            if (b != null) b.setDisabled(true);
        }
        status.setValue("Waiting for second player to verify OTP...");
    }

    // Enable board once verified
    private void enableGameBoard() {
        for (int i = 0; i < 9; i++) {
            Button b = getButton(i);
            if (b != null) b.setDisabled(false);
        }
        status.setValue("Player X's turn");
    }

    // Optional: Reset game room manually if needed (when restarting everything)
    private static void resetGameRoom() {
        sharedOTP = null;
        playerCount = 0;
    }

    /** simple serializable move object */
    private static class Move implements Serializable {
        private static final long serialVersionUID = 1L;
        int index;
        char symbol;
        Move(int index, char symbol) { this.index = index; this.symbol = symbol; }
    }
}
