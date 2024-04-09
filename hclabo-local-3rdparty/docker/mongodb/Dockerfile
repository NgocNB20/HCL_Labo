ARG mongodb_version
FROM mongo:$mongodb_version
ENV TZ Asia/Tokyo

COPY ./conf.d/mongod.conf /etc/mongod.conf
