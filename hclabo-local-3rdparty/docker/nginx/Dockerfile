FROM alpine:3.13
ARG nginx_version
ENV NGINX_VERSION=${nginx_version}
ENV NGINX_SHA="6b4ab4eff3c617e133819f43fdfc14708e593a79"

RUN addgroup --system --gid 1001 nginx \
    && adduser --system --uid 1001 --ingroup nginx nginx

RUN apk --update add \
    git \
    openssl-dev \
    pcre-dev \
    zlib-dev \
    wget \
    perl \
    perl-dev \
    build-base \
    # alphine3.14だとImageMagick7が入ってしまうため6を明示的に指定する
    imagemagick6 \
    imagemagick6-dev \
    imagemagick6-c++ \
    imagemagick6-libs \
    tzdata && \
    cp /usr/share/zoneinfo/Asia/Tokyo /etc/localtime && \
    apk del tzdata

ADD build.sh /tmp/build.sh
RUN sh /tmp/build.sh \
    rm /tmp/build.sh

RUN mkdir -p /var/cache/nginx
ADD nginx.conf /etc/nginx/nginx.conf
WORKDIR /etc/nginx

EXPOSE 80 443

ENV OMP_NUM_THREADS=1

CMD ["nginx", "-g", "daemon off;"]
