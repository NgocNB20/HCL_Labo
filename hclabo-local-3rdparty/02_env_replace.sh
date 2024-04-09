#!/bin/sh

. ./.env

cd ..

# frontend
for dir in ./frontend/*; do

    yaml_app=${dir}/src/main/resources/application-lo.yml
    echo "Replace ${yaml_app}"
    sed -i "s#<<PATH_HOST>>#${PATH_HOST}#g" ${yaml_app}
    sed -i "s#<<PATH_LOG_APP>>#${PATH_LOG_APP}#g" ${yaml_app}
    sed -i "s#<<PATH_LOG_SERVICE>>#${PATH_LOG_SERVICE}#g" ${yaml_app}
    sed -i "s/<<ENABLE_ZIPKIN>>/${ENABLE_ZIPKIN}/g" ${yaml_app}
    sed -i "s/<<MAIL_ADDR>>/${MAIL_ADDR}/g" ${yaml_app}
    sed -i "s/<<PREFIX_GMO>>/${PREFIX_GMO}/g" ${yaml_app}

    xml_log=${dir}/src/main/resources/config/hitmall/log/logback-lo.xml
    echo "Replace ${xml_log}"
    sed -i "s#<<PATH_HOST>>#${PATH_HOST}#g" ${xml_log}
    sed -i "s#<<PATH_LOG_APP>>#${PATH_LOG_APP}#g" ${xml_log}
    sed -i "s#<<PATH_LOG_SERVICE>>#${PATH_LOG_SERVICE}#g" ${xml_log}

done

# backend
for dir in ./backend/*; do

    yaml_app=${dir}/src/main/resources/application-lo.yml
    echo "Replace ${yaml_app}"
    sed -i "s#<<PATH_HOST>>#${PATH_HOST}#g" ${yaml_app}
    sed -i "s#<<PATH_LOG_APP>>#${PATH_LOG_APP}#g" ${yaml_app}
    sed -i "s#<<PATH_LOG_SERVICE>>#${PATH_LOG_SERVICE}#g" ${yaml_app}
    sed -i "s/<<ENABLE_ZIPKIN>>/${ENABLE_ZIPKIN}/g" ${yaml_app}
    sed -i "s/<<MAIL_ADDR>>/${MAIL_ADDR}/g" ${yaml_app}
    sed -i "s/<<PREFIX_GMO>>/${PREFIX_GMO}/g" ${yaml_app}

    xml_log=${dir}/src/main/resources/config/hitmall/log/logback-lo.xml
    echo "Replace ${xml_log}"
    sed -i "s#<<PATH_HOST>>#${PATH_HOST}#g" ${xml_log}
    sed -i "s#<<PATH_LOG_APP>>#${PATH_LOG_APP}#g" ${xml_log}
    sed -i "s#<<PATH_LOG_SERVICE>>#${PATH_LOG_SERVICE}#g" ${xml_log}

done
