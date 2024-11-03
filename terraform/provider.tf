terraform {
  backend "kubernetes" {
    secret_suffix = "open-mood-tracker-state"
    namespace     = "open-mood-tracker"
  }
}

provider "helm" {
}

provider "kubernetes" {

}
