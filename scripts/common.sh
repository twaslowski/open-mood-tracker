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

  export TF_VAR_image_tag="$TAG"
  export TF_VAR_telegram_token="$TELEGRAM_TOKEN"

  pushd terraform || exit
  terraform init
  terraform apply -auto-approve
}

function unit_test() {
  ./mvnw test
}

function integration_test() {
  ./mvnw package test -P integration
}

function run() {
  ./mvnw spring-boot:run
}