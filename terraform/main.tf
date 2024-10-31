resource "helm_release" "postgres" {
  name             = "postgres"
  repository       = "oci://registry-1.docker.io/bitnamicharts"
  chart            = "postgresql"
  namespace        = "open-mood-tracker"
  create_namespace = true
}

resource "helm_release" "application" {
  chart     = "../charts/open-mood-tracker"
  name      = "open-mood-tracker"
  namespace = "open-mood-tracker"

  values = [<<YAML
    environmentVariables:
      - name: SPRING_PROFILES_ACTIVE
        value: prod
      - name: TELEGRAM_TOKEN
        value: ${var.telegram_token}
    YAML
  ]

  depends_on = [helm_release.postgres]
}