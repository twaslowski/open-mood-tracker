#!/usr/bin/env zsh

trap stop EXIT SIGINT

function stop() {
  ./scripts/stop-environment.sh
}

./scripts/start-environment.sh
source .env

./mvnw test -P integration
./mvnw jacoco:report