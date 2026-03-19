package com.example;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class WeatherService {
    private static final String API_KEY = "d61d14163c0b407c921165315252108";
    
    public static String getCurrentWeather(String city) throws IOException {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String urlString = String.format("http://api.weatherapi.com/v1/current.json?key=%s&q=%s&aqi=yes", 
                                        API_KEY, encodedCity);
        return fetchData(urlString);
    }
    
    public static String getForecast(String city, int days) throws IOException {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String urlString = String.format("http://api.weatherapi.com/v1/forecast.json?key=%s&q=%s&days=%d", 
                                        API_KEY, encodedCity, days);
        return fetchData(urlString);
    }
    
    public static String getHistory(String city, String date) throws IOException {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String urlString = String.format("http://api.weatherapi.com/v1/history.json?key=%s&q=%s&dt=%s", 
                                        API_KEY, encodedCity, date);
        return fetchData(urlString);
    }
    
    private static String fetchData(String urlString) throws IOException {
        URL url = URI.create(urlString).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("HTTP Response Code: " + responseCode);
        }

        Scanner scanner = new Scanner(url.openStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        return response.toString();
    }
    
    // Helper method to extract values from JSON string using string manipulation
    public static String extractValue(String data, String startDelimiter, String endDelimiter) {
        try {
            if (data == null) return null;
            
            int startIndex = data.indexOf(startDelimiter);
            if (startIndex == -1) return null;
            
            startIndex += startDelimiter.length();
            int endIndex = data.indexOf(endDelimiter, startIndex);
            if (endIndex == -1) return null;
            
            return data.substring(startIndex, endIndex).replace("\"", "").trim();
        } catch (Exception e) {
            return null;
        }
    }
    
    // Improved method to extract multiple forecast days
    public static String[] extractForecastDays(String data) {
        try {
            if (data == null || data.isEmpty()) {
                System.out.println("Data is null or empty");
                return new String[0];
            }
            
            // Find the forecast section first
            int forecastIndex = data.indexOf("\"forecast\":");
            if (forecastIndex == -1) {
                System.out.println("No forecast section found");
                return new String[0];
            }
            
            // Now find the forecastday array within the forecast section
            int startIndex = data.indexOf("\"forecastday\":[", forecastIndex);
            if (startIndex == -1) {
                System.out.println("No forecastday array found");
                return new String[0];
            }
            
            startIndex += 15; // Length of "\"forecastday\":["
            
            // Find the matching closing bracket for the array
            int bracketCount = 1;
            int endIndex = startIndex;
            boolean inString = false;
            boolean escapeNext = false;
            
            while (endIndex < data.length() && bracketCount > 0) {
                char c = data.charAt(endIndex);
                
                if (escapeNext) {
                    escapeNext = false;
                } else if (c == '\\') {
                    escapeNext = true;
                } else if (c == '"' && !escapeNext) {
                    inString = !inString;
                } else if (!inString) {
                    if (c == '[') {
                        bracketCount++;
                    } else if (c == ']') {
                        bracketCount--;
                    }
                }
                endIndex++;
            }
            
            if (bracketCount > 0) {
                System.out.println("No closing bracket found for forecastday array");
                return new String[0];
            }
            
            String forecastDaysContent = data.substring(startIndex, endIndex - 1); // -1 to exclude the closing ]
            System.out.println("Forecast days content length: " + forecastDaysContent.length());
            
            // Parse each day object
            List<String> daysList = new ArrayList<>();
            int pos = 0;
            
            while (pos < forecastDaysContent.length()) {
                // Skip whitespace and commas
                while (pos < forecastDaysContent.length() && 
                       (Character.isWhitespace(forecastDaysContent.charAt(pos)) || 
                        forecastDaysContent.charAt(pos) == ',')) {
                    pos++;
                }
                
                if (pos >= forecastDaysContent.length()) break;
                
                // Find the start of the object
                if (forecastDaysContent.charAt(pos) != '{') {
                    System.out.println("Expected '{' at position " + pos + ", found '" + forecastDaysContent.charAt(pos) + "'");
                    break;
                }
                
                // Extract the complete object
                int objectStart = pos;
                int braceCount = 0;
                inString = false;
                escapeNext = false;
                
                while (pos < forecastDaysContent.length()) {
                    char c = forecastDaysContent.charAt(pos);
                    
                    if (escapeNext) {
                        escapeNext = false;
                    } else if (c == '\\') {
                        escapeNext = true;
                    } else if (c == '"' && !escapeNext) {
                        inString = !inString;
                    } else if (!inString) {
                        if (c == '{') {
                            braceCount++;
                        } else if (c == '}') {
                            braceCount--;
                            if (braceCount == 0) {
                                pos++; // Include the closing brace
                                break;
                            }
                        }
                    }
                    pos++;
                }
                
                if (braceCount == 0) {
                    String dayObject = forecastDaysContent.substring(objectStart, pos);
                    daysList.add(dayObject);
                    System.out.println("Extracted day object " + daysList.size() + ", length: " + dayObject.length());
                } else {
                    System.out.println("Incomplete object found");
                    break;
                }
            }
            
            System.out.println("Successfully extracted " + daysList.size() + " forecast days");
            return daysList.toArray(new String[0]);
            
        } catch (Exception e) {
            System.out.println("Error in extractForecastDays: " + e.getMessage());
            e.printStackTrace();
            return new String[0];
        }
    }
    
    // Method to extract the day object from forecast data
    public static String extractDayData(String forecastDayData) {
        try {
            if (forecastDayData == null || forecastDayData.isEmpty()) {
                System.out.println("Forecast day data is null or empty");
                return null;
            }
            
            // Find the day object within the forecast day
            int dayStart = forecastDayData.indexOf("\"day\":{");
            if (dayStart == -1) {
                System.out.println("No day object found in forecast day data");
                System.out.println("Data: " + forecastDayData.substring(0, Math.min(200, forecastDayData.length())));
                return null;
            }
            
            dayStart += 6; // Length of "\"day\":"
            
            // Skip the opening brace
            if (dayStart < forecastDayData.length() && forecastDayData.charAt(dayStart) == '{') {
                dayStart++;
            }
            
            // Find the matching closing brace for the day object
            int braceCount = 1;
            int dayEnd = dayStart;
            boolean inString = false;
            boolean escapeNext = false;
            
            while (dayEnd < forecastDayData.length() && braceCount > 0) {
                char c = forecastDayData.charAt(dayEnd);
                
                if (escapeNext) {
                    escapeNext = false;
                } else if (c == '\\') {
                    escapeNext = true;
                } else if (c == '"' && !escapeNext) {
                    inString = !inString;
                } else if (!inString) {
                    if (c == '{') {
                        braceCount++;
                    } else if (c == '}') {
                        braceCount--;
                    }
                }
                dayEnd++;
            }
            
            if (braceCount > 0) {
                System.out.println("No closing brace found for day object");
                return null;
            }
            
            String result = forecastDayData.substring(dayStart, dayEnd - 1); // -1 to exclude the closing brace
            System.out.println("Extracted day data length: " + result.length());
            return result;
            
        } catch (Exception e) {
            System.out.println("Error extracting day data: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    // Method to extract air quality data
    public static String extractAirQualityData(String data) {
        try {
            if (data == null) return null;
            
            int startIndex = data.indexOf("\"air_quality\":{");
            if (startIndex == -1) return null;
            
            startIndex += 15; // Length of "\"air_quality\":{"
            int endIndex = data.indexOf("}", startIndex);
            if (endIndex == -1) return null;
            
            return data.substring(startIndex, endIndex);
        } catch (Exception e) {
            return null;
        }
    }
    
    // Method to extract historical day data
    public static String extractHistoryDay(String historyData) {
        try {
            if (historyData == null) return null;
            
            // Find the day object in history data
            int dayStart = historyData.indexOf("\"day\":{");
            if (dayStart == -1) return null;
            
            dayStart += 6; // Length of "\"day\":"
            
            // Skip the opening brace
            if (dayStart < historyData.length() && historyData.charAt(dayStart) == '{') {
                dayStart++;
            }
            
            // Find the matching closing brace for the day object
            int braceCount = 1;
            int dayEnd = dayStart;
            boolean inString = false;
            boolean escapeNext = false;
            
            while (dayEnd < historyData.length() && braceCount > 0) {
                char c = historyData.charAt(dayEnd);
                
                if (escapeNext) {
                    escapeNext = false;
                } else if (c == '\\') {
                    escapeNext = true;
                } else if (c == '"' && !escapeNext) {
                    inString = !inString;
                } else if (!inString) {
                    if (c == '{') {
                        braceCount++;
                    } else if (c == '}') {
                        braceCount--;
                    }
                }
                dayEnd++;
            }
            
            if (braceCount > 0) return null;
            
            return historyData.substring(dayStart, dayEnd - 1); // -1 to exclude the closing brace
            
        } catch (Exception e) {
            return null;
        }
    }
}