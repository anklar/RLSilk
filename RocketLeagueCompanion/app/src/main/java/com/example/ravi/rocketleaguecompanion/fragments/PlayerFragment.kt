package com.example.ravi.rocketleaguecompanion.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.ravi.rocketleaguecompanion.custom.Player
import com.example.ravi.rlcomp.custom.Timestamp
import com.example.ravi.rocketleaguecompanion.R
import com.example.ravi.rocketleaguecompanion.R.drawable.*
import com.example.ravi.rocketleaguecompanion.R.string.*
import com.example.ravi.rocketleaguecompanion.SearchActivity
import kotlinx.android.synthetic.main.fragment_player.*


class PlayerFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onStart() {
        super.onStart()
        toSearchButton.setOnClickListener {
            val intent = Intent(this.activity, SearchActivity::class.java)
                    intent.apply {
                        putExtra("searchAgain", true)
                    }
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance(): PlayerFragment = PlayerFragment()
    }

    /**
     * selects the image and text according to tier and div
     */
    @SuppressLint("SetTextI18n")    //Lint marks concatenated strings AND raw strings
    private fun setRankVisuals(image: ImageView, text: TextView, tier: Int, div: Int) {

        when (tier) {
        //unranked
            0 -> {
                image.setImageResource(rank0)
                text.text = getString(unranked)
            }
        //bronze
            1 -> {
                image.setImageResource(rank1)
                text.text = getString(bronze1) + div
            }
            2 -> {
                image.setImageResource(rank2)
                text.text = getString(bronze2) + div
            }
            3 -> {
                image.setImageResource(rank3)
                text.text = getString(bronze3) + div
            }
        //silver
            4 -> {
                image.setImageResource(rank4)
                text.text = getString(silver1) + div
            }
            5 -> {
                image.setImageResource(rank5)
                text.text = getString(silver2) + div
            }
            6 -> {
                image.setImageResource(rank6)
                text.text = getString(silver3) + div
            }
        //gold
            7 -> {
                image.setImageResource(rank7)
                text.text = getString(gold1) + div
            }
            8 -> {
                image.setImageResource(rank8)
                text.text = getString(gold2) + div
            }
            9 -> {
                image.setImageResource(rank9)
                text.text = getString(gold3) + div
            }
        //platinum
            10 -> {
                image.setImageResource(rank10)
                text.text = getString(plat1) + div
            }
            11 -> {
                image.setImageResource(rank11)
                text.text = getString(plat2) + div
            }
            12 -> {
                image.setImageResource(rank12)
                text.text = getString(plat3) + div
            }
        //diamond
            13 -> {
                image.setImageResource(rank13)
                text.text = getString(dia1) + div
            }
            14 -> {
                image.setImageResource(rank14)
                text.text = getString(dia2) + div
            }
            15 -> {
                image.setImageResource(rank15)
                text.text = getString(dia3) + div
            }
        //champion
            16 -> {
                image.setImageResource(rank16)
                text.text = getString(champ1) + div
            }
            17 -> {
                image.setImageResource(rank17)
                text.text = getString(champ2) + div
            }
            18 -> {
                image.setImageResource(rank18)
                text.text = getString(champ3) + div
            }
        //grand champ
            19 -> {
                image.setImageResource(rank19)
                text.text = getString(grandChamp)
            }

        }
    }

    @SuppressLint("SetTextI18n")
            /**
             * updates all UI elements with a Timestamp
             */
    fun updateUI(stamp: Timestamp?, player: Player) {
        try {
            if (player.avatarUrl == "null")
                Glide.with(this).load("https://steamuserimages-a.akamaihd.net/ugc/868480752636433334/1D2881C5C9B3AD28A1D8852903A8F9E1FF45C2C8/").into(profilePic)
            else
                Glide.with(this).load(player.avatarUrl).into(profilePic)
        } catch (e: Exception) {
            android.util.Log.e("@avatar", e.message)
        }
        userName.text = player.name
        if (platform != null)
            when (player.platform) {
                1 -> platform.text = getString(R.string.steam)
                2 -> platform.text = getString(R.string.ps4)
                3 -> platform.text = getString(R.string.xbox)
                else -> platform.text = ""
            }
        if (stamp != null) {
            //updated image and text for each rank if changed
            setRankVisuals(duelImg, duelTxt, stamp.rankingList[0].tier, stamp.rankingList[0].div)
            setRankVisuals(duoImg, duoTxt, stamp.rankingList[1].tier, stamp.rankingList[1].div)
            setRankVisuals(soloImg, soloTxt, stamp.rankingList[2].tier, stamp.rankingList[2].div)
            setRankVisuals(standardImg, standardTxt, stamp.rankingList[3].tier, stamp.rankingList[3].div)
            //set MMR
            duelMmr.text = getString(mmr) + stamp.rankingList[0].mmr
            duoMmr.text = getString(mmr) + stamp.rankingList[1].mmr
            soloMmr.text = getString(mmr) + stamp.rankingList[2].mmr
            standardMmr.text = getString(mmr) + stamp.rankingList[3].mmr
            //set stats
            goalTxt.text = stamp.goals.toString()
            saveTxt.text = stamp.saves.toString()
            winTxt.text = stamp.wins.toString()
            shotTxt.text = stamp.shots.toString()
            assistTxt.text = stamp.assists.toString()
            mvpTxt.text = stamp.mvps.toString()
        }
    }


}
