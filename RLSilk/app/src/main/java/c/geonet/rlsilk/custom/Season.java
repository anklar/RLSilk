package c.geonet.rlsilk.custom;

import java.util.HashMap;

/**
 * Created by Ravi on 15.03.2018.
 */

public class Season {
    private int number;
    private HashMap<Long,Ranking> solo;
    private HashMap<Long,Ranking> duel;
    private HashMap<Long,Ranking> duo;
    private HashMap<Long,Ranking> standard;

    public Season(int number, Ranking solo, Ranking duel, Ranking duo, Ranking standard,long timestamp) {
        this.number = number;
        this.solo = new HashMap<>();
        this.solo.put(timestamp,solo);
        this.duel = new HashMap<>();
        this.duel.put(timestamp,duel);
        this.duo = new HashMap<>();
        this.duo.put(timestamp,duo);
        this.standard = new HashMap<>();
        this.standard.put(timestamp,standard);
    }

    public void setStats(Ranking solo,Ranking duel,Ranking duo,Ranking standard,long timestamp){
        setSolo(solo,timestamp);
        setDuel(duel,timestamp);
        setDuo(duo,timestamp);
        setStandard(standard,timestamp);
    }

    public int getNumber() {
        return number;
    }

    public HashMap<Long, Ranking> getSolo() {
        return solo;
    }

    public void setSolo(Ranking solo,long timestamp) {
        this.solo.put(timestamp,solo);
    }

    public HashMap<Long, Ranking> getDuel() {
        return duel;
    }

    public void setDuel(Ranking duel,long timestamp) {
        this.duel.put(timestamp,duel);
    }

    public HashMap<Long, Ranking> getDuo() {
        return duo;
    }

    public void setDuo(Ranking duo, long timestamp) {
        this.duo.put(timestamp,duo);
    }

    public HashMap<Long, Ranking> getStandard() {
        return standard;
    }

    public void setStandard(Ranking standard,long timestamp) {
        this.standard.put(timestamp,standard);
    }
}
