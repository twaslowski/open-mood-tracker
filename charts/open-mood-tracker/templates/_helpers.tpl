{{/*
Expand the name of the chart.
*/}}
{{- define "mood-tracker.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "mood-tracker.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "mood-tracker.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "mood-tracker.labels" -}}
helm.sh/chart: {{ include "mood-tracker.chart" . }}
{{ include "mood-tracker.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "mood-tracker.selectorLabels" -}}
app.kubernetes.io/name: {{ include "mood-tracker.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "mood-tracker.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "mood-tracker.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{- define "utils.secret.keepOrCreate" -}}
{{- /*
    Template function to lookup a secret value or generate a new one if it doesn't exist.

    Parameters:
      - .namespace: The namespace of the secret to lookup
      - .name: The name of the secret to lookup
      - .key: The key within the secret to look up

    Usage:
    {{ include "utils.secret.lookup" (dict "namespace" "my-namespace" "name" "my-secret" "key" "secret-key") }}
*/}}
{{- $secretObj := (lookup "v1" "Secret" .namespace .name) | default dict }}
{{- $secretData := (get $secretObj "data") | default dict }}
{{- $value := (get $secretData .key) | default (randAlphaNum 64 | b64enc) }}
{{- $value | quote }}
{{- end }}
