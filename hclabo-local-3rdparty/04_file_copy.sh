#!/bin/sh

. ./.env

echo "Copy no_images.gif to /public_html/g_images"
cp -r ../frontend/hclabo-web-static/public_html/g_images/*.gif ${PATH_HOST}/public_html/g_images/
cp -r ../frontend/hclabo-web-static/public_html/d_images/* ${PATH_HOST}/public_html/d_images/
rm -f ${PATH_HOST}/public_html/d_images/.gitkeep
