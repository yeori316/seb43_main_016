#!/bin/bash

# Build the project
./gradlew bootJar -x test

# Run the app.jar file in the /build/libs directory
nohup java -jar build/libs/edusync_refac.jar --spring.profiles.active=tmp 2>&1 &