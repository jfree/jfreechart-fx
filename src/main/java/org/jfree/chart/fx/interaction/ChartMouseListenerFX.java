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
 * -------------------------
 * ChartMouseListenerFX.java
 * -------------------------
 * (C) Copyright 2014-2021, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.fx.interaction;

import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.fx.ChartViewer;

/**
 * A mouse listener that can receive event notifications from a (JavaFX) 
 * {@link ChartViewer} instance.  This interface is equivalent to the 
 * {@link ChartMouseListener} interface used for charts in Swing.
 */
public interface ChartMouseListenerFX {
    
    /**
     * Receives notification of a mouse click on a chart.
     * 
     * @param event  event information (never {@code null}). 
     */
    void chartMouseClicked(ChartMouseEventFX event);

    /**
     * Receives notification of a mouse move event on a chart.
     *
     * @param event  event information (never {@code null}). 
     */
    void chartMouseMoved(ChartMouseEventFX event);

}

