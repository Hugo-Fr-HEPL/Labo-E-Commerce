package datamining.application;

import datamining.package_reseaux.other.GetDirectory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class jFreeChart extends javax.swing.JFrame {
    public static void ShowScatterPoint(String title, String x, String y, double[][] dataset) {
        XYSeries serieObs = new XYSeries("Relations " + x + "-" + y);
        for (int i = 0; i < dataset.length; i++)
            serieObs.add(dataset[i][0], dataset[i][1]);
        XYSeriesCollection ds = new XYSeriesCollection();
        ds.addSeries(serieObs);

        DisplayGraph(title, ChartFactory.createScatterPlot(title, y, x, ds, PlotOrientation.VERTICAL, true, true, false));
    }

    public static void ShowHistogram(String title, String x, String y, double[] data, String[] names) {
        HistogramDataset hd = new HistogramDataset();
        
        hd.addSeries(title, data, names.length);

        DisplayGraph(title, ChartFactory.createHistogram(title, y, x, hd, PlotOrientation.VERTICAL, true, true, false));
    }
    
    public static void ShowHistogramComp(String title, String x, String y, String[] name, String[] comp, Double[][] data) {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        
        for(int i = 0; i < name.length; i++)
            for(int j = 0; j < comp.length; j++)
                ds.addValue(data[i][j], comp[j], name[i]);

        DisplayGraph(title, ChartFactory.createBarChart(title, y, x, ds, PlotOrientation.VERTICAL, true, true, false));
    }
    
    public static void ShowBoxPlot(String title, List<String> x, List<List> data) {
        DefaultBoxAndWhiskerCategoryDataset ds = new DefaultBoxAndWhiskerCategoryDataset();

        for(int j = 0; j < data.size(); j++)
            ds.add(data.get(j), x.get(j), "");
        
        DisplayGraph(title, ChartFactory.createBoxAndWhiskerChart(title, "destination", "poids", ds, true));
    }
    
    public static void ShowPieShart(String title, List<String> x, List<List> data) {
        DefaultPieDataset dp = new DefaultPieDataset();
        Double d = 0.0;
        for(int j = 0; j < data.size(); j++)
        {
            for(int k = 0; k < data.get(j).size(); k++)
                d += (Double) data.get(j).get(k);
                
            dp.setValue(x.get(j), d);
        }
        DisplayGraph(title, ChartFactory.createPieChart(title, dp, true, true, true));
    }

    public static void DisplayGraph(String title, JFreeChart jfc) {
        JFrame frame = new JFrame();
        frame.add(new ChartPanel(jfc));

        frame.pack();
        frame.setTitle(title);
        frame.setVisible(true);
    }

    public static void SavePng(String title, JFreeChart jfc) {
        try {
            ChartUtils.saveChartAsPNG(new File(GetDirectory.FileDir(title +".png")), jfc, 800, 700);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
