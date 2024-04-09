# hclabo-web-static

Webサーバ（NGINX）管理下の静的コンテンツ用リポジトリ

## サーバ環境ディレクトリイメージ
``` bash
/home/{TENANT_ID}/
|--internal_html
|  |--error.html    # NGINXが表示するエラーページ
|  |--mainte.html   # ショップメンテナンス中に表示するエラーページ
|--g_images
|  |--noimage.gif
|  |--noimage_pdm.gif
|  |--noimage_pds.gif
|  |--{商品管理番号}
|  |  |--xxxx_01.jpg
|--d_images
|  |--{管理画面からZIPアップロードされた構造で配置}
```

## internal_html配下のHTMLファイル作成ルール
- Thymeleaf（Javaアプリ）は動作しないため、純粋なHTML記法に則って記述すること
- 画像などfront-app管理下の外部コンテンツを参照する場合は`/sttc/assets/images/***`のパスで参照すること
  - `sttc`を付与することでNGINX単体で静的コンテンツを返却させている
  - CSSの場合はHTML内に直接記載するの推奨
