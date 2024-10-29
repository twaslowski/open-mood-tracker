function stop_environment() {
  echo "Stopping ..."
  docker compose -p mood-tracker -f local/docker-compose.yaml down
}

function start_environment() {
  echo "Starting ..."
  docker compose -p mood-tracker -f local/docker-compose.yaml up -d
}

function unit_test() {
  ./mvnw test
}

function integration_test() {
  ./mvnw test -P integration
}

function run() {
  ./mvnw spring-boot:run
}