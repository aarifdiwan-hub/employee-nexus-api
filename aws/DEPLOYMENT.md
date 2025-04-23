# AWS EC2 Deployment Guide

## Prerequisites
1. AWS Account with appropriate permissions
2. EC2 instance type: t2.micro or larger
3. Amazon Linux 2 AMI
4. Security Group with following inbound rules:
   - TCP 22 (SSH)
   - TCP 8080 (Application)

## Deployment Steps

### 1. Launch EC2 Instance
1. Go to EC2 Dashboard
2. Click "Launch Instance"
3. Choose Amazon Linux 2 AMI
4. Select instance type (t2.micro minimum)
5. Configure Instance:
   - Network: Your VPC
   - Subnet: Public subnet
   - Auto-assign Public IP: Enable
6. Add Storage: 20GB minimum
7. Configure Security Group
8. Create or select key pair

### 2. Configure User Data
1. In Advanced Details section, paste the contents of `ec2-user-data.sh`
2. Modify environment variables:
   - DB_PASSWORD
   - ADMIN_USER
   - ADMIN_PASSWORD

### 3. Post-Deployment Verification
1. Wait for instance to initialize (~5 minutes)
2. SSH into instance:
   ```bash
   ssh -i your-key.pem ec2-user@your-instance-ip
   ```
3. Check service status:
   ```bash
   sudo systemctl status employee-nexus
   ```
4. Check logs:
   ```bash
   sudo tail -f /var/log/employee-nexus/application.log
   ```

### 4. Access Application
- Application URL: http://your-instance-ip:8080
- Swagger UI: http://your-instance-ip:8080/swagger-ui.html
- Health Check: http://your-instance-ip:8080/actuator/health

## Monitoring
- CloudWatch Logs: /ec2/employee-nexus
- Metrics: Available in CloudWatch Metrics
- Alerts: Configure in CloudWatch Alarms

## Security Notes
1. Update passwords in user data script
2. Consider using AWS Secrets Manager for sensitive data
3. Use HTTPS in production
4. Restrict security group access
5. Regular security patches