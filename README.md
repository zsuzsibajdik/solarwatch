# SolarWatch

## About The Project
SolarWatch is a web application that tracks sunrise and sunset times based on location. It helps users stay informed about daylight hours and provides additional weather data using the OpenWeather API. The app combines backend reliability with a responsive frontend to make data easily accessible and interactive.

## Core Features
- Track sunrise and sunset times for multiple locations
- Store and cache data in a PostgreSQL database
- Fetch real-time weather data from OpenWeather API
- Responsive and interactive frontend built with React
- CI/CD pipeline for automated builds and deployments
- Dockerized environment for easy setup and consistent development

## Built With / Tech Stack
- **Backend:** Java, Spring Boot, Docker, PostgreSQL, CI/CD pipeline
- **Frontend:** JavaScript, React
- **Other:** Docker Compose for easy container management, OpenWeather API integration

## Prerequisites / Dependencies
- Java 17+
- Node.js 16+
- Docker & Docker Compose
- PostgreSQL database
- OpenWeather API key stored in `.env` file

## How to Run
1. Clone the repository:
```bash
git clone https://github.com/zsuzsibajdik/solarwatch.git
2. Set up environment variables in .env (API keys, database credentials)
3. Start the application using Docker Compose:
docker-compose up --build
4. Open the app in your browser at http://localhost:3000

Project Status
🟡 In active development
Next planned steps:
UI/UX improvements
Advanced features for caching and notifications
