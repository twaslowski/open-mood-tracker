terraform {
  backend "kubernetes" {
    secret_suffix = "open-mood-tracker-state"
    namespace     = "arc-runner"
  }
}

provider "helm" {
}

provider "kubernetes" {

}
