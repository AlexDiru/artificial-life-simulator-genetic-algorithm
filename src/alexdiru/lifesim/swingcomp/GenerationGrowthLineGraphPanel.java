package alexdiru.lifesim.swingcomp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;

import alexdiru.lifesim.enums.GraphType;
import alexdiru.lifesim.main.GrowthOverGenerationsDataset;

public class GenerationGrowthLineGraphPanel extends JPanel {

	private static final long serialVersionUID = 1L;

    public GenerationGrowthLineGraphPanel(GrowthOverGenerationsDataset ggDataset, GraphType graphData) {
    	
    	XYDataset dataset;
    	switch (graphData) {
    	case MINIMUM:
    		dataset = ggDataset.toXYSeriesCollectionMinimumOnly();
    		break;
    	case AVERAGE:
    		dataset = ggDataset.toXYSeriesCollectionAverageOnly();
    		break;
    	case MAXIMUM:
    	default:
    		dataset = ggDataset.toXYSeriesCollectionMaximumOnly();
    		break;
    	}
    	
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        add(chartPanel);
    }
    /**
     * Creates a sample chart.
     *
-     * @param dataset  the dataset.
     *
     * @return The chart.
     */
    private static JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createXYLineChart("Fitness Over Generations","Generation","Fitness Value",dataset,PlotOrientation.VERTICAL,true,true,false);

        chart.setBackgroundPaint(Color.white);

        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShape(0,new Ellipse2D.Double(-3, -3, 6, 6));
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);

        ((NumberAxis) plot.getRangeAxis()).setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        return chart;

    }
}
