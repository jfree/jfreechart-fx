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
 * ---------------------
 * TooltipHandlerFX.java
 * ---------------------
 * (C) Copyright 2014-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.fx.interaction;

import javafx.scene.input.MouseEvent;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.fx.ChartCanvas;

/**
 * Handles the updating of tooltips on a {@link ChartCanvas}.
 */
public class TooltipHandlerFX extends AbstractMouseHandlerFX 
        implements MouseHandlerFX {
    
    /**
     * Creates a new instance with the specified ID.
     * 
     * @param id  the handler id ({@code null} not permitted).
     */
    public TooltipHandlerFX(String id) {
        super(id, false, false, false, false);
    }

    /**
     * Handles a mouse moved event by updating the tooltip.
     * 
     * @param canvas  the chart canvas ({@code null} not permitted).
     * @param e  the mouse event.
     */
    @Override
    public void handleMouseMoved(ChartCanvas canvas, MouseEvent e) {
        if (canvas.getChart() == null || !canvas.isTooltipEnabled()) {
            return;
        }
        String text = getTooltipText(canvas, e.getX(), e.getY());
        canvas.setTooltip(text, e.getScreenX(), e.getScreenY());
    }
    
    /**
     * Returns the tooltip text.
     * 
     * @param canvas  the canvas that is displaying the chart.
     * @param x  the x-coordinate of the mouse pointer.
     * @param y  the y-coordinate of the mouse pointer.
     * 
     * @return String The tooltip text (possibly {@code null}).
      */
    private String getTooltipText(ChartCanvas canvas, double x, double y) {
        ChartRenderingInfo info = canvas.getRenderingInfo();
        if (info == null) {
            return null;
        }
        EntityCollection entities = info.getEntityCollection();
        if (entities == null) {
            return null;
        }
        ChartEntity entity = entities.getEntity(x, y);
        if (entity == null) {
            return null;
        }
        return entity.getToolTipText();
    }
    
}
