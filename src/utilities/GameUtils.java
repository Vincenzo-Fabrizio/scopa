package utilities;

import javax.swing.*;
import logic.Card;
import logic.Opponent;
import logic.Player;
import logic.Score;
import logic.Seme;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class per giochi di carte.
 * Contiene metodi per creare e gestire un mazzo, calcolare combinazioni,
 * caricare immagini delle carte e determinare il vincitore.
 */
public final class GameUtils {

    // Impedisce la creazione di istanze
    private GameUtils() {}

    /**
     * Crea un mazzo di 40 carte napoletane.
     */
    public static ArrayList<Card> creaMazzo() {
        ArrayList<Card> mazzo = new ArrayList<>();
        for (Seme seme : Seme.values()) {
            for (int valore = 1; valore <= 10; valore++) {
                mazzo.add(new Card(seme, valore));
            }
        }
        return mazzo;
    }

    /**
     * Mischia il mazzo.
     */
    public static void mischiaMazzo(List<Card> mazzo) {
        Collections.shuffle(mazzo);
    }

    /**
     * Pesca la prima carta dal mazzo.
     */
    public static Card pescaCarta(List<Card> mazzo) {
        if (mazzo != null && !mazzo.isEmpty()) {
            return mazzo.remove(0);
        }
        return null;
    }

    /**
     * Mappa le carte del mazzo con le relative immagini ridimensionate.
     */
    public static Map<Card, ImageIcon> creaMappaImmagini(ArrayList<Card> mazzo, int larghezza, int altezza) {
        Map<Card, ImageIcon> mappa = new HashMap<>();

        for (Card carta : mazzo) {
            String seme = carta.getSeme().name().toLowerCase();
            String path = "/images/" + seme + carta.getValore() + ".png";

            try {
                // usa getResourceAsStream invece di getResource
                var inputStream = GameUtils.class.getResourceAsStream(path);
                if (inputStream == null) {
                    System.err.println("Immagine non trovata: " + path);
                    continue;
                }

                ImageIcon icon = new ImageIcon(inputStream.readAllBytes());
                icon = ridimensionaIcona(icon, larghezza, altezza);
                mappa.put(carta, icon);
            } catch (Exception e) {
                System.err.println("Errore nel caricamento immagine " + path + ": " + e.getMessage());
            }
        }

        return mappa;
    }

    /**
     * Ridimensiona un'icona alle dimensioni specificate.
     */
    public static ImageIcon ridimensionaIcona(ImageIcon icon, int larghezza, int altezza) {
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(larghezza, altezza, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    /**
     * Restituisce l'icona associata a una carta.
     */
    public static ImageIcon getIcona(Map<Card, ImageIcon> mappa, Card carta) {
        return mappa.get(carta);
    }

    /**
     * Determina il vincitore tra due giocatori in base ai punti.
     */
    public static String calcolaVincitore(Player user, Opponent avversario) {
        Score puntiGiocatore = user.infoPunteggioAttuale();
        Score puntiAvversario = avversario.infoPunteggioAttuale();

        int puntiG = 0;
        int puntiA = 0;

        if (puntiGiocatore.getCarte() > puntiAvversario.getCarte()) puntiG++;
        else if (puntiGiocatore.getCarte() < puntiAvversario.getCarte()) puntiA++;

        if (puntiGiocatore.getDenari() > puntiAvversario.getDenari()) puntiG++;
        else if (puntiGiocatore.getDenari() < puntiAvversario.getDenari()) puntiA++;

        if (puntiGiocatore.getPrimera() > puntiAvversario.getPrimera()) puntiG++;
        else if (puntiGiocatore.getPrimera() < puntiAvversario.getPrimera()) puntiA++;

        if (puntiGiocatore.getSetteBello() > puntiAvversario.getSetteBello()) puntiG++;
        else if (puntiGiocatore.getSetteBello() < puntiAvversario.getSetteBello()) puntiA++;

        puntiG += puntiGiocatore.getScope();
        puntiA += puntiAvversario.getScope();

        String messaggio;
        if (puntiG > puntiA) messaggio = "HAI VINTO!";
        else if (puntiG < puntiA) messaggio = "HAI PERSO!";
        else messaggio = "PARI!";

        messaggio += String.format(
                "%n%nGiocatore [PUNTI: %d] %s%nAvversario [PUNTI: %d] %s",
                puntiG, puntiGiocatore, puntiA, puntiAvversario);

        return messaggio;
    }

    /**
     * Sceglie la combinazione migliore di carte da prendere sul tavolo.
     * Restituisce la carta giocata + la migliore combinazione di carte.
     */
    public static ArrayList<Card> scegliMigliorCombinazione(Card cartaGiocata, ArrayList<Card> carteATerra) {
        ArrayList<ArrayList<Card>> tutteLeCombinazioni = generaCombinazioni(carteATerra);

        List<ArrayList<Card>> valide = tutteLeCombinazioni.stream()
                .filter(c -> sommaValori(c) == cartaGiocata.getValore())
                .collect(Collectors.toList());

        if (valide.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<Card> miglior = valide.stream()
                .max(Comparator
                        .comparingInt((List<Card> c) -> c.size()) // <---- qui specifica il tipo
                        .thenComparingLong(c -> c.stream().filter(Card::isDenari).count()))
                .orElse(new ArrayList<>());

        ArrayList<Card> risultato = new ArrayList<>();
        risultato.add(cartaGiocata);
        risultato.addAll(miglior);

        return risultato;
    }

    /**
     * Calcola la somma dei valori di una lista di carte.
     */
    private static int sommaValori(List<Card> carte) {
        return carte.stream().mapToInt(Card::getValore).sum();
    }

    /**
     * Genera tutte le combinazioni non vuote delle carte date.
     */
    private static ArrayList<ArrayList<Card>> generaCombinazioni(ArrayList<Card> carte) {
        ArrayList<ArrayList<Card>> combinazioni = new ArrayList<>();
        int n = carte.size();
        for (int i = 1; i < (1 << n); i++) {
            ArrayList<Card> combinazione = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) > 0) {
                    combinazione.add(carte.get(j));
                }
            }
            combinazioni.add(combinazione);
        }
        return combinazioni;
    }

    public static List<Card> trovaCombinazione(List<Card> tavolo, int target) {
        List<Card> migliore = new ArrayList<>();
        trovaCombinazioni(tavolo, target, new ArrayList<>(), migliore);
        return migliore;
    }

    private static void trovaCombinazioni(List<Card> tavolo, int target, List<Card> corrente, List<Card> migliore) {
        int somma = corrente.stream().mapToInt(Card::getValore).sum();
        if (somma == target && corrente.size() > migliore.size()) {
            migliore.clear();
            migliore.addAll(corrente);
        }
        if (somma >= target) return;

        for (int i = 0; i < tavolo.size(); i++) {
            List<Card> next = new ArrayList<>(corrente);
            next.add(tavolo.get(i));
            trovaCombinazioni(tavolo.subList(i + 1, tavolo.size()), target, next, migliore);
        }
    }

    /**
     * Carica l'immagine della carta.
     * Se `coperta` è true, restituisce il retro della carta.
     * @param c carta
     * @param coperta true per retro, false per faccia
     * @return immagine della carta
     */
    public static Image loadCardImage(Card c, boolean coperta) {
        String path;
        if (coperta) {
            path = "/images/retro.png"; // immagine retro
        } else {
            path = "/images/" + c.getSeme().name().toLowerCase() + c.getValore() + ".png";
        }

        ImageIcon icon = new ImageIcon(GameUtils.class.getResource(path));
        return icon.getImage();
    }
    
}
