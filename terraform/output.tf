output "postgresql_admin_password" {
  value     = random_password.rp.result
  sensitive = true
}

output "frontend_webapp_url" {
  value       = "https://${azurerm_linux_web_app.frontend_app.default_hostname}"
  description = "Frontend web app URL"
}