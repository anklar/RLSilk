package com.example.ravi.rocketleaguecompanion

import android.content.Intent
import android.graphics.Color
import android.graphics.Color.*
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.ravi.rocketleaguecompanion.R.drawable.*
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment

class IntroActivity : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //adding slides
        addSlide(AppIntroFragment.newInstance("Welcome to Rocket League Companion", Typeface.DEFAULT.toString(),
                "Keep track of your progression and statistics in Rocket League", Typeface.DEFAULT.toString(),
                rank19, parseColor("#800090"), WHITE, WHITE))
        addSlide(AppIntroFragment.newInstance("How to start", Typeface.DEFAULT.toString(),
                "Enter your account name and tap on it in the list", Typeface.DEFAULT.toString(),
                rank3, parseColor("#993d00"), WHITE, WHITE))
        addSlide(AppIntroFragment.newInstance("The Overview", Typeface.DEFAULT.toString(),
                "Get a quick overview of your ranks and stats on the first tab", Typeface.DEFAULT.toString(),
                rank6, parseColor("#424242"), WHITE, WHITE))
        addSlide(AppIntroFragment.newInstance("The Details", Typeface.DEFAULT.toString(),
                "Inspect your shooting percentage,rank progression and playstyle", Typeface.DEFAULT.toString(),
                rank9, parseColor("#d6a800"), WHITE, WHITE))
        addSlide(AppIntroFragment.newInstance("You want to change players?", Typeface.DEFAULT.toString(),
                "Hit Search Player on the Overview. All your collected data is save", Typeface.DEFAULT.toString(),
                rank12, parseColor("#008fb3"), WHITE, WHITE))
        addSlide(AppIntroFragment.newInstance("Ready. Steady. Go!", Typeface.DEFAULT.toString(),
                "That's all. Select your account and find a match!", Typeface.DEFAULT.toString(),
                rank15, Color.parseColor("#00005f"),
                WHITE,
                WHITE))
        skipButtonEnabled = false
    }

    /**
     * sends the user back to the SearchActivity when Intro is done
     */
    override fun onDonePressed(currentFragment: Fragment) {
        super.onDonePressed(currentFragment)
        //return to SearchActivity when finished
        val intent = Intent(this, SearchActivity::class.java) // Call the AppIntro java class
        startActivity(intent)
    }
}
