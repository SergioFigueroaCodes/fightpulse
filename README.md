# FightPulse

FightPulse is a Java and Spring Boot backend application that collects MMA video data from the YouTube Data API. It categorizes videos, calculates engagement rates, stores video information, and maintains historical statistics snapshots.

## Features

- Searches YouTube for MMA-related videos
- Retrieves video views, likes, and comments
- Calculates engagement rates
- Categorizes videos based on their titles
- Stores unique videos in a database
- Prevents duplicate video records
- Creates historical statistics snapshots
- Returns saved videos and snapshot history through REST endpoints
- Provides clean API error responses
- Includes automated service tests

## Technologies

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Gradle
- JUnit 5
- H2 Database
- MySQL Connector
- YouTube Data API v3

## Project Structure

```text
src
├── main
│   ├── java/com/sergiofigueroa/fightpulse
│   │   ├── controller
│   │   ├── exception
│   │   ├── model
│   │   ├── repository
│   │   └── service
│   └── resources
└── test
    └── java/com/sergiofigueroa/fightpulse
        └── service
```

## API Endpoints

### Search for YouTube videos

```http
GET /api/youtube/search?query=MMA
```

Example:

```bash
curl "http://localhost:8080/api/youtube/search?query=MMA"
```

### Collect and save videos

```http
POST /api/youtube/collect?query=MMA
```

Example:

```bash
curl -X POST "http://localhost:8080/api/youtube/collect?query=MMA"
```

### Get all saved videos

```http
GET /api/youtube/saved
```

Example:

```bash
curl "http://localhost:8080/api/youtube/saved"
```

### Get database record counts

```http
GET /api/youtube/saved/count
```

Example:

```bash
curl "http://localhost:8080/api/youtube/saved/count"
```

### Get a video's snapshot history

```http
GET /api/youtube/saved/{youtubeVideoId}/snapshots
```

Example:

```bash
curl "http://localhost:8080/api/youtube/saved/w7om9P1puOw/snapshots"
```

## YouTube API Setup

FightPulse requires a YouTube Data API v3 key.

The application reads the key from this environment variable:

```text
YOUTUBE_API_KEY
```

Never place the real API key inside the source code or commit it to GitHub.

Set the environment variable before starting the application:

```bash
export YOUTUBE_API_KEY="your_api_key_here"
```

## Running the Application

Run the automated tests:

```bash
./gradlew test
```

Start the application:

```bash
./gradlew bootRun
```

The API will be available at:

```text
http://localhost:8080
```

## Testing

The automated tests verify that:

- New videos and snapshots are saved
- Existing videos are not duplicated
- Every collection creates a historical snapshot
- Empty collections are handled correctly
- Video categorization works
- The Spring application context loads successfully

Run all tests with:

```bash
./gradlew test
```

## Database

The development version uses an in-memory H2 database. The data is cleared whenever the application stops.

MySQL support is included for future persistent database configuration.

## Error Handling

When YouTube rejects a request because the API key is missing or invalid, FightPulse returns a clean JSON response instead of exposing an internal Java stack trace.

Example:

```json
{
  "status": 502,
  "error": "YouTube API request failed",
  "message": "YouTube rejected the request. Verify that the API key is configured and that YouTube Data API v3 is enabled."
}
```

## Future Improvements

- Configure MySQL for permanent storage
- Add pagination and filtering
- Add scheduled data collection
- Add authentication and authorization
- Build a frontend analytics dashboard
- Display engagement trends using charts

## Author

Sergio Figueroa

- GitHub: [SergioFigueroaCodes](https://github.com/SergioFigueroaCodes)
- LinkedIn: [sergio-figueroa-codes](https://www.linkedin.com/in/sergio-figueroa-codes/)