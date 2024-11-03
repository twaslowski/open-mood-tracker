resource "kubernetes_namespace" "namespace" {
  metadata {
    name = local.namespace
  }
}