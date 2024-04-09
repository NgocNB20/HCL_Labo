#!/bin/bash

# 起動コマンド
# ./start.sh "envファイル"
# 例： ./start.sh .env

# envファイル読み込み
# shellcheck disable=SC2039
if [ -z $1 ]
then
  # envファイルを設定しない場合はデフォルトファイルを読み込む
  ENV_FILE=".env"
else
  # envファイルを設定する場合はデ設定したファイルを読み込む
  ENV_FILE=$1
fi

source ./$ENV_FILE

# Dockerイメージをビルド
docker build -t ${APP_NAME}:${APP_VERSION} .

# 既存コンテナを削除
docker rm -f ${APP_NAME}

# Dockerコンテナを起動
docker run -it -d \
--name ${APP_NAME} \
--restart always \
-p ${PORT}:8080 \
--add-host=host.docker.internal:host-gateway \
--network hitmall_internal_network \
--memory=512m \
--memory-swap=1024m \
--env-file ./$ENV_FILE \
-e JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=80 -XX:InitialRAMPercentage=80 -Xloggc:${CONTAINER_LOG_DIR}services/order/gc_order.log -XX:+UseGCLogFileRotation -XX:GCLogFileSize=10M -XX:NumberOfGCLogFiles=5 -XX:+PrintGCDetails -XX:+PrintGCDateStamps" \
-v ${HOST_COMMON_DIR}:${CONTAINER_COMMON_DIR} \
-v ${HOST_PUBLIC_DIR}:${CONTAINER_PUBLIC_DIR} \
-v ${HOST_LOG_DIR}:${CONTAINER_LOG_DIR} \
${APP_NAME}:${APP_VERSION}
