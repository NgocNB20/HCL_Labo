#!/bin/sh

. ./.env

echo '-----------------------------------------------'
echo '● 必要なディレクトリの作成：開始'
echo '-----------------------------------------------'

# コンテナにmountされるディレクトリを作成
mkdir -p ${PATH_HOST}/common/download/order
mkdir -p ${PATH_HOST}/common/download/shipment
mkdir -p ${PATH_HOST}/common/download/soldgoods
mkdir -p ${PATH_HOST}/common/download/ordersales
mkdir -p ${PATH_HOST}/common/tmp
mkdir -p ${PATH_HOST}/common/g_images_input/error
mkdir -p ${PATH_HOST}/common/work/g_imagesUpdate
mkdir -p ${PATH_HOST}/common/backup/g_imagesUpdate
mkdir -p ${PATH_HOST}/contents/services/customize-service/examresults
mkdir -p ${PATH_HOST}/public_html/g_images
mkdir -p ${PATH_HOST}/public_html/d_images/category
mkdir -p ${PATH_LOG_SERVICE}/analytics
mkdir -p ${PATH_LOG_SERVICE}/logistic
mkdir -p ${PATH_LOG_SERVICE}/order
mkdir -p ${PATH_LOG_SERVICE}/payment
mkdir -p ${PATH_LOG_SERVICE}/price-planning
mkdir -p ${PATH_LOG_SERVICE}/product
mkdir -p ${PATH_LOG_SERVICE}/promotion
mkdir -p ${PATH_LOG_SERVICE}/shop-management
mkdir -p ${PATH_LOG_SERVICE}/user
mkdir -p ${PATH_LOG_SERVICE}/customize
mkdir -p ${PATH_LOG_APP}/admin
mkdir -p ${PATH_LOG_APP}/front
mkdir -p ${PATH_HOST}/log/datastore/mongodb
mkdir -p ${PATH_HOST}/log/datastore/postgresql
mkdir -p ${PATH_HOST}/log/datastore/rabbitmq
mkdir -p ${PATH_HOST}/log/middleware/cron
mkdir -p ${PATH_HOST}/log/middleware/haproxy
mkdir -p ${PATH_HOST}/log/middleware/nginx

echo '-----------------------------------------------'
echo '● 必要なディレクトリの作成：完了'
echo '-----------------------------------------------'
