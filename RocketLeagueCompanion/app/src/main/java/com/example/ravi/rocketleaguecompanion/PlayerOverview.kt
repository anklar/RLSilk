package com.example.ravi.rocketleaguecompanion

import android.content.Context
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
import com.example.ravi.rocketleaguecompanion.custom.Player
import com.example.ravi.rlcomp.custom.Timestamp
import com.example.ravi.rocketleaguecompanion.fragments.GraphFragment
import com.example.ravi.rocketleaguecompanion.fragments.PlayerFragment
import kotlinx.android.synthetic.main.activity_player_overview.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Thread.sleep
import java.util.*
import kotlin.concurrent.fixedRateTimer

class PlayerOverview : AppCompatActivity() {

    //TODO Fußnoten
    private val baseUrl = "https://api.rocketleaguestats.com/v1/"
    private var req: RequestQueue? = null
    private lateinit var player: Player
    private val playerFragment = PlayerFragment.newInstance()
    private val graphFragment = GraphFragment.newInstance()
    private val generateDummyStats = false

    //vars for dummy values
    private var dayCount = 0
    private var goalAdd = 0
    private var shotAdd = 0
    private var assistAdd = 0
    private var saveAdd = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_overview)


        req = Volley.newRequestQueue(this.applicationContext)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        val ctx = this
        if (intent.hasExtra("player")) {
            this.player = Player.fromJSON(JSONObject(intent?.getStringExtra("player")))!!
        } else {
            this.player = Player("76561198026480940", "Ravi,", 1, "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/5e/5ea978e3b5b400fa44c036597f3f34d479e81d03_full.jpg")
            android.util.Log.e("@playerSet", "settingPlayer failed")
        }
        loadPlayer(ctx, player.id)

        viewPager.adapter = PagerAdapter(supportFragmentManager, 2)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onStart() {
        super.onStart()
        Thread {
            sleep(2000)
            runOnUiThread({ drawLoadedData() })
        }.start()


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


    /**
     *
     * fetches player data from the rest api and processes it further
     *
     */
    private fun requestPlayerStats() {
        val request = object : JsonObjectRequest(Request.Method.GET, baseUrl + "player?unique_id=" + player.id + "&platform_id=" + player.platform, null,
                { response ->
                    try {
                        if (generateDummyStats) {
                            dummyUpdate()
                            savePlayer(this)
                        } else {
                            updatePlayerStats(response)
                            savePlayer(this)
                        }
                    } catch (error: JSONException) {
                        error.printStackTrace()
                    }
                }, { android.util.Log.e("upPlStat", "Coudln't update Player stats") }) {
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
        updateVisuals(this.player.update(playerson))
    }

    /**
     * updates all fragments with the Timestamp data
     */
    private fun updateVisuals(stamp: Timestamp?) {
        if (stamp != null) {
            playerFragment.updateUI(stamp, this.player)
            graphFragment.addChartEntries(stamp, player.season.getLastStampBefore(stamp))
        }
    }

    /**
     * generates dummy data for testing
     */
    fun dummyUpdate() {
        val duelDiff = Random().nextInt(100)
        val duoDiff = Random().nextInt(100)
        val standDiff = Random().nextInt(100)
        val soloDiff = Random().nextInt(100)
        goalAdd += Random().nextInt(50)
        shotAdd += Random().nextInt(50) + 50
        assistAdd += Random().nextInt(100)
        saveAdd += Random().nextInt(100)
        val dummyResponse = "{\"uniqueId\":\"76561198033227582\",\"displayName\":\"BIN | St0rmhunter\",\"platform\":{\"id\":1,\"name\":\"Steam\"}," +
                "\"avatar\":\"https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/5e/5ea978e3b5b400fa44c036597f3f34d479e81d03_full.jpg\"," +
                "\"profileUrl\":\"https://rocketleaguestats.com/profile/Steam/76561198033227582\",\"signatureUrl\":\"http://signature.rocketleaguestats.com/normal/Steam/76561198033227582.png\"" +
                ",\"stats\":{\"wins\":5384,\"goals\":" + (17469 + goalAdd) + ",\"mvps\":2052,\"saves\":" + (10075 + saveAdd) + ",\"shots\":" + (35774 + shotAdd) + ",\"assists\":" + (4606 + assistAdd) + "}," +
                "\"rankedSeasons\":{" +
                "\"4\":{\"10\":{\"rankPoints\":697,\"matchesPlayed\":266,\"tier\":8,\"division\":2},\"11\":{\"rankPoints\":783,\"matchesPlayed\":828,\"tier\":9,\"division\":2},\"12\":{\"rankPoints\":656,\"matchesPlayed\":369,\"tier\":8,\"division\":0},\"13\":{\"rankPoints\":902,\"matchesPlayed\":758,\"tier\":11,\"division\":1}}," +
                "\"5\":{\"10\":{\"rankPoints\":782,\"matchesPlayed\":402,\"tier\":10,\"division\":0},\"11\":{\"rankPoints\":779,\"matchesPlayed\":1488,\"tier\":9,\"division\":2},\"12\":{\"rankPoints\":737,\"matchesPlayed\":8,\"tier\":0,\"division\":0},\"13\":{\"rankPoints\":1049,\"matchesPlayed\":1406,\"tier\":13,\"division\":1}}," +
                "\"6\":{\"10\":{\"rankPoints\":738,\"matchesPlayed\":86,\"tier\":10,\"division\":3},\"11\":{\"rankPoints\":850,\"matchesPlayed\":374,\"tier\":12,\"division\":0},\"12\":{\"rankPoints\":899,\"matchesPlayed\":262,\"tier\":13,\"division\":1},\"13\":{\"rankPoints\":1123,\"matchesPlayed\":792,\"tier\":15,\"division\":1}}," +
                "\"7\":{\"10\":{\"rankPoints\":738,\"matchesPlayed\":86,\"tier\":10,\"division\":3},\"11\":{\"rankPoints\":850,\"matchesPlayed\":374,\"tier\":12,\"division\":0},\"12\":{\"rankPoints\":899,\"matchesPlayed\":262,\"tier\":13,\"division\":1},\"13\":{\"rankPoints\":1123,\"matchesPlayed\":792,\"tier\":15,\"division\":1}}," +
                "\"8\":{" +
                "\"10\":{\"rankPoints\":" + (821 + duelDiff) + ",\"matchesPlayed\":204,\"tier\":12,\"division\":2},\"" +
                "11\":{\"rankPoints\":" + (1001 + duoDiff) + ",\"matchesPlayed\":403,\"tier\":14,\"division\":0},\"" +
                "12\":{\"rankPoints\":" + (872 + standDiff) + ",\"matchesPlayed\":247,\"tier\":13,\"division\":1},\"" +
                "13\":{\"rankPoints\":" + (1186 + soloDiff) + ",\"matchesPlayed\":408,\"tier\":16,\"division\":0}}}," +
                "\"lastRequested\":1526916194,\"createdAt\":1492121089,\"updatedAt\":" + (System.currentTimeMillis() + dayCount).toString().dropLast(3) + ",\"nextUpdateAt\":1526916417}"
        dayCount += 86400000 / 2
        updatePlayerStats(JSONObject(dummyResponse))
    }

    /**
     * saves the current player to the storage
     */
    fun savePlayer(context: Context) {
        try {
            val out = ObjectOutputStream(context.openFileOutput(this.player.id, Context.MODE_PRIVATE))
            out.writeObject(this.player)
            out.close()
        } catch (e: Exception) {
            android.util.Log.e("@playsave", e.message)
        }
    }

    /**
     * loads the player for the given id
     */
    private fun loadPlayer(context: Context, id: String) {
        try {
            val input = ObjectInputStream(context.openFileInput(id))
            this.player = input.readObject() as Player
            input.close()
        } catch (e: Exception) {
            android.util.Log.e("@playload", e.message)
        }
        dayCount += player.season.values.count()
        dayCount *= 86400000
    }

    /**
     * draws data loaded into the GraphFragment
     */
    private fun drawLoadedData() {
        //write chart entries
        val ps = player.season.values.iterator()
        try {
            do {
                val item = ps.next()
                updateVisuals(item)
            } while (ps.hasNext())
        } catch (e: Exception) {

        }
        if (generateDummyStats) {
            Thread({
                for (i in 0..10) {
                    requestPlayerStats()
                    sleep(5000)
                }
            }).start()
        } else {
            fixedRateTimer(name = "requestLoop", initialDelay = 0, period = 42000) {
                requestPlayerStats()
            }
        }
    }

    inner class PagerAdapter(fm: FragmentManager, private var tabCount: Int) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {

            return when (position) {
                0 -> playerFragment
                1 -> graphFragment
                else -> null
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
