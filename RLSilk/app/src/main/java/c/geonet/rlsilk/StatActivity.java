package c.geonet.rlsilk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import c.geonet.rlsilk.custom.Player;
import c.geonet.rlsilk.custom.Ranking;
import icepick.Icepick;

public class StatActivity extends AppCompatActivity {
    private RequestQueue req;
    private TextView text;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        Icepick.restoreInstanceState(this, savedInstanceState);
        ButterKnife.bind(this);
        req = Volley.newRequestQueue(this.getApplicationContext());
        text = findViewById(R.id.text);
        initDummy();
    }
    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public void updatePlayerStats(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://api.rocketleaguestats.com/v1/player?unique_id="+player.getId()+"&platform_id="+player.getPlatform(),null,
                (response)->{
                    try {
                        if (response.has("displayName"))
                            player.setName(response.getString("displayName"));
                        if (response.has("avatar"))
                            player.setAvatarUrl(response.getString("avatar"));
                        if (response.has("updatedAt"))
                            player.setUpdated(response.getLong("updatedAt"));
                        if (response.has("nextUpdateAt"))
                            player.setNextUpdate(response.getLong("nextUpdateAt"));
                        if(response.has("stats")){
                            JSONObject stats = response.getJSONObject("stats");
                            player.setWins(stats.getInt("wins"));
                            player.setGoals(stats.getInt("goals"));
                            player.setMvps(stats.getInt("mvps"));
                            player.setSaves(stats.getInt("saves"));
                            player.setShots(stats.getInt("shots"));
                            player.setAssists(stats.getInt("assists"));
                        }
                        if(response.has("profileUrl"))
                            player.setProfileUrl(response.getString("profileUrl"));
                        if (response.has("rankedSeasons")){
                            JSONObject season = response.getJSONObject("rankedSeasons");
                            int i = 0;
                            while(season.has(""+i+1))
                                i++;
                            season = season.getJSONObject(""+i);
                            JSONObject duel = season.getJSONObject("10");
                            JSONObject duo = season.getJSONObject("11");
                            JSONObject solo = season.getJSONObject("12");
                            JSONObject standard = season.getJSONObject("13");
                            player.currentSeason().setStats(
                                    new Ranking(solo.getInt("rankedPoints"),solo.getInt("matchesPlayed"),solo.getInt("tier"),solo.getInt("division"))
                                    ,new Ranking(duel.getInt("rankedPoints"),duel.getInt("matchesPlayed"),duel.getInt("tier"),duel.getInt("division"))
                                    ,new Ranking(duo.getInt("rankedPoints"),duo.getInt("matchesPlayed"),duo.getInt("tier"),duo.getInt("division"))
                                    ,new Ranking(standard.getInt("rankedPoints"),standard.getInt("matchesPlayed"),standard.getInt("tier"),standard.getInt("division"))
                                    ,player.getUpdated()

                            );
                        }

                        refreshIn(response.getLong("nextUpdateAt")-response.getLong("lastRequested"));
                    }catch (JSONException error){
                        error.printStackTrace();
                    }
        },      (error) ->android.util.Log.e("upPlStat","Coudln't update Player stats")){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "ENG5EKOIR4BBEJGSAF5NW068RH5BU2VU");
                return params;
            }
        };
        req.add(request);
    }

    public void refreshIn(final long diff){
        new Thread(()->{
            try {
                this.wait(diff + 100);
                updatePlayerStats();
            }catch (InterruptedException e){

            }
        }).start();
    }

    public void initDummy(){
        this.player = new Player();
        player.setId("76561198026480940");
        player.setPlatform(1);
    }

    @OnClick(R.id.button)
    public void ButtonClick(){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,"https://api.rocketleaguestats.com/v1/data/platforms",null,(response)->{
            text.setText(response.toString());
        }, error->{
            android.util.Log.e("tstEx",error.getStackTrace().toString());
            error.printStackTrace();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "ENG5EKOIR4BBEJGSAF5NW068RH5BU2VU");
                return params;
            }
        };
        req.add(request);
    }

}
