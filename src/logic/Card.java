package logic;

public class Card {
    private Seme seme;
    private int valore;
    
    public Card(Seme seme, int valore) {
        this.seme = seme;
        this.valore = valore;
    }

    public Seme getSeme() {
        return seme;
    }

    public void setSeme(Seme seme) {
        this.seme = seme;
    }

    public int getValore() {
        return valore;
    }

    public void setValore(int valore) {
        this.valore = valore;
    }

    @Override
    public String toString() {
        return valore + " di " + seme;
    }

    public boolean isCoppe() {
        if(this.seme.compareTo(Seme.SEME_COPPE) == 0) return true;
        return false;
    }

    public boolean isDenari() {
        if(this.seme.compareTo(Seme.SEME_DENARI) == 0) return true;
        return false;
    }

    public boolean isBastoni() {
        if(this.seme.compareTo(Seme.SEME_BASTONI) == 0) return true;
        return false;
    }

    public boolean isSpade() {
        if(this.seme.compareTo(Seme.SEME_SPADE) == 0) return true;
        return false;
    }

    public boolean isSetteBello() {
        if(this.seme.compareTo(Seme.SEME_DENARI) == 0 && this.valore == 7) return true;
        return false;
    }

    public boolean isSette() {
        if(
            (
                this.seme.compareTo(Seme.SEME_COPPE) == 0 ||
                this.seme.compareTo(Seme.SEME_BASTONI) == 0 ||
                this.seme.compareTo(Seme.SEME_SPADE) == 0
            ) && this.valore == 7
        ) return true;
        return false;
    }

    public boolean isSei() {
        if(
            (
                this.seme.compareTo(Seme.SEME_COPPE) == 0 ||
                this.seme.compareTo(Seme.SEME_DENARI) == 0 ||
                this.seme.compareTo(Seme.SEME_BASTONI) == 0 ||
                this.seme.compareTo(Seme.SEME_SPADE) == 0
            ) && this.valore == 6
        ) return true;
        return false;        
    }

}
