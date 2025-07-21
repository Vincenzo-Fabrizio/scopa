package logic;

import java.util.*;

public class Opponent extends Player {

    private static final int MAX_DEPTH = 2; 

    /**
     * Strategia con minimax (sperimentale).
     */
    public Card playTurn(List<Card> tavolo, List<Card> playerHand) {
        Card bestMove = null;
        int bestValue = Integer.MIN_VALUE;
        for(Card move : getMano()) {
            List<Card> newTavolo = new ArrayList<>(tavolo);
            newTavolo.add(move);
            List<Card> newOppHand = new ArrayList<>(getMano());
            newOppHand.remove(move); // rimuovi la carta simulata
            int value = minimax(newTavolo, newOppHand, playerHand, MAX_DEPTH, false);
            if(value > bestValue) {
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
        if(depth == 0 || oppHand.isEmpty() || playerHand.isEmpty()) return valutaStato(tavolo, oppHand, playerHand);
        if(maximizing) {
            int maxEval = Integer.MIN_VALUE;
            for(Card move : oppHand) {
                List<Card> newTavolo = simulaMossa(tavolo, move);
                List<Card> newOppHand = new ArrayList<>(oppHand);
                newOppHand.remove(move);

                int eval = minimax(newTavolo, newOppHand, playerHand, depth - 1, false);
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        }else {
            int minEval = Integer.MAX_VALUE;
            for(Card move : playerHand) {
                List<Card> newTavolo = simulaMossa(tavolo, move);
                List<Card> newPlayerHand = new ArrayList<>(playerHand);
                newPlayerHand.remove(move);

                int eval = minimax(newTavolo, oppHand, newPlayerHand, depth - 1, true);
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
        score += oppHand.size() - playerHand.size();
        for(Card c : oppHand) {
            if(c.getSeme() == Seme.SEME_DENARI && c.getValore() == 7) {
                score += 5;
            }
            if(c.getValore() == 1) {
                score += 2; 
            }
        }
        return score;
    }

    /**
     * Espone la mano in lettura.
     */
    private List<Card> simulaMossa(List<Card> tavolo, Card move) {
        List<Card> newTavolo = new ArrayList<>(tavolo);
        newTavolo.add(move);
        return newTavolo;
    }
    
}

