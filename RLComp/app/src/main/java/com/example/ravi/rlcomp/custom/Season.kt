package com.example.ravi.rlcomp.custom

import java.util.HashMap

/**
 * Created by Ravi on 15.03.2018.
 */

class Season(val number: Int, solo: Ranking, duel: Ranking, duo: Ranking, standard: Ranking, timestamp: Long) {
    val solo: HashMap<Long, Ranking>
    val duel: HashMap<Long, Ranking>
    val duo: HashMap<Long, Ranking>
    val standard: HashMap<Long, Ranking>

    init {
        this.solo = HashMap()
        this.solo[timestamp] = solo
        this.duel = HashMap()
        this.duel[timestamp] = duel
        this.duo = HashMap()
        this.duo[timestamp] = duo
        this.standard = HashMap()
        this.standard[timestamp] = standard
    }

    fun setStats(solo: Ranking, duel: Ranking, duo: Ranking, standard: Ranking, timestamp: Long) {
        setSolo(solo, timestamp)
        setDuel(duel, timestamp)
        setDuo(duo, timestamp)
        setStandard(standard, timestamp)
    }

    fun setSolo(solo: Ranking, timestamp: Long) {
        this.solo[timestamp] = solo
    }

    fun setDuel(duel: Ranking, timestamp: Long) {
        this.duel[timestamp] = duel
    }

    fun setDuo(duo: Ranking, timestamp: Long) {
        this.duo[timestamp] = duo
    }

    fun setStandard(standard: Ranking, timestamp: Long) {
        this.standard[timestamp] = standard
    }
}
