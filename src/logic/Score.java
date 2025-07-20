package logic;

import java.util.List;

public class Score {
    private int denari;
    private int carte;
    private int scope;
    private int primera;
    private int setteBello;
    private boolean settebelloPreso; 
    private int cartePrese;              
    private int denariPrese;            
    private int primieraPunteggio;   

    public Score() {
        this.denari = 0;
        this.carte = 0;
        this.scope = 0;
        this.primera = 0;
        this.setteBello = 0;
        this.settebelloPreso = false;
        this.cartePrese = 0;
        this.denariPrese = 0;
        this.primieraPunteggio = 0;
    }

    public int getDenari() {
        return denari;
    }

    public void setDenari(int denari) {
        this.denari = denari;
    }

    public int getCarte() {
        return carte;
    }

    public void setCarte(int carte) {
        this.carte = carte;
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public int getPrimera() {
        return primera;
    }

    public void setPrimera(int primera) {
        this.primera = primera;
    }

    public int getSetteBello() {
        return setteBello;
    }

    public void setSetteBello(int setteBello) {
        this.setteBello = setteBello;
    }

    public int sumTotalScore() {
        return (settebelloPreso ? 1 : 0) + cartePrese + scope + denariPrese + primieraPunteggio;
    }

    public void resetScorePartial() {
        this.denari = 0;
        this.carte = 0;
        this.primera = 0;
        this.setteBello = 0;
    }

    public void resetScoreTotal() {
        this.denari = 0;
        this.carte = 0;
        this.scope = 0;
        this.primera = 0;
        this.setteBello = 0;
    }

    @Override
    public String toString() {
        return 
                "Punti" 
                + "\ndenari = " + denari 
                + "\ncarte = " + carte 
                + "\nscope = " + scope 
                + "\nprimera = " + primera
                + "\nsette bello = " + setteBello;
    }

    public void aggiungiCarte(List<Card> prese) {
        cartePrese += prese.size();
        for (Card c : prese) {
            if (c.getSeme() == Seme.SEME_DENARI) {
                denariPrese++;
                if (c.getValore() == 7) {
                    settebelloPreso = true;
                }
            }
        }
    }

    public int calcolaPunteggioFinale(Score avversario) {
        int punti = scope;
        if (cartePrese > avversario.cartePrese) punti++;
        if (denariPrese > avversario.denariPrese) punti++;
        if (settebelloPreso) punti++;
        if (primieraPunteggio > avversario.primieraPunteggio) punti++;
        return punti;
    }

}
