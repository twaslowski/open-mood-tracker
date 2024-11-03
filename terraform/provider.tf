terraform {
  backend "kubernetes" {
    secret_suffix = "open-mood-tracker-state"
    namespace     = "arc-runner"
  }

  required_providers {
    helm = {
      source  = "hashicorp/helm"
      version = "~> 2.0"
    }

    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.0"
    }
  }
}

provider "kubernetes" {}

provider "helm" {}