# 宝宝奖励计划 - Spring Boot 后端

基于 Spring Boot 3.2 的后端服务，用于微信云托管部署。

## 📋 技术栈

- **Java 17**
- **Spring Boot 3.2**
- **Spring Data JPA**
- **MySQL**
- **Lombok**

## 🚀 快速开始

### 本地开发

1. **安装依赖**
   ```bash
   # 需要 Java 17 和 Maven
   java -version
   mvn -version
   ```

2. **配置数据库**
   
   修改 `src/main/resources/application.yml`：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://你的数据库地址:3306/baby_reward
       username: 用户名
       password: 密码
   ```

3. **运行应用**
   ```bash
   mvn spring-boot:run
   ```

4. **测试接口**
   ```bash
   # 健康检查
   curl http://localhost:8080/api/health
   
   # 登录
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"admin123"}'
   ```

### 云托管部署

1. **构建 Docker 镜像**
   ```bash
   docker build -t baby-reward-springboot:latest .
   ```

2. **推送镜像**
   ```bash
   # 登录腾讯云容器仓库
   docker login ccr.ccs.tencentyun.com
   
   # 推送镜像
   docker tag baby-reward-springboot:latest ccr.ccs.tencentyun.com/wx641c37512334834c/baby-reward-api:latest
   docker push ccr.ccs.tencentyun.com/wx641c37512334834c/baby-reward-api:latest
   ```

3. **部署到云托管**
   
   访问 [微信云托管控制台](https://cloud.weixin.qq.com/cloudrun)：
   - 创建服务
   - 选择镜像
   - 配置端口：8080
   - 配置环境变量

## 📡 API 接口

### 认证相关

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 登录 | POST | /api/auth/login | 普通登录 |
| 微信登录 | POST | /api/auth/wechat-login | 微信一键登录 |
| 获取当前用户 | GET | /api/auth/me | 获取当前登录用户 |

### 奖品管理

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 获取奖品列表 | GET | /api/prizes | 获取奖品列表 |
| 创建奖品 | POST | /api/prizes | 创建新奖品 |
| 更新奖品 | PUT | /api/prizes | 更新奖品信息 |
| 删除奖品 | DELETE | /api/prizes?id=xxx | 删除奖品 |

### 健康检查

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 健康检查 | GET | /api/health | 检查服务状态 |

## 📝 请求示例

### 普通登录

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123",
    "familyCode": "8888"
  }'
```

### 微信登录

```bash
curl -X POST http://localhost:8080/api/auth/wechat-login \
  -H "Content-Type: application/json" \
  -d '{
    "code": "微信登录 code",
    "familyCode": "8888",
    "role": "admin"
  }'
```

### 获取奖品列表

```bash
curl http://localhost:8080/api/prizes?familyId=xxx
```

## 🔧 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| DB_HOST | 数据库主机 | 8.153.98.86 |
| DB_USER | 数据库用户名 | baby_reward |
| DB_PASSWORD | 数据库密码 | BabyReward2026! |
| DB_NAME | 数据库名 | baby_reward |
| SERVER_PORT | 服务端口 | 8080 |

## 📊 项目结构

```
baby-reward-springboot/
├── src/main/java/com/clovey/babyreward/
│   ├── BabyRewardApplication.java    # 启动类
│   ├── controller/                    # 控制器
│   │   ├── AuthController.java
│   │   ├── PrizeController.java
│   │   └── HealthController.java
│   ├── service/                       # 服务层
│   ├── repository/                    # 数据访问层
│   │   ├── FamilyRepository.java
│   │   ├── UserRepository.java
│   │   └── PrizeRepository.java
│   ├── entity/                        # 实体类
│   │   ├── Family.java
│   │   ├── User.java
│   │   ├── Prize.java
│   │   ├── DrawRequest.java
│   │   ├── DrawRecord.java
│   │   └── Report.java
│   └── dto/                           # 数据传输对象
│       ├── ApiResponse.java
│       ├── LoginRequest.java
│       ├── WechatLoginRequest.java
│       └── UserResponse.java
├── src/main/resources/
│   └── application.yml                # 配置文件
├── Dockerfile                         # Docker 配置
├── pom.xml                            # Maven 配置
└── scripts/
    └── deploy.sh                      # 部署脚本
```

## 📞 技术支持

如有问题，请联系开发团队。
