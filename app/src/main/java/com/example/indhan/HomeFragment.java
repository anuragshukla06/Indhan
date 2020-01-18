package com.example.indhan;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_graph, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GraphView mileageGraph = getView().findViewById(R.id.MileageGraph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
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

        GraphView distanceGraph = getView().findViewById(R.id.DistanceGraph);
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[]{
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

        GraphView fuelGraph = getView().findViewById(R.id.FuelGraph);
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[]{
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
