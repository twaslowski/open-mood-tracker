terraform {
  backend "kubernetes" {
    secret_suffix = "open-mood-tracker-state"
    config_path   = "~/.kube/config"
  }

  required_providers {
    helm = {
      source  = "hashicorp/helm"
      version = "~> 2.0"
    }
  }
}