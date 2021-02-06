# Top Score Game

Basic Java Spring Test Application: Top Score Ranking Game System
Build Restful API for a simple Note-Taking application using Spring Boot, H2 DB, JPA and Gradle.

## Requirements

1. Java - 1.11.x

2. gradle - 6.x.x

3. H2 - H2

## Steps to Setup

**1. Clone the application from the master branch using below url**

```bash
git clone https://github.com/cmanix07/top-score-ranking.git
```

**2. Create mysql database**
```bash
create database PLAYER
Script has been added and it create on startup
```


**3. Credentials are added into properties file**

+ open `src/main/resources/application.properties`


**4. Build and run the app using gradle**

```bash
./gradlew -Pprod clean bootJar
java -jar build/libs/*.jar
```

Alternatively, you can run the app without packaging it using -

```bash
./gradlew
```

The app will start running at <http://localhost:9191>.

## Explore Rest APIs

The app defines following APIs.

Get score

    GET /score-ranking/api/v1/player/{id}
    
Delete score

    DELETE /score-ranking/api/v1/player/{id}
    
Get list of score

    GET /score-ranking/api/v1/player-history/?name=chinta&before=2021-02-11&after=2021-02-02
    
Player's history
    
    GET /score-ranking/api/v1/player-history
    
Add player score

    POST /score-ranking/api/v1/score
    
    {
    "player":"chinta",
    "score": 100,
    "time":"2021-02-10 10:10:00"
    }
    

You can test them using postman or any other rest client.


## Testing

To launch your application's tests, run:

    ./gradlew test integrationTest 
    
    
