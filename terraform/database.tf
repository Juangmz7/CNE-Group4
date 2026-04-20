resource "azurerm_postgresql_flexible_server" "db_server" {
  name                   = "postgresql-spring-server-${random_integer.ri.result}"
  resource_group_name    = azurerm_resource_group.main.name
  location               = azurerm_resource_group.main.location
  version                = "14"
  public_network_access_enabled = true
  administrator_login    = var.postgresql_server_administrator_username
  administrator_password = random_password.rp.result
  storage_mb             = var.postgresql_storage_mb
  sku_name               = var.postgresql_sku_name

  lifecycle {
    ignore_changes = [
      zone,
      high_availability.0.standby_availability_zone
    ]
  }
}

resource "azurerm_postgresql_flexible_server_database" "app_db" {
  name      = var.postgresql_db_name
  server_id = azurerm_postgresql_flexible_server.db_server.id
  collation = "en_US.utf8"
  charset   = "UTF8"

  # prevent the possibility of accidental data loss
  lifecycle {
    prevent_destroy = false
  }
}

resource "azurerm_postgresql_flexible_server_firewall_rule" "allow_azure_services" {
  name             = "allow-azure-services"
  server_id        = azurerm_postgresql_flexible_server.db_server.id
  start_ip_address = "0.0.0.0"
  end_ip_address   = "0.0.0.0"
}