package com.example.ravi.rocketleaguecompanion.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.ButterKnife
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.ravi.rlcomp.custom.Player
import com.example.ravi.rlcomp.custom.Timestamp
import com.example.ravi.rocketleaguecompanion.R.drawable.*

import com.example.ravi.rocketleaguecompanion.R
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_player.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class PlayerFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    companion object {
        fun newInstance(): PlayerFragment = PlayerFragment()
        /**
         * converts the given Long value into day since UNIX time
         */
        fun getDayfromLong(timeMillis: Long = System.currentTimeMillis()): Float {
            return (timeMillis.toDouble() / 86400000).toFloat()
        }
    }



    private var req: RequestQueue? = null
    private var player = Player("76561198026480940","Ravi,",1,"https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/5e/5ea978e3b5b400fa44c036597f3f34d479e81d03_full.jpg")
    private val BASEURL = "https://api.rocketleaguestats.com/v1/"

  /*  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Icepick.restoreInstanceState<StatActivity>(this, savedInstanceState) //TODO
        //ButterKnife.bind(this)
        initDummy()
        req = Volley.newRequestQueue(this.activity?.applicationContext)
        userName.text= player.name
        Glide.with(this).load(player.avatarUrl).into(profilePic)
        requestPlayerStats()
        //updateUI()
    }*/

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Icepick.saveInstanceState<StatActivity>(this, outState)
    }

    /**
     * selects the image and text according to tier and div
     */
    private fun setRankVisuals(image: ImageView, text: TextView, tier:Int, div:Int){
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
    fun updateUI(stamp: Timestamp?) {
        userName.text = this.player.name
        Glide.with(this).load(player.avatarUrl).into(profilePic)
        if(stamp!=null) {
            //updated image and text for each rank
            setRankVisuals(duelImg, duelTxt, stamp.rankingList[0].tier, stamp.rankingList[0].div)
            setRankVisuals(duoImg, duoTxt, stamp.rankingList[1].tier, stamp.rankingList[1].div)
            setRankVisuals(soloImg, soloTxt, stamp.rankingList[2].tier, stamp.rankingList[2].div)
            setRankVisuals(standardImg, standardTxt, stamp.rankingList[3].tier, stamp.rankingList[3].div)
            //set MMR
            duelMmr.text = "MMR: "+stamp.rankingList[0].mmr
            duoMmr.text = "MMR: "+stamp.rankingList[1].mmr
            soloMmr.text = "MMR: "+stamp.rankingList[2].mmr
            standardMmr.text = "MMR: "+stamp.rankingList[3].mmr
            //addChartEntries(stamp)
        }
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

}
