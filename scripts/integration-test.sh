#!/usr/bin/env zsh

./scripts/start-environment.sh
source .env

./mvnw test -P integration
./mvnw jacoco:report