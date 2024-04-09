ARG fluent_version
FROM fluent/fluentd:$fluent_version

USER root

RUN apk add --no-cache --update --virtual .build-deps \
    sudo build-base ruby-dev \
    && sudo gem sources --clear-all \
	&& sudo gem install fluent-plugin-mongo  \
    && apk del .build-deps \
    && rm -rf /tmp/* /var/tmp/* /usr/lib/ruby/gems/*/cache/*.gem

RUN fluent-gem install fluent-plugin-mongo

RUN ["gem", "install", "fluent-plugin-mongo"]

USER fluent

EXPOSE 24284