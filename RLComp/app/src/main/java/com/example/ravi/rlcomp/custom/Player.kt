package com.example.ravi.rlcomp.custom

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

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
    var seasons: LinkedHashMap<Int,TimestampList> = LinkedHashMap()
    var updated: Long = 0
    var nextUpdate: Long = 0
    var currentSeason: Int = 0

    fun toText():String{
        var bob:StringBuilder = StringBuilder()
        bob.append(""" name: $name" wins: ${wins}goals: ${goals}mvps: ${mvps}saves: ${saves}shots: ${shots}assists: $assists""")
        for(key:Int in seasons.keys){
            bob.append(" Season: ")
            bob.append(key)
            bob.append("    ")
            bob.append(seasons[key]?.getGoals())
        }
        return bob.toString()

    }
    fun update(playerson : JSONObject){
        try {
            if (playerson.has("displayName"))
                this.name = playerson.getString("displayName")
            if (playerson.has("avatar"))
                this.avatarUrl = playerson.getString("avatar")
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
                this.profileUrl = playerson.getString("profileUrl")
            if (playerson.has("rankedSeasons")) {
                var allSeasons = playerson.getJSONObject("rankedSeasons")
                for (key: String in allSeasons.keys()) {
                    var processedSeason = allSeasons.getJSONObject(key)
                    if (key.toInt() >= currentSeason) {
                        var matchesPlayed = processedSeason.getJSONObject("" + 10).getInt("matchesPlayed") + processedSeason.getJSONObject("" + 11).getInt("matchesPlayed")
                        +processedSeason.getJSONObject("" + 12).getInt("matchesPlayed") + processedSeason.getJSONObject("" + 13).getInt("matchesPlayed")
                        var rankings = getRankings(processedSeason)
                        if (key.toInt() > currentSeason)
                            seasons[key.toInt()] = TimestampList(Timestamp(this.updated, matchesPlayed, rankings, shots, goals,
                                    saves, assists,wins, mvps))
                        else {
                            seasons[key.toInt()]!!.add(Timestamp(this.updated, matchesPlayed, rankings, shots, goals,
                                    saves, assists,wins, mvps))
                        }
                    }
                }
            }
        } catch (error: JSONException) {
            android.util.Log.e("@playerUpdate","Parsing of the JSONResponse for updating the player failed with an JSONException " +error.message )
        }
    }
    private fun getRankings(processedSeason:JSONObject):Array<Ranking>{
        return arrayOf(Ranking(processedSeason.getJSONObject(""+10).getInt("rankPoints"),
                processedSeason.getJSONObject(""+10).getInt("tier"),
                processedSeason.getJSONObject(""+10).getInt("division")),
                Ranking(processedSeason.getJSONObject(""+11).getInt("rankPoints"),
                        processedSeason.getJSONObject(""+11).getInt("tier"),
                        processedSeason.getJSONObject(""+11).getInt("division")),
                Ranking(processedSeason.getJSONObject(""+12).getInt("rankPoints"),
                        processedSeason.getJSONObject(""+12).getInt("tier"),
                        processedSeason.getJSONObject(""+12).getInt("division")),
                Ranking(processedSeason.getJSONObject(""+13).getInt("rankPoints"),
                        processedSeason.getJSONObject(""+13).getInt("tier"),
                        processedSeason.getJSONObject(""+13).getInt("division")))

    }
}
