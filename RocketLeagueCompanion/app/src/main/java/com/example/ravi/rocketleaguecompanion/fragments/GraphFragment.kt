package com.example.ravi.rocketleaguecompanion.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ravi.rlcomp.custom.Player
import com.example.ravi.rlcomp.custom.Timestamp
import com.example.ravi.rocketleaguecompanion.R
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_graph.*


class GraphFragment : Fragment() {

    private var today: Float = 0f
    private val currentShotPercColor = "#5Fa161"
    private val totalShotPercColor = "#F42A3B"
    private lateinit var player: Player
    private lateinit var totalShotPercentageDataSet: LineDataSet
    private lateinit var currentShotPercentageDataSet: LineDataSet
    private lateinit var skillDataSetList: Array<LineDataSet>
    private lateinit var balanceDataSet: PieDataSet
    private var initStart = true


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //TODO set player
        today = PlayerFragment.getDayfromLong().toInt().toFloat()
        return inflater.inflate(R.layout.fragment_graph, container, false)
    }

    companion object {
        fun newInstance(): GraphFragment = GraphFragment()
    }

    /**
     * puts rolling shot percentage on the line chart
     */
    private fun addTotalShotPercentage(recentStamp: Timestamp) {
        totalShotPercentageDataSet.addEntry(Entry(recentStamp.day.toFloat(), recentStamp.getTotalShotPercentage()))
    }

    /**
     * puts daily shot percentage on the line chart
     */
    private fun addCurrentShotPercentage(recentStamp: Timestamp, oldStamp: Timestamp) {
        currentShotPercentageDataSet.addEntry(Entry(recentStamp.day.toFloat(),
                recentStamp.getShotPerc(oldStamp)
        ))
    }

    /**
     * puts mmr on the line chart
     */
    private fun addMmr(recentStamp: Timestamp){
        for( i in 0..recentStamp.rankingList.size){
            skillDataSetList[i].addEntry(Entry(recentStamp.day.toFloat(),
                    recentStamp.rankingList[i].mmr.toFloat()
                    ))
        }
    }

    /**
     * puts all relevant data to the line charts
     */
    fun addChartEntries(recentStamp: Timestamp, oldStamp: Timestamp) {
        if (initStart) {
            initCharts(recentStamp, oldStamp)
            initStart = false
        } else {
            addTotalShotPercentage(recentStamp)
            addCurrentShotPercentage(recentStamp, oldStamp)
            addMmr(recentStamp)
        }
        //refresh chart to draw updates
        shotpercChart.notifyDataSetChanged()
        shotpercChart.invalidate()
        balanceChart.notifyDataSetChanged()
        balanceChart.invalidate()

    }

    private fun initCharts(recentStamp: Timestamp, oldStamp: Timestamp) {
        //initalize shotPercChart
        //create each graph and set colors
        val x = recentStamp.day.toFloat()
        //TODO define real xAxis scaling
        val xAxisStart = -30f
        val xAxisEnd = 30f

        totalShotPercentageDataSet = LineDataSet(arrayListOf(Entry(
                x, recentStamp.getTotalShotPercentage())), "Total Shot Percentage")
        totalShotPercentageDataSet.color = ColorTemplate.rgb(totalShotPercColor)
        totalShotPercentageDataSet.setCircleColor(ColorTemplate.rgb(totalShotPercColor))

        currentShotPercentageDataSet = LineDataSet(arrayListOf(Entry(
                x, recentStamp.getShotPerc(oldStamp))), "Total Shot Percentage")
        currentShotPercentageDataSet.color = ColorTemplate.rgb(currentShotPercColor)
        currentShotPercentageDataSet.setCircleColor(ColorTemplate.rgb(currentShotPercColor))

        //add graphs to the chart
        shotpercChart.data = LineData()
        shotpercChart.data.addDataSet(totalShotPercentageDataSet)
        shotpercChart.data.addDataSet(currentShotPercentageDataSet)

        //alter axises
        shotpercChart.xAxis.axisMinimum = xAxisStart
        shotpercChart.xAxis.setDrawAxisLine(true)
        shotpercChart.xAxis.setDrawLabels(true)
        shotpercChart.xAxis.axisMaximum = xAxisEnd

        shotpercChart.axisRight.setDrawLabels(false)
        shotpercChart.axisRight.setDrawGridLines(false)

        shotpercChart.axisLeft.axisMinimum = 0f
        shotpercChart.axisLeft.axisMaximum = 1f

        shotpercChart.description.text = ""

        //initalize balance chart
        val sum = (recentStamp.goals + recentStamp.saves + recentStamp.assists).toFloat()
        balanceChart.centerText = "Playstyle"
        balanceDataSet = PieDataSet(arrayListOf(PieEntry(recentStamp.goals / sum, "Goal%"),
                PieEntry(recentStamp.saves / sum, "Save%"),
                PieEntry(recentStamp.assists / sum, "Assist%")
        ), "")
        balanceChart.setUsePercentValues(true)
        balanceDataSet.colors = arrayListOf(ColorTemplate.rgb("#0000FF"), ColorTemplate.rgb("#00FF00"), ColorTemplate.rgb("#FF0000"))
        balanceChart.data = PieData(balanceDataSet)
        balanceChart.description.text = ""


        //initalize ranking chart

        //init mmr datasets
        val duelDataSet = LineDataSet(arrayListOf(Entry(
                x,recentStamp.rankingList[0].mmr.toFloat()
        )),"Duel MMR")
        val duoDataSet = LineDataSet(arrayListOf(Entry(
                x,recentStamp.rankingList[1].mmr.toFloat()
        )),"Duo MMR")
        val standardDataSet = LineDataSet(arrayListOf(Entry(
                x,recentStamp.rankingList[3].mmr.toFloat()
        )),"Duel MMR")
        val soloDataSet = LineDataSet(arrayListOf(Entry(
                x,recentStamp.rankingList[4].mmr.toFloat()
        )),"Duo MMR")

        skillDataSetList = arrayOf(duelDataSet,duoDataSet,standardDataSet,soloDataSet)

        //set skilChart data
        skillChart.data = LineData()
        skillChart.data.addDataSet(duelDataSet)
        skillChart.data.addDataSet(duoDataSet)
        skillChart.data.addDataSet(standardDataSet)
        skillChart.data.addDataSet(soloDataSet)

        //alter skillChart axis
        skillChart.xAxis.axisMinimum = xAxisStart
        skillChart.xAxis.setDrawAxisLine(true)
        skillChart.xAxis.setDrawLabels(true)
        skillChart.xAxis.axisMaximum = xAxisEnd

        skillChart.axisRight.setDrawLabels(false)
        skillChart.axisRight.setDrawGridLines(false)

        skillChart.axisLeft.axisMinimum = 0f
        skillChart.axisLeft.axisMaximum = 2200f

        skillChart.description.text = ""

    }
}
