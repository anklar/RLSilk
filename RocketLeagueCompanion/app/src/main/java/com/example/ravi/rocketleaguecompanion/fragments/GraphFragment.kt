package com.example.ravi.rocketleaguecompanion.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ravi.rlcomp.custom.Player
import com.example.ravi.rlcomp.custom.Timestamp
import com.example.ravi.rocketleaguecompanion.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_graph.*
import java.util.*
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry




class GraphFragment : Fragment() {

    private var today:Float = 0f
    private val currentShotPercColor = "#5Fa161"
    private val totalShotPercColor = "#F42A3B"
    private lateinit var player: Player
    private lateinit var totalShotPercentageDataSet: LineDataSet
    private lateinit var currentShotPercentageDataSet: LineDataSet
    private lateinit var balanceDataSet: PieDataSet

    private var dummyCount = 1

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

    fun addChartEntries(recentStamp: Timestamp?,oldStamp: Timestamp){
        if(recentStamp!= null) {
            //TODO assign correct Values for x Axis
            // val x = (dummyCount).toFloat()
            dummyCount++
            var dummyY = Random().nextFloat()


            val x = PlayerFragment.getDayfromLong(recentStamp.time) - today
            if (shotpercChart.data != null) {
                //add points to existing charts
                /*TODO reactivate actual values           totalShotPercentageDataSet.addEntry(Entry(x,recentStamp.getTotalShotPercentage()))
                       currentShotPercentageDataSet.addEntry(Entry(x,recentStamp.getShotPerc(getDayStartingStamp()))) */
                totalShotPercentageDataSet.addEntry(Entry(x, dummyY))
                currentShotPercentageDataSet.addEntry(Entry(x, dummyY / 2))
            } else {
                initCharts(recentStamp,oldStamp)
            }
            //refresh chart to draw updates
            shotpercChart.notifyDataSetChanged()
            shotpercChart.invalidate()
            balanceChart.notifyDataSetChanged()
            balanceChart.invalidate()
        }
    }

    private fun initCharts(recentStamp: Timestamp, oldStamp:Timestamp){
        //initalize shotPercChart
        //create each graph and set colors
        var dummyY = Random().nextFloat()
        val x = (dummyCount).toFloat()


        //TODO val x = PlayerFragment.getDayfromLong(recentStamp.time) -today
        //TODO totalShotPercentageDataSet = LineDataSet(arrayListOf(Entry(x,recentStamp.getTotalShotPercentage())), "Total Shot Percentage")
        totalShotPercentageDataSet = LineDataSet(arrayListOf(Entry(x,dummyY)), "Total Shot Percentage")
        totalShotPercentageDataSet.color = ColorTemplate.rgb(totalShotPercColor)
        totalShotPercentageDataSet.setCircleColor(ColorTemplate.rgb(totalShotPercColor))

        currentShotPercentageDataSet = LineDataSet(arrayListOf(Entry(x,recentStamp.getShotPerc(oldStamp))), "Total Shot Percentage")
        currentShotPercentageDataSet = LineDataSet(arrayListOf(Entry(x,dummyY/2)), "Total Shot Percentage")
        currentShotPercentageDataSet.color = ColorTemplate.rgb(currentShotPercColor)
        currentShotPercentageDataSet.setCircleColor(ColorTemplate.rgb(currentShotPercColor))

        //add graphs to the chart
        shotpercChart.data = LineData()
        shotpercChart.data.addDataSet(totalShotPercentageDataSet)
        shotpercChart.data.addDataSet(currentShotPercentageDataSet)

        //alter axises
        shotpercChart.xAxis.axisMinimum = -30f
        shotpercChart.xAxis.setDrawAxisLine(true)
        shotpercChart.xAxis.setDrawLabels(true)
        shotpercChart.xAxis.axisMaximum = 30f

        shotpercChart.axisRight.setDrawLabels(false)
        shotpercChart.axisRight.setDrawGridLines(false)

        shotpercChart.axisLeft.axisMinimum = 0f
        shotpercChart.axisLeft.axisMaximum = 1f

        shotpercChart.description.text = ""

        //initalize balance chart
        val sum = (recentStamp.goals+recentStamp.saves+recentStamp.assists).toFloat()
        balanceChart.centerText = "Playstyle"
        balanceDataSet = PieDataSet(arrayListOf(PieEntry(recentStamp.goals/sum,"Goal%"),
                PieEntry(recentStamp.saves/sum,"Save%"),
                PieEntry(recentStamp.assists/sum, "Assist%")
        ),"")
        balanceChart.setUsePercentValues(true)
        balanceDataSet.colors = arrayListOf(ColorTemplate.rgb("#0000FF"),ColorTemplate.rgb("#00FF00"),ColorTemplate.rgb("#FF0000"))
        balanceChart.data = PieData(balanceDataSet)
        balanceChart.description.text = ""



        //initalize ranking chart



    }
}
