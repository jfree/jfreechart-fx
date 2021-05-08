/**
 * <b>JFreeChart-FX</b> is an extension for <a href="http://www.jfree.org/jfreechart">JFreeChart</a>
 * to provide JavaFX controls for displaying and interacting with charts created 
 * with JFreeChart - for more information, see <a href="http://github.com/jfree/jfreechart-fx" target="_blank">http://github.com/jfree/jfreechart-fx</a>.
 * <p>
 * JFreeChart-FX requires Java 11 or later.
 */
module org.jfree.chart.fx {
    requires java.desktop;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires org.jfree.fxgraphics2d;
    requires org.jfree.chart;
    exports org.jfree.chart.fx;
    exports org.jfree.chart.fx.interaction;
    exports org.jfree.chart.fx.overlay;
}