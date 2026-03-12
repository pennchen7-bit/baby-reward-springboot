#!/bin/bash

# 宝宝奖励计划 - 一键部署脚本

set -e

echo "======================================"
echo "🚀 宝宝奖励计划 Spring Boot - 一键部署"
echo "======================================"
echo ""

APP_ID="wx641c37512334834c"
SERVICE_NAME="baby-reward-api"
REGISTRY="ccr.ccs.tencentyun.com/${APP_ID}"

cd /Users/clovey-macmini/.openclaw/workspace/code/baby-reward-springboot

echo "✅ Docker 镜像已构建完成"
echo ""
echo "📋 接下来请执行以下步骤："
echo ""
echo "1️⃣  登录腾讯云容器仓库"
echo "   访问：https://console.cloud.tencent.com/ccr"
echo "   获取用户名和密码"
echo ""
read -p "   输入用户名：" CCR_USER
read -s -p "   输入密码：" CCR_PASS
echo ""
echo ""

echo "2️⃣  登录并推送镜像..."
docker login ccr.ccs.tencentyun.com -u "${CCR_USER}" -p "${CCR_PASS}"
docker push ${REGISTRY}/${SERVICE_NAME}:latest

echo ""
echo "3️⃣  部署到微信云托管"
echo ""
echo "   请访问：https://cloud.weixin.qq.com/cloudrun"
echo ""
echo "   配置信息："
echo "   ┌─────────────────────────────────────┐"
echo "   │ 服务名称：baby-reward-api           │"
echo "   │ 镜像：${REGISTRY}/${SERVICE_NAME}:latest"
echo "   │ 端口：8080                          │"
echo "   │ CPU: 0.5 核                          │"
echo "   │ 内存：512MB                         │"
echo "   │ 最小实例数：0                       │"
echo "   │ 最大实例数：5                       │"
echo "   └─────────────────────────────────────┘"
echo ""
echo "   环境变量："
echo "   ┌─────────────────────────────────────┐"
echo "   │ SPRING_PROFILES_ACTIVE=prod         │"
echo "   │ SERVER_PORT=8080                    │"
echo "   │ DB_HOST=10.17.103.12                │"
echo "   │ DB_PORT=3306                        │"
echo "   │ DB_NAME=baby_reward                 │"
echo "   │ DB_USER=root                        │"
echo "   │ DB_PASSWORD=P@ssword123             │"
echo "   └─────────────────────────────────────┘"
echo ""
echo "======================================"
echo "🎉 部署完成！"
echo "======================================"
echo ""
echo "服务地址：https://${SERVICE_NAME}-<环境 ID>.wxcloudrun.com"
echo ""
echo "测试命令："
echo "curl https://${SERVICE_NAME}-<环境 ID>.wxcloudrun.com/api/health"
echo ""
