package com.example.ravi.rlcomp.custom

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

/**
 * Created by Ravi on 15.03.2018.
 */

class Player {
    var id: String? = null
    var name: String? = null
    var platform: Int = 0
    var avatarUrl: String? = null
    var profileUrl: String? = null
    var wins: Int = 0
    var goals: Int = 0
    var mvps: Int = 0
    var saves: Int = 0
    var shots: Int = 0
    var assists: Int = 0
    var seasons: ArrayList<Season>? = null
    var updated: Long = 0
    var nextUpdate: Long = 0

    fun currentSeason(): Season {
        return seasons!![seasons!!.size - 1]
    }

    fun mergeWithPlayer(recent: Player): Player {
        for (i in 0 until recent.seasons!!.size) {
            recent.seasons!![i].solo.putAll(this.seasons!![i].solo)
        }
        return recent
    }

    companion object {

        fun fromJSON(response: JSONObject): Player {
            val player = Player()
            try {
                if (response.has("displayName"))
                    player.name = response.getString("displayName")
                if (response.has("avatar"))
                    player.avatarUrl = response.getString("avatar")
                if (response.has("updatedAt"))
                    player.updated = response.getLong("updatedAt")
                if (response.has("nextUpdateAt"))
                    player.nextUpdate = response.getLong("nextUpdateAt")
                if (response.has("stats")) {
                    val stats = response.getJSONObject("stats")
                    player.wins = stats.getInt("wins")
                    player.goals = stats.getInt("goals")
                    player.mvps = stats.getInt("mvps")
                    player.saves = stats.getInt("saves")
                    player.shots = stats.getInt("shots")
                    player.assists = stats.getInt("assists")
                }
                if (response.has("profileUrl"))
                    player.profileUrl = response.getString("profileUrl")
                if (response.has("rankedSeasons")) {
                    var season = response.getJSONObject("rankedSeasons")
                    var i = 0
                    while (season.has("" + i + 1))
                        i++
                    season = season.getJSONObject("" + i)
                    val duel = season.getJSONObject("10")
                    val duo = season.getJSONObject("11")
                    val solo = season.getJSONObject("12")
                    val standard = season.getJSONObject("13")
                    player.currentSeason().setStats(
                            Ranking(solo.getInt("rankedPoints"), solo.getInt("matchesPlayed"), solo.getInt("tier"), solo.getInt("division")), Ranking(duel.getInt("rankedPoints"), duel.getInt("matchesPlayed"), duel.getInt("tier"), duel.getInt("division")), Ranking(duo.getInt("rankedPoints"), duo.getInt("matchesPlayed"), duo.getInt("tier"), duo.getInt("division")), Ranking(standard.getInt("rankedPoints"), standard.getInt("matchesPlayed"), standard.getInt("tier"), standard.getInt("division")), player.updated)
                }
            } catch (error: JSONException) {

            } finally {
                return player
            }
        }
    }
}
