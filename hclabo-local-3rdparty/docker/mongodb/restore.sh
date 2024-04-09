#!/bin/sh

source ../../.env

docker exec ${PROJECT_NAME}-mongodb mongorestore --username=hclabo_analytics --password=password --db="hclabo_analytics" /tmp/dump_data/accessKeywordsQueryModel.bson
docker exec ${PROJECT_NAME}-mongodb mongorestore --username=hclabo_analytics --password=password --db="hclabo_analytics" /tmp/dump_data/batchManagementQueryModel.bson
docker exec ${PROJECT_NAME}-mongodb mongorestore --username=hclabo_analytics --password=password --db="hclabo_analytics" /tmp/dump_data/orderSearchQueryModel.bson