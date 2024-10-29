#!/usr/bin/env bash

./mvnw clean test

./scripts/start-environment.sh
./mvnw test -P integration
./scripts/stop-environment.sh

./mvnw jacoco:report