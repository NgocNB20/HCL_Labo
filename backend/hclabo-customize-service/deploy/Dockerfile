############################################################################
## 新HMの「兵庫臨床カスタマイズサービス」の Docker Image 定義ファイル
############################################################################

FROM adoptopenjdk/openjdk11-openj9:alpine-slim

# Timezoneライブラリのインストール
RUN apk update && apk add tzdata && rm -rf /var/cache/apk/*

# ロケールライブラリのインストール
RUN wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub; \
    wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.31-r0/glibc-2.31-r0.apk; \
    wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.31-r0/glibc-bin-2.31-r0.apk; \
    wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.31-r0/glibc-i18n-2.31-r0.apk; \
    apk add --no-cache --update \
    bash \
    glibc-2.31-r0.apk \
    glibc-bin-2.31-r0.apk \
    glibc-i18n-2.31-r0.apk; \
    rm glibc-2.31-r0.apk glibc-bin-2.31-r0.apk glibc-i18n-2.31-r0.apk
RUN /usr/glibc-compat/bin/localedef -i ja_JP -f UTF-8 ja_JP.UTF-8

# Timezoneの設定
ENV TZ='Asia/Tokyo'

# ロケールの設定
ENV LANG ja_JP.utf8
ENV LC_ALL ja_JP.utf8

# ★ Javaアプリケーション | START ★
# hm4で使用するディレクトリ作成
WORKDIR /app
COPY ./hclabo-customize-service-4.0.0-SNAPSHOT.jar /app/hclabo-customize-service-4.0.0-SNAPSHOT.jar
COPY ./newrelic.jar /app/newrelic.jar
COPY ./newrelic.yml /app/newrelic.yml
# add 2023/08/09 from ando-ss　STERT
RUN mkdir extensions
COPY ./hikaricp-2.4.0.jar /app/extensions/hikaricp-2.4.0.jar
# add 2023/08/09 from ando-ss END
ENV NEW_RELIC_APP_NAME="hclabo_customize-service"
ENV NEW_RELIC_LICENSE_KEY="04840c3a0051c47aa9f1f47362a55e228c2fNRAL"
# ENV NEW_RELIC_LOG_FILE_NAME="STDOUT"
ENTRYPOINT ["java","-javaagent:/app/newrelic.jar","-jar","/app/hclabo-customize-service-4.0.0-SNAPSHOT.jar"]
# ★ Javaアプリケーション | END ★

CMD ["/bin/sh"]
