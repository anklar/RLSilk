package com.example.ravi.rocketleaguecompanion.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.ravi.rlcomp.custom.Player
import com.example.ravi.rlcomp.custom.Timestamp
import com.example.ravi.rocketleaguecompanion.R
import com.example.ravi.rocketleaguecompanion.R.drawable.*
import com.example.ravi.rocketleaguecompanion.SearchActivity
import kotlinx.android.synthetic.main.fragment_player.*


class PlayerFragment : Fragment() {
    var init: Boolean = true
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

    /**
     * selects the image and text according to tier and div
     */
    private fun setRankVisuals(image: ImageView, text: TextView, tier: Int, div: Int) {

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
            3 -> {
                image.setImageResource(rank3)
                text.text = "Bronze 3 Division " + div
            }
        //silver
            4 -> {
                image.setImageResource(rank4)
                text.text = "Silver 1 Division " + div
            }
            5 -> {
                image.setImageResource(rank5)
                text.text = "Silver 2 Division " + div
            }
            6 -> {
                image.setImageResource(rank6)
                text.text = "Silver 3 Division " + div
            }
        //gold
            7 -> {
                image.setImageResource(rank7)
                text.text = "Gold 1 Division " + div
            }
            8 -> {
                image.setImageResource(rank8)
                text.text = "Gold 2 Division " + div
            }
            9 -> {
                image.setImageResource(rank9)
                text.text = "Gold 3 Division " + div
            }
        //platinum
            10 -> {
                image.setImageResource(rank10)
                text.text = "Platinum 1 Division " + div
            }
            11 -> {
                image.setImageResource(rank11)
                text.text = "Platinum 2 Division " + div
            }
            12 -> {
                image.setImageResource(rank12)
                text.text = "Platinum 3 Division " + div
            }
        //diamond
            13 -> {
                image.setImageResource(rank13)
                text.text = "Diamond 1 Division " + div
            }
            14 -> {
                image.setImageResource(rank14)
                text.text = "Diamond 2 Division " + div
            }
            15 -> {
                image.setImageResource(rank15)
                text.text = "Diamond 3 Division " + div
            }
        //champion
            16 -> {
                image.setImageResource(rank16)
                text.text = "Champion 1 Division " + div
            }
            17 -> {
                image.setImageResource(rank17)
                text.text = "Champion 2 Division " + div
            }
            18 -> {
                image.setImageResource(rank18)
                text.text = "Champion 3 Division " + div
            }
        //grand champ
            19 -> {
                image.setImageResource(rank19)
                text.text = "Grand Champion"
            }

        }
    }

    /**
     * updates all UI elements with a Timestamp
     */
    fun updateUI(stamp: Timestamp?, player: Player) {
        try {
            Glide.with(this).load(player.avatarUrl).into(profilePic)
        }catch(e:Exception){
            android.util.Log.e("@avatar",e.message)
        }
        userName.text = player.name
        if (stamp != null) {
            //updated image and text for each rank if changed
            setRankVisuals(duelImg, duelTxt, stamp.rankingList[0].tier, stamp.rankingList[0].div)
            setRankVisuals(duoImg, duoTxt, stamp.rankingList[1].tier, stamp.rankingList[1].div)
            setRankVisuals(soloImg, soloTxt, stamp.rankingList[2].tier, stamp.rankingList[2].div)
            setRankVisuals(standardImg, standardTxt, stamp.rankingList[3].tier, stamp.rankingList[3].div)
            //set MMR
            duelMmr.text = "MMR: " + stamp.rankingList[0].mmr
            duoMmr.text = "MMR: " + stamp.rankingList[1].mmr
            soloMmr.text = "MMR: " + stamp.rankingList[2].mmr
            standardMmr.text = "MMR: " + stamp.rankingList[3].mmr
            //set stats
            goalTxt.text = stamp.goals.toString()
            saveTxt.text = stamp.saves.toString()
            winTxt.text = stamp.wins.toString()
            shotTxt.text = stamp.shots.toString()
            assistTxt.text = stamp.assists.toString()
            mvpTxt.text = stamp.mvps.toString()
        }
    }


    override fun onStart() {
        super.onStart()
        toSearchButton.setOnClickListener {
            openSearchActivity()
        }
    }
    fun openSearchActivity() {
        val intent = Intent(this.activity, SearchActivity::class.java)
        startActivity(intent)
    }

}
