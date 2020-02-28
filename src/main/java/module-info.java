module org.jfree.chart.fx {
    requires java.desktop;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires org.jfree.fxgraphics2d;
    requires org.jfree.jfreechart;
    exports org.jfree.chart.fx;
    exports org.jfree.chart.fx.interaction;
    exports org.jfree.chart.fx.overlay;
}
