package com.example;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.*;

public class WeatherController {

    @FXML
    private TextField cityField;

    @FXML
    private Button searchButton;

    @FXML
    private Button themeToggleButton;

    @FXML
    private Label currentCityLabel;

    @FXML
    private Label currentTempLabel;

    @FXML
    private Label feelsLikeLabel;

    @FXML
    private Label humidityLabel;

    @FXML
    private Label cloudLabel;

    @FXML
    private Label precipitationLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label aqiLabel;

    @FXML
    private VBox forecastBox;

    @FXML
    private VBox historyBox;

    @FXML
    private ProgressIndicator loadingIndicator;

    @FXML
    private Label errorLabel;

    @FXML
    private ScrollPane forecastScrollPane;

    @FXML
    private ScrollPane historyScrollPane;

    @FXML
    private VBox headerSection;

    @FXML
    private VBox currentWeatherCard;

    @FXML
    private VBox forecastSection;

    @FXML
    private VBox historySection;

    private boolean isDarkTheme = false;

    @FXML
    private void initialize() {
        // Set initial focus and handlers
        cityField.setOnAction(event -> searchWeather());
        searchButton.setOnAction(event -> searchWeather());
        
        // Hide loading and error initially
        loadingIndicator.setVisible(false);
        errorLabel.setVisible(false);
        
        // Set enhanced placeholder text
        currentCityLabel.setText("🌍 Welcome! Search for any city to get started");
        currentTempLabel.setText("--°C");
        feelsLikeLabel.setText("Feels like: --°C");
        humidityLabel.setText("--%");
        cloudLabel.setText("--%");
        precipitationLabel.setText("-- mm");
        timeLabel.setText("Last updated: --");
        aqiLabel.setText("--");
        
        // Configure scroll panes with increased size
        forecastScrollPane.setFitToWidth(true);
        forecastScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        forecastScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        forecastScrollPane.setPrefHeight(250);  // Increased from 180
        forecastScrollPane.setMaxHeight(250);
        
        historyScrollPane.setFitToWidth(true);
        historyScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        historyScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        historyScrollPane.setPrefHeight(400);  // Increased more to accommodate all 7 days
        historyScrollPane.setMaxHeight(400);
        
        // Set initial theme
        applyLightTheme();
        
        // Add hover effects to buttons
        addButtonHoverEffects();
        
        // Add subtle animations to cards
        addCardAnimations();
    }

    private void addButtonHoverEffects() {
        // Search button hover effect
        searchButton.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), searchButton);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });
        
        searchButton.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), searchButton);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
        
        // Theme button hover effect
        themeToggleButton.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), themeToggleButton);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });
        
        themeToggleButton.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), themeToggleButton);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
    }

    private void addCardAnimations() {
        // Subtle breathing animation for current weather card
        Timeline breatheAnimation = new Timeline(
            new KeyFrame(Duration.seconds(0), e -> {}),
            new KeyFrame(Duration.seconds(3), e -> {
                ScaleTransition breathe = new ScaleTransition(Duration.seconds(2), currentWeatherCard);
                breathe.setToX(1.02);
                breathe.setToY(1.02);
                breathe.setAutoReverse(true);
                breathe.setCycleCount(2);
                breathe.play();
            })
        );
        breatheAnimation.setCycleCount(Timeline.INDEFINITE);
        breatheAnimation.play();
    }

    @FXML
    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        
        // Add transition effect
        FadeTransition fade = new FadeTransition(Duration.millis(300));
        fade.setNode(headerSection.getParent());
        fade.setFromValue(1.0);
        fade.setToValue(0.8);
        fade.setAutoReverse(true);
        fade.setCycleCount(2);
        
        if (isDarkTheme) {
            fade.setOnFinished(e -> {
                applyDarkTheme();
                themeToggleButton.setText("☀ Light Mode");
            });
        } else {
            fade.setOnFinished(e -> {
                applyLightTheme();
                themeToggleButton.setText("☾ Dark Mode");
            });
        }
        
        fade.play();
    }

    private void applyDarkTheme() {
        // Main container background with dark gradient
        VBox mainContainer = (VBox) themeToggleButton.getParent().getParent().getParent();
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom, #2c3e50, #34495e); -fx-padding: 25;");
        
        // Header section - dark gradient
        headerSection.setStyle("-fx-background-color: linear-gradient(to right, #4a4a4a, #2c3e50); -fx-background-radius: 15; -fx-padding: 25;");
        
        // Current weather card
        currentWeatherCard.setStyle("-fx-background-color: #3a3a3a; -fx-background-radius: 20; -fx-padding: 30; -fx-border-color: #555555; -fx-border-radius: 20; -fx-border-width: 1;");
        
        // Forecast and history sections with increased height
        forecastSection.setStyle("-fx-background-color: #3a3a3a; -fx-background-radius: 20; -fx-padding: 25; -fx-border-color: #555555; -fx-border-radius: 20; -fx-border-width: 1; -fx-min-height: 300;");
        historySection.setStyle("-fx-background-color: #3a3a3a; -fx-background-radius: 20; -fx-padding: 25; -fx-border-color: #555555; -fx-border-radius: 20; -fx-border-width: 1; -fx-min-height: 450;");
        
        // Button styles
        searchButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 12 20; -fx-font-size: 14px;");
        themeToggleButton.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: rgba(255,255,255,0.3); -fx-border-width: 1;");
        
        // Text field style
        cityField.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: white; -fx-prompt-text-fill: #aaaaaa; -fx-background-radius: 25; -fx-padding: 12; -fx-font-size: 14px; -fx-border-radius: 25; -fx-border-color: rgba(255,255,255,0.2); -fx-border-width: 1;");
        
        // Scroll panes
        forecastScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        historyScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        // Text colors
        setTextColor(Color.WHITE);
        
        // Update forecast and history boxes
        updateBoxStyles(true);
        
        // Loading indicator
        loadingIndicator.setStyle("-fx-progress-color: white;");
        
        // Ensure all text is visible in dark theme
        ensureTextVisibility(true);
    }

    private void applyLightTheme() {
        // Main container background with light gradient
        VBox mainContainer = (VBox) themeToggleButton.getParent().getParent().getParent();
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom, #ecf0f1, #bdc3c7); -fx-padding: 25;");
        
        // Header section - original gradient
        headerSection.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2); -fx-background-radius: 15; -fx-padding: 25;");
        
        // Current weather card
        currentWeatherCard.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-padding: 30; -fx-border-color: #e0e0e0; -fx-border-radius: 20; -fx-border-width: 1;");
        
        // Forecast and history sections with increased height
        forecastSection.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-padding: 25; -fx-border-color: #e0e0e0; -fx-border-radius: 20; -fx-border-width: 1; -fx-min-height: 300;");
        historySection.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-padding: 25; -fx-border-color: #e0e0e0; -fx-border-radius: 20; -fx-border-width: 1; -fx-min-height: 450;");
        
        // Button styles
        searchButton.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 12 20; -fx-font-size: 14px;");
        themeToggleButton.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: rgba(255,255,255,0.3); -fx-border-width: 1;");
        
        // Text field style
        cityField.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-text-fill: black; -fx-prompt-text-fill: #666666; -fx-background-radius: 25; -fx-padding: 12; -fx-font-size: 14px; -fx-border-radius: 25;");
        
        // Scroll panes
        forecastScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        historyScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        // Text colors
        setTextColor(Color.BLACK);
        
        // Update forecast and history boxes
        updateBoxStyles(false);
        
        // Loading indicator
        loadingIndicator.setStyle("-fx-progress-color: #667eea;");
        
        // Ensure all text is visible in light theme
        ensureTextVisibility(false);
    }

    private void ensureTextVisibility(boolean isDark) {
        String textColor = isDark ? "white" : "black";
        String textStyle = "-fx-text-fill: " + textColor + ";";
        
        // Apply to forecast boxes
        for (javafx.scene.Node node : forecastBox.getChildren()) {
            if (node instanceof HBox) {
                HBox box = (HBox) node;
                for (javafx.scene.Node child : box.getChildren()) {
                    if (child instanceof Label) {
                        Label label = (Label) child;
                        String currentStyle = label.getStyle();
                        currentStyle = currentStyle.replaceAll("-fx-text-fill:\\s*[^;]+;", "");
                        label.setStyle(currentStyle + textStyle);
                    }
                }
            } else if (node instanceof Label) {
                Label label = (Label) node;
                label.setStyle("-fx-text-fill: " + (isDark ? "lightgray" : "gray") + "; -fx-font-style: italic; -fx-font-size: 14px;");
            }
        }
        
        // Apply to history boxes
        for (javafx.scene.Node node : historyBox.getChildren()) {
            if (node instanceof HBox) {
                HBox box = (HBox) node;
                for (javafx.scene.Node child : box.getChildren()) {
                    if (child instanceof Label) {
                        Label label = (Label) child;
                        String currentStyle = label.getStyle();
                        currentStyle = currentStyle.replaceAll("-fx-text-fill:\\s*[^;]+;", "");
                        label.setStyle(currentStyle + textStyle);
                    }
                }
            } else if (node instanceof Label) {
                Label label = (Label) node;
                label.setStyle("-fx-text-fill: " + (isDark ? "lightgray" : "gray") + "; -fx-font-style: italic; -fx-font-size: 14px;");
            }
        }
    }

    private void setTextColor(Color color) {
        // Apply to all main labels with specific colors for dark/light theme
        if (isDarkTheme) {
            currentCityLabel.setStyle(currentCityLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: white;");
            currentTempLabel.setStyle(currentTempLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: #ff6b6b;");
            feelsLikeLabel.setStyle(feelsLikeLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: #bdc3c7;");
            timeLabel.setStyle(timeLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: #95a5a6;");
            humidityLabel.setStyle(humidityLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: #4ecdc4;");
            cloudLabel.setStyle(cloudLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: #95a5a6;");
            precipitationLabel.setStyle(precipitationLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: #3498db;");
            aqiLabel.setStyle(aqiLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: #e74c3c;");
            errorLabel.setStyle(errorLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: #ffcccb;");
        } else {
            currentCityLabel.setStyle(currentCityLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: #2c3e50;");
            currentTempLabel.setStyle(currentTempLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: #ff6b6b;");
            feelsLikeLabel.setStyle(feelsLikeLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: #666666;");
            timeLabel.setStyle(timeLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: #999999;");
            humidityLabel.setStyle(humidityLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: #4ecdc4;");
            cloudLabel.setStyle(cloudLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: #95a5a6;");
            precipitationLabel.setStyle(precipitationLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: #3498db;");
            aqiLabel.setStyle(aqiLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: #e74c3c;");
            errorLabel.setStyle(errorLabel.getStyle().replaceAll("-fx-text-fill:\\s*[^;]+;", "") + "-fx-text-fill: #e74c3c;");
        }
        
        // Apply to section headers
        VBox mainContainer = (VBox) themeToggleButton.getParent().getParent().getParent();
        mainContainer.lookupAll("Label").forEach(node -> {
            Label label = (Label) node;
            String currentStyle = label.getStyle();
            if (currentStyle.contains("font-size: 20px") && currentStyle.contains("font-weight: bold")) {
                currentStyle = currentStyle.replaceAll("-fx-text-fill:\\s*[^;]+;", "");
                label.setStyle(currentStyle + "-fx-text-fill: " + (isDarkTheme ? "white" : "#2c3e50") + ";");
            }
        });
    }

    private void updateBoxStyles(boolean isDark) {
        // Enhanced forecast box styles
        for (javafx.scene.Node node : forecastBox.getChildren()) {
            if (node instanceof HBox) {
                HBox box = (HBox) node;
                if (isDark) {
                    box.setStyle("-fx-padding: 15; -fx-border-color: #555555; -fx-border-radius: 12; -fx-background-color: linear-gradient(to right, #4a4a4a, #3a3a3a); -fx-background-radius: 12;");
                } else {
                    box.setStyle("-fx-padding: 15; -fx-border-color: #e0e0e0; -fx-border-radius: 12; -fx-background-color: linear-gradient(to right, #f8f9fa, #e9ecef); -fx-background-radius: 12;");
                }
            }
        }
        
        // Enhanced history box styles
        for (javafx.scene.Node node : historyBox.getChildren()) {
            if (node instanceof HBox) {
                HBox box = (HBox) node;
                if (isDark) {
                    box.setStyle("-fx-padding: 15; -fx-border-color: #555555; -fx-border-radius: 12; -fx-background-color: linear-gradient(to right, #4a4a4a, #3a3a3a); -fx-background-radius: 12;");
                } else {
                    box.setStyle("-fx-padding: 15; -fx-border-color: #e0e0e0; -fx-border-radius: 12; -fx-background-color: linear-gradient(to right, #f8f9fa, #e9ecef); -fx-background-radius: 12;");
                }
            }
        }
    }

    @FXML
    private void searchWeather() {
        String city = cityField.getText().trim();
        if (city.isEmpty()) {
            showError("Please enter a city name");
            return;
        }

        // Check for spelling errors
        if (hasSpellingErrors(city)) {
            showError("Possible spelling error in city name. Please check and try again.");
            return;
        }

        // Show loading state with animation
        setLoadingState(true);
        errorLabel.setVisible(false);

        // Run API calls in a separate thread to avoid blocking UI
        new Thread(() -> {
            try {
                // Get current weather and 3-day forecast using WeatherService
                String currentData = WeatherService.getCurrentWeather(city);
                String forecastData = WeatherService.getForecast(city, 3); // Get 3 days forecast
                
                // Get historical data for the last 7 days (fixed to get all 7 days)
                String[] historyDates = getLast7Days();
                String[] historyData = new String[7];
                for (int i = 0; i < 7; i++) {
                    try {
                        historyData[i] = WeatherService.getHistory(city, historyDates[i]);
                        // Add small delay to avoid rate limiting
                        Thread.sleep(200);
                    } catch (Exception e) {
                        historyData[i] = null;
                    }
                }

                // Update UI on JavaFX thread with animations
                Platform.runLater(() -> {
                    updateCurrentWeather(currentData);
                    updateForecast(forecastData);
                    updateHistory(historyData, historyDates);
                    setLoadingState(false);
                    
                    // Add success animation
                    animateContentUpdate();
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("Error fetching weather data: " + e.getMessage());
                    setLoadingState(false);
                });
            }
        }).start();
    }

    private void animateContentUpdate() {
        // Fade in animation for updated content
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), currentWeatherCard);
        fadeIn.setFromValue(0.5);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), currentWeatherCard);
        scaleIn.setFromX(0.95);
        scaleIn.setFromY(0.95);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);
        scaleIn.play();
    }

    private boolean hasSpellingErrors(String city) {
        // Simple spelling check - look for repeated characters or unusual patterns
        if (city.length() < 2) {
            return false;
        }
        
        // Check for repeated characters (e.g., "Loonndon")
        for (int i = 0; i < city.length() - 2; i++) {
            if (city.charAt(i) == city.charAt(i + 1) && city.charAt(i) == city.charAt(i + 2)) {
                return true;
            }
        }
        
        // Check for numbers in city names
        if (city.matches(".*\\d.*")) {
            return true;
        }
        
        // Check for unusual character combinations
        if (city.matches(".*[^a-zA-Z\\s\\-'].*")) {
            return true;
        }
        
        return false;
    }

    private void updateCurrentWeather(String data) {
        try {
            // Extract data using WeatherService methods
            String city = WeatherService.extractValue(data, "\"name\":\"", "\"");
            String country = WeatherService.extractValue(data, "\"country\":\"", "\"");
            String temp = WeatherService.extractValue(data, "\"temp_c\":", ",");
            String feelsLike = WeatherService.extractValue(data, "\"feelslike_c\":", ",");
            String humidity = WeatherService.extractValue(data, "\"humidity\":", ",");
            String cloud = WeatherService.extractValue(data, "\"cloud\":", ",");
            String precip = WeatherService.extractValue(data, "\"precip_mm\":", ",");
            String lastUpdated = WeatherService.extractValue(data, "\"last_updated\":\"", "\"");
            
            // Extract air quality data more carefully
            String airQualityData = WeatherService.extractAirQualityData(data);
            String co = airQualityData != null ? WeatherService.extractValue(airQualityData, "\"co\":", ",") : null;
            String no2 = airQualityData != null ? WeatherService.extractValue(airQualityData, "\"no2\":", ",") : null;
            String o3 = airQualityData != null ? WeatherService.extractValue(airQualityData, "\"o3\":", ",") : null;
            String so2 = airQualityData != null ? WeatherService.extractValue(airQualityData, "\"so2\":", ",") : null;
            String pm2_5 = airQualityData != null ? WeatherService.extractValue(airQualityData, "\"pm2_5\":", ",") : null;
            String pm10 = airQualityData != null ? WeatherService.extractValue(airQualityData, "\"pm10\":", "}") : null;

            // Update UI with extracted data
            if (city != null && country != null) {
                currentCityLabel.setText(city + ", " + country);
            }
            
            if (temp != null) {
                currentTempLabel.setText(temp + "°C");
            }
            
            if (feelsLike != null) {
                feelsLikeLabel.setText("Feels like: " + feelsLike + "°C");
            }
            
            if (humidity != null) {
                humidityLabel.setText(humidity + "%");
            }
            
            if (cloud != null) {
                cloudLabel.setText(cloud + "%");
            }
            
            if (precip != null) {
                precipitationLabel.setText(precip + " mm");
            }
            
            if (lastUpdated != null) {
                timeLabel.setText("Last updated: " + formatTime(lastUpdated));
            }
            
            // Calculate AQI from components
            double aqi = calculateAQI(co, no2, o3, so2, pm2_5, pm10);
            aqiLabel.setText(String.format("%.1f", aqi) + getAQIStatus(aqi));
            
        } catch (Exception e) {
            showError("Error parsing current weather data: " + e.getMessage());
        }
    }

    private String getAQIStatus(double aqi) {
        if (aqi <= 25) return " (Good)";
        else if (aqi <= 50) return " (Moderate)";
        else if (aqi <= 75) return " (Poor)";
        else return " (Very Poor)";
    }

    private String formatTime(String timeStr) {
        try {
            // Simple time formatting
            if (timeStr.length() >= 16) {
                return timeStr.substring(11, 16); // Extract HH:MM
            }
            return timeStr;
        } catch (Exception e) {
            return timeStr;
        }
    }

    private double calculateAQI(String co, String no2, String o3, String so2, String pm2_5, String pm10) {
        try {
            double aqi = 0;
            int validComponents = 0;
            
            if (co != null && !co.isEmpty()) {
                aqi += Double.parseDouble(co) / 5000.0;
                validComponents++;
            }
            if (no2 != null && !no2.isEmpty()) {
                aqi += Double.parseDouble(no2) / 200.0;
                validComponents++;
            }
            if (o3 != null && !o3.isEmpty()) {
                aqi += Double.parseDouble(o3) / 180.0;
                validComponents++;
            }
            if (so2 != null && !so2.isEmpty()) {
                aqi += Double.parseDouble(so2) / 350.0;
                validComponents++;
            }
            if (pm2_5 != null && !pm2_5.isEmpty()) {
                aqi += Double.parseDouble(pm2_5) / 35.0;
                validComponents++;
            }
            if (pm10 != null && !pm10.isEmpty()) {
                aqi += Double.parseDouble(pm10) / 50.0;
                validComponents++;
            }
            
            // Average and scale to a reasonable AQI range (0-100)
            if (validComponents > 0) {
                return (aqi / validComponents) * 50;
            }
            return 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void updateForecast(String data) {
        try {
            forecastBox.getChildren().clear();
            
            // Extract forecast data using WeatherService
            String[] days = WeatherService.extractForecastDays(data);
            
            System.out.println("Found " + (days != null ? days.length : 0) + " forecast days");
            
            if (days != null && days.length > 0) {
                // Show today + next 2 days (total 3 days: today, tomorrow, day after tomorrow)
                for (int i = 0; i < Math.min(days.length, 3); i++) { // Changed back to i=0
                    String dayData = days[i];
                    System.out.println("Day " + i + " data: " + dayData.substring(0, Math.min(100, dayData.length())) + "...");
                    
                    // Extract the date first
                    String date = WeatherService.extractValue(dayData, "\"date\":\"", "\"");
                    System.out.println("Day " + i + " date: " + date);
                    
                    // Extract day information from the day object
                    String dayInfo = WeatherService.extractDayData(dayData);
                    if (dayInfo == null) {
                        System.out.println("Day info is null for day " + i);
                        continue;
                    }
                    
                    System.out.println("Day " + i + " info: " + dayInfo.substring(0, Math.min(100, dayInfo.length())) + "...");
                    
                    String avgTemp = WeatherService.extractValue(dayInfo, "\"avgtemp_c\":", ",");
                    String rainChance = WeatherService.extractValue(dayInfo, "\"daily_chance_of_rain\":", ",");
                    String totalPrecip = WeatherService.extractValue(dayInfo, "\"totalprecip_mm\":", ",");
                    String avgHumidity = WeatherService.extractValue(dayInfo, "\"avghumidity\":", ",");
                    
                    System.out.println("Day " + i + ": date=" + date + ", temp=" + avgTemp + ", rain=" + rainChance + ", precip=" + totalPrecip + ", humidity=" + avgHumidity);
                    
                    // If any value is null, skip this day
                    if (date == null || avgTemp == null || rainChance == null || 
                        totalPrecip == null || avgHumidity == null) {
                        System.out.println("Skipping day " + i + " due to missing data");
                        continue;
                    }
                    
                    HBox dayBox = createEnhancedForecastDayBox(
                        formatDate(date),
                        Double.parseDouble(avgTemp),
                        Double.parseDouble(rainChance),
                        Double.parseDouble(totalPrecip),
                        Integer.parseInt(avgHumidity)
                    );
                    
                    forecastBox.getChildren().add(dayBox);
                    
                    // Add entrance animation (simplified)
                    Platform.runLater(() -> {
                        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), dayBox);
                        fadeIn.setFromValue(0);
                        fadeIn.setToValue(1);
                        fadeIn.play();
                    });
                }
            }
            
            // If no forecast data was found, show a message
            if (forecastBox.getChildren().isEmpty()) {
                Label noDataLabel = new Label("🔭 No forecast data available");
                noDataLabel.setStyle("-fx-text-fill: " + (isDarkTheme ? "lightgray" : "gray") + "; -fx-font-style: italic; -fx-font-size: 16px; -fx-padding: 20;");
                forecastBox.getChildren().add(noDataLabel);
                
                // Debug: Print the first 500 characters of the response
                System.out.println("Forecast API Response (first 500 chars):");
                System.out.println(data.length() > 500 ? data.substring(0, 500) + "..." : data);
            }
        } catch (Exception e) {
            showError("Error parsing forecast data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateHistory(String[] historyData, String[] dates) {
        try {
            historyBox.getChildren().clear();
            
            if (historyData != null) {
                // Process all 7 days of history data
                for (int i = 0; i < historyData.length; i++) {
                    if (historyData[i] == null || historyData[i].isEmpty()) continue;
                    
                    // Extract historical data using WeatherService
                    String dayData = WeatherService.extractHistoryDay(historyData[i]);
                    if (dayData == null) continue;
                    
                    String avgTemp = WeatherService.extractValue(dayData, "\"avgtemp_c\":", ",");
                    String totalPrecip = WeatherService.extractValue(dayData, "\"totalprecip_mm\":", ",");
                    String avgHumidity = WeatherService.extractValue(dayData, "\"avghumidity\":", ",");
                    
                    if (avgTemp != null && totalPrecip != null && avgHumidity != null) {
                        HBox dayBox = createEnhancedHistoryDayBox(
                            formatDate(dates[i]),
                            Double.parseDouble(avgTemp),
                            Double.parseDouble(totalPrecip),
                            Integer.parseInt(avgHumidity)
                        );
                        
                        historyBox.getChildren().add(dayBox);
                        
                        // Add entrance animation (simplified)
                        Platform.runLater(() -> {
                            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), dayBox);
                            fadeIn.setFromValue(0);
                            fadeIn.setToValue(1);
                            fadeIn.play();
                        });
                    }
                }
            }
            
            // If no history data was found, show a message
            if (historyBox.getChildren().isEmpty()) {
                Label noDataLabel = new Label("🔭 No historical data available");
                noDataLabel.setStyle("-fx-text-fill: " + (isDarkTheme ? "lightgray" : "gray") + "; -fx-font-style: italic; -fx-font-size: 16px; -fx-padding: 20;");
                historyBox.getChildren().add(noDataLabel);
            }
        } catch (Exception e) {
            showError("Error parsing historical data: " + e.getMessage());
        }
    }

    private HBox createEnhancedForecastDayBox(String date, double temp, double rainChance, double precip, int humidity) {
        HBox box = new HBox(15);
        
        // Apply theme-appropriate styling immediately
        if (isDarkTheme) {
            box.setStyle("-fx-padding: 15; -fx-border-color: #555555; -fx-border-radius: 12; -fx-background-color: linear-gradient(to right, #4a4a4a, #3a3a3a); -fx-background-radius: 12;");
        } else {
            box.setStyle("-fx-padding: 15; -fx-border-color: #e0e0e0; -fx-border-radius: 12; -fx-background-color: linear-gradient(to right, #f8f9fa, #e9ecef); -fx-background-radius: 12;");
        }
        
        Label dateLabel = new Label(date);
        dateLabel.setStyle("-fx-font-weight: bold; -fx-min-width: 120; -fx-font-size: 14px;" + 
                          (isDarkTheme ? " -fx-text-fill: white;" : " -fx-text-fill: black;"));
        
        // Fixed temperature icon - using proper Unicode character
        Label tempLabel = new Label(String.format("🌡 %.1f°C", temp));
        tempLabel.setStyle("-fx-min-width: 80; -fx-font-weight: bold; -fx-font-size: 14px;" +
                          (isDarkTheme ? " -fx-text-fill: #ff6b6b;" : " -fx-text-fill: #ff6b6b;"));
        
        // Fixed rain icon
        Label rainLabel = new Label(String.format("🌧 %.0f%%", rainChance));
        rainLabel.setStyle("-fx-min-width: 70; -fx-font-size: 13px;" +
                          (isDarkTheme ? " -fx-text-fill: #3498db;" : " -fx-text-fill: #3498db;"));
        
        // Fixed precipitation icon
        Label precipLabel = new Label(String.format("💧 %.1fmm", precip));
        precipLabel.setStyle("-fx-min-width: 80; -fx-font-size: 13px;" +
                           (isDarkTheme ? " -fx-text-fill: #4ecdc4;" : " -fx-text-fill: #4ecdc4;"));
        
        // Fixed humidity icon
        Label humidityLabel = new Label(String.format("💨 %d%%", humidity));
        humidityLabel.setStyle("-fx-min-width: 70; -fx-font-size: 13px;" +
                             (isDarkTheme ? " -fx-text-fill: #95a5a6;" : " -fx-text-fill: #95a5a6;"));
        
        box.getChildren().addAll(dateLabel, tempLabel, rainLabel, precipLabel, humidityLabel);
        
        // Add hover effect
        box.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), box);
            scale.setToX(1.02);
            scale.setToY(1.02);
            scale.play();
        });
        
        box.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), box);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
        
        return box;
    }

    private HBox createEnhancedHistoryDayBox(String date, double temp, double precip, int humidity) {
        HBox box = new HBox(15);
        
        // Apply theme-appropriate styling immediately
        if (isDarkTheme) {
            box.setStyle("-fx-padding: 15; -fx-border-color: #555555; -fx-border-radius: 12; -fx-background-color: linear-gradient(to right, #4a4a4a, #3a3a3a); -fx-background-radius: 12;");
        } else {
            box.setStyle("-fx-padding: 15; -fx-border-color: #e0e0e0; -fx-border-radius: 12; -fx-background-color: linear-gradient(to right, #f8f9fa, #e9ecef); -fx-background-radius: 12;");
        }
        
        Label dateLabel = new Label(date);
        dateLabel.setStyle("-fx-font-weight: bold; -fx-min-width: 120; -fx-font-size: 14px;" +
                          (isDarkTheme ? " -fx-text-fill: white;" : " -fx-text-fill: black;"));
        
        // Fixed temperature icon
        Label tempLabel = new Label(String.format("🌡 %.1f°C", temp));
        tempLabel.setStyle("-fx-min-width: 80; -fx-font-size: 14px;" +
                          (isDarkTheme ? " -fx-text-fill: #ff6b6b;" : " -fx-text-fill: #ff6b6b;"));
        
        // Fixed precipitation icon
        Label precipLabel = new Label(String.format("💧 %.1fmm", precip));
        precipLabel.setStyle("-fx-min-width: 80; -fx-font-size: 13px;" +
                           (isDarkTheme ? " -fx-text-fill: #4ecdc4;" : " -fx-text-fill: #4ecdc4;"));
        
        // Fixed humidity icon
        Label humidityLabel = new Label(String.format("💨 %d%%", humidity));
        humidityLabel.setStyle("-fx-min-width: 70; -fx-font-size: 13px;" +
                             (isDarkTheme ? " -fx-text-fill: #95a5a6;" : " -fx-text-fill: #95a5a6;"));
        
        box.getChildren().addAll(dateLabel, tempLabel, precipLabel, humidityLabel);
        
        // Add hover effect
        box.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), box);
            scale.setToX(1.02);
            scale.setToY(1.02);
            scale.play();
        });
        
        box.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), box);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
        
        return box;
    }

    private String[] getLast7Days() {
        String[] dates = new String[7];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long now = System.currentTimeMillis();
        
        // Fixed: Generate all 7 days properly (from 7 days ago to 1 day ago)
        for (int i = 0; i < 7; i++) {
            long time = now - ((7 - i) * 24 * 60 * 60 * 1000);
            dates[i] = sdf.format(new Date(time));
        }
        
        return dates;
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("E, MMM d");
            return outputFormat.format(inputFormat.parse(dateStr));
        } catch (Exception e) {
            return dateStr;
        }
    }

    private void setLoadingState(boolean loading) {
        loadingIndicator.setVisible(loading);
        searchButton.setDisable(loading);
        cityField.setDisable(loading);
        
        if (loading) {
            searchButton.setText("Searching...");
        } else {
            searchButton.setText("Search");
        }
    }

    private void showError(String message) {
        errorLabel.setText("⚠️ " + message);
        errorLabel.setVisible(true);
        
        // Add shake animation to error
        Timeline shake = new Timeline();
        for (int i = 0; i < 4; i++) {
            shake.getKeyFrames().add(new KeyFrame(Duration.millis(i * 100), e -> errorLabel.setTranslateX(5)));
            shake.getKeyFrames().add(new KeyFrame(Duration.millis(i * 100 + 50), e -> errorLabel.setTranslateX(-5)));
        }
        shake.getKeyFrames().add(new KeyFrame(Duration.millis(400), e -> errorLabel.setTranslateX(0)));
        shake.play();
    }
}