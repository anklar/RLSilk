package com.example.ravi.rlcomp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_player_activity.*

import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ravi.rlcomp.custom.Player

import org.json.JSONException
import org.json.JSONObject

import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.example.ravi.rlcomp.R.drawable.*
import com.example.ravi.rlcomp.custom.Timestamp
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import java.lang.Thread.sleep
import com.github.mikephil.charting.data.LineData
import java.util.*


class PlayerActivity : AppCompatActivity() {
    private var req: RequestQueue? = null
    private var text: TextView? = null
    private lateinit var player: Player
    private val BASEURL = "https://api.rocketleaguestats.com/v1/"

    private var dummyCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_activity)
        //Icepick.restoreInstanceState<StatActivity>(this, savedInstanceState)
        ButterKnife.bind(this)
        initDummy()
        req = Volley.newRequestQueue(this.applicationContext)
        userName.text= player.name
        Glide.with(this).load(player.avatarUrl).into(profilePic)
        requestPlayerStats()
        //updateUI()
       /* button.setOnClickListener {
                val request = object : JsonArrayRequest(Request.Method.GET, "https://api.rocketleaguestats.com/v1/data/platforms", null, { response -> text!!.text = response.toString() }, { error ->
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
                req!!.add(request)
            requestPlayerStats()
        }*/
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Icepick.saveInstanceState<StatActivity>(this, outState)
    }

    /**
     * selects the image and text according to tier and div
     */
    private fun setRankVisuals(image: ImageView, text:TextView,tier:Int ,div:Int){
        when (tier) {
        //unranked
            0 -> {
                image.setImageResource(rank0)
                text.text = "Unranked"
            }
        //bronze
            1 -> {
                image.setImageResource(rank1)
                text.text = "Bronze 1 Division " + div
            }
            2 -> {
                image.setImageResource(rank2)
                text.text = "Bronze 2 Division " + div
            }
            3 ->{
                image.setImageResource(rank3)
                text.text = "Bronze 3 Division " + div
            }
        //silver
            4 ->{
                image.setImageResource(rank4)
                text.text = "Silver 1 Division " + div
            }
            5 ->{
                image.setImageResource(rank5)
                text.text = "Silver 2 Division " + div
            }
            6 ->{
                image.setImageResource(rank6)
                text.text = "Silver 3 Division " + div
            }
        //gold
            7 ->{
                image.setImageResource(rank7)
                text.text = "Gold 1 Division " + div
            }
            8 ->{
                image.setImageResource(rank8)
                text.text = "Gold 2 Division " + div
            }
            9 ->{
                image.setImageResource(rank9)
                text.text = "Gold 3 Division " + div
            }
        //platinum
            10 ->{
                image.setImageResource(rank10)
                text.text = "Platinum 1 Division " + div
            }
            11 ->{
                image.setImageResource(rank11)
                text.text = "Platinum 2 Division " + div
            }
            12 ->{
                image.setImageResource(rank12)
                text.text = "Platinum 3 Division " + div
            }
        //diamond
            13 ->{
                image.setImageResource(rank13)
                text.text = "Diamond 1 Division " + div
            }
            14 ->{
                image.setImageResource(rank14)
                text.text = "Diamond 2 Division " + div
            }
            15 ->{
                image.setImageResource(rank15)
                text.text = "Diamond 3 Division " + div
            }
        //champion
            16 ->{
                image.setImageResource(rank16)
                text.text = "Champion 1 Division " + div
            }
            17 ->{
                image.setImageResource(rank17)
                text.text = "Champion 2 Division " + div
            }
            18 ->{
                image.setImageResource(rank18)
                text.text = "Champion 3 Division " + div
            }
        //grand champ
            19 ->{
                image.setImageResource(rank19)
                text.text = "Grand Champion"
            }

        }
    }

    /**
     * updates all UI elements with a Timestamp
     */
    private fun updateUI(stamp:Timestamp?) {
        userName.text = this.player.name
        Glide.with(this).load(player!!.avatarUrl).into(profilePic)
        if(stamp!=null) {
            setRankVisuals(duelImg, duelTxt, stamp.rankingList[0].tier, stamp.rankingList[0].div)
            setRankVisuals(duoImg, duoTxt, stamp.rankingList[1].tier, stamp.rankingList[1].div)
            setRankVisuals(standardImg, standardTxt, stamp.rankingList[2].tier, stamp.rankingList[2].div)
            setRankVisuals(soloImg, soloTxt, stamp.rankingList[3].tier, stamp.rankingList[3].div)
            addChartEntries(stamp)
        }
    }

    /**
     * puts the data from the timestamp into all charts
     */
    private fun addChartEntries(stamp:Timestamp){
        //TODO assign correct Values for x Axis
        val x = (dummyCount).toFloat()
        dummyCount++


        val y = stamp.goals.toFloat()/stamp.shots.toFloat()
        android.util.Log.e("@ChartEntry", "Entry at point " +x +" : "+y +" based on shots: " +stamp.shots +" and goals "+stamp.goals)
        if(shotpercChart.data!=null)
            //add new entry into chart
            shotpercChart.data.dataSets[0].addEntry(Entry(x,y))
        else{
            //initalize chart and add entry afterwards
            val dataSet = LineDataSet(arrayListOf(Entry(x,y)), "Label")
            dataSet.color = (R.color.bright_foreground_inverse_material_light)
            dataSet.valueTextColor =(R.color.bright_foreground_inverse_material_light)
            val lineData = LineData(dataSet)
            shotpercChart.data = lineData
            shotpercChart.xAxis.axisMinimum = 1f
            shotpercChart.xAxis.setDrawAxisLine(true)
            shotpercChart.xAxis.setDrawLabels(true)
            shotpercChart.xAxis.axisMaximum = 25f       //TODO correct scale, maybe dynamic

            shotpercChart.axisRight.setDrawLabels(false)
            shotpercChart.axisRight.setDrawGridLines(false)

            shotpercChart.axisLeft.axisMinimum = 0f
            shotpercChart.axisLeft.axisMaximum = 1f
        }
        shotpercChart.invalidate()      //refresh chart
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

    /**
     * fetches data for the player from the webapi
     */
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

    /**
     * updates the player with data from the JSON
     */
    fun updatePlayerStats(playerson: JSONObject) {
        android.util.Log.e("@update",playerson.toString())
        updateUI(this.player.update(playerson))
    }


    /**
     * timed trigger for the next update
     */
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

    private fun initDummy() {
        this.player = Player()
        player!!.id = "76561198026480940"
        player!!.platform = 1
        player.name = "Ravi"
        player.avatarUrl = "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/5e/5ea978e3b5b400fa44c036597f3f34d479e81d03_full.jpg"
    }





}

