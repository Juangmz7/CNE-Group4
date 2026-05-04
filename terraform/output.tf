output "postgresql_admin_password" {
  value     = random_password.rp.result
  sensitive = true
}

output "frontend_webapp_url" {
  value       = "https://${azurerm_linux_web_app.frontend_app.default_hostname}"
  description = "Frontend web app URL"
}

output "frontend_webapp_hostname" {
  value       = azurerm_linux_web_app.frontend_app.default_hostname
  description = "Frontend web app hostname"
}

output "backend_function_app_url" {
  value       = "https://${azurerm_linux_function_app.spring_func.default_hostname}.azurewebsites.net/api/AzureWebAdapter/api"
  description = "Backend function app URL (API)"
}

output "backend_function_app_hostname" {
  value       = azurerm_linux_function_app.spring_func.default_hostname
  description = "Backend function app hostname"
}