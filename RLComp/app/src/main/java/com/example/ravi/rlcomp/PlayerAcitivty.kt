package com.example.ravi.rlcomp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_player_acitivty.*

import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ravi.rlcomp.custom.Player

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.HashMap

import butterknife.ButterKnife
import butterknife.OnClick
import icepick.Icepick
import java.lang.Thread.sleep

class PlayerAcitivty : AppCompatActivity() {
    private var req: RequestQueue? = null
    private var text: TextView? = null
    private var player: Player? = null
    private val BASEURL = "https://api.rocketleaguestats.com/v1/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_acitivty)
        //Icepick.restoreInstanceState<StatActivity>(this, savedInstanceState)
        ButterKnife.bind(this)
        req = Volley.newRequestQueue(this.applicationContext)
        text = findViewById(R.id.text)
        initDummy()
        button.setOnClickListener {
                /*val request = object : JsonArrayRequest(Request.Method.GET, "https://api.rocketleaguestats.com/v1/data/platforms", null, { response -> text!!.text = response.toString() }, { error ->
                    android.util.Log.e("tstEx", error.stackTrace.toString())
                    error.printStackTrace()
                }) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val params = HashMap<String, String>()
                        params["Authorization"] = "ENG5EKOIR4BBEJGSAF5NW068RH5BU2VU"
                        return params
                    }
                }
                req!!.add(request) */
            requestPlayerStats()
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Icepick.saveInstanceState<StatActivity>(this, outState)
    }

    /*fun searchPlayers(keyword: String) {
        val request = object : JsonObjectRequest(Request.Method.GET, BASEURL + "search/players?display_name=" + keyword, null,
                { response ->
                    try {
                        val jayray = response.getJSONArray("data")
                        val players = arrayOfNulls<Player>(jayray.length())
                        for (i in 0 until jayray.length()) {
                            players[i] = Player.fromJSON(jayray.getJSONObject(i))
                        }
                        //Display players in ScrollView
                    } catch (error: JSONException) {

                    }
                }, { error ->

        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = "ENG5EKOIR4BBEJGSAF5NW068RH5BU2VU"
                return params
            }
        }
    }*/

    private fun requestPlayerStats() {
        val request = object : JsonObjectRequest(Request.Method.GET, BASEURL + "player?unique_id=" + player!!.id + "&platform_id=" + player!!.platform, null,
                { response ->
                    try {
                        updatePlayerStats(response)
                        refreshIn(response.getLong("nextUpdateAt") - response.getLong("lastRequested"))
                    } catch (error: JSONException) {
                        error.printStackTrace()
                    }
                }, { error -> android.util.Log.e("upPlStat", "Coudln't update Player stats") }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = "ENG5EKOIR4BBEJGSAF5NW068RH5BU2VU"
                return params
            }
        }
        req!!.add(request)
    }

    fun updatePlayerStats(playerson: JSONObject) {
        this.player!!.update(playerson)
        val player = this.player
        text?.text = player?.toText()
    }


    fun refreshIn(diff: Long) {
        Thread {
            try {
                if (diff > 42000)
                    sleep(diff)
                else
                    sleep(42000)
                requestPlayerStats()
            } catch (e: InterruptedException) {

            }
        }.start()
    }

    fun initDummy() {
        this.player = Player()
        player!!.id = "76561198026480940"
        player!!.platform = 1
    }




}

