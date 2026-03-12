# 宝宝奖励计划 - 环境配置说明

## 📋 两套环境配置

### 1️⃣ 本地开发环境 (dev)

**数据库配置：**
- **主机**: sh-cynosdbmysql-grp-as7r6ioi.sql.tencentcdb.com
- **端口**: 23701
- **数据库**: baby_reward
- **用户名**: root
- **密码**: P@ssword123

**启动方式：**
```bash
cd /Users/clovey-macmini/.openclaw/workspace/code/baby-reward-springboot

# 方式 1: 使用 Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 方式 2: 设置环境变量
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run
```

**测试接口：**
```bash
# 健康检查
curl http://localhost:8080/api/health

# 登录测试
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

---

### 2️⃣ 云托管生产环境 (prod)

**数据库配置：**
- **主机**: 10.17.103.12（内网地址）
- **端口**: 3306
- **数据库**: baby_reward
- **用户名**: root
- **密码**: P@ssword123

**Docker 镜像：**
- 默认使用 prod 配置
- 通过环境变量 `SPRING_PROFILES_ACTIVE` 切换

**部署步骤：**

```bash
# 1. 构建 Docker 镜像（使用 prod 配置）
cd /Users/clovey-macmini/.openclaw/workspace/code/baby-reward-springboot
docker build -t baby-reward-springboot:latest .

# 2. 推送镜像
docker tag baby-reward-springboot:latest ccr.ccs.tencentyun.com/wx641c37512334834c/baby-reward-api:latest
docker login ccr.ccs.tencentyun.com
docker push ccr.ccs.tencentyun.com/wx641c37512334834c/baby-reward-api:latest

# 3. 在微信云托管控制台部署
# - 选择镜像：baby-reward-api:latest
# - 端口：8080
# - 环境变量：SPRING_PROFILES_ACTIVE=prod
```

---

## 🔧 配置文件说明

### application.yml（主配置）
```yaml
spring:
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:prod}  # 默认 prod
```

### application-dev.yml（开发环境）
- 使用外网数据库地址
- 允许从本地访问

### application-prod.yml（生产环境）
- 使用内网数据库地址（10.17.103.12）
- 仅在云托管内网可访问
- 开启 DEBUG 日志

---

## 📊 环境对比

| 配置项 | 开发环境 (dev) | 生产环境 (prod) |
|--------|---------------|----------------|
| 数据库主机 | sh-cynosdbmysql-grp-as7r6ioi.sql.tencentcdb.com | 10.17.103.12 |
| 端口 | 23701 | 3306 |
| 网络类型 | 外网 | 内网 |
| 日志级别 | INFO | DEBUG |
| 使用场景 | 本地开发测试 | 云托管部署 |

---

## ⚠️ 注意事项

### 开发环境
1. 确保本地能访问外网数据库地址
2. 可能需要在腾讯云数据库控制台添加白名单
3. 添加你的本地 IP 到白名单

### 生产环境
1. 云托管自动在内网，无需额外配置
2. 确保数据库 10.17.103.12 在云托管同一 VPC
3. 数据库需要允许云托管服务访问

---

## 🚨 故障排查

### 本地无法连接数据库

```bash
# 1. 检查网络连通性
ping sh-cynosdbmysql-grp-as7r6ioi.sql.tencentcdb.com

# 2. 检查端口
telnet sh-cynosdbmysql-grp-as7r6ioi.sql.tencentcdb.com 23701

# 3. 查看应用日志
tail -f target/spring-boot-application.log
```

### 云托管无法连接数据库

1. **检查 VPC 配置**
   - 云托管服务和数据库必须在同一 VPC
   - 检查云托管控制台的网络配置

2. **检查数据库白名单**
   - 腾讯云数据库控制台
   - 添加云托管的 IP 段或允许整个 VPC

3. **查看云托管日志**
   - 云托管控制台 → 服务详情 → 日志查询

---

## 📞 快速切换环境

```bash
# 本地测试生产配置
export SPRING_PROFILES_ACTIVE=prod
mvn spring-boot:run

# 本地测试开发配置
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run

# Docker 容器指定环境
docker run -e SPRING_PROFILES_ACTIVE=dev baby-reward-springboot:latest
```
