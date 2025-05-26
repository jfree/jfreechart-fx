JFreeChart-FX
=============

Version 2.0.2, 22 May 2025.

[![Maven Central](https://img.shields.io/maven-central/v/org.jfree/org.jfree.chart.fx)](https://central.sonatype.com/artifact/org.jfree/org.jfree.chart.fx/versions) [![javadoc](https://javadoc.io/badge2/org.jfree/org.jfree.chart.fx/javadoc.svg)](https://javadoc.io/doc/org.jfree/org.jfree.chart.fx)

Overview
--------
JFreeChart-FX is an extension for [JFreeChart](https://github.com/jfree/jfreechart "JFreeChart Project Page at GitHub") 
that allows JFreeChart to be used in JavaFX applications.  

![JFreeChart sample](http://jfree.org/jfreechart/images/coffee_prices.png)


Include
-------
To include JFreeChart-FX in your application:

    <dependency>
        <groupId>org.jfree</groupId>
        <artifactId>org.jfree.chart.fx</artifactId>
        <version>2.0.2</version>
    </dependency>

JFreeChart-FX has dependencies on:

* JFreeChart 1.5.6
* FXGraphics2D 2.1.5


Demo Programs
-------------
There are demo programs for this API provided in the following project:

https://github.com/jfree/jfree-fxdemos


History
-------

##### Version 2.0.2 (22 May 2025)

- update JFreeChart dependency to 1.5.6.
- update FXGraphics2D dependency to 2.1.5.

##### Version 2.0.1 (21 February 2021)

- update JFreeChart dependency to 1.5.3.

##### Version 2.0 (30 October 2020)

- created a Java module `org.jfree.chart.fx` (requires Java 11 or later);
- fixed a bug in `ChartViewer` ([#7](https://github.com/jfree/jfreechart-fx/issues/7));
- fixed a bug in `ZoomHandler` ([#8](https://github.com/jfree/jfreechart-fx/issues/8));
- fix a bug related to a `ChartCanvas` having a `null` chart ([#12](https://github.com/jfree/jfreechart-fx/issues/12));
- updated `JFreeChart` dependency to version 1.5.1;
- updated `FXGraphics2D` dependency to version 2.1.

##### Version 1.0.1 (30 December 2017)

- fixed a bug in `ChartCanvas` ([#3](https://github.com/jfree/jfreechart-fx/issues/3));
- added source files to Javadoc output.

##### Version 1.0.0 (5 November 2017)

Initial release after extracting the JavaFX code from the JFreeChart project.
