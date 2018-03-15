package c.geonet.rlsilk.custom;

/**
 * Created by Ravi on 15.03.2018.
 */

public class Ranking {
    private int mmr;
    private int tier;
    private int div;
    private int played;

    public Ranking(int mmr,int played ,int tier, int div) {
        this.played = played;
        this.mmr = mmr;
        this.tier = tier;
        this.div = div;
    }

    public int getMmr() {
        return mmr;
    }

    public void setMmr(int mmr) {
        this.mmr = mmr;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public int getDiv() {
        return div;
    }

    public void setDiv(int div) {
        this.div = div;
    }
}
