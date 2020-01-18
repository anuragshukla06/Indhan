package com.example.indhan;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;

public class GraphActivity extends AppCompatActivity {









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        GraphView mileageGraph = (GraphView) findViewById(R.id.MileageGraph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        mileageGraph.addSeries(series);
        series.setTitle("Mileage");
        series.setColor(Color.MAGENTA);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);
        mileageGraph.getLegendRenderer().setVisible(true);
        mileageGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        GraphView distanceGraph = (GraphView) findViewById(R.id.DistanceGraph);
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 3),
                new DataPoint(1, 3),
                new DataPoint(2, 6),
                new DataPoint(3, 2),
                new DataPoint(4, 5)
        });
        distanceGraph.addSeries(series2);
        series2.setTitle("Distance");
        series2.setColor(Color.RED);
        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(10);
        series2.setThickness(8);
        distanceGraph.getLegendRenderer().setVisible(true);
        distanceGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        GraphView fuelGraph = (GraphView) findViewById(R.id.FuelGraph);
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 3),
                new DataPoint(2, 3),
                new DataPoint(3, 5),
                new DataPoint(3, 2),
                new DataPoint(5, 4)
        });
        fuelGraph.addSeries(series3);
        series3.setTitle("Fuel");
        series3.setColor(Color.BLACK);
        series3.setDrawDataPoints(true);
        series3.setDataPointsRadius(10);
        series3.setThickness(8);
        fuelGraph.getLegendRenderer().setVisible(true);
        fuelGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }


}
