resource "azurerm_key_vault" "key-vault" {
  name                = "kv-spring-${random_integer.ri.result}"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  tenant_id           = data.azurerm_client_config.current.tenant_id
  sku_name            = "standard"
}

resource "azurerm_key_vault_access_policy" "terraform_policy" {
  key_vault_id = azurerm_key_vault.key-vault.id
  tenant_id    = data.azurerm_client_config.current.tenant_id
  object_id    = data.azurerm_client_config.current.object_id

  secret_permissions = ["Get", "List", "Set", "Delete"]
}

resource "azurerm_key_vault_access_policy" "func_kv_policy" {
  key_vault_id = azurerm_key_vault.key-vault.id
  tenant_id    = data.azurerm_client_config.current.tenant_id
  object_id    = azurerm_linux_function_app.spring_func.identity[0].principal_id

  secret_permissions = ["Get", "List"]
}

resource "azurerm_key_vault_secret" "jwt_private_key" {
  name         = "jwt-private-key-b64"
  value        = var.jwt_private_key_b64
  key_vault_id = azurerm_key_vault.key-vault.id

  depends_on = [
    azurerm_key_vault_access_policy.terraform_policy
  ]
}