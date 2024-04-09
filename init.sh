#!/bin/sh

rm -rf .git

## determine working directory name
echo "Please enter this project name. Will be working directory name. ex) hclabo"
echo -n "project name:"
read input
if [ -z $input ]; then
    echo "keep hclabo-starter-project."
    input="hclabo-starter-project"
else
    cd ..
    cp -r hclabo-starter-project $input
    cd $input
    rm -rf ../hclabo-starter-project
fi

## create frontend project
cd frontend
git clone https://e4-vcs.itechh.ne.jp/gitlab/hitmall/hclabo/program/apps/hclabo-admin.git
git clone https://e4-vcs.itechh.ne.jp/gitlab/hitmall/hclabo/program/apps/hclabo-front.git
git clone https://e4-vcs.itechh.ne.jp/gitlab/hitmall/hclabo/program/apps/hclabo-web-static.git

## create backend project
cd ../backend
git clone https://e4-vcs.itechh.ne.jp/gitlab/hitmall/hclabo/program/services/hclabo-user-service.git
git clone https://e4-vcs.itechh.ne.jp/gitlab/hitmall/hclabo/program/services/hclabo-product-service.git
git clone https://e4-vcs.itechh.ne.jp/gitlab/hitmall/hclabo/program/services/hclabo-order-service.git
git clone https://e4-vcs.itechh.ne.jp/gitlab/hitmall/hclabo/program/services/hclabo-price-planning-service.git
git clone https://e4-vcs.itechh.ne.jp/gitlab/hitmall/hclabo/program/services/hclabo-payment-service.git
git clone https://e4-vcs.itechh.ne.jp/gitlab/hitmall/hclabo/program/services/hclabo-logistic-service.git
git clone https://e4-vcs.itechh.ne.jp/gitlab/hitmall/hclabo/program/services/hclabo-promotion-service.git
git clone https://e4-vcs.itechh.ne.jp/gitlab/hitmall/hclabo/program/services/hclabo-analytics-service.git
git clone https://e4-vcs.itechh.ne.jp/gitlab/hitmall/hclabo/program/services/hclabo-shop-management-service.git
git clone https://e4-vcs.itechh.ne.jp/gitlab/hitmall/hclabo/program/services/hclabo-customize-service.git

# create 3rd party project
cd ..
git clone https://e4-vcs.itechh.ne.jp/gitlab/hitmall/hclabo/tools/hclabo-local-3rdparty.git
