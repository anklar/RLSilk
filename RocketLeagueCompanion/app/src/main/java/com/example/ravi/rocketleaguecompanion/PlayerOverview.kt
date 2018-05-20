package com.example.ravi.rocketleaguecompanion

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ravi.rlcomp.custom.Player
import com.example.ravi.rocketleaguecompanion.fragments.GraphFragment
import com.example.ravi.rocketleaguecompanion.fragments.PlayerFragment
import kotlinx.android.synthetic.main.activity_player_overview.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class PlayerOverview : AppCompatActivity() {


    //TODO Implement Player Search
//------------------------------------------------------
    private val BASEURL = "https://api.rocketleaguestats.com/v1/"
    private var req: RequestQueue? = null
    //private var player = Player("76561198026480940","Ravi,",1,"https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/5e/5ea978e3b5b400fa44c036597f3f34d479e81d03_full.jpg")
    private var player = Player("76561198033227582","Placeholder,",1,"https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/5e/5ea978e3b5b400fa44c036597f3f34d479e81d03_full.jpg")
    private val playerFragment = PlayerFragment.newInstance()
    private val graphFragment = GraphFragment.newInstance()

    /**
     *
     * fetches player data from the rest api and processes it further
     *
     */
    private fun requestPlayerStats() {
        val request = object : JsonObjectRequest(Request.Method.GET, BASEURL + "player?unique_id=" + player.id + "&platform_id=" + player.platform, null,
                { response ->
                    try {
                        updatePlayerStats(response)
                        refreshIn(response.getLong("nextUpdateAt") - response.getLong("lastRequested"))
                    } catch (error: JSONException) {
                    error.printStackTrace()
                }
                }, {android.util.Log.e("upPlStat", "Coudln't update Player stats")}) {
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
        val stamp = this.player.update(playerson)
        if(stamp != null) {
            playerFragment.updateUI(stamp, this.player)
            val oldStamp = player.seasons[player.currentSeason]?.getLastStampBefore(stamp)
            //TODO oldStamp is always null
            if(oldStamp != null)
                graphFragment.addChartEntries(stamp,oldStamp)
        }
    }


    /**
     * timed trigger for the next update
     */
    fun refreshIn(diff: Long) {
        Thread {
            try {
                if (diff > 42000)
                    Thread.sleep(diff)
                else
                    Thread.sleep(42000)
                requestPlayerStats()
            } catch (e: InterruptedException) {

            }
        }.start()
    }








    //-------------------------------------------------------



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_overview)

        req = Volley.newRequestQueue(this.applicationContext)

        viewPager.adapter = PagerAdapter(supportFragmentManager,2)
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        requestPlayerStats()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_player_overview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    inner class PagerAdapter(fm: FragmentManager, private var tabCount: Int) :FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {

            return when (position) {
                0 ->  playerFragment
                1 ->  graphFragment
                else ->  null
            }
        }

        override fun getCount(): Int {
            return tabCount
        }
        override fun getPageTitle(position: Int): CharSequence = when (position) {
            0 -> "Player"
            1 -> "Statistic"
            else -> ""
        }

    }


}
