package c.geonet.rlsilk.custom;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static Player fromJSON(JSONObject response){
        Player player = new  Player();
        try {
            if (response.has("displayName"))
                player.setName(response.getString("displayName"));
            if (response.has("avatar"))
                player.setAvatarUrl(response.getString("avatar"));
            if (response.has("updatedAt"))
                player.setUpdated(response.getLong("updatedAt"));
            if (response.has("nextUpdateAt"))
                player.setNextUpdate(response.getLong("nextUpdateAt"));
            if (response.has("stats")) {
                JSONObject stats = response.getJSONObject("stats");
                player.setWins(stats.getInt("wins"));
                player.setGoals(stats.getInt("goals"));
                player.setMvps(stats.getInt("mvps"));
                player.setSaves(stats.getInt("saves"));
                player.setShots(stats.getInt("shots"));
                player.setAssists(stats.getInt("assists"));
            }
            if (response.has("profileUrl"))
                player.setProfileUrl(response.getString("profileUrl"));
            if (response.has("rankedSeasons")) {
                JSONObject season = response.getJSONObject("rankedSeasons");
                int i = 0;
                while (season.has("" + i + 1))
                    i++;
                season = season.getJSONObject("" + i);
                JSONObject duel = season.getJSONObject("10");
                JSONObject duo = season.getJSONObject("11");
                JSONObject solo = season.getJSONObject("12");
                JSONObject standard = season.getJSONObject("13");
                player.currentSeason().setStats(
                        new Ranking(solo.getInt("rankedPoints"), solo.getInt("matchesPlayed"), solo.getInt("tier"), solo.getInt("division"))
                        , new Ranking(duel.getInt("rankedPoints"), duel.getInt("matchesPlayed"), duel.getInt("tier"), duel.getInt("division"))
                        , new Ranking(duo.getInt("rankedPoints"), duo.getInt("matchesPlayed"), duo.getInt("tier"), duo.getInt("division"))
                        , new Ranking(standard.getInt("rankedPoints"), standard.getInt("matchesPlayed"), standard.getInt("tier"), standard.getInt("division"))
                        , player.getUpdated());
            }
        }catch (JSONException error){

        }finally{
            return player;
        }
    }

    public Player mergeWithPlayer(Player recent){
        for(int i = 0; i< recent.getSeasons().size();i++) {
            recent.getSeasons().get(i).getSolo().putAll(this.seasons.get(i).getSolo());
        }
        return recent;
    }
}
