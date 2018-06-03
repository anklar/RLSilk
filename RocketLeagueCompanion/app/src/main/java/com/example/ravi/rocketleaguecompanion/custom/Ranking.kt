package com.example.ravi.rocketleaguecompanion.custom

import java.io.Serializable

/**
 * an individual ranking in a queue alone
 */

class Ranking(var mmr: Int, var tier: Int, var div: Int) : Serializable
