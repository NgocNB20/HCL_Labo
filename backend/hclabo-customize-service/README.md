# hclabo-customize-service

兵庫臨床カスタマイズサービス

## 各ライブラリのバージョン

こちらの記事に沿って定義すること  
http://e4-lhasa.itechh.ne.jp/654380066b1b5f001569a1c6

## 注意点

* 各マイクロサービス側では、OpenAPI定義による自動生成ソース（サーバー側のAPIインターフェース）を使ってAPIコントローラを実装するため、pom.xmlに定義された該当jarファイルをインポートする必要がある。
* 本来、これらのjarファイルは、Nexusに公開され、mavenビルド時に直接ダウンロードしてくるが、現時点（2022年2月）では、OpenAPI定義がまだ固まっておらず頻繁に更新したりするので、Nexusへの公開は未実施の状態である。
* 従って、マイクロサービスの実装者が各自OpenAPI定義リポジトリをクローンし、ローカル環境でソースの自動生成＆mavenビルド（jarファイル化）を行う必要がある。
* ビルド手順は、各OpenAPI定義リポジトリのREADMEに記載しているので、それに沿って実行してください。  
  https://e4-vcs.itechh.ne.jp/gitlab/hitmall/hclabo/program/openapi-specs