variable "telegram_token" {
  description = "The token for the Telegram bot"
  type = string
}

variable "image_tag" {
  description = "The tag for the Docker image"
  type = string
  default = "latest"
}