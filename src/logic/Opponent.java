package logic;

import java.util.*;

public class Opponent extends Player {

    /**
     * Gioca la mossa migliore possibile.
     * Se possibile prende carte, altrimenti scarta una carta.
     */
    public boolean giocaMossa(
            ArrayList<Card> mano,
            ArrayList<Card> tavolo,
            ArrayList<Card> prese,
            Player avversario) {

        boolean haPreso = false;
        int punteggioMigliore = Integer.MIN_VALUE;
        ArrayList<Card> migliorMossa = new ArrayList<>();

        for (Card carta : mano) {
            for (ArrayList<Card> combinazione : trovaCombinazioni(carta, tavolo)) {
                int punteggio = valutaCombinazione(combinazione, mano, tavolo, prese, avversario, 2);
                if (punteggio > punteggioMigliore) {
                    punteggioMigliore = punteggio;
                    migliorMossa = new ArrayList<>(combinazione);
                }
            }
        }

        if (!migliorMossa.isEmpty()) {
            prese.addAll(migliorMossa);
            mano.removeAll(migliorMossa);
            tavolo.removeAll(migliorMossa.subList(1, migliorMossa.size())); // rimuove carte dal tavolo
            System.out.println("L'avversario ha preso: " + migliorMossa);
            haPreso = true;
        } else {
            Card cartaScartata = mano.remove(new Random().nextInt(mano.size()));
            tavolo.add(cartaScartata);
            System.out.println("L'avversario ha scartato: " + cartaScartata);
        }

        return haPreso;
    }

    /**
     * Valuta una combinazione considerando anche le mosse future (fino a profondità).
     */
    private int valutaCombinazione(
            ArrayList<Card> combinazione,
            ArrayList<Card> mano,
            ArrayList<Card> tavolo,
            ArrayList<Card> prese,
            Player avversario,
            int profondità) {

        // stato dopo la mossa
        ArrayList<Card> nuovaMano = new ArrayList<>(mano);
        ArrayList<Card> nuovoTavolo = new ArrayList<>(tavolo);
        ArrayList<Card> nuovePrese = new ArrayList<>(prese);

        nuovaMano.removeAll(combinazione);
        nuovoTavolo.removeAll(combinazione.subList(1, combinazione.size()));
        nuovePrese.addAll(combinazione);

        int punteggioAttuale = sommaPunti(nuovePrese);

        // caso base: profondità = 0
        if (profondità <= 0 || nuovaMano.isEmpty()) {
            return punteggioAttuale;
        }

        // simulazione mossa successiva dell'avversario
        int peggiorRisposta = Integer.MAX_VALUE;
        for (Card carta : nuovaMano) {
            for (ArrayList<Card> risposta : trovaCombinazioni(carta, nuovoTavolo)) {
                int punteggioRisposta = valutaCombinazione(
                        risposta, nuovaMano, nuovoTavolo, nuovePrese, avversario, profondità - 1
                );
                peggiorRisposta = Math.min(peggiorRisposta, punteggioRisposta);
            }
        }

        return punteggioAttuale - (peggiorRisposta == Integer.MAX_VALUE ? 0 : peggiorRisposta);
    }

    /**
     * Calcola i punti di una serie di carte prese.
     * Qui puoi implementare la tua logica di punteggio.
     */
    private int sommaPunti(ArrayList<Card> carte) {
        int punti = 0;
        for (Card c : carte) {
            punti += c.getValore(); // puoi aggiungere altre regole
        }
        return punti;
    }

    /**
     * Trova tutte le combinazioni valide tra una carta in mano e le carte sul tavolo.
     * In questa versione considera solo abbinamenti 1:1 (stesso valore).
     */
    public static ArrayList<ArrayList<Card>> trovaCombinazioni(Card carta, ArrayList<Card> tavolo) {
        ArrayList<ArrayList<Card>> combinazioni = new ArrayList<>();
        for (Card c : tavolo) {
            if (c.getValore() == carta.getValore()) {
                ArrayList<Card> abbinamento = new ArrayList<>();
                abbinamento.add(carta);
                abbinamento.add(c);
                combinazioni.add(abbinamento);
            }
        }
        return combinazioni;
    }

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

