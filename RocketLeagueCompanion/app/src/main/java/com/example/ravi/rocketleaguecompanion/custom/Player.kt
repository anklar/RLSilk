package com.example.ravi.rocketleaguecompanion.custom

import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable

/**
 * represents a player with all attributes
 */
class Player(var id: String, var name: String, var platform: Int, var avatarUrl: String) : Serializable {
    private var profileUrl: String? = null
    var wins: Int = 0
    var goals: Int = 0
    var mvps: Int = 0
    var saves: Int = 0
    var shots: Int = 0
    var assists: Int = 0
    var season = TimestampMap()
    private var updated: Long = 0
    private var currentSeason: Int = 0
    private var latestResponseString = ""
    var latestResponse: JSONObject?
        get () =
            try {
                JSONObject(latestResponseString)
            } catch (e: Exception) {
                null
            }
        set(jay) {
            latestResponseString = jay.toString()
        }


    companion object {
        /**
         * static conversion to construct a player out of a JSON
         */
        fun fromJSON(playerson: JSONObject): Player? {
            return try {
                val p = Player(playerson.getString("uniqueId"), playerson.getString("displayName"),
                        playerson.getJSONObject("platform").getInt("id"), playerson.getString("avatar"))
                p.latestResponse = playerson
                p.update(playerson)
                p
            } catch (e: Exception) {
                android.util.Log.e("@playParse", e.message)
                null
            }
        }
    }


    /**
     * updates this player with fresh data from the JSON
     */
    fun update(playerson: JSONObject): Timestamp? {
        try {
            if (playerson.has("displayName")) {
                this.name = playerson.getString("displayName")
            }
            if (playerson.has("avatar"))
                this.avatarUrl = playerson.getString("avatar").replace("\\", "")
            if (playerson.has("updatedAt"))
                this.updated = playerson.getLong("updatedAt")
            if (playerson.has("stats")) {
                val stats = playerson.getJSONObject("stats")
                this.wins = stats.getInt("wins")
                this.goals = stats.getInt("goals")
                this.mvps = stats.getInt("mvps")
                this.saves = stats.getInt("saves")
                this.shots = stats.getInt("shots")
                this.assists = stats.getInt("assists")
            }
            if (playerson.has("profileUrl"))
                this.profileUrl = playerson.getString("profileUrl").replace("\\", "")
            var stamp: Timestamp? = null
            if (playerson.has("rankedSeasons")) {
                val allSeasons = playerson.getJSONObject("rankedSeasons")
                val it = allSeasons.keys()
                var key = ""
                while (it.hasNext())
                    key = it.next()
                try {
                    val processedSeason = allSeasons.getJSONObject(key)
                    if (key.toInt() >= currentSeason) {
                        val rankings = getRankings(processedSeason)
                        stamp = Timestamp((this.updated) * 1000, rankings, shots, goals, saves, assists, wins, mvps)
                        if (key.toInt() > currentSeason) {
                            //in case season wasnt set, init season with a stamp
                            currentSeason = key.toInt()
                            val newestSeason = TimestampMap(stamp)
                            season = newestSeason
                        } else {
                            season.put(stamp)
                        }
                    }
                } catch (error: JSONException) {
                    android.util.Log.e("@playerUpdate", "Parsing of the JSONResponse for updating the player failed with an JSONException " + error.message)
                    error.printStackTrace()
                    return null
                }
            }
            return stamp
        } catch (error: JSONException) {
            android.util.Log.e("@playerUpdate", "Parsing of the JSONResponse for updating the player failed with an JSONException " + error.message)
            error.printStackTrace()
            return null
        }
    }


    /**
     * parses the JSON into an array containing queue infos in the order "duel,duo,solo,standard"
     */
    private fun getRankings(processedSeason: JSONObject): Array<Ranking> {
        // 10 =  duel , 11 = duo, 12 = solo, 13 = standard
        return arrayOf(
                Ranking(processedSeason.getJSONObject("" + 10).getInt("rankPoints"),
                        processedSeason.getJSONObject("" + 10).getInt("tier"),
                        processedSeason.getJSONObject("" + 10).getInt("division")),
                Ranking(processedSeason.getJSONObject("" + 11).getInt("rankPoints"),
                        processedSeason.getJSONObject("" + 11).getInt("tier"),
                        processedSeason.getJSONObject("" + 11).getInt("division")),
                Ranking(processedSeason.getJSONObject("" + 12).getInt("rankPoints"),
                        processedSeason.getJSONObject("" + 12).getInt("tier"),
                        processedSeason.getJSONObject("" + 12).getInt("division")),
                Ranking(processedSeason.getJSONObject("" + 13).getInt("rankPoints"),
                        processedSeason.getJSONObject("" + 13).getInt("tier"),
                        processedSeason.getJSONObject("" + 13).getInt("division")))

    }
}

