# 宝宝奖励计划 - Spring Boot 快速部署指南

## 🚀 一键部署（推荐）

### 步骤 1: 构建并推送镜像

```bash
cd /Users/clovey-macmini/.openclaw/workspace/code/baby-reward-springboot

# 运行自动部署脚本
./scripts/auto-deploy.sh
```

### 步骤 2: 在微信云托管控制台部署

1. **访问控制台**
   ```
   https://cloud.weixin.qq.com/cloudrun
   ```

2. **创建服务**
   - 点击「新建服务」
   - 服务名称：`baby-reward-api`
   - 部署方式：容器镜像
   - 镜像来源：腾讯云容器镜像服务
   - 镜像：选择刚才推送的镜像

3. **配置服务**
   - **端口**: 8080
   - **CPU**: 0.5 核
   - **内存**: 512MB
   - **最小实例数**: 0
   - **最大实例数**: 5

4. **环境变量**（重要！）
   ```
   SPRING_PROFILES_ACTIVE=prod
   SERVER_PORT=8080
   DB_HOST=10.17.103.12
   DB_PORT=3306
   DB_NAME=baby_reward
   DB_USER=root
   DB_PASSWORD=P@ssword123
   ```

5. **确认部署**
   - 点击「创建」
   - 等待 2-5 分钟

---

## 📡 测试服务

部署成功后，测试接口：

```bash
# 获取服务地址（在云托管控制台查看）
SERVICE_URL="https://baby-reward-api-<环境 ID>.wxcloudrun.com"

# 健康检查
curl ${SERVICE_URL}/api/health

# 登录测试
curl -X POST ${SERVICE_URL}/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 微信登录测试
curl -X POST ${SERVICE_URL}/api/auth/wechat-login \
  -H "Content-Type: application/json" \
  -d '{"code":"test_123"}'
```

---

## 📱 配置小程序

修改小程序的 API 地址：

```javascript
// baby-reward-uniapp/src/utils/config.js
export const API_BASE_URL = 'https://baby-reward-api-<环境 ID>.wxcloudrun.com/api';
```

---

## ⚠️ 注意事项

### 数据库白名单

确保腾讯云数据库允许云托管服务访问：

1. 登录 [腾讯云数据库控制台](https://console.cloud.tencent.com/cdb)
2. 找到 `baby_reward` 数据库
3. 点击「白名单」
4. 添加云托管的 IP 段或整个 VPC

### 网络配置

云托管服务必须和数据库在同一 VPC：

1. 云托管控制台 → 网络配置
2. 选择与数据库相同的 VPC
3. 选择相同的子网

---

## 🆘 故障排查

### 服务启动失败

查看日志：
1. 云托管控制台 → 服务详情
2. 点击「日志查询」
3. 查看错误信息

常见错误：
- 数据库连接失败 → 检查白名单和网络配置
- 端口错误 → 确保设置为 8080

### 镜像推送失败

```bash
# 重新登录
docker logout ccr.ccs.tencentyun.com
docker login ccr.ccs.tencentyun.com

# 使用代理
export https_proxy=http://127.0.0.1:7890
```

---

## 📞 需要帮助？

如果遇到无法解决的问题：

1. 查看云托管控制台日志
2. 检查数据库连接配置
3. 确认网络和白名单配置
4. 联系技术支持

---

**部署成功后，服务地址格式：**
```
https://baby-reward-api-<环境 ID>.wxcloudrun.com
```

**完整示例：**
```
https://baby-reward-api-prod-1gdi8k8w31c0d359.wxcloudrun.com
```
