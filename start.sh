#!/bin/bash

# Build the project
./gradlew bootJar -x test

# Run the app.jar file in the /build/libs directory
nohup java -jar build/libs/edusync_refac.jar 2>&1 &