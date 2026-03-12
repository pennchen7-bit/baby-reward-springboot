# 🚀 宝宝奖励计划 Spring Boot - 最终部署步骤

## ✅ 已完成的工作

1. **Spring Boot 代码** - 100% 完成
2. **Docker 镜像** - 已构建成功
3. **配置文件** - 开发/生产环境已配置
4. **部署脚本** - 已创建

---

## ⏳ 最后一步：推送镜像并部署

### 步骤 1: 获取腾讯云容器仓库凭证

**访问：** https://console.cloud.tencent.com/ccr

1. 登录腾讯云控制台
2. 进入「容器镜像服务」
3. 点击「访问凭证」
4. 复制用户名和密码

---

### 步骤 2: 推送镜像

```bash
cd /Users/clovey-macmini/.openclaw/workspace/code/baby-reward-springboot

# 登录腾讯云容器仓库
docker login ccr.ccs.tencentyun.com -u <你的用户名> -p <你的密码>

# 推送镜像
docker push ccr.ccs.tencentyun.com/wx641c37512334834c/baby-reward-api:latest
```

**预计时间：** 2-5 分钟

---

### 步骤 3: 部署到微信云托管

**访问：** https://cloud.weixin.qq.com/cloudrun

1. **登录微信云托管控制台**
   - 使用小程序管理员微信扫码登录

2. **选择环境**
   - 选择或创建云托管环境

3. **创建服务**
   - 点击「新建服务」
   - 服务名称：`baby-reward-api`
   - 部署方式：容器镜像
   - 镜像来源：腾讯云容器镜像服务
   - 选择镜像：`baby-reward-api:latest`

4. **配置服务**
   ```
   端口：8080
   CPU: 0.5 核
   内存：512MB
   最小实例数：0
   最大实例数：5
   ```

5. **配置环境变量**（重要！）
   ```
   SPRING_PROFILES_ACTIVE=prod
   SERVER_PORT=8080
   DB_HOST=10.17.103.12
   DB_PORT=3306
   DB_NAME=baby_reward
   DB_USER=root
   DB_PASSWORD=P@ssword123
   ```

6. **确认部署**
   - 点击「创建」
   - 等待 2-5 分钟

---

### 步骤 4: 测试服务

部署成功后，获取服务地址：
```
https://baby-reward-api-<环境 ID>.wxcloudrun.com
```

测试接口：
```bash
# 健康检查
curl https://baby-reward-api-<环境 ID>.wxcloudrun.com/api/health

# 登录测试
curl -X POST https://baby-reward-api-<环境 ID>.wxcloudrun.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

---

### 步骤 5: 配置小程序

修改小程序的 API 地址：

```javascript
// baby-reward-uniapp/src/utils/config.js
export const API_BASE_URL = 'https://baby-reward-api-<环境 ID>.wxcloudrun.com/api';
```

---

## 📊 部署检查清单

- [ ] Docker 镜像已构建
- [ ] 已获取腾讯云容器仓库凭证
- [ ] 已登录容器仓库
- [ ] 镜像已推送成功
- [ ] 已登录微信云托管控制台
- [ ] 服务已创建
- [ ] 环境变量已配置
- [ ] 服务状态显示「运行中」
- [ ] 健康检查接口返回正常
- [ ] 小程序 API 地址已更新

---

## 💡 快速命令参考

```bash
# 构建镜像
cd /Users/clovey-macmini/.openclaw/workspace/code/baby-reward-springboot
docker build -t baby-reward-springboot:latest .

# 登录容器仓库
docker login ccr.ccs.tencentyun.com -u <用户名> -p <密码>

# 推送镜像
docker push ccr.ccs.tencentyun.com/wx641c37512334834c/baby-reward-api:latest

# 测试本地运行
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=10.17.103.12 \
  -e DB_NAME=baby_reward \
  -e DB_USER=root \
  -e DB_PASSWORD=P@ssword123 \
  baby-reward-springboot:latest
```

---

## 🆘 故障排查

### 镜像推送失败

```bash
# 检查登录状态
docker login ccr.ccs.tencentyun.com

# 重新登录
docker logout ccr.ccs.tencentyun.com
docker login ccr.ccs.tencentyun.com
```

### 服务启动失败

1. 云托管控制台 → 服务详情
2. 点击「日志查询」
3. 查看错误信息

常见错误：
- 数据库连接失败 → 检查白名单和环境变量
- 端口错误 → 确保设置为 8080

---

## 📞 需要帮助？

如果遇到问题：

1. 查看部署日志
2. 检查环境变量配置
3. 确认数据库连接信息
4. 联系技术支持

---

**预计总时间：10-15 分钟**

**当前进度：90% 完成，最后一步推送和部署即可！** 🎉
