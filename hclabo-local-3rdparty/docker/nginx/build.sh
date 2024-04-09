set -o errexit
set -o nounset

workdir="/tmp/src"

mkdir -p "$workdir"

echo "Setting up ngx_small_light..."
cd "$workdir"
git clone https://github.com/cubicdaiya/ngx_small_light.git
cd ngx_small_light
./setup
#sed -i s/MagickWandGenesis/IsMagickWandInstantiated/ config
cat config
if [ ! -f config ]
then
  echo "failed setting up ngx_small_light"
  exit 1
fi
ldconfig /usr/local/lib

echo "Building nginx..."
cd "$workdir"
wget "http://nginx.org/download/nginx-${NGINX_VERSION}.tar.gz"
shasum "nginx-${NGINX_VERSION}.tar.gz" | grep "${NGINX_SHA}"
tar -zxf "nginx-${NGINX_VERSION}.tar.gz"
cd "nginx-${NGINX_VERSION}"
./configure \
    --prefix=/opt/nginx \
    --conf-path=/etc/nginx/nginx.conf \
    --sbin-path=/usr/local/sbin/nginx \
    --modules-path=/usr/lib64/nginx/modules \
    --error-log-path=/home/hclabo/log/middleware/nginx/error.log \
    --http-log-path=/home/hclabo/log/middleware/nginx/access.log \
    --user=nginx --group=nginx \
    --with-http_stub_status_module \
    --with-http_perl_module \
    --with-pcre \
    --with-http_ssl_module \
    --with-http_gzip_static_module \
    --with-http_perl_module \
    --add-dynamic-module=$workdir/ngx_small_light

make modules
mkdir -p /usr/lib64/nginx/modules
cp objs/ngx_http_small_light_module.so /usr/lib64/nginx/modules

make install


apk del build-base
rm -rf "$workdir"
rm -rf /var/cache/apk/*
