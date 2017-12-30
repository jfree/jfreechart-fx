JFreeChart-FX
=============

Version 1.0.1, 30 December 2017.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.jfree/jfreechart-fx/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.jfree/jfreechart-fx)

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
        <artifactId>jfreechart-fx</artifactId>
        <version>1.0.1</version>
    </dependency>

JFreeChart-FX has dependencies on:

* JFreeChart 1.5.0
* FXGraphics2D 1.6


Demo Programs
-------------
There are demo programs for this API provided in the following project:

https://github.com/jfree/jfree-fxdemos


History
-------

##### Version 1.0.1 (30 December 2017)

- fixed a bug in `ChartCanvas` ([#3](https://github.com/jfree/jfreechart-fx/issues/3));
- added source files to Javadoc output.

##### Version 1.0.0 (5 November 2017)

Initial release after extracting the JavaFX code from the JFreeChart project.