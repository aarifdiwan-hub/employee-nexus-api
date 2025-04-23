#!/bin/bash
set -e

# Update system
yum update -y

# Install Java 17
amazon-linux-extras enable java-openjdk17
yum install -y java-17-openjdk

# Install required tools
yum install -y wget unzip

# Create application directory
mkdir -p /opt/employee-nexus
cd /opt/employee-nexus

# Create application user
useradd -r -s /sbin/nologin employee-nexus

# Download the latest release from GitHub (adjust the URL as needed)
wget https://github.com/aarifdiwan-hub/employee-nexus-api/releases/latest/download/employee-nexus-api.jar

# Set proper ownership
chown -R employee-nexus:employee-nexus /opt/employee-nexus

# Create application configuration
cat > /opt/employee-nexus/application.yml << 'EOL'
spring:
  application:
    name: employee-nexus-api
  datasource:
    url: jdbc:h2:file:/opt/employee-nexus/data/employeedb
    username: sa
    password: ${DB_PASSWORD}
    driver-class-name: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update
  security:
    user:
      name: ${ADMIN_USER}
      password: ${ADMIN_PASSWORD}

server:
  port: 8080
  error:
    include-message: always

# Set logging to file
logging:
  file:
    name: /var/log/employee-nexus/application.log
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
EOL

# Create systemd service file
cat > /etc/systemd/system/employee-nexus.service << 'EOL'
[Unit]
Description=Employee Nexus API Service
After=network.target

[Service]
Type=simple
User=employee-nexus
Environment="DB_PASSWORD=your-secure-password"
Environment="ADMIN_USER=admin"
Environment="ADMIN_PASSWORD=your-admin-password"
Environment="JAVA_OPTS=-Xms512m -Xmx1024m -Djava.security.egd=file:/dev/./urandom"
ExecStart=/usr/bin/java $JAVA_OPTS -jar /opt/employee-nexus/employee-nexus-api.jar
SuccessExitStatus=143
TimeoutStopSec=10
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
EOL

# Create log directory
mkdir -p /var/log/employee-nexus
chown employee-nexus:employee-nexus /var/log/employee-nexus

# Start and enable the service
systemctl daemon-reload
systemctl enable employee-nexus
systemctl start employee-nexus

# Install and configure CloudWatch agent for monitoring
yum install -y amazon-cloudwatch-agent

# Create CloudWatch agent configuration
cat > /opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json << 'EOL'
{
    "agent": {
        "metrics_collection_interval": 60,
        "run_as_user": "root"
    },
    "logs": {
        "logs_collected": {
            "files": {
                "collect_list": [
                    {
                        "file_path": "/var/log/employee-nexus/application.log",
                        "log_group_name": "/ec2/employee-nexus",
                        "log_stream_name": "{instance_id}",
                        "timezone": "UTC"
                    }
                ]
            }
        }
    },
    "metrics": {
        "metrics_collected": {
            "mem": {
                "measurement": ["mem_used_percent"]
            },
            "swap": {
                "measurement": ["swap_used_percent"]
            }
        }
    }
}
EOL

# Start CloudWatch agent
systemctl enable amazon-cloudwatch-agent
systemctl start amazon-cloudwatch-agent

# Setup basic security
cat > /etc/security/limits.d/employee-nexus.conf << 'EOL'
employee-nexus soft nofile 65536
employee-nexus hard nofile 65536
EOL

# Configure firewall
firewall-cmd --permanent --add-port=8080/tcp
firewall-cmd --reload