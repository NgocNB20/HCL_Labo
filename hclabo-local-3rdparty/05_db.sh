#!/bin/sh

. ./.env

## copy init_data to mongodb
echo "Copy mongodb data from hclabo-analytics-service"
cp -r ../backend/hclabo-analytics-service/common/30_DB/20_DML/*.json ./docker/mongodb/dump_data/

## copy init_data to postgresql
for dir in ../backend/*; do

    service=$(basename $dir)
    if [[ "$service" == *"hclabo-analytics-service"* ]]; then
        continue
    fi

    echo "Copy postgres data from $service"
    dbname=$(echo "$service" | sed -e 's/-/_/g')
    dbname=$(echo "$dbname" | sed 's/_service//')
    dump_dir="./docker/postgres/dump_data/$dbname"
    cp -r $dir/common/* ${dump_dir}/common/

    for sql in ${dump_dir}/common/30_DB/20_DML/*; do
        echo "Replace ${sql}"
        sed -i "s/<<MAIL_ADDR>>/${MAIL_ADDR}/g" ${sql}
        sed -i "s/<<MAIL_ADDR_BCC>>/${MAIL_ADDR}/g" ${sql}
    done

done

## 初期データの投入は、Dockerが initdb.d フォルダの中のシェルを自動で叩いて実行します

