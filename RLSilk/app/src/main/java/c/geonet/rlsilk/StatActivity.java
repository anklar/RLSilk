package com.example.ravi.rlcomp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
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
    private String BASEURL = "https://api.rocketleaguestats.com/v1/";

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

    public void searchPlayers(String keyword){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,BASEURL+"search/players?display_name="+keyword,null,
                (response)->{
                    try {
                        JSONArray jayray = response.getJSONArray("data");
                        Player[] players = new Player[jayray.length()];
                        for(int i = 0; i < jayray.length();i++){
                            players[i] = Player.fromJSON(jayray.getJSONObject(i));
                        }
                        //Display players in ScrollView
                    }catch (JSONException error){

                    }
                },(error)->{

                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "ENG5EKOIR4BBEJGSAF5NW068RH5BU2VU");
                return params;
            }
        };
    }

    public void requestPlayerStats(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASEURL +"player?unique_id2="+player.getId()+"&platform_id="+player.getPlatform(),null,
                (response)->{try{
                        updatePlayerStats(response);
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

    public void updatePlayerStats(JSONObject playerson){
        this.player = player.mergeWithPlayer(Player.fromJSON(playerson));
    }



    public void refreshIn(final long diff){
        new Thread(()->{
            try {
                if(diff > 42000)
                    this.wait(diff);
                else
                    this.wait(42000);
                requestPlayerStats();
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
