resource "azurerm_resource_group" "main" {
  name     = var.resource_group_name
  location = var.resource_group_location
}

data "azurerm_client_config" "current" {}

resource "random_integer" "ri" {
  min = 10000
  max = 99999
}

resource "random_password" "rp" {
  length  = 16
  special = false
}