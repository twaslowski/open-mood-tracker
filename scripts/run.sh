#!/usr/bin/env bash

trap stop SIGINT EXIT SIGTERM

function stop() {
  echo "Stopping ..."
  unset SPRING_PROFILES_ACTIVE
  scripts/stop-environment.sh
}

source ".env"
export SPRING_PROFILES_ACTIVE=local

scripts/start-environment.sh

./mvnw clean spring-boot:run
