package ui;

import logic.*;
import utilities.GameUtils;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class ScopaGui extends JFrame {

    private static final int X_CARTA = 80;
    private static final int Y_CARTA = 120;

    private JPanel pannelloGiocatore;
    private JPanel pannelloAvversario;
    private JPanel pannelloTavolo;
    private JTextArea logArea;

    private final Map<JButton, Card> buttonMap = new HashMap<>();
    private Map<Card, ImageIcon> mappaImmagini;

    private final ArrayList<Card> carteGiocatore = new ArrayList<>();
    private final ArrayList<Card> carteAvversario = new ArrayList<>();
    private final ArrayList<Card> tavolo = new ArrayList<>();
    private final List<Card> mazzo = new ArrayList<>();

    private final Player player = new Player();
    private final Opponent opponent = new Opponent();

    public ScopaGui() {
        setTitle("Scopa - Tavolo da Gioco");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        toFront();
        requestFocus();

        initComponents();
        preparaMazzo();
        distribuisciInizioPartita();
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));

        pannelloGiocatore = createHandPanel("Tua mano");
        pannelloAvversario = createHandPanel("Mano avversario");
        pannelloTavolo = new JPanel(new GridLayout(2, 5, 5, 5));
        pannelloTavolo.setBackground(new Color(0, 100, 0));
        pannelloTavolo.setBorder(BorderFactory.createTitledBorder("Carte sul tavolo"));

        JPanel pannelloCarte = new JPanel(new BorderLayout(5, 5));
        pannelloCarte.add(pannelloAvversario, BorderLayout.NORTH);
        pannelloCarte.add(pannelloTavolo, BorderLayout.CENTER);
        pannelloCarte.add(pannelloGiocatore, BorderLayout.SOUTH);

        logArea = new JTextArea(4, 20);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        logArea.setBackground(new Color(245, 245, 245));
        logArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(0, 120));

        add(pannelloCarte, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private JPanel createHandPanel(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel.setBackground(new Color(85, 107, 47));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }

    private void preparaMazzo() {
        mazzo.clear();
        mazzo.addAll(GameUtils.creaMazzo());
        GameUtils.mischiaMazzo(mazzo);
        mappaImmagini = GameUtils.creaMappaImmagini(new ArrayList<>(mazzo), X_CARTA, Y_CARTA);
    }

    private void distribuisciInizioPartita() {
        carteGiocatore.clear();
        carteAvversario.clear();
        tavolo.clear();

        for (int i = 0; i < 4; i++) {
            tavolo.add(mazzo.remove(0));
        }

        distribuisciNuovaMano();

        updateTavolo();
        updateManoAvversario();

        log("🎲 Partita iniziata! Tocca a te.");
        logStats();
    }

    private void updateTavolo() {
        pannelloTavolo.removeAll();
        for (Card carta : tavolo) {
            pannelloTavolo.add(new JLabel(GameUtils.getIcona(mappaImmagini, carta)));
        }
        pannelloTavolo.revalidate();
        pannelloTavolo.repaint();
    }

    private void updateManoGiocatore() {
        pannelloGiocatore.removeAll();
        buttonMap.clear();

        for (Card carta : carteGiocatore) {
            JButton btn = new JButton(GameUtils.getIcona(mappaImmagini, carta));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    giocaCartaGiocatore(btn, carta);
                }
            });
            buttonMap.put(btn, carta);
            pannelloGiocatore.add(btn);
        }

        pannelloGiocatore.revalidate();
        pannelloGiocatore.repaint();
    }

    private void updateManoAvversario() {
        pannelloAvversario.removeAll();
        ImageIcon retro = new ImageIcon(getClass().getResource("/images/retro.png"));
        Image img = retro.getImage().getScaledInstance(X_CARTA, Y_CARTA, Image.SCALE_SMOOTH);
        ImageIcon retroRidimensionato = new ImageIcon(img);

        for (int i = 0; i < carteAvversario.size(); i++) {
            pannelloAvversario.add(new JLabel(retroRidimensionato));
        }
        pannelloAvversario.revalidate();
        pannelloAvversario.repaint();
    }

    private void giocaCartaGiocatore(JButton btn, Card carta) {
        carteGiocatore.remove(carta);
        List<Card> presa = GameUtils.trovaCombinazione(tavolo, carta.getValore());

        if (!presa.isEmpty()) {
            player.getCartePrese().addAll(presa);
            tavolo.removeAll(presa);
            log("🏆 Hai preso: " + presa);
            if (tavolo.isEmpty()) player.addScopa();
        } else {
            tavolo.add(carta);
            log("👉 Hai giocato: " + carta);
        }

        pannelloGiocatore.remove(btn);
        updateTavolo();
        updateManoGiocatore();

        pannelloGiocatore.revalidate();
        pannelloGiocatore.repaint();

        logStats();

        giocaAvversario();

        if (carteGiocatore.isEmpty() && carteAvversario.isEmpty() && mazzo.isEmpty()) {
            finePartita();
        } else if (carteGiocatore.isEmpty()) {
            distribuisciNuovaMano();
        }
    }

    private void giocaAvversario() {
        boolean presa = opponent.giocaMossa(
            new ArrayList<>(carteAvversario),
            new ArrayList<>(tavolo),
            new ArrayList<>(opponent.getCartePrese()),
            player
        );

        if (!presa && !carteAvversario.isEmpty()) {
            Card scarta = carteAvversario.remove(0);
            tavolo.add(scarta);
            log("🤖 Avversario scarta: " + scarta);
        }

        updateManoAvversario();
        updateTavolo();
        logStats();
    }

    private void distribuisciNuovaMano() {
        carteGiocatore.clear();
        carteAvversario.clear();

        for (int i = 0; i < 3 && !mazzo.isEmpty(); i++) {
            carteGiocatore.add(mazzo.remove(0));
            carteAvversario.add(mazzo.remove(0));
        }

        updateManoGiocatore();
        updateManoAvversario();

        log("🃏 Nuova mano distribuita. Tocca a te.");
        logStats();
    }

    private void finePartita() {
        log("🎮 Partita finita!");
        String messaggio = GameUtils.calcolaVincitore(player, opponent);

        JOptionPane.showMessageDialog(this, messaggio, "Fine Partita", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private void logStats() {
        log(String.format(
            "📊 Carte prese: Giocatore=%d, Avversario=%d | Scope: Giocatore=%d, Avversario=%d",
            player.getCartePrese().size(), opponent.getCartePrese().size(),
            player.getPunteggio().getScope(), opponent.getPunteggio().getScope()
        ));
    }

    private void log(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

}