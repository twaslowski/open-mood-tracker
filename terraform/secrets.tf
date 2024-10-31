resource "kubernetes_secret" "telegram_token" {
  metadata {
    name = "telegram-token"
    namespace = local.namespace
  }

  data = {
    telegram_token = var.telegram_token
  }
}