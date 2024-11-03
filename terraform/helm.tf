resource "helm_release" "postgres" {
  name             = "postgres"
  repository       = "oci://registry-1.docker.io/bitnamicharts"
  chart            = "postgresql"
  namespace        = local.namespace
  create_namespace = true

  values = [
    <<YAML
    global:
      postgresql:
        auth:
          password: some-password
          username: some-user
          database: mood-tracker
    nameOverride: postgres
  YAML
  ]
}

resource "helm_release" "application" {
  chart     = "../charts/open-mood-tracker"
  name      = "open-mood-tracker"
  namespace = "open-mood-tracker"

  values = [
    <<YAML
    environmentVariables:
      - name: SPRING_PROFILES_ACTIVE
        value: prod
      - name: TELEGRAM_TOKEN
        valueFrom:
            secretKeyRef:
                name: telegram-token
                key: telegram_token
      - name: DATASOURCE_URL
        value: "jdbc:postgresql://postgres.${local.namespace}.svc.cluster.local:5432/mood-tracker"
      - name: DATASOURCE_USERNAME
        value: some-user
      - name: DATASOURCE_PASSWORD
        value: some-password
    YAML
  ]

  timeout    = 150
  depends_on = [helm_release.postgres]
}