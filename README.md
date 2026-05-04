# Deployment Guide

## Prerequisites

- [Terraform](https://developer.hashicorp.com/terraform/install) >= 1.0
- [Azure CLI](https://learn.microsoft.com/en-us/cli/azure/install-azure-cli) logged in (`az login`)
- Node.js 20.x
- Java 21 ([Eclipse Temurin](https://adoptium.net/) recommended)
- Maven 3.x
- A GitHub repository with Actions enabled

---

## 1. Terraform Setup

### 1.1 Configure variables

Copy the template and fill in your values:

```bash
cp terraform.tfvars_template terraform.tfvars
```

Edit `terraform.tfvars`:

```hcl
subscription_id                          = "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
postgresql_server_administrator_username = "your_postgres_admin_username"
jwt_private_key_b64                      = "your_base64_encoded_jwt_private_key"
```

> `terraform.tfvars` is gitignored and should never be committed.

Place the corresponding RSA public key in:
 
```
src/main/resources/certs/public.pem
```
 
It must match the private key encoded in `jwt_private_key_b64`. Expected format:
 
```
-----BEGIN PUBLIC KEY-----
<base64 encoded key>
-----END PUBLIC KEY-----
```

### 1.2 Initialize and apply

```bash
terraform init
terraform plan
terraform apply
```

### 1.3 Retrieve outputs

Once apply completes, get the values you'll need for GitHub configuration:

```bash
# App names (for GitHub repository variables)
terraform output frontend_webapp_hostname
terraform output backend_function_app_hostname

# Full URLs (for reference and CORS configuration)
terraform output frontend_webapp_url
terraform output backend_function_app_url

# Postgres password (sensitive)
terraform output -raw postgresql_admin_password
```

---

## 2. GitHub Repository Configuration

### 2.1 Repository Variables

Go to your repository → **Settings** → **Secrets and variables** → **Actions** → **Variables tab**.

Create the following variables using the values from `terraform output`:

| Variable | Value |
|---|---|
| `AZURE_WEBAPP_FRONTEND_NAME` | output of `terraform output frontend_webapp_hostname` |
| `AZURE_FUNCTIONAPP_NAME` | output of `terraform output backend_function_app_hostname` |
| `REACT_APP_API_BASE_URL` | output of `terraform output backend_function_app_url` |

### 2.2 Publish Profiles (Secrets)

You need to download a publish profile for each Azure resource and add it as a GitHub secret.

**Frontend (React Web App):**

1. Go to [Azure Portal](https://portal.azure.com)
2. Navigate to your **App Service** (frontend)
3. Click **Overview** → **Download publish profile**
4. Copy the file contents
5. In GitHub → **Settings** → **Secrets and variables** → **Actions** → **New repository secret**
6. Name: `AZURE_WEBAPP_PUBLISH_PROFILE`, Value: paste the file contents

**Backend (Function App):**

1. Go to [Azure Portal](https://portal.azure.com)
2. Navigate to your **Function App** (backend)
3. Click **Overview** → **Download publish profile**
4. Copy the file contents
5. In GitHub → **New repository secret**
6. Name: `AZURE_FUNCTIONAPP_PUBLISH_PROFILE`, Value: paste the file contents

---

## 3. Deploy

Go to your repository → **Actions**.

Run the following workflows manually (or push to `main`):

1. **Build and Deploy React Frontend**
2. **Build and Deploy Backend Function App**

Ensure both complete successfully before proceeding.

---

## 4. Access the Application

Once both pipelines are green, retrieve the frontend URL:

```bash
terraform output frontend_webapp_url
```

Open that URL in your browser.
