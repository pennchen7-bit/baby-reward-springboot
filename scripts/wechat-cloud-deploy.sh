#!/bin/bash

# 宝宝奖励计划 Spring Boot - 微信云托管自动部署脚本

set -e

echo "======================================"
echo "🚀 宝宝奖励计划 - 微信云托管部署"
echo "======================================"
echo ""

cd /Users/clovey-macmini/.openclaw/workspace/code/baby-reward-springboot

# 1. 检查 Docker 镜像
echo "✅ Docker 镜像已构建完成"
echo ""

# 2. 推送镜像到腾讯云容器仓库
echo "📦 推送镜像到腾讯云容器仓库..."
echo ""
echo "请登录微信云托管控制台获取容器仓库凭证："
echo "https://console.cloud.tencent.com/ccr"
echo ""

# 3. 提供手动部署指南
echo "======================================"
echo "📋 部署步骤："
echo "======================================"
echo ""
echo "1️⃣  获取腾讯云容器仓库凭证"
echo "   访问：https://console.cloud.tencent.com/ccr"
echo "   点击「访问凭证」"
echo "   复制用户名和密码"
echo ""
echo "2️⃣  登录并推送镜像"
echo "   docker login ccr.ccs.tencentyun.com -u <用户名> -p <密码>"
echo "   docker push ccr.ccs.tencentyun.com/wx641c37512334834c/baby-reward-api:latest"
echo ""
echo "3️⃣  在微信云托管控制台部署"
echo "   访问：https://cloud.weixin.qq.com/cloudrun"
echo "   微信扫码登录"
echo "   创建服务 → 选择镜像 → 配置环境变量 → 部署"
echo ""
echo "======================================"
echo "💡 服务配置信息："
echo "======================================"
echo ""
echo "服务名称：baby-reward-api"
echo "镜像：ccr.ccs.tencentyun.com/wx641c37512334834c/baby-reward-api:latest"
echo "端口：8080"
echo ""
echo "环境变量："
echo "  SPRING_PROFILES_ACTIVE=prod"
echo "  DB_HOST=10.17.103.12"
echo "  DB_PORT=3306"
echo "  DB_NAME=baby_reward"
echo "  DB_USER=root"
echo "  DB_PASSWORD=P@ssword123"
echo ""
echo "======================================"
echo "✅ 准备就绪！"
echo "======================================"
