#!/bin/bash

echo "开始构建所有服务..."

mvn clean package -DskipTests

echo "构建完成！"
