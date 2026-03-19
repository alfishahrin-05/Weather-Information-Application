# Weather Information Application

A modern desktop weather application built with **Java** and **JavaFX**, providing real-time weather data, forecasts, and historical weather information for cities worldwide.

## 🌟 Features

- 🌡️ **Current Weather** - Get real-time weather conditions including temperature, humidity, wind speed, and more
- 📅 **Weather Forecasts** - View detailed weather forecasts for up to 10 days
- 📊 **Weather History** - Access historical weather data for past dates
- 🔍 **Smart City Search** - Search for cities worldwide, including multi-word city names (e.g., "New York", "Los Angeles")
- 💨 **Air Quality Index (AQI)** - Monitor air quality alongside weather data
- 🎨 **Intuitive GUI** - Clean and user-friendly interface built with JavaFX
- 🌐 **Real-time Data** - Live weather data from WeatherAPI.com

## 💻 Prerequisites

- **Java 21** or higher
- **Maven 3.6+** (for building and running)
- **Git** (for cloning the repository)

## 📦 Technologies Used

- **Language**: Java 21
- **GUI Framework**: JavaFX (Modern UI toolkit)
- **Build Tool**: Maven
- **Weather API**: [WeatherAPI.com](https://www.weatherapi.com/)
- **Module System**: Java Modules (JPMS)
- **Layout**: FXML (JavaFX Markup Language)

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/alfishahrin-05/Weather-Information-Application.git
cd Weather-Information-Application
```

### 2. Build the Project
```bash
mvn clean package
```

### 3. Run the Application
```bash
mvn javafx:run
```

Or run directly with Java:
```bash
mvn clean compile exec:java -Dexec.mainClass="com.example.App"
```

## 📂 Project Structure

```
weatherapplication/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/
│   │   │       ├── App.java                 # Main application entry point
│   │   │       ├── WeatherController.java   # UI controller and event handling
│   │   │       ├── WeatherService.java      # Weather API integration service
│   │   │       ├── WeatherData.java         # Data model for weather information
│   │   │       └── module-info.java         # Java module configuration
│   │   └── resources/
│   │       └── com/example/
│   │           └── weather.fxml             # JavaFX UI layout file
│   └── test/
│       └── java/                            # Unit tests (if applicable)
├── pom.xml                                  # Maven configuration file
├── README.md                                # This file
└── .gitignore                              # Git ignore rules
```

## 🎯 How to Use

1. **Launch the Application**
   - Run the application using the commands above

2. **Search for Weather**
   - Enter a city name in the search field (e.g., "London", "New York", "Tokyo")
   - The application supports both single-word and multi-word city names

3. **View Current Weather**
   - The main screen displays current weather conditions
   - Shows temperature, humidity, wind speed, and air quality index

4. **Check Forecasts**
   - Navigate to the forecast section for upcoming weather predictions
   - Select the number of days for the forecast

5. **Access Weather History**
   - Look up historical weather data for specific dates
   - Useful for comparing past weather patterns

## 🔧 Configuration

### API Key
The application uses WeatherAPI.com for weather data. The API key is embedded in `WeatherService.java`:

```java
private static final String API_KEY = "d61d14163c0b407c921165315252108";
```

To use your own API key:
1. Sign up at [WeatherAPI.com](https://www.weatherapi.com/)
2. Replace the API_KEY value in `WeatherService.java`

### Window Dimensions
Default window size is set to **900x700 pixels** in `App.java`. Modify as needed:
```java
scene = new Scene(loadFXML("weather"), 900, 700);
```

## 🐛 Troubleshooting

### Application won't start
- Ensure Java 21+ is installed: `java -version`
- Verify Maven is properly installed: `mvn -version`
- Clean and rebuild: `mvn clean install`

### Weather data not loading
- Check your internet connection
- Verify the API key is valid
- Check if the city name is spelled correctly

### Module errors
- Ensure all dependencies are properly resolved: `mvn dependency:resolve`
- Check that `module-info.java` includes required modules

## 📝 Features in Detail

### Current Weather Display
- Real-time temperature, feel-like temperature
- Humidity and dew point
- Wind speed and direction
- Air quality index (AQI)
- Weather condition with icon

### Forecast View
- Multi-day weather predictions
- Temperature highs and lows
- Precipitation probability
- Wind conditions

### History View
- Past weather data retrieval
- Date-based queries
- Historical trend analysis

## 🤝 Contributing

Contributions are welcome! Please follow these steps:
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is open-source and available under the MIT License. See the LICENSE file for details.

## 👨‍💻 Author

**Alfis Shahrin**
- GitHub: [@alfishahrin-05](https://github.com/alfishahrin-05)

## 🔗 Useful Links

- [WeatherAPI.com Documentation](https://www.weatherapi.com/docs/)
- [JavaFX Documentation](https://openjfx.io/)
- [Maven Documentation](https://maven.apache.org/)
- [Java Modules Guide](https://docs.oracle.com/en/java/javase/21/docs/api/)

## 📧 Support

For issues, questions, or suggestions, please open an issue on [GitHub Issues](https://github.com/alfishahrin-05/Weather-Information-Application/issues).

---

**Last Updated**: March 2026
**Status**: Active Development
