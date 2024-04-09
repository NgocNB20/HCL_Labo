Get-NetTCPConnection -LocalPort 80,443,5432,5672,8080,8081,8180,8280,8380,8480,8580,8680,8780,8880,8980,58080,9411,15672,18080,24224,27017,28081,60001,60002,60003 | fl

echo ""

echo "上記コマンド結果が、すべてのポートについて、赤文字で"
Write-Host "Get-NetTCPConnection : プロパティ 'LocalPort' が 'xx' の MSFT_NetTCPConnection オブジェクトが見つかりません。"  -ForegroundColor Red
echo "であることを確認してください。"

echo ""

echo "1件でも白文字で待ち受けポートがあった場合は、該当する OwningProcess のプロセスIDの他アプリと競合しています。"
echo "そのアプリを停止してから、構築手順を進めてください。"
echo "そのまま進めると、必要なアプリのインストールやDockerコンテナの起動に失敗し、手順通りに進みません。"

echo ""

