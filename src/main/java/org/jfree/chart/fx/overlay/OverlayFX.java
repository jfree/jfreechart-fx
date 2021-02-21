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
 * --------------
 * OverlayFX.java
 * --------------
 * (C) Copyright 2016-2021, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.fx.overlay;

import java.awt.Graphics2D;
import org.jfree.chart.fx.ChartCanvas;
import org.jfree.chart.panel.Overlay;

/**
 * An overlay that can be added to a {@link ChartCanvas}.
 */
public interface OverlayFX extends Overlay {

    /**
     * Paints the content of the overlay onto the specified chart canvas.
     *
     * @param g2  the graphics target ({@code null} not permitted).
     * @param chartCanvas  the chart canvas ({@code null} not permitted).
     */
    public void paintOverlay(Graphics2D g2, ChartCanvas chartCanvas);

}
