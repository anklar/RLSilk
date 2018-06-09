package com.example.ravi.rocketleaguecompanion

import android.annotation.SuppressLint
import android.app.ListActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.ravi.rocketleaguecompanion.custom.Player
import kotlinx.android.synthetic.main.activity_search.*
import org.json.JSONException
import java.util.*
import android.preference.PreferenceManager
import android.widget.*
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


class SearchActivity : ListActivity() {

    private val baseUrl = "https://api.rocketleaguestats.com/v1/"
    private var req: RequestQueue? = null
    private var foundPlayers = LinkedList<Player>()
    private var recentlyUsedPlayers: HashMap<String, Player> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        req = Volley.newRequestQueue(this.applicationContext)
    }

    override fun onResume(){
        super.onResume()
        try {
            val input = ObjectInputStream(applicationContext.openFileInput("selectedPlayer"))
            val player = input.readObject() as Player
            input.close()
                val intent = Intent(this, PlayerOverview::class.java).apply {
                    putExtra("player", player.latestResponse.toString())
                }
                startActivity(intent)
        }catch(e:FileNotFoundException){
            android.util.Log.e("@ResumEx",e.message)
        }
    }

    override fun onStart() {
        super.onStart()


        //run AppIntro on the initial start
        val sp = PreferenceManager.getDefaultSharedPreferences(baseContext)
        if (!sp.getBoolean("first", false)) {
            val editor = sp.edit()
            editor.putBoolean("first", true)
            editor.apply()
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        } else {
            //this isnt the first start, there may be data to load
            try {
                //Load recent Player Objects from the Storage
                val input = ObjectInputStream(applicationContext.openFileInput("recentlyUsedPlayers"))
                recentlyUsedPlayers = input.readObject() as HashMap<String, Player>
                foundPlayers = LinkedList(recentlyUsedPlayers.values)
                handleSearchResponse(LinkedList(recentlyUsedPlayers.values))
                input.close()


            } catch (e: Exception) {
                recentlyUsedPlayers = HashMap()
            }

        }


        //Bind function to the searchButton
        searchButton.setOnClickListener {
            if (searchText.text.toString() != "")
                searchPlayers(searchText.text.toString())
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, PlayerOverview::class.java).apply {
                putExtra("player", foundPlayers[position].latestResponse.toString())
            }
            //save selected Player to add him to the recently used list
            val output = ObjectOutputStream(applicationContext.openFileOutput("recentlyUsedPlayers", Context.MODE_PRIVATE))
            recentlyUsedPlayers[foundPlayers[position].id] = foundPlayers[position]
            output.writeObject(recentlyUsedPlayers)
            output.close()
            //save selected pplayer for onResume()
            val saveInstanceOutput = ObjectOutputStream(applicationContext.openFileOutput("selectedPlayer", Context.MODE_PRIVATE))
            saveInstanceOutput.writeObject(foundPlayers[position])
            saveInstanceOutput.close()
            startActivity(intent)
        }


    }

    /**
     * fires a search request to find players to a given name
     */
    private fun searchPlayers(name: String) {
        searchProgressBar.isIndeterminate = true
        searchProgressBar.visibility = View.VISIBLE
        val request = object : JsonObjectRequest(Request.Method.GET, baseUrl + "search/players?display_name=" + name, null,
                { response ->
                    try {
                        val jayray = response.getJSONArray("data")
                        foundPlayers = LinkedList()
                        for (i in 0 until jayray.length()) {
                            foundPlayers.add(Player.fromJSON(jayray.getJSONObject(i))!!)
                        }
                        handleSearchResponse(foundPlayers)
                        searchProgressBar.visibility = View.INVISIBLE
                    } catch (error: JSONException) {

                    }
                }, { error ->
            android.util.Log.e("@playerSearch", error.message)
            searchProgressBar.visibility = View.INVISIBLE
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                //add api authorization to the request
                val params = HashMap<String, String>()
                params["Authorization"] = "ENG5EKOIR4BBEJGSAF5NW068RH5BU2VU"
                return params
            }
        }
        req!!.add(request)
    }

    /**
     * creates adapter that puts a list of found players into the listview
     */
    private fun handleSearchResponse(playerData: LinkedList<Player>) {
        if (playerData.isEmpty()) {
            Toast.makeText(this, "Didn't find any players for: " + searchText.text.toString(), Toast.LENGTH_LONG).show()
        } else {
            val customAdapter = CustomAdapter(this, playerData)
            listView.adapter = customAdapter
        }
    }

    /**
     * Adapter to put players in the listview
     */
    inner class CustomAdapter(private val context: Context, private val foundPlayers: LinkedList<Player>) : BaseAdapter() {
        private val inflater: LayoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return foundPlayers.size
        }

        override fun getItem(i: Int): Any? {
            return null
        }

        override fun getItemId(i: Int): Long {
            return 0
        }

        @SuppressLint("ViewHolder", "InflateParams")
        override fun getView(i: Int, v: View?, viewGroup: ViewGroup): View {

            val view = inflater.inflate(R.layout.player_tile, null)
            val playerName = view.findViewById<TextView>(R.id.userName)
            val playerImage = view.findViewById<ImageView>(R.id.profilePic)
            val platform = view.findViewById<TextView>(R.id.platform)

            try {
                if (foundPlayers[i].avatarUrl == "null")
                    Glide.with(context).load("https://steamuserimages-a.akamaihd.net/ugc/868480752636433334/1D2881C5C9B3AD28A1D8852903A8F9E1FF45C2C8/").into(playerImage)
                else
                    Glide.with(context).load(foundPlayers[i].avatarUrl).into(playerImage)
            } catch (e: Exception) {

            }
            playerName.text = foundPlayers[i].name
            if (platform != null)
                when (foundPlayers[i].platform) {
                    1 -> platform.text = getString(R.string.steam)
                    2 -> platform.text = getString(R.string.ps4)
                    3 -> platform.text = getString(R.string.xbox)
                    else -> platform.text = ""
                }
            return view
        }
    }
}


