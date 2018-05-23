package com.example.ravi.rocketleaguecompanion.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ravi.rlcomp.custom.Timestamp
import com.example.ravi.rocketleaguecompanion.R
import com.example.ravi.rocketleaguecompanion.custom.DateAxisFormatter
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.android.synthetic.main.fragment_graph.*
import java.text.SimpleDateFormat
import java.util.*


class GraphFragment : Fragment() {

    private val currentShotPercColor = "#5Fa161"
    private val totalShotPercColor = "#F42A3B"
    private val duelGraphColor = "#FFFF00"
    private val duoGraphColor = "#5Fa1FF"
    private val standardGraphColor = "#F0a161"
    private val soloGraphColor = "#5F0061"
    private lateinit var totalShotPercentageDataSet: LineDataSet
    private lateinit var currentShotPercentageDataSet: LineDataSet
    private lateinit var skillDataSetList: Array<LineDataSet>
    private lateinit var balanceDataSet: PieDataSet
    private var initStart = true


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph, container, false)
    }

    companion object {
        fun newInstance(): GraphFragment = GraphFragment()
    }

    /**
     * puts rolling shot percentage on the line chart
     */
    private fun addTotalShotPercentage(recentStamp: Timestamp) {
        android.util.Log.e("@totl%", "Putting total% on " + recentStamp.time.toFloat() + " " + recentStamp.getTotalShotPercentage())
        val lastEntryDay = SimpleDateFormat("yyyyMMdd").format(
                Date(totalShotPercentageDataSet.getEntryForIndex(totalShotPercentageDataSet.entryCount - 1).x.toLong())).toInt()
        if (recentStamp.day == lastEntryDay)
        //if last entry was on the same day, delete it
            totalShotPercentageDataSet.removeEntryByXValue(recentStamp.day.toFloat())

        totalShotPercentageDataSet.addEntry(Entry(recentStamp.time.toFloat(), recentStamp.getTotalShotPercentage()))
    }

    /**
     * puts daily shot percentage on the line chart
     */
    private fun addCurrentShotPercentage(recentStamp: Timestamp, oldStamp: Timestamp) {
        //currentShotPercentageDataSet?.removeEntryByXValue(recentStamp.day.toFloat())
        android.util.Log.e("@cur%", "Putting current % on " + recentStamp.time.toFloat() + " " +
                recentStamp.getShotPerc(oldStamp))
        val lastEntryDay = SimpleDateFormat("yyyyMMdd").format(
                Date(currentShotPercentageDataSet.getEntryForIndex(currentShotPercentageDataSet.entryCount - 1).x.toLong())).toInt()
        if (recentStamp.day == lastEntryDay)
        //if last entry was on the same day, delete it
            currentShotPercentageDataSet.removeEntryByXValue(recentStamp.day.toFloat())

        currentShotPercentageDataSet.addEntry(Entry(recentStamp.time.toFloat(),
                recentStamp.getShotPerc(oldStamp)
        ))
    }

    /**
     * puts mmr on the line chart
     */
    private fun addMmr(recentStamp: Timestamp) {
        for (i in recentStamp.rankingList.indices) {
            android.util.Log.e("@addMmr", "Putting mmr for Queue " + i + " on " + recentStamp.time.toFloat() + " " +
                    recentStamp.rankingList[i].mmr.toFloat())
            val lastEntryDay = SimpleDateFormat("yyyyMMdd").format(
                    Date(skillDataSetList[i].getEntryForIndex(skillDataSetList[i].entryCount - 1).x.toLong())).toInt()
            if (recentStamp.day == lastEntryDay)
            //if last entry was on the same day, delete it
                skillDataSetList[i].removeEntryByXValue(recentStamp.day.toFloat())
            skillDataSetList[i].addEntry(Entry(recentStamp.time.toFloat(),
                    recentStamp.rankingList[i].mmr.toFloat()
            ))
        }
    }

    /**
     * puts all relevant data to the line charts
     */
    fun addChartEntries(recentStamp: Timestamp, oldStamp: Timestamp) {
        //recalculate Axis scale
        shotpercChart.xAxis.axisMaximum = recentStamp.time.toFloat() + (86400000L * 1L).toFloat()
        skillChart.xAxis.axisMaximum = recentStamp.time.toFloat() + (86400000L * 1L).toFloat()
        skillChart.axisLeft.axisMinimum = minOf(minOf(recentStamp.rankingList[0].mmr, recentStamp.rankingList[1].mmr, recentStamp.rankingList[2].mmr), recentStamp.rankingList[3].mmr).toFloat() - 200
        skillChart.axisLeft.axisMaximum = maxOf(maxOf(recentStamp.rankingList[0].mmr, recentStamp.rankingList[1].mmr, recentStamp.rankingList[2].mmr), recentStamp.rankingList[3].mmr).toFloat() + 200
        //init if necessary, or just fill in data
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
        skillChart.notifyDataSetChanged()
        skillChart.invalidate()
    }

    /**
     * initializes all charts and puts the first stamp on it
     */
    private fun initCharts(recentStamp: Timestamp, oldStamp: Timestamp) {
        //initalize shotPercChart
        //create each graph and set colors
        val x = recentStamp.time.toFloat()
        val xAxisStart = x - (86400000L * 1L).toFloat()
        val xAxisEnd = x + (86400000L * 1L).toFloat()


        totalShotPercentageDataSet = LineDataSet(arrayListOf(Entry(
                x, recentStamp.getTotalShotPercentage())), "Total Shot Percentage")
        totalShotPercentageDataSet.color = ColorTemplate.rgb(totalShotPercColor)
        totalShotPercentageDataSet.setCircleColor(ColorTemplate.rgb(totalShotPercColor))

        currentShotPercentageDataSet = LineDataSet(arrayListOf(Entry(
                x, recentStamp.getShotPerc(oldStamp))), "Current Shot Percentage")
        currentShotPercentageDataSet.color = ColorTemplate.rgb(currentShotPercColor)
        currentShotPercentageDataSet.setCircleColor(ColorTemplate.rgb(currentShotPercColor))


        totalShotPercentageDataSet.valueFormatter = PercentFormatter()

        currentShotPercentageDataSet.valueFormatter = PercentFormatter()

        //add graphs to the chart
        shotpercChart.data = LineData()
        shotpercChart.data.addDataSet(totalShotPercentageDataSet)
        shotpercChart.data.addDataSet(currentShotPercentageDataSet)

        //alter axises
        shotpercChart.xAxis.axisMinimum = xAxisStart
        shotpercChart.xAxis.setDrawAxisLine(true)
        shotpercChart.xAxis.setDrawLabels(true)
        shotpercChart.xAxis.axisMaximum = xAxisEnd
        shotpercChart.xAxis.valueFormatter = DateAxisFormatter()

        shotpercChart.axisRight.setDrawLabels(false)
        shotpercChart.axisRight.setDrawGridLines(false)

        shotpercChart.axisLeft.axisMinimum = 0f
        shotpercChart.axisLeft.axisMaximum = 100f
        shotpercChart.axisLeft.valueFormatter = PercentFormatter()

        shotpercChart.description.text = ""

        //initalize balance chart
        val sum = (recentStamp.goals + recentStamp.saves + recentStamp.assists).toFloat()
        //balanceChart.centerText = "Playstyle"
        balanceDataSet = PieDataSet(arrayListOf(PieEntry(recentStamp.goals / sum, "Goal%"),
                PieEntry(recentStamp.saves / sum, "Save%"),
                PieEntry(recentStamp.assists / sum, "Assist%")
        ), "")
        balanceChart.setUsePercentValues(true)
        balanceDataSet.colors = arrayListOf(ColorTemplate.rgb("#0000FF"), ColorTemplate.rgb("#00FF00"), ColorTemplate.rgb("#FF0000"))
        balanceChart.data = PieData(balanceDataSet)
        balanceChart.holeRadius = 0f
        balanceChart.setTransparentCircleAlpha(0)
        balanceChart.description.text = ""
        balanceDataSet.valueFormatter = PercentFormatter()


        //initalize ranking chart

        //init mmr datasets
        val duelDataSet = LineDataSet(arrayListOf(Entry(
                x, recentStamp.rankingList[0].mmr.toFloat()
        )), "Duel MMR")
        val duoDataSet = LineDataSet(arrayListOf(Entry(
                x, recentStamp.rankingList[1].mmr.toFloat()
        )), "Duo MMR")
        val standardDataSet = LineDataSet(arrayListOf(Entry(
                x, recentStamp.rankingList[2].mmr.toFloat()
        )), "Standard MMR")
        val soloDataSet = LineDataSet(arrayListOf(Entry(
                x, recentStamp.rankingList[3].mmr.toFloat()
        )), "Solo MMR")

        //set colors
        duelDataSet.color = ColorTemplate.rgb(duelGraphColor)
        duoDataSet.color = ColorTemplate.rgb(duoGraphColor)
        standardDataSet.color = ColorTemplate.rgb(standardGraphColor)
        soloDataSet.color = ColorTemplate.rgb(soloGraphColor)
        duelDataSet.setCircleColor(ColorTemplate.rgb(duelGraphColor))
        duoDataSet.setCircleColor(ColorTemplate.rgb(duoGraphColor))
        standardDataSet.setCircleColor(ColorTemplate.rgb(standardGraphColor))
        soloDataSet.setCircleColor(ColorTemplate.rgb(soloGraphColor))

        //set axis lable
        skillChart.xAxis.valueFormatter = DateAxisFormatter()

        skillDataSetList = arrayOf(duelDataSet, duoDataSet, standardDataSet, soloDataSet)

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

        skillChart.axisLeft.axisMinimum = minOf(minOf(recentStamp.rankingList[0].mmr, recentStamp.rankingList[1].mmr, recentStamp.rankingList[2].mmr), recentStamp.rankingList[3].mmr).toFloat() - 200
        skillChart.axisLeft.axisMaximum = maxOf(maxOf(recentStamp.rankingList[0].mmr, recentStamp.rankingList[1].mmr, recentStamp.rankingList[2].mmr), recentStamp.rankingList[3].mmr).toFloat() + 200

        skillChart.description.text = ""

    }
}
