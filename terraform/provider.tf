terraform {
  backend "kubernetes" {
    secret_suffix = "${local.application_name}-state"
    namespace     = "arc-runner"
  }
}

provider "helm" {
}

provider "kubernetes" {

}
