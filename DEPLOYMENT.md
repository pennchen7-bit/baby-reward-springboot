# 宝宝奖励计划 - 微信云托管部署指南

## 📋 部署前准备

### 1. 确认环境

```bash
# 检查 Docker
docker --version

# 检查项目
cd /Users/clovey-macmini/.openclaw/workspace/code/baby-reward-springboot
ls -la
```

### 2. 确认数据库信息

- **主机**: 8.153.98.86
- **数据库**: baby_reward
- **用户名**: baby_reward
- **密码**: BabyReward2026!

---

## 🚀 部署步骤

### 步骤 1: 构建 Docker 镜像

```bash
cd /Users/clovey-macmini/.openclaw/workspace/code/baby-reward-springboot
docker build -t baby-reward-springboot:latest .
```

**预计时间**: 5-10 分钟（首次需要下载基础镜像）

**成功标志**:
```
Successfully built xxxxx
Successfully tagged baby-reward-springboot:latest
```

---

### 步骤 2: 登录腾讯云容器仓库

```bash
# 方式 1: 使用微信云托管 CLI（如果已安装）
npm install -g @cloudbase/cli
cloudbase login

# 方式 2: 手动登录（推荐）
# 1. 访问 https://cloud.weixin.qq.com/cloudrun
# 2. 进入「镜像仓库」→「访问凭证」
# 3. 复制用户名和密码
# 4. 执行：
docker login ccr.ccs.tencentyun.com -u <用户名> -p <密码>
```

---

### 步骤 3: 推送镜像

```bash
# 标记镜像
docker tag baby-reward-springboot:latest ccr.ccs.tencentyun.com/wx641c37512334834c/baby-reward-api:latest

# 推送到腾讯云容器仓库
docker push ccr.ccs.tencentyun.com/wx641c37512334834c/baby-reward-api:latest
```

**预计时间**: 2-5 分钟（取决于网络）

**成功标志**:
```
latest: digest: sha256:xxxxx size: xxxx
```

---

### 步骤 4: 在微信云托管控制台部署

#### 4.1 访问云托管控制台

1. 打开 [微信云托管控制台](https://cloud.weixin.qq.com/cloudrun)
2. 选择你的环境（或创建新环境）

#### 4.2 创建服务

1. 点击「新建服务」
2. 填写服务信息：
   - **服务名称**: baby-reward-api
   - **部署方式**: 容器镜像
   - **镜像来源**: 选择「腾讯云容器镜像服务」
   - **镜像**: 选择刚才推送的镜像 `ccr.ccs.tencentyun.com/wx641c37512334834c/baby-reward-api:latest`

#### 4.3 配置服务

1. **端口配置**: 8080
2. **CPU/内存**: 
   - CPU: 0.5 核（起步）
   - 内存: 512MB（起步）
3. **扩缩容配置**:
   - 最小实例数: 0（节省成本）
   - 最大实例数: 5
4. **环境变量**（重要！）:
   ```
   DB_HOST=8.153.98.86
   DB_USER=baby_reward
   DB_PASSWORD=BabyReward2026!
   DB_NAME=baby_reward
   ```

#### 4.4 确认部署

1. 点击「创建」
2. 等待部署完成（约 2-5 分钟）
3. 部署成功后会显示服务地址

---

### 步骤 5: 测试服务

```bash
# 获取服务地址（在云托管控制台查看）
# 格式：https://baby-reward-api-<环境 ID>.wxcloudrun.com

# 测试健康检查
curl https://baby-reward-api-<环境 ID>.wxcloudrun.com/api/health

# 测试登录
curl -X POST https://baby-reward-api-<环境 ID>.wxcloudrun.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 测试微信登录
curl -X POST https://baby-reward-api-<环境 ID>.wxcloudrun.com/api/auth/wechat-login \
  -H "Content-Type: application/json" \
  -d '{"code":"test_123"}'
```

---

### 步骤 6: 配置小程序

修改小程序的 API 地址：

```javascript
// baby-reward-uniapp/src/utils/config.js
export const API_BASE_URL = 'https://baby-reward-api-<环境 ID>.wxcloudrun.com/api';
```

---

## 🔧 常见问题

### Q1: 镜像推送失败

**原因**: 网络问题或凭证错误

**解决**:
```bash
# 重新登录
docker logout ccr.ccs.tencentyun.com
docker login ccr.ccs.tencentyun.com

# 使用 HTTPS 代理
export https_proxy=http://127.0.0.1:7890
```

### Q2: 服务启动失败

**检查日志**:
1. 云托管控制台 → 服务详情
2. 点击「日志查询」
3. 查看错误信息

**常见错误**:
- 数据库连接失败 → 检查环境变量
- 端口配置错误 → 确保设置为 8080

### Q3: 数据库连接超时

**解决**:
1. 在阿里云 RDS 控制台添加白名单
2. 添加云托管的 IP 段（控制台会显示）
3. 或临时设置 0.0.0.0/0 测试

---

## 📊 部署检查清单

- [ ] Docker 镜像构建成功
- [ ] 镜像推送到腾讯云容器仓库
- [ ] 在云托管控制台创建服务
- [ ] 配置端口为 8080
- [ ] 配置环境变量（数据库信息）
- [ ] 服务状态显示「运行中」
- [ ] 健康检查接口返回正常
- [ ] 登录接口测试成功
- [ ] 小程序 API 地址已更新

---

## 💰 费用说明

**微信云托管按量计费**:

| 资源 | 价格 | 估算（月） |
|------|------|-----------|
| CPU | 0.0000925 元/秒核 | 20-50 元 |
| 内存 | 0.00003125 元/秒 GB | 10-30 元 |
| 流量 | 0.08 元/GB | 5-20 元 |
| **总计** | | **约 35-100 元/月** |

---

## 📞 需要帮助？

如果在部署过程中遇到问题：

1. 查看云托管控制台日志
2. 检查环境变量配置
3. 确认数据库连接信息
4. 联系技术支持

---

**祝部署顺利！🎉**
