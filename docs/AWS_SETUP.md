# AWS Infrastructure Setup

> Documentation of AWS setup for cesar-portfolio deployment

**Account:** 771784399457
**Region:** us-east-1
**Domain:** cesarvillarreal.dev
**Date:** 2026-02-01

---

## Infrastructure Summary

| Resource | Identifier | Status |
|----------|------------|--------|
| ECR Repository | cesar-portfolio | ✅ Active |
| RDS PostgreSQL | cesar-portfolio-db | ✅ Running |
| App Runner | cesar-portfolio | ✅ Running |
| Security Group | portfolio-rds-sg | ✅ Configured |

---

## 1. ECR Repository

Container registry for Docker images.

```bash
aws ecr create-repository \
  --repository-name cesar-portfolio \
  --region us-east-1 \
  --image-scanning-configuration scanOnPush=true
```

**Repository URI:** `771784399457.dkr.ecr.us-east-1.amazonaws.com/cesar-portfolio`

### Building and Pushing Images

**IMPORTANT:** Build for linux/amd64 architecture (App Runner requirement):

```bash
# Login to ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 771784399457.dkr.ecr.us-east-1.amazonaws.com

# Build for correct architecture and push
docker buildx build --platform linux/amd64 -t 771784399457.dkr.ecr.us-east-1.amazonaws.com/cesar-portfolio:latest --push .
```

---

## 2. RDS PostgreSQL

Managed PostgreSQL database.

### Configuration

| Setting | Value |
|---------|-------|
| Engine | PostgreSQL 16.11 |
| Instance | db.t3.micro |
| Storage | 20 GB gp2 |
| Database | portfolio |
| Username | portfolio_admin |
| Multi-AZ | No |
| Public Access | Yes |

**Endpoint:** `cesar-portfolio-db.cy0ciel023wm.us-east-1.rds.amazonaws.com:5432`

### Security Group

- **Name:** portfolio-rds-sg
- **ID:** sg-042123129440e064a
- **Inbound Rule:** TCP 5432 from 0.0.0.0/0

### Connection String

```
jdbc:postgresql://cesar-portfolio-db.cy0ciel023wm.us-east-1.rds.amazonaws.com:5432/portfolio
```

### Commands Used

```bash
# Create security group
aws ec2 create-security-group \
  --group-name portfolio-rds-sg \
  --description "Security group for Portfolio RDS" \
  --vpc-id vpc-084e05ac8fb31f357

# Allow PostgreSQL access
aws ec2 authorize-security-group-ingress \
  --group-id sg-042123129440e064a \
  --protocol tcp \
  --port 5432 \
  --cidr 0.0.0.0/0

# Create subnet group
aws rds create-db-subnet-group \
  --db-subnet-group-name portfolio-subnet-group \
  --db-subnet-group-description "Subnet group for Portfolio RDS" \
  --subnet-ids subnet-0d99f96c368d03d1c subnet-09d9df5f1a3139a25 subnet-083dfebf419a7aaec

# Create RDS instance
aws rds create-db-instance \
  --db-instance-identifier cesar-portfolio-db \
  --db-instance-class db.t3.micro \
  --engine postgres \
  --engine-version 16.11 \
  --master-username portfolio_admin \
  --master-user-password <PASSWORD> \
  --allocated-storage 20 \
  --storage-type gp2 \
  --db-name portfolio \
  --vpc-security-group-ids sg-042123129440e064a \
  --db-subnet-group-name portfolio-subnet-group \
  --publicly-accessible \
  --backup-retention-period 7 \
  --no-multi-az
```

---

## 3. App Runner Service

Managed container hosting with auto-scaling.

### Configuration

| Setting | Value |
|---------|-------|
| CPU | 1 vCPU |
| Memory | 2 GB |
| Port | 8080 |
| Auto-deploy | Enabled |
| Health Check | TCP on port 8080 |

**Service URL:** `https://pnzm3urbwb.us-east-1.awsapprunner.com`
**Service ARN:** `arn:aws:apprunner:us-east-1:771784399457:service/cesar-portfolio/15061563b85c41139499ed9184f2c67f`

### Environment Variables

```
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:postgresql://cesar-portfolio-db.cy0ciel023wm.us-east-1.rds.amazonaws.com:5432/portfolio
DATABASE_USERNAME=portfolio_admin
DATABASE_PASSWORD=<PASSWORD>
```

### IAM Role

- **Name:** AppRunnerECRAccessRole
- **ARN:** `arn:aws:iam::771784399457:role/AppRunnerECRAccessRole`
- **Policy:** AWSAppRunnerServicePolicyForECRAccess

---

## 4. GitHub Secrets

Required secrets for CI/CD pipeline (Settings → Secrets → Actions):

| Secret | Value | Status |
|--------|-------|--------|
| AWS_ACCESS_KEY_ID | IAM access key | ⬜ To configure |
| AWS_SECRET_ACCESS_KEY | IAM secret key | ⬜ To configure |
| APP_RUNNER_SERVICE_ARN | `arn:aws:apprunner:us-east-1:771784399457:service/cesar-portfolio/15061563b85c41139499ed9184f2c67f` | ⬜ To configure |
| APP_DOMAIN | cesarvillarreal.dev | ⬜ To configure |

---

## 5. Cloudflare DNS

CNAME record pointing domain to App Runner.

| Type | Name | Target |
|------|------|--------|
| CNAME | @ | pnzm3urbwb.us-east-1.awsapprunner.com |
| CNAME | www | pnzm3urbwb.us-east-1.awsapprunner.com |

---

## Troubleshooting

### Architecture Mismatch Error

If you see `exec format error` in App Runner logs, the Docker image was built for the wrong architecture.

**Solution:** Always build with `--platform linux/amd64`:

```bash
docker buildx build --platform linux/amd64 -t <ECR_URI>:latest --push .
```

### Viewing App Runner Logs

```bash
# Find log groups
aws logs describe-log-groups --log-group-name-prefix "/aws/apprunner"

# Get recent logs
aws logs get-log-events \
  --log-group-name "/aws/apprunner/cesar-portfolio/<SERVICE_ID>/application" \
  --log-stream-name "<STREAM_NAME>" \
  --query 'events[*].message' \
  --output text
```

### Testing RDS Connectivity

```bash
PGPASSWORD=<PASSWORD> psql -h cesar-portfolio-db.cy0ciel023wm.us-east-1.rds.amazonaws.com -U portfolio_admin -d portfolio -c "SELECT 1;"
```

---

## Cost Estimates (Monthly)

| Service | Estimate |
|---------|----------|
| RDS db.t3.micro | ~$15-20 (or free tier) |
| App Runner (1 vCPU, 2GB) | ~$25-40 |
| ECR Storage | < $1 |
| Data Transfer | Varies |

**Total:** ~$40-60/month (less with free tier)

---

## Cleanup Commands

```bash
# Delete App Runner
aws apprunner delete-service --service-arn <ARN>

# Delete RDS
aws rds delete-db-instance --db-instance-identifier cesar-portfolio-db --skip-final-snapshot

# Delete ECR
aws ecr delete-repository --repository-name cesar-portfolio --force

# Delete security group
aws ec2 delete-security-group --group-id sg-042123129440e064a

# Delete subnet group
aws rds delete-db-subnet-group --db-subnet-group-name portfolio-subnet-group

# Delete IAM role
aws iam detach-role-policy --role-name AppRunnerECRAccessRole --policy-arn arn:aws:iam::aws:policy/service-role/AWSAppRunnerServicePolicyForECRAccess
aws iam delete-role --role-name AppRunnerECRAccessRole
```
