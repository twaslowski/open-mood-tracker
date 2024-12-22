function stop_environment() {
  echo "Stopping ..."
  docker compose -p mood-tracker -f local/docker-compose.yaml down
}

function start_environment() {
  echo "Starting ..."
  docker compose -p mood-tracker -f local/docker-compose.yaml up -d
}

function package() {
  ./mvnw package -DskipTests
}

function build() {
  TAG=$1

  if [ -z "$TAG" ]; then
    echo "TAG is required"
    exit 1
  fi

  ROOT=$(git rev-parse --show-toplevel)
  package
  docker build -t "open-mood-tracker:$TAG" "$ROOT"
}

function deploy() {
  TAG="sha-$(git rev-parse --short HEAD)"
  HELM_TIMEOUT=300s

  export DATASOURCE_PASSWORD=$(tr -dc 'A-Za-z0-9' </dev/urandom | head -c 48; echo)

  helm upgrade --install \
    --set global.postgresql.auth.password="$DATASOURCE_PASSWORD" \
    --values ./charts/values/postgres-values.yaml \
    --namespace grammr --create-namespace \
    --wait --timeout "$HELM_TIMEOUT" \
    postgres oci://registry-1.docker.io/bitnamicharts/postgresql

  helm upgrade --install \
    --set image.tag="$TAG" \
    --set telegramToken="$TELEGRAM_TOKEN" \
    --wait --timeout "$HELM_TIMEOUT" \
    open-mood-tracker ./charts/open-mood-tracker
}

function unit_test() {
  ./mvnw test
}

function integration_test() {
  ./mvnw package test -P integration
}

function run() {
  start_environment
  SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
}