# Telegram Weather Bot

This is a simple Telegram bot that provides current weather information for a given location using the [Weatherstack API](https://weatherstack.com). The bot is built with Java and Spring Boot.

## Features
- Fetches weather information based on user input location.
- Provides temperature, weather condition, and other details.
- Built using Spring Boot for easy configuration and scalability.
- Uses REST API to interact with external weather service.

## Technologies Used
- **Java 17**
- **Spring Boot 3.x**
- **Telegram Bots API** (Java wrapper for Telegram Bot API)
- **Weathestack API** (for fetching weather data)
- **Maven** (for dependency management)

## Setup Instructions

### 1. Clone the Repository
git clone https://github.com/yourusername/weatherbot.git
cd weather-bot
### 2. Configure the properties
set up your bot token and your Weatherstack API key in the 
bot.token=<YOUR_BOT_TOKEN>
weatherstack.api.key=<YOUR_API_KEY>
### 3. Build the Project
Use Maven to build the project:
mvn clean install
### 4. Run the Application
Start the bot:
mvn spring-boot:run
### 5. Interact with the Bot
Open Telegram and search for your bot using its username.
Send a message with the name of a city (e.g., "San Francisco") to get the current weather information.