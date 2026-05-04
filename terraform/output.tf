output "postgresql_admin_password" {
  value     = random_password.rp.result
  sensitive = true
}

output "frontend_webapp_url" {
  value       = "https://${azurerm_linux_web_app.frontend_app.default_hostname}"
  description = "Frontend web app URL"
}

output "frontend_webapp_name" {
  value       = azurerm_linux_web_app.frontend_app.name
  description = "Frontend web app name"
}

output "backend_function_app_url" {
  value       = "https://${azurerm_linux_function_app.spring_func.default_hostname}/api/AzureWebAdapter/api"
  description = "Backend function app URL (API)"
}

output "backend_function_app_name" {
  value       = azurerm_linux_function_app.spring_func.name
  description = "Backend function app name"
}