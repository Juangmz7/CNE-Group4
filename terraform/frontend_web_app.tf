resource "azurerm_service_plan" "frontend_plan" {
  name                = "frontend-app-service-plan"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  os_type             = "Linux"
  sku_name            = "F1"
}

resource "azurerm_linux_web_app" "frontend_app" {
  name                = "webapp-react-front-${random_integer.ri.result}"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_service_plan.frontend_plan.location
  service_plan_id     = azurerm_service_plan.frontend_plan.id

  site_config {
    application_stack {
      node_version = "20-lts"
    }

    always_on = false
    
    app_command_line = "pm2 serve /home/site/wwwroot --no-daemon --spa"
  }
}

