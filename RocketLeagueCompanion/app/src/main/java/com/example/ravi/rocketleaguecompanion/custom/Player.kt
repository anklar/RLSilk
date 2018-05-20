package com.example.ravi.rlcomp.custom

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

class Player(var id: String, var name: String, var platform: Int, var avatarUrl: String) {
    var profileUrl: String? = null
    var wins: Int = 0
    var goals: Int = 0
    var mvps: Int = 0
    var saves: Int = 0
    var shots: Int = 0
    var assists: Int = 0
    var seasons: LinkedHashMap<Int, TimestampList> = LinkedHashMap()
    var updated: Long = 0
    var nextUpdate: Long = 0
    var currentSeason: Int = 0


    override fun toString(): String {
        var bob = StringBuilder()
        bob.append(""" name: $name" wins: ${wins}goals: ${goals}mvps: ${mvps}saves: ${saves}shots: ${shots}assists: $assists""")
        for (key: Int in seasons.keys) {
            bob.append(" Season: ")
            bob.append(key)
            bob.append("    ")
        }
        return bob.toString()

    }

    /**
     * updates this player with fresh data from the JSON
     */
    fun update(playerson: JSONObject): Timestamp? {
        try {
            if (playerson.has("displayName")) {
                this.name = playerson.getString("displayName")
                android.util.Log.e("name", this.name)
            }
            if (playerson.has("avatar"))
                this.avatarUrl = playerson.getString("avatar").replace("\\", "")
            if (playerson.has("updatedAt"))
                this.updated = playerson.getLong("updatedAt")
            if (playerson.has("nextUpdateAt"))
                this.nextUpdate = playerson.getLong("nextUpdateAt")
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
                var allSeasons = playerson.getJSONObject("rankedSeasons")
                val it = allSeasons.keys()
                var key = ""
                while (it.hasNext())
                    key = it.next()
                try {
                    var processedSeason = allSeasons.getJSONObject(key)
                    android.util.Log.e("@playerUpdate", processedSeason.toString())
                    if (key.toInt() >= currentSeason) {
                        val matchesPlayed =
                                when (processedSeason.has("matchesPlayed")) {
                                    true -> {
                                        processedSeason.getJSONObject("" + 10).getInt("matchesPlayed") + processedSeason.getJSONObject("" + 11).getInt("matchesPlayed")
                                        +processedSeason.getJSONObject("" + 12).getInt("matchesPlayed") + processedSeason.getJSONObject("" + 13).getInt("matchesPlayed")
                                    }
                                    false -> 0
                                }
                        var rankings = getRankings(processedSeason)
                        stamp = Timestamp(this.updated * 1000, matchesPlayed, rankings, shots, goals, saves, assists, wins, mvps)
                        if (key.toInt() > currentSeason)
                            seasons[key.toInt()] = TimestampList(stamp)
                        else {
                            seasons[key.toInt()]?.put(stamp)
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
