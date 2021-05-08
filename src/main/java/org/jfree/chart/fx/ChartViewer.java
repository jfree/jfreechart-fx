/* ================================================
 * JFreeChart-FX : JavaFX extensions for JFreeChart
 * ================================================
 *
 * (C) Copyright 2017-2021 by Object Refinery Limited and Contributors.
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
 * ChartViewer.java
 * ----------------
 * (C) Copyright 2014-2021, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.fx;

import java.io.File;
import java.io.IOException;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.interaction.ChartMouseListenerFX;
import org.jfree.chart.fx.interaction.ZoomHandlerFX;
import org.jfree.chart.swing.ChartMouseEvent;
import org.jfree.chart.util.ExportUtils;

/**
 * A control for displaying a {@link JFreeChart} in JavaFX (embeds a 
 * {@link ChartCanvas}, adds drag zooming and provides a popup menu for export
 * to PNG/JPG/SVG and PDF formats).  Many behaviours (tooltips, zooming etc) are 
 * provided directly by the canvas.
 */
public class ChartViewer extends Region {

    private final ChartCanvas canvas;
    
    /** 
     * The zoom rectangle is used to display the zooming region when
     * doing a drag-zoom with the mouse.  Most of the time this rectangle
     * is not visible.
     */
    private final Rectangle zoomRectangle;

    /** The context menu for the chart viewer. */
    private final ContextMenu contextMenu;
    
    /**
     * Creates a new instance, initially with no chart to display.  This 
     * constructor is required so that this control can be used within
     * FXML.
     */
    public ChartViewer() {
        this(null);
    }

    /**
     * Creates a new viewer to display the supplied chart in JavaFX.
     * 
     * @param chart  the chart ({@code null} permitted). 
     */
    public ChartViewer(JFreeChart chart) {
        this(chart, true);
    }
    
    /**
     * Creates a new viewer instance.
     * 
     * @param chart  the chart ({@code null} permitted).
     * @param contextMenuEnabled  enable the context menu?
     */
    public ChartViewer(JFreeChart chart, boolean contextMenuEnabled) {
        this.canvas = new ChartCanvas(chart);
        this.canvas.setTooltipEnabled(true);
        this.canvas.addMouseHandler(new ZoomHandlerFX("zoom", this));
        setFocusTraversable(true);
        getChildren().add(this.canvas);
        
        this.zoomRectangle = new Rectangle(0, 0, new Color(0, 0, 1, 0.25));
        this.zoomRectangle.setManaged(false);
        this.zoomRectangle.setVisible(false);
        getChildren().add(this.zoomRectangle);
        
        this.contextMenu = createContextMenu();
        setOnContextMenuRequested((ContextMenuEvent event) -> {
            contextMenu.show(ChartViewer.this.getScene().getWindow(),
                    event.getScreenX(), event.getScreenY());
        });
        this.contextMenu.setOnShowing(
                e -> ChartViewer.this.getCanvas().setTooltipEnabled(false));
        this.contextMenu.setOnHiding(
                e -> ChartViewer.this.getCanvas().setTooltipEnabled(true));
    }

    /**
     * Returns the chart that is being displayed by this viewer.
     * 
     * @return The chart (possibly {@code null}). 
     */
    public JFreeChart getChart() {
        return this.canvas.getChart();
    }
    
    /**
     * Sets the chart to be displayed by this viewer.
     * 
     * @param chart  the chart ({@code null} not permitted). 
     */
    public void setChart(JFreeChart chart) {
        if (chart == null) throw new IllegalArgumentException("chart must not be null");
        this.canvas.setChart(chart);
    }

    /**
     * Returns the {@link ChartCanvas} embedded in this component.
     * 
     * @return The {@code ChartCanvas} (never {@code null}).
     */
    public ChartCanvas getCanvas() {
        return this.canvas;
    }
 
    /**
     * Returns the context menu for this component.
     * 
     * @return The context menu for this component. 
     */
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }
    
    /**
     * Returns the rendering info from the most recent drawing of the chart.
     * 
     * @return The rendering info (possibly {@code null}).
     */
    public ChartRenderingInfo getRenderingInfo() {
        return getCanvas().getRenderingInfo();
    }
    
    /**
     * Returns the current fill paint for the zoom rectangle.
     * 
     * @return The fill paint.
     */
    public Paint getZoomFillPaint() {
        return this.zoomRectangle.getFill();
    }
    
    /**
     * Sets the fill paint for the zoom rectangle.
     * 
     * @param paint  the new paint.
     */
    public void setZoomFillPaint(Paint paint) {
        this.zoomRectangle.setFill(paint);
    }
    
    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        this.canvas.setLayoutX(0);
        this.canvas.setLayoutY(0);
        this.canvas.setWidth(getWidth());
        this.canvas.setHeight(getHeight());
    }
    
    /**
     * Registers a listener to receive {@link ChartMouseEvent} notifications
     * from the canvas embedded in this viewer.
     *
     * @param listener  the listener ({@code null} not permitted).
     */
    public void addChartMouseListener(ChartMouseListenerFX listener) {
        if (listener == null) throw new IllegalArgumentException("listener must not be null");
        this.canvas.addChartMouseListener(listener);
    }

    /**
     * Removes a listener from the list of objects listening for chart mouse
     * events.
     *
     * @param listener  the listener.
     */
    public void removeChartMouseListener(ChartMouseListenerFX listener) {
        if (listener == null) throw new IllegalArgumentException("listener must not be null");
        this.canvas.removeChartMouseListener(listener);
    }
    
    /**
     * Creates the context menu.
     * 
     * @return The context menu.
     */
    private ContextMenu createContextMenu() {
        final ContextMenu menu = new ContextMenu();
        menu.setAutoHide(true);
        Menu export = new Menu("Export As");
        
        MenuItem pngItem = new MenuItem("PNG...");
        pngItem.setOnAction(e -> handleExportToPNG());        
        export.getItems().add(pngItem);
        
        MenuItem jpegItem = new MenuItem("JPEG...");
        jpegItem.setOnAction(e -> handleExportToJPEG());        
        export.getItems().add(jpegItem);
        
        if (ExportUtils.isOrsonPDFAvailable()) {
            MenuItem pdfItem = new MenuItem("PDF...");
            pdfItem.setOnAction(e -> handleExportToPDF());
            export.getItems().add(pdfItem);
        }
        if (ExportUtils.isJFreeSVGAvailable()) {
            MenuItem svgItem = new MenuItem("SVG...");
            svgItem.setOnAction(e -> handleExportToSVG());
            export.getItems().add(svgItem);        
        }
        menu.getItems().add(export);
        return menu;
    }
    
    /**
     * A handler for the export to PDF option in the context menu.
     */
    private void handleExportToPDF() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export to PDF");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                "Portable Document Format (PDF)", "*.pdf");
        chooser.getExtensionFilters().add(filter);
        File file = chooser.showSaveDialog(getScene().getWindow());
        if (file != null) {
            ExportUtils.writeAsPDF(this.canvas.getChart(), (int) getWidth(), 
                    (int) getHeight(), file);
        } 
    }
    
    /**
     * A handler for the export to SVG option in the context menu.
     */
    private void handleExportToSVG() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export to SVG");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                "Scalable Vector Graphics (SVG)", "*.svg");
        chooser.getExtensionFilters().add(filter);
        File file = chooser.showSaveDialog(getScene().getWindow());
        if (file != null) {
            ExportUtils.writeAsSVG(this.canvas.getChart(), (int) getWidth(), 
                    (int) getHeight(), file);
        }
    }
    
    /**
     * A handler for the export to PNG option in the context menu.
     */
    private void handleExportToPNG() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export to PNG");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                "Portable Network Graphics (PNG)", "*.png");
        chooser.getExtensionFilters().add(filter);
        File file = chooser.showSaveDialog(getScene().getWindow());
        if (file != null) {
            try {
                ExportUtils.writeAsPNG(this.canvas.getChart(), (int) getWidth(),
                        (int) getHeight(), file);
            } catch (IOException ex) {
                // FIXME: show a dialog with the error
                throw new RuntimeException(ex);
            }
        }        
    }

    /**
     * A handler for the export to JPEG option in the context menu.
     */
    private void handleExportToJPEG() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export to JPEG");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("JPEG", "*.jpg");
        chooser.getExtensionFilters().add(filter);
        File file = chooser.showSaveDialog(getScene().getWindow());
        if (file != null) {
            try {
                ExportUtils.writeAsJPEG(this.canvas.getChart(), (int) getWidth(),
                        (int) getHeight(), file);
            } catch (IOException ex) {
                // FIXME: show a dialog with the error
                throw new RuntimeException(ex);
            }
        }        
    }

    /**
     * Sets the size and location of the zoom rectangle and makes it visible
     * if it wasn't already visible..  This method is provided for the use of 
     * the {@link ZoomHandlerFX} class, you won't normally need to call it 
     * directly.
     * 
     * @param x  the x-location.
     * @param y  the y-location.
     * @param w  the width.
     * @param h  the height.
     */
    public void showZoomRectangle(double x, double y, double w, double h) {
        this.zoomRectangle.setX(x);
        this.zoomRectangle.setY(y);
        this.zoomRectangle.setWidth(w);
        this.zoomRectangle.setHeight(h);
        this.zoomRectangle.setVisible(true);
    }
    
    /**
     * Hides the zoom rectangle.  This method is provided for the use of the
     * {@link ZoomHandlerFX} class, you won't normally need to call it directly.
     */
    public void hideZoomRectangle() {
        this.zoomRectangle.setVisible(false);
    }

}

