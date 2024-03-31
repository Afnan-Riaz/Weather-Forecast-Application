
# Weather Application

The Weather Application is a comprehensive project designed to exemplify principles of software design and architecture. Tailored for students studying software design and architecture, this project offers a hands-on exploration of key concepts such as modularity, scalability, and maintainability. The application features a dual interface approach, providing both a terminal-based interface and a Java desktop application, while emphasizing the importance of design patterns and architectural considerations.


## Purpose
This project serves as an educational tool for students of software design and architecture, enabling them to:

* Explore software design patterns and architectural principles in a practical setting.
* Gain experience in designing modular, extensible, and maintainable software systems.
* Understand the trade-offs involved in choosing different design approaches and architectural styles.
* Apply design principles to solve real-world problems, such as integrating with external APIs and managing complex data structures.
## Features
### User Interfaces
#### 1. Terminal-based UI:
Offers a command-line interface for quick access to weather and air quality information, demonstrating the principles of modular design and separation of concerns.

#### 2. Java Desktop Application:
A graphical user interface (GUI) built using Java Swing, showcasing the application of design patterns such as Model-View-Controller (MVC) and event-driven architecture.

### Storage Methods
#### 1. SQL Database: 
Utilizes SQL for storing weather data efficiently, enabling robust data management and retrieval.
#### 2. Text-based Storage: 
Implements a ".txt" based storage mechanism for storing weather records in a simple and portable format.


### Key Use Cases
#### Add Locations:
 Users can add multiple locations using either longitude and latitude or city/country names to check weather forecasts.
#### Current Weather Conditions: 
Provides real-time updates on current weather conditions, including temperature, humidity, and wind speed.
#### Weather Forecast: 
Offers a detailed 5-day weather forecast with a 3-hour interval, aiding users in planning ahead.
#### Sunrise and Sunset Time:
 Displays sunrise and sunset times for each location to assist users in scheduling outdoor activities.
#### Air Quality Information:
 Retrieves Air Quality Index (AQI) and pollutant data, including carbon monoxide, nitrogen dioxide, and particulate matter.
#### Cache Management: 
Implements cache management using a database to store frequently accessed weather data, reducing API calls and improving application performance.
#### Notifications: 
Generates notifications emails for poor weather conditions and deteriorating air quality, ensuring users stay informed and prepared.


## Getting Started
#### 1. Sign Up: 
Create an account on openweathermap.org to obtain an API key required for accessing weather data.
#### 2. Installation:
Clone the repository and install dependencies.
#### 3. Configuration:
Enter your API key in the designated configuration file.
#### 4.Usage: 
Run the application and follow on-screen instructions or command-line prompts to access weather and air quality information.
