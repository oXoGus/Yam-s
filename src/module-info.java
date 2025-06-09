module Yams {
	requires javafx.controls;
	requires transitive javafx.graphics;
	requires javafx.fxml;
	
	opens fr.uge.yams.controllers to javafx.graphics, javafx.fxml;
	opens fr.uge.yams.models to javafx.graphics, javafx.fxml, java.base;
	opens fr.uge.yams.views to javafx.graphics, javafx.fxml;

	exports fr.uge.yams.controllers;
	exports fr.uge.yams.models;
	exports fr.uge.yams.views;
}