package c.geonet.rlsilk.custom;

import java.util.ArrayList;

/**
 * Created by Ravi on 15.03.2018.
 */

public class Player {
    private String id;
    private String name;
    private int platform;
    private String avatarUrl;
    private String profileUrl;
    private int wins;
    private int goals;
    private int mvps;
    private int saves;
    private int shots;
    private int assists;
    private ArrayList<Season> seasons;
    private long updated;
    private long nextUpdate;

    public Season currentSeason(){
        return seasons.get(seasons.size()-1);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPlatform() {
        return platform;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public int getWins() {
        return wins;
    }

    public int getGoals() {
        return goals;
    }

    public int getMvps() {
        return mvps;
    }

    public int getSaves() {
        return saves;
    }

    public int getShots() {
        return shots;
    }

    public int getAssists() {
        return assists;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public long getUpdated() {
        return updated;
    }

    public long getNextUpdate() {
        return nextUpdate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public void setMvps(int mvps) {
        this.mvps = mvps;
    }

    public void setSaves(int saves) {
        this.saves = saves;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public void setSeasons(ArrayList<Season> seasons) {
        this.seasons = seasons;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public void setNextUpdate(long nextUpdate) {
        this.nextUpdate = nextUpdate;
    }
}
