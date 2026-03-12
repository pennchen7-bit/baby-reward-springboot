#!/bin/bash

# 宝宝奖励计划 Spring Boot 云托管部署脚本

set -e

# 配置
APP_NAME="baby-reward-springboot"
APP_ID="wx641c37512334834c"
SERVICE_NAME="baby-reward-api"
REGION="ap-guangzhou"
PORT=8080

echo "======================================"
echo "宝宝奖励计划 - Spring Boot 云托管部署"
echo "======================================"
echo ""

# 1. 构建 Docker 镜像
echo "1️⃣  构建 Docker 镜像..."
cd /Users/clovey-macmini/.openclaw/workspace/code/baby-reward-springboot
docker build -t ${APP_NAME}:latest .

echo ""
echo "2️⃣  标记镜像..."
# 微信云托管镜像仓库地址（需要替换为实际的）
REGISTRY="ccr.ccs.tencentyun.com/wx641c37512334834c"
docker tag ${APP_NAME}:latest ${REGISTRY}/${SERVICE_NAME}:latest

echo ""
echo "3️⃣  推送镜像到腾讯云容器仓库..."
echo "请登录微信云托管控制台获取镜像仓库凭证"
echo "然后执行："
echo "  docker login -u <用户名> -p <密码> ${REGISTRY}"
echo "  docker push ${REGISTRY}/${SERVICE_NAME}:latest"

echo ""
echo "4️⃣  部署到云托管..."
echo "请在微信云托管控制台操作："
echo "  1. 访问 https://cloud.weixin.qq.com/cloudrun"
echo "  2. 选择环境"
echo "  3. 创建/更新服务"
echo "  4. 选择镜像：${REGISTRY}/${SERVICE_NAME}:latest"
echo "  5. 设置端口：${PORT}"
echo "  6. 配置环境变量："
echo "     - DB_HOST=8.153.98.86"
echo "     - DB_USER=baby_reward"
echo "     - DB_PASSWORD=BabyReward2026!"
echo "     - DB_NAME=baby_reward"

echo ""
echo "======================================"
echo "部署完成！"
echo "======================================"
echo ""
echo "服务地址：https://${SERVICE_NAME}-<环境 ID>.wxcloudrun.com"
echo "健康检查：https://${SERVICE_NAME}-<环境 ID>.wxcloudrun.com/api/health"
