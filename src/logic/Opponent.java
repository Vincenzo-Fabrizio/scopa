package logic;

import java.util.*;

public class Opponent extends Player {

    /**
     * Strategia con minimax (sperimentale).
     */
    public Card playTurn(List<Card> tavolo, List<Card> playerHand) {
        Card bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        for (Card move : getMano()) {
            List<Card> newTavolo = new ArrayList<>(tavolo);
            newTavolo.add(move);
            int value = minimax(newTavolo, new ArrayList<>(getMano()), playerHand, 2, false);
            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }

        getMano().remove(bestMove);
        return bestMove;
    }

    /**
     * Algoritmo minimax ricorsivo.
     */
    private int minimax(List<Card> tavolo, List<Card> oppHand, List<Card> playerHand, int depth, boolean maximizing) {
        if (depth == 0 || oppHand.isEmpty() || playerHand.isEmpty()) {
            return valutaStato(tavolo, oppHand, playerHand);
        }

        if (maximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Card move : oppHand) {
                List<Card> newTavolo = new ArrayList<>(tavolo);
                newTavolo.add(move);
                int eval = minimax(newTavolo, oppHand, playerHand, depth - 1, false);
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Card move : playerHand) {
                List<Card> newTavolo = new ArrayList<>(tavolo);
                newTavolo.add(move);
                int eval = minimax(newTavolo, oppHand, playerHand, depth - 1, true);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }

    /**
     * Euristica per valutare lo stato della partita.
     */
    private int valutaStato(List<Card> tavolo, List<Card> oppHand, List<Card> playerHand) {
        int score = 0;

        // Esempio di euristica:
        score += oppHand.size() - playerHand.size();
        for (Card c : oppHand) {
            if (c.getSeme() == Seme.SEME_DENARI && c.getValore() == 7) score += 5; // settebello
            if (c.getValore() == 1) score += 2; // assi
        }

        return score;
    }

    /**
     * Espone la mano in lettura.
     */
    public List<Card> getMano() {
        return super.getMano(); // usa un getter nella classe padre
    }
    
}

