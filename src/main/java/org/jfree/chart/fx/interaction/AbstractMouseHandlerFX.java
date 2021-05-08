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
 * ---------------------------
 * AbstractMouseHandlerFX.java
 * ---------------------------
 * (C) Copyright 2014-2021, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.fx.interaction;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import org.jfree.chart.fx.ChartCanvas;

/**
 * A base class that can be used to implement the {@link MouseHandlerFX}
 * interface.
 */
public class AbstractMouseHandlerFX implements MouseHandlerFX {

    /** The handler id. */
    private final String id;
    
    /** 
     * A flag used to enable/disable the handler (usually temporarily, removing
     * a handler is the preferred way to disable it permanently).
     */
    private boolean enabled;
    
    /** Requires ALT key modifier? */
    private final boolean altKey;
    
    /** Requires CTRL key modifier? */
    private final boolean ctrlKey;

    /** Requires META key modifier? */
    private final boolean metaKey;
    
    /** Requires SHIFT key modifier? */
    private final boolean shiftKey;
    
    /**
     * Creates a new instance.  The modifier keys are used to select a 
     * mouse handler to be the current "live" handler (when a handler is
     * used as an auxiliary handler, the modifier keys are not relevant).
     * 
     * @param id  the handler id ({@code null} not permitted).
     * @param altKey  require ALT key modifier?
     * @param ctrlKey  require ALT key modifier?
     * @param metaKey  require ALT key modifier?
     * @param shiftKey   require ALT key modifier?
     */
    public AbstractMouseHandlerFX(String id, boolean altKey, boolean ctrlKey, 
            boolean metaKey, boolean shiftKey) {
        if (id == null) throw new IllegalArgumentException("id must not be null");
        this.id = id;
        this.enabled = true;
        this.altKey = altKey;
        this.ctrlKey = ctrlKey;
        this.metaKey = metaKey;
        this.shiftKey = shiftKey;
    }
    
    /**
     * Returns the ID for the handler.
     * 
     * @return The ID (never {@code null}).
     */
    @Override
    public String getID() {
        return this.id;
    }
    
    /**
     * Returns the flag that controls whether or not the handler is enabled.
     * 
     * @return A boolean. 
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
    
    /**
     * Sets the flag that controls the enabled/disabled state of the handler.
     * 
     * @param enabled  the new flag value. 
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * Returns {@code true} if the specified mouse event has modifier
     * keys that match this handler.
     * 
     * @param e  the mouse event ({@code null} not permitted).
     * 
     * @return A boolean. 
     */
    @Override
    public boolean hasMatchingModifiers(MouseEvent e) {
        boolean b = true;
        b = b && (this.altKey == e.isAltDown());
        b = b && (this.ctrlKey == e.isControlDown());
        b = b && (this.metaKey == e.isMetaDown());
        b = b && (this.shiftKey == e.isShiftDown());
        return b;
    }
    
    /**
     * Handles a mouse moved event.  This implementation does nothing,
     * override the method if required.
     * 
     * @param canvas  the canvas ({@code null} not permitted).
     * @param e  the event ({@code null} not permitted).
     */
    @Override
    public void handleMouseMoved(ChartCanvas canvas, MouseEvent e) {
        // does nothing unless overridden
    }
    
    /**
     * Handles a mouse clicked event.  This implementation does nothing,
     * override the method if required.
     * 
     * @param canvas  the canvas ({@code null} not permitted).
     * @param e  the event ({@code null} not permitted).
     */
    @Override
    public void handleMouseClicked(ChartCanvas canvas, MouseEvent e) {
        // does nothing unless overridden
    }

    /**
     * Handles a mouse pressed event.  This implementation does nothing,
     * override the method if required.
     * 
     * @param canvas  the canvas ({@code null} not permitted).
     * @param e  the event ({@code null} not permitted).
     */
    @Override
    public void handleMousePressed(ChartCanvas canvas, MouseEvent e) {
        // does nothing unless overridden        
    }
    
    /**
     * Handles a mouse dragged event.  This implementation does nothing,
     * override the method if required.
     * 
     * @param canvas  the canvas ({@code null} not permitted).
     * @param e  the event ({@code null} not permitted).
     */
    @Override
    public void handleMouseDragged(ChartCanvas canvas, MouseEvent e) {
        // does nothing unless overridden
    }
    
    /**
     * Handles a mouse released event.  This implementation does nothing,
     * override the method if required.
     * 
     * @param canvas  the canvas ({@code null} not permitted).
     * @param e  the event ({@code null} not permitted).
     */
    @Override
    public void handleMouseReleased(ChartCanvas canvas, MouseEvent e) {
        // does nothing unless overridden
    }

    /**
     * Handles a scroll event.  This implementation does nothing,
     * override the method if required.
     * 
     * @param canvas  the canvas ({@code null} not permitted).
     * @param e  the event ({@code null} not permitted).
     */
    @Override
    public void handleScroll(ChartCanvas canvas, ScrollEvent e) {
        // does nothing unless overridden
    }
    
}
