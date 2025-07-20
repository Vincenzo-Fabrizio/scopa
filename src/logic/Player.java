package logic;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List <Card> mano;
    private List <Card> cartePrese;
    private Score punteggio;

    public Player() {
        this.mano = new ArrayList<>();
        this.cartePrese = new ArrayList<>();
        this.punteggio = new Score();
    }

    public List<Card> getMano() {
        return mano;
    }

    public void setMano(List<Card> mano) {
        this.mano = mano;
    }

    public List<Card> getCartePrese() {
        return cartePrese;
    }

    public void setCartePrese(List<Card> cartePrese) {
        this.cartePrese = cartePrese;
    }

    public Score getPunteggio() {
        return punteggio;
    }

    public void setPunteggio(Score punteggio) {
        this.punteggio = punteggio;
    }

    public void resetPunteggio() {
        this.punteggio.resetScoreTotal();
    }

    public void addScopa() {
        this.punteggio.setScope(this.punteggio.getScope()+1);
        System.out.println("SCOPA!");
    }

    public Score infoPunteggioAttuale() {
        punteggio.resetScorePartial();
        for(Card c : this.cartePrese) {
            punteggio.setCarte(punteggio.getCarte()+1);
            if(c.isDenari()) punteggio.setDenari(punteggio.getDenari()+1);
            if(c.isSette()) punteggio.setPrimera(punteggio.getPrimera()+1);
            if(c.isSetteBello()) punteggio.setSetteBello(1);
        }
        return punteggio;
    }    




}
