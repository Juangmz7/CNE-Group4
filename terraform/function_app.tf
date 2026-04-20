resource "azurerm_storage_account" "func_storage" {
  name                     = "funcspringstorage${random_integer.ri.result}"
  resource_group_name      = azurerm_resource_group.main.name
  location                 = azurerm_resource_group.main.location
  account_tier             = "Standard"
  account_replication_type = var.function_app_storage_account_replication_type
}

resource "azurerm_service_plan" "func_plan" {
  name                = "spring-app-service-plan"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  os_type             = "Linux"
  sku_name            = var.function_app_sku_name
}

resource "azurerm_log_analytics_workspace" "app_analytics" {
  name                = "log-workspace-${random_integer.ri.result}"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  sku                 = "PerGB2018"
  retention_in_days   = var.function_app_analytics_workspace_retention_in_days        
}

resource "azurerm_application_insights" "app_insights" {
  name                = "insights-spring-app-${random_integer.ri.result}"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  workspace_id        = azurerm_log_analytics_workspace.app_analytics.id
  application_type    = "java" 
}

resource "azurerm_linux_function_app" "spring_func" {
  name                = "function-spring-app-${random_integer.ri.result}"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location

  storage_account_name       = azurerm_storage_account.func_storage.name
  storage_account_access_key = azurerm_storage_account.func_storage.primary_access_key
  service_plan_id            = azurerm_service_plan.func_plan.id

  site_config {
    application_stack {
      java_version = "21"
    }

    cors {
      allowed_origins     = ["*"]
      support_credentials = false 
    }
  }


  app_settings = {
    "FUNCTIONS_WORKER_RUNTIME" = "java"
    
    "APPLICATIONINSIGHTS_CONNECTION_STRING" = azurerm_application_insights.app_insights.connection_string

    "SPRING_DATASOURCE_URL"      = "jdbc:postgresql://${azurerm_postgresql_flexible_server.db_server.fqdn}:5432/${azurerm_postgresql_flexible_server_database.app_db.name}?sslmode=require"
    "SPRING_DATASOURCE_USERNAME" = var.postgresql_server_administrator_username
    "SPRING_DATASOURCE_PASSWORD" = random_password.rp.result
  }
}