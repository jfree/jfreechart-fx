/* ================================================
 * JFreeChart-FX : JavaFX extensions for JFreeChart
 * ================================================
 *
 * (C) Copyright 2017-2021, by Object Refinery Limited and Contributors.
 *
 * Project Info:  https://github.com/jfree/jfreechart-fx
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * --------------------
 * ScrollHandlerFX.java
 * --------------------
 * (C) Copyright 2014-2021, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.fx.interaction;

import java.awt.geom.Point2D;
import javafx.scene.input.ScrollEvent;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartCanvas;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.Zoomable;

/**
 * Handles scroll events (mouse wheel etc) on a {@link ChartCanvas}.
 */
public class ScrollHandlerFX extends AbstractMouseHandlerFX 
        implements MouseHandlerFX {

    /** The zoom factor. */
    private double zoomFactor = 0.1;
    
    /**
     * Creates a new instance with the specified ID.
     * 
     * @param id  the handler ID ({@code null} not permitted).
     */
    public ScrollHandlerFX(String id) {
        super(id, false, false, false, false);
        this.zoomFactor = 0.1;
    };

    /**
     * Returns the zoom factor.  The default value is 0.10 (ten percent).
     * 
     * @return The zoom factor. 
     */
    public double getZoomFactor() {
        return this.zoomFactor;
    }

    /**
     * Sets the zoom factor (a percentage amount by which the mouse wheel 
     * movement will change the chart size).
     * 
     * @param zoomFactor  the zoom factor.
     */
    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }
    
    @Override
    public void handleScroll(ChartCanvas canvas, ScrollEvent e) {
        JFreeChart chart = canvas.getChart();
        if (chart == null) {
            return;
        }
        Plot plot = chart.getPlot();
        if (plot instanceof Zoomable) {
            Zoomable zoomable = (Zoomable) plot;
            handleZoomable(canvas, zoomable, e);
        }
        else if (plot instanceof PiePlot) {
            PiePlot pp = (PiePlot) plot;
            pp.handleMouseWheelRotation((int) e.getDeltaY());
        }
    }
    
    /**
     * Handle the case where a plot implements the {@link Zoomable} interface.
     *
     * @param canvas  the chart canvas.
     * @param zoomable  the zoomable plot.
     * @param e  the mouse wheel event.
     */
    private void handleZoomable(ChartCanvas canvas, Zoomable zoomable, 
            ScrollEvent e) {
    	if (canvas.getChart() == null) {
    	    return;
    	}
        int clicks = (int) e.getDeltaY();
        if (clicks == 0) {
            // don't zoom on events with no wheel movement (occurs on Linux)
            return;
        }
        // don't zoom unless the mouse pointer is in the plot's data area
        ChartRenderingInfo info = canvas.getRenderingInfo();
        PlotRenderingInfo pinfo = info.getPlotInfo();
        Point2D p = new Point2D.Double(e.getX(), e.getY());
        if (pinfo.getDataArea().contains(p)) {
            Plot plot = (Plot) zoomable;
            // do not notify while zooming each axis
            boolean notifyState = plot.isNotify();
            plot.setNotify(false);
            double zf = 1.0 + this.zoomFactor;
            if (clicks < 0) {
                zf = 1.0 / zf;
            }
            if (canvas.isDomainZoomable()) {
                zoomable.zoomDomainAxes(zf, pinfo, p, true);
            }
            if (canvas.isRangeZoomable()) {
                zoomable.zoomRangeAxes(zf, pinfo, p, true);
            }
            plot.setNotify(notifyState);  // this generates the change event too
        } 
    }

}
