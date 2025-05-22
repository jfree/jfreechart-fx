/* ================================================
 * JFreeChart-FX : JavaFX extensions for JFreeChart
 * ================================================
 *
 * (C) Copyright 2017-present, by David Gilbert and Contributors.
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
 * AnchorHandlerFX.java
 * --------------------
 * (C) Copyright 2014-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.fx.interaction;

import java.awt.geom.Point2D;
import javafx.scene.input.MouseEvent;
import org.jfree.chart.fx.ChartCanvas;

/**
 * Handles mouse clicks on the {@link ChartCanvas} by updating the anchor and 
 * redrawing the chart.
 */
public class AnchorHandlerFX extends AbstractMouseHandlerFX {
    
    /** Records the mouse down location. */
    private Point2D mousePressedPoint;
    
    /**
     * Creates a new instance.
     * 
     * @param id  the id ({@code null} not permitted).
     */
    public AnchorHandlerFX(String id) {
        super(id, false, false, false, false);
    }
    
    /**
     * Handles a mouse pressed event by recording the location of the mouse
     * pointer (so that later we can check that the click isn't part of a
     * drag).
     * 
     * @param canvas  the chart canvas.
     * @param e  the mouse event.
     */
    @Override
    public void handleMousePressed(ChartCanvas canvas, MouseEvent e) {
        this.mousePressedPoint = new Point2D.Double(e.getX(), e.getY());
    }

    /**
     * Handles a mouse clicked event by setting the anchor point for the
     * canvas and redrawing the chart (the anchor point is a reference point
     * used by the chart to determine crosshair lines).
     * 
     * @param canvas  the chart canvas ({@code null} not permitted).
     * @param e  the mouse event ({@code null} not permitted).
     */
    @Override
    public void handleMouseClicked(ChartCanvas canvas, MouseEvent e) {
        if (this.mousePressedPoint == null) {
            return;
        }
        Point2D currPt = new Point2D.Double(e.getX(), e.getY());
        if (this.mousePressedPoint.distance(currPt) < 2) {
            canvas.setAnchor(currPt);
        }
        this.mousePressedPoint = null;
    }
    
}

