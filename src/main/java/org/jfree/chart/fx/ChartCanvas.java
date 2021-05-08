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
 * ----------------
 * ChartCanvas.java
 * ----------------
 * (C) Copyright 2014-2021, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.fx;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.FontSmoothingType;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.fx.interaction.AnchorHandlerFX;
import org.jfree.chart.fx.interaction.ChartMouseListenerFX;
import org.jfree.chart.fx.interaction.DispatchHandlerFX;
import org.jfree.chart.fx.interaction.MouseHandlerFX;
import org.jfree.chart.fx.interaction.PanHandlerFX;
import org.jfree.chart.fx.interaction.ScrollHandlerFX;
import org.jfree.chart.fx.interaction.TooltipHandlerFX;
import org.jfree.chart.fx.overlay.OverlayFX;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.swing.ChartMouseEvent;
import org.jfree.chart.swing.OverlayChangeEvent;
import org.jfree.chart.swing.OverlayChangeListener;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.FXHints;

/**
 * A canvas for displaying a {@link JFreeChart} in JavaFX.  You can use the
 * canvas directly to display charts, but usually the {@link ChartViewer}
 * class (which embeds a canvas) is a better option as it provides additional
 * features.
 * <p>
 * The canvas installs several default mouse handlers, if you don't like the
 * behaviour provided by these you can retrieve the handler by ID and
 * disable or remove it (the IDs are "tooltip", "scroll", "anchor", "pan" and 
 * "dispatch").</p>
 * <p>
 * The {@code FontSmoothingType} for the underlying {@code GraphicsContext} is
 * set to {@code FontSmoothingType.LCD} as this gives better results on the 
 * systems we've tested on.  You can modify this using 
 * {@code getGraphicsContext().setFontSmoothingType(yourValue)}.</p>
 *
 */
public class ChartCanvas extends Canvas implements ChartChangeListener,
        OverlayChangeListener {
    
    /** The chart being displayed in the canvas. */
    private JFreeChart chart;
    
    /**
     * The graphics drawing context (will be an instance of FXGraphics2D).
     */
    private Graphics2D g2;
   
    /** 
     * The anchor point (can be null) is usually updated to reflect the most 
     * recent mouse click and is used during chart rendering to update 
     * crosshairs belonging to the chart.  
     */
    private Point2D anchor;
    
    /** The chart rendering info from the most recent drawing of the chart. */
    private ChartRenderingInfo info;
    
    /** The tooltip object for the canvas (can be null). */
    private Tooltip tooltip;
    
    /** 
     * A flag that controls whether or not tooltips will be generated from the
     * chart as the mouse pointer moves over it.
     */
    private boolean tooltipEnabled;
    
    /** Storage for registered chart mouse listeners. */
    private transient List<ChartMouseListenerFX> chartMouseListeners;

    /** The current live handler (can be null). */
    private MouseHandlerFX liveHandler;
    
    /** 
     * The list of available live mouse handlers (can be empty but not null). 
     */
    private List<MouseHandlerFX> availableMouseHandlers;
    
    /** The auxiliary mouse handlers (can be empty but not null). */
    private List<MouseHandlerFX> auxiliaryMouseHandlers;
    
    private ObservableList<OverlayFX> overlays;
    
    /** 
     * A flag that can be used to override the plot setting for domain (x) axis
     * zooming. 
     */
    private boolean domainZoomable;
    
    /** 
     * A flag that can be used to override the plot setting for range (y) axis
     * zooming. 
     */
    private boolean rangeZoomable;
    
    /**
     * Creates a new canvas to display the supplied chart in JavaFX.  If
     * {@code chart} is {@code null}, a blank canvas will be displayed.
     * 
     * @param chart  the chart ({@code null} permitted). 
     */
    public ChartCanvas(JFreeChart chart) {
        this.chart = chart;
        if (this.chart != null) {
            this.chart.addChangeListener(this);
        }
        this.tooltip = null;
        this.tooltipEnabled = true;
        this.chartMouseListeners = new ArrayList<>();
        
        widthProperty().addListener(e -> draw());
        heightProperty().addListener(e -> draw());
        // change the default font smoothing for better results
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFontSmoothingType(FontSmoothingType.LCD);
        FXGraphics2D fxg2 = new FXGraphics2D(gc);
        fxg2.setRenderingHint(FXHints.KEY_USE_FX_FONT_METRICS, true);
        fxg2.setZeroStrokeWidth(0.1);
        fxg2.setRenderingHint(
                    RenderingHints.KEY_FRACTIONALMETRICS, 
                    RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        this.g2 = fxg2;
        this.liveHandler = null;
        this.availableMouseHandlers = new ArrayList<>();
        
        this.availableMouseHandlers.add(new PanHandlerFX("pan", true, false, 
                false, false));
 
        this.auxiliaryMouseHandlers = new ArrayList<>();
        this.auxiliaryMouseHandlers.add(new TooltipHandlerFX("tooltip"));
        this.auxiliaryMouseHandlers.add(new ScrollHandlerFX("scroll"));
        this.domainZoomable = true;
        this.rangeZoomable = true;
        this.auxiliaryMouseHandlers.add(new AnchorHandlerFX("anchor"));
        this.auxiliaryMouseHandlers.add(new DispatchHandlerFX("dispatch"));

        this.overlays = FXCollections.observableArrayList();

        setOnMouseMoved(e -> handleMouseMoved(e));
        setOnMouseClicked(e -> handleMouseClicked(e));
        setOnMousePressed(e -> handleMousePressed(e));
        setOnMouseDragged(e -> handleMouseDragged(e));
        setOnMouseReleased(e -> handleMouseReleased(e));
        setOnScroll(e -> handleScroll(e));
    }
    
    /**
     * Returns the chart that is being displayed by this node.
     * 
     * @return The chart (possibly {@code null}). 
     */
    public JFreeChart getChart() {
        return this.chart;
    }
    
    /**
     * Sets the chart to be displayed by this node.
     * 
     * @param chart  the chart ({@code null} permitted). 
     */
    public void setChart(JFreeChart chart) {
        if (this.chart != null) {
            this.chart.removeChangeListener(this);
        }
        this.chart = chart;
        if (this.chart != null) {
            this.chart.addChangeListener(this);
        }
        draw();
    }
    
    /**
     * Returns the flag that determines whether or not zooming is enabled for
     * the domain axis.
     *
     * @return A boolean.
     */
    public boolean isDomainZoomable() {
        return this.domainZoomable;
    }
    
    /**
     * Sets the flag that controls whether or not domain axis zooming is 
     * enabled.  If the underlying plot does not support domain axis zooming,
     * then setting this flag to {@code true} will have no effect.
     * 
     * @param zoomable  the new flag value.
     */
    public void setDomainZoomable(boolean zoomable) {
        this.domainZoomable = zoomable;
    }
    
    /**
     * Returns the flag that determines whether or not zooming is enabled for
     * the range axis.
     *
     * @return A boolean.
     */
    public boolean isRangeZoomable() {
        return this.rangeZoomable;
    }

    /**
     * Sets the flag that controls whether or not range axis zooming is 
     * enabled.  If the underlying plot does not support range axis zooming,
     * then setting this flag to {@code true} will have no effect.
     * 
     * @param zoomable  the new flag value.
     */
    public void setRangeZoomable(boolean zoomable) {
        this.rangeZoomable = zoomable;
    }

    /**
     * Returns the rendering info from the most recent drawing of the chart.
     * 
     * @return The rendering info (possibly {@code null}). 
     */
    public ChartRenderingInfo getRenderingInfo() {
        return this.info;
    }

    /**
     * Returns the flag that controls whether or not tooltips are enabled.  
     * The default value is {@code true}.  The {@link TooltipHandlerFX} 
     * class will only update the tooltip if this flag is set to 
     * {@code true}.
     * 
     * @return The flag. 
     */
    public boolean isTooltipEnabled() {
        return this.tooltipEnabled;
    }

    /**
     * Sets the flag that controls whether or not tooltips are enabled.
     * 
     * @param tooltipEnabled  the new flag value. 
     */
    public void setTooltipEnabled(boolean tooltipEnabled) {
        this.tooltipEnabled = tooltipEnabled;
    }
    
    /**
     * Returns the anchor point.  This is the last point on the canvas
     * that the user clicked with the mouse, and is used during chart 
     * rendering to determine the position of crosshairs (if visible).
     * 
     * @return The anchor point (possibly {@code null}).
     */
    public Point2D getAnchor() {
        return this.anchor;        
    } 

    /**
     * Set the anchor point and forces a redraw of the chart (the anchor point
     * is used to determine the position of the crosshairs on the chart, if
     * they are visible).
     * 
     * @param anchor  the anchor ({@code null} permitted). 
     */
    public void setAnchor(Point2D anchor) {
        this.anchor = anchor;
        if (this.chart != null) {
            this.chart.setNotify(true);  // force a redraw
        }
    }

    /**
     * Add an overlay to the canvas.
     *
     * @param overlay  the overlay ({@code null} not permitted).
     */
    public void addOverlay(OverlayFX overlay) {
        if (overlay == null) throw new IllegalArgumentException("overlay must not be null");
        this.overlays.add(overlay);
        overlay.addChangeListener(this);
        draw();
    }

    /**
     * Removes an overlay from the canvas.
     *
     * @param overlay  the overlay to remove ({@code null} not permitted).
     */
    public void removeOverlay(OverlayFX overlay) {
        if (overlay == null) throw new IllegalArgumentException("overlay must not be null");
        boolean removed = this.overlays.remove(overlay);
        if (removed) {
            overlay.removeChangeListener(this);
            draw();
        }
    }

    /**
     * Handles a change to an overlay by repainting the chart canvas.
     *
     * @param event  the event.
     */
    @Override
    public void overlayChanged(OverlayChangeEvent event) {
        draw();
    }

    /**
     * Returns a (newly created) list containing the listeners currently 
     * registered with the canvas.
     * 
     * @return A list of listeners (possibly empty but never {@code null}).
     */
    public List<ChartMouseListenerFX> getChartMouseListeners() {
        return new ArrayList<>(this.chartMouseListeners);
    }
    
    /**
     * Registers a listener to receive {@link ChartMouseEvent} notifications.
     *
     * @param listener  the listener ({@code null} not permitted).
     */
    public void addChartMouseListener(ChartMouseListenerFX listener) {
        if (listener == null) throw new IllegalArgumentException("listener must not be null");
        this.chartMouseListeners.add(listener);
    }

    /**
     * Removes a listener from the list of objects listening for chart mouse
     * events.
     *
     * @param listener  the listener.
     */
    public void removeChartMouseListener(ChartMouseListenerFX listener) {
        this.chartMouseListeners.remove(listener);
    }
    
    /**
     * Returns the mouse handler with the specified ID, or {@code null} if
     * there is no handler with that ID.  This method will look for handlers
     * in both the regular and auxiliary handler lists.
     * 
     * @param id  the ID ({@code null} not permitted).
     * 
     * @return The handler with the specified ID 
     */
    public MouseHandlerFX getMouseHandler(String id) {
        for (MouseHandlerFX h: this.availableMouseHandlers) {
            if (h.getID().equals(id)) {
                return h;
            }
        }
        for (MouseHandlerFX h: this.auxiliaryMouseHandlers) {
            if (h.getID().equals(id)) {
                return h;
            }
        }
        return null;    
    }
    
    /**
     * Adds a mouse handler to the list of available handlers (handlers that
     * are candidates to take the position of live handler).  The handler must
     * have an ID that uniquely identifies it amongst the handlers registered
     * with this canvas.
     * 
     * @param handler  the handler ({@code null} not permitted). 
     */
    public void addMouseHandler(MouseHandlerFX handler) {
        if (!hasUniqueID(handler)) {
            throw new IllegalArgumentException(
                    "There is already a handler with that ID (" 
                            + handler.getID() + ").");
        }
        this.availableMouseHandlers.add(handler);
    }
    
    /**
     * Removes a handler from the list of available handlers.
     * 
     * @param handler  the handler ({@code null} not permitted). 
     */
    public void removeMouseHandler(MouseHandlerFX handler) {
        this.availableMouseHandlers.remove(handler);
    }
    
    /**
     * Adds a handler to the list of auxiliary handlers.  The handler must
     * have an ID that uniquely identifies it amongst the handlers registered
     * with this canvas.
     * 
     * @param handler  the handler ({@code null} not permitted).
     */
    public void addAuxiliaryMouseHandler(MouseHandlerFX handler) {
        if (!hasUniqueID(handler)) {
            throw new IllegalArgumentException(
                    "There is already a handler with that ID (" 
                            + handler.getID() + ").");
        }
        this.auxiliaryMouseHandlers.add(handler);
    }
    
    /**
     * Removes a handler from the list of auxiliary handlers.
     * 
     * @param handler  the handler ({@code null} not permitted).
     */
    public void removeAuxiliaryMouseHandler(MouseHandlerFX handler) {
        this.auxiliaryMouseHandlers.remove(handler);
    }

    /**
     * Validates that the specified handler has an ID that uniquely identifies 
     * it amongst the existing handlers for this canvas.
     * 
     * @param handler  the handler ({@code null} not permitted).
     * 
     * @return A boolean.
     */
    private boolean hasUniqueID(MouseHandlerFX handler) {
        for (MouseHandlerFX h: this.availableMouseHandlers) {
            if (handler.getID().equals(h.getID())) {
                return false;
            }
        }
        for (MouseHandlerFX h: this.auxiliaryMouseHandlers) {
            if (handler.getID().equals(h.getID())) {
                return false;
            }
        }
        return true;    
    }
    
    /**
     * Clears the current live handler.  This method is intended for use by the
     * handlers themselves, you should not call it directly.
     */
    public void clearLiveHandler() {
        this.liveHandler = null;    
    }
    
    /**
     * Draws the content of the canvas and updates the 
     * {@code renderingInfo} attribute with the latest rendering 
     * information.
     */
    public final void draw() {
        GraphicsContext ctx = getGraphicsContext2D();
        ctx.save();
        double width = getWidth();
        double height = getHeight();
        if (width > 0 && height > 0) {
            ctx.clearRect(0, 0, width, height);
            this.info = new ChartRenderingInfo();
            if (this.chart != null) {
                this.chart.draw(this.g2, new Rectangle((int) width, 
                        (int) height), this.anchor, this.info);
            }
        }
        ctx.restore();
        for (OverlayFX overlay : this.overlays) {
            overlay.paintOverlay(g2, this);
        }
        this.anchor = null;
    }
 
    /**
     * Returns the data area (the area inside the axes) for the plot or subplot.
     *
     * @param point  the selection point (for subplot selection).
     *
     * @return The data area.
     */
    public Rectangle2D findDataArea(Point2D point) {
        PlotRenderingInfo plotInfo = this.info.getPlotInfo();
        Rectangle2D result;
        if (plotInfo.getSubplotCount() == 0) {
            result = plotInfo.getDataArea();
        } else {
            int subplotIndex = plotInfo.getSubplotIndex(point);
            if (subplotIndex == -1) {
                return null;
            }
            result = plotInfo.getSubplotInfo(subplotIndex).getDataArea();
        }
        return result;
    }
    
    /**
     * Return {@code true} to indicate the canvas is resizable.
     * 
     * @return {@code true}. 
     */
    @Override
    public boolean isResizable() {
        return true;
    }

    /**
     * Sets the tooltip text, with the (x, y) location being used for the
     * anchor.  If the text is {@code null}, no tooltip will be displayed.
     * This method is intended for calling by the {@link TooltipHandlerFX}
     * class, you won't normally call it directly.
     * 
     * @param text  the text ({@code null} permitted).
     * @param x  the x-coordinate of the mouse pointer.
     * @param y  the y-coordinate of the mouse pointer.
     */
    public void setTooltip(String text, double x, double y) {
        if (text != null) {
            if (this.tooltip == null) {
                this.tooltip = new Tooltip(text);
                Tooltip.install(this, this.tooltip);
            } else {
                this.tooltip.setText(text);           
                this.tooltip.setAnchorX(x);
                this.tooltip.setAnchorY(y);
            }                   
        } else {
            Tooltip.uninstall(this, this.tooltip);
            this.tooltip = null;
        }
    }
    
    /**
     * Handles a mouse pressed event by (1) selecting a live handler if one
     * is not already selected, (2) passing the event to the live handler if
     * there is one, and (3) passing the event to all enabled auxiliary 
     * handlers.
     * 
     * @param e  the mouse event.
     */
    private void handleMousePressed(MouseEvent e) {
        if (this.liveHandler == null) {
            for (MouseHandlerFX handler: this.availableMouseHandlers) {
                if (handler.isEnabled() && handler.hasMatchingModifiers(e)) {
                    this.liveHandler = handler;      
                }
            }
        }
        
        if (this.liveHandler != null) {
            this.liveHandler.handleMousePressed(this, e);
        }
        
        // pass on the event to the auxiliary handlers
        for (MouseHandlerFX handler: this.auxiliaryMouseHandlers) {
            if (handler.isEnabled()) {
                handler.handleMousePressed(this, e);
            }
        }
    }
    
    /**
     * Handles a mouse moved event by passing it on to the registered handlers.
     * 
     * @param e  the mouse event.
     */
    private void handleMouseMoved(MouseEvent e) {
        if (this.liveHandler != null && this.liveHandler.isEnabled()) {
            this.liveHandler.handleMouseMoved(this, e);
        }
        
        for (MouseHandlerFX handler: this.auxiliaryMouseHandlers) {
            if (handler.isEnabled()) {
                handler.handleMouseMoved(this, e);
            }
        }
    }

    /**
     * Handles a mouse dragged event by passing it on to the registered 
     * handlers.
     * 
     * @param e  the mouse event.
     */
    private void handleMouseDragged(MouseEvent e) {
        if (this.liveHandler != null && this.liveHandler.isEnabled()) {
            this.liveHandler.handleMouseDragged(this, e);
        }
        
        // pass on the event to the auxiliary handlers
        for (MouseHandlerFX handler: this.auxiliaryMouseHandlers) {
            if (handler.isEnabled()) {
                handler.handleMouseDragged(this, e);
            }
        }
    }

    /**
     * Handles a mouse released event by passing it on to the registered 
     * handlers.
     * 
     * @param e  the mouse event.
     */
    private void handleMouseReleased(MouseEvent e) {
        if (this.liveHandler != null && this.liveHandler.isEnabled()) {
            this.liveHandler.handleMouseReleased(this, e);
        }
        
        // pass on the event to the auxiliary handlers
        for (MouseHandlerFX handler: this.auxiliaryMouseHandlers) {
            if (handler.isEnabled()) {
                handler.handleMouseReleased(this, e);
            }
        }
    }
    
    /**
     * Handles a mouse released event by passing it on to the registered 
     * handlers.
     * 
     * @param e  the mouse event.
     */
    private void handleMouseClicked(MouseEvent e) {
        if (this.liveHandler != null && this.liveHandler.isEnabled()) {
            this.liveHandler.handleMouseClicked(this, e);
        }

        // pass on the event to the auxiliary handlers
        for (MouseHandlerFX handler: this.auxiliaryMouseHandlers) {
            if (handler.isEnabled()) {
                handler.handleMouseClicked(this, e);
            }
        }
    }

    /**
     * Handles a scroll event by passing it on to the registered handlers.
     * 
     * @param e  the scroll event.
     */
    protected void handleScroll(ScrollEvent e) {
        if (this.liveHandler != null && this.liveHandler.isEnabled()) {
            this.liveHandler.handleScroll(this, e);
        }
        for (MouseHandlerFX handler: this.auxiliaryMouseHandlers) {
            if (handler.isEnabled()) {
                handler.handleScroll(this, e);
            }
        }
    }
    
    /**
     * Receives a notification from the chart that it has been changed and
     * responds by redrawing the chart entirely.
     * 
     * @param event  event information. 
     */
    @Override
    public void chartChanged(ChartChangeEvent event) {
        draw();
    }

}

