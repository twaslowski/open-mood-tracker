terraform {
  backend "kubernetes" {
    secret_suffix = "open-mood-tracker-state"
  }
}

provider "helm" {
  kubernetes {
  }
}

provider "kubernetes" {
}
