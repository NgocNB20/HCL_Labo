# はじめに
基本的には、hclabo-starter-projectリポジトリの「HIT-MALL Ver4初期設定」経由で実行されることを想定しています

3rd-Partyのみ起動する場合は、このリポジトリをクローンした後、docker-composeで起動してください

``` bash
# 全3rd-Partyアプリ起動
docker compose up -d

# 特定の3rd-Partyアプリのみ起動
docker compose up -d haproxy
``` 
※`docker compose`か`docker-compose`かは、各自の環境に応じて切り替えてください(ハイフンありなし)



## 補足
Appleシリコン搭載Mac（Arm64）で実行する場合は、fluentdイメージをArm64版に変更した上で実行してください

### 変更箇所
.env
```
FLUENTD_VERSION=v1.14-debian-arm64-1
```

docker/fluentd/Dockerfile
```
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        sudo build-essential ruby-dev \
    && gem sources --clear-all \
    && gem install fluent-plugin-mongo \
    && apt-get remove -y build-essential ruby-dev \
    && apt-get autoremove -y \
    && rm -rf /var/lib/apt/lists/*
```