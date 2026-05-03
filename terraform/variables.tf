variable "subscription_id" {
    type        = string
    description = "Azure subscription Id"
    sensitive   = true
}

variable "resource_group_name" {
    type        = string
    default     = "cne-final-project-v1"
    description = "Name of the resource group"
    sensitive = false
}

variable "resource_group_location" {
    type        = string
    default     = "uaenorth"
    description = "Location of the resource group"
    sensitive = false
}


variable "postgresql_server_administrator_username" {
    type        = string
    description = "PostgreSQL server administrator username"
    sensitive   = true
}

variable "postgresql_storage_mb" {
    type        = number
    default     = 32768
    description = "PostgreSQL server storage in MB"
    sensitive   = false
}

variable "postgresql_sku_name" {
    type        = string
    default     = "B_Standard_B1ms"
    description = "PostgreSQL server SKU name"
    sensitive   = false
}

variable "function_app_sku_name" {
    type        = string
    default     = "Y1"
    description = "Function app SKU name"
    sensitive   = false
}

variable "postgresql_db_name" {
    type        = string
    default     = "cne-db"
    description = "PostgreSQL database name"
    sensitive   = false
}

variable "function_app_analytics_workspace_retention_in_days" {
    type        = number
    default     = 30
    description = "Retention period for the Function app analytics workspace in days"
    sensitive   = false
}

variable "function_app_storage_account_replication_type" {
    type        = string
    default     = "LRS"
    description = "Replication type for the Function app storage account"
    sensitive   = false
}

variable "jwt_private_key_b64" {
  description = "JWT private key in base64"
  type        = string
  sensitive   = true
}
