package com.example.ravi.rocketleaguecompanion

import android.annotation.SuppressLint
import android.app.ListActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.ravi.rocketleaguecompanion.custom.Player
import kotlinx.android.synthetic.main.activity_search.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class SearchActivity : ListActivity() {

    private val baseUrl = "https://api.rocketleaguestats.com/v1/"
    private var req: RequestQueue? = null
    private var playerDataList = LinkedList<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        req = Volley.newRequestQueue(this.applicationContext)
    }

    override fun onStart() {
        super.onStart()

        searchButton.setOnClickListener {
            android.util.Log.e("@Searchclick", searchText.text.toString())
            searchPlayers(searchText.text.toString())
        }


        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, PlayerOverview::class.java).apply {
                putExtra("player", playerDataList[position].toString())
            }
            startActivity(intent)
        }
    }

    /**
     * fires a search request to find players to a given name
     */
    private fun searchPlayers(name: String) {
        val request = object : JsonObjectRequest(Request.Method.GET, baseUrl + "search/players?display_name=" + name, null,
                { response ->
                    try {
                        val jayray = response.getJSONArray("data")
                        val foundPlayers = LinkedList<Player>()
                        for (i in 0 until jayray.length()) {
                            foundPlayers.add(Player.fromJSON(jayray.getJSONObject(i))!!)
                            playerDataList.add(jayray.getJSONObject(i))
                        }
                        handleSearchResponse(foundPlayers)
                        //Display players in ScrollView
                    } catch (error: JSONException) {

                    }
                }, { error ->
            android.util.Log.e("@playerSearch", error.message)
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
            val customAdapter = CustomAdapter(this, playerData)
            listView.adapter = customAdapter
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
            val playerName = view.findViewById<TextView>(R.id.playerName)
            val playerImage = view.findViewById<ImageView>(R.id.playerImage)

            try {
                Glide.with(context).load(foundPlayers[i].avatarUrl).into(playerImage)
            } catch (e: Exception) {

            }
            playerName.text = foundPlayers[i].name
            return view
        }
    }
}


