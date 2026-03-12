#!/bin/bash

# 宝宝奖励计划 Spring Boot - 微信云托管自动部署脚本

set -e

echo "======================================"
echo "宝宝奖励计划 - Spring Boot 云托管部署"
echo "======================================"
echo ""

# 配置
APP_NAME="baby-reward-springboot"
APP_ID="wx641c37512334834c"
SERVICE_NAME="baby-reward-api"
REGION="ap-guangzhou"
PORT=8080
REGISTRY="ccr.ccs.tencentyun.com/${APP_ID}"

cd /Users/clovey-macmini/.openclaw/workspace/code/baby-reward-springboot

echo "1️⃣  构建 Docker 镜像..."
docker build -t ${APP_NAME}:latest .

echo ""
echo "2️⃣  标记镜像..."
docker tag ${APP_NAME}:latest ${REGISTRY}/${SERVICE_NAME}:latest

echo ""
echo "3️⃣  推送镜像到腾讯云容器仓库..."
echo "请登录微信云托管控制台获取凭证："
echo "https://cloud.weixin.qq.com/cloudrun"
echo ""
echo "然后执行："
echo "  docker login -u <用户名> -p <密码> ${REGISTRY}"
echo "  docker push ${REGISTRY}/${SERVICE_NAME}:latest"
echo ""
read -p "按回车继续..."

echo ""
echo "4️⃣  部署说明："
echo ""
echo "请在微信云托管控制台操作："
echo "  1. 访问 https://cloud.weixin.qq.com/cloudrun"
echo "  2. 选择环境"
echo "  3. 创建/更新服务"
echo "  4. 服务名称：${SERVICE_NAME}"
echo "  5. 选择镜像：${REGISTRY}/${SERVICE_NAME}:latest"
echo "  6. 设置端口：${PORT}"
echo "  7. 配置环境变量："
echo "     - SPRING_PROFILES_ACTIVE=prod"
echo "     - DB_HOST=10.17.103.12"
echo "     - DB_PORT=3306"
echo "     - DB_NAME=baby_reward"
echo "     - DB_USER=root"
echo "     - DB_PASSWORD=P@ssword123"
echo ""
echo "======================================"
echo "部署指南已完成！"
echo "======================================"
