

jQuery(function($){
	// ルートコンテキストに /demo が含まれていた場合、デモ環境とみなす。
	var DEMO = "demo";
	if (location.pathname.substr(1,4) == DEMO) {

		// グローバルメニューのマニュアルのリンクを削除
		$("#g_manual a").each(function(){
			$(this).attr('onclick', '');
			$(this).attr('href', 'javascript:void(0);');
		});

		// URL取得
		var URL_REPLACE = new RegExp("/" + DEMO + "[^/]*/admin(?=/)");
		var applicationPath = location.pathname.replace(URL_REPLACE, '');


		// TOP
		var demoUrl;
		if (applicationPath == "/" || applicationPath == "/top.html") {
			demoUrl = location.pathname;
			if (applicationPath == "/") {
				demoUrl += "top.html";
			}
			demoUrl = demoUrl.replace(/[.]html$/, 'demo.html')
			$.ajaxSetup({ cache: false });
			$("h2:first").before('<div align="right" style="margin-bottom:-12px;"><a id="goDemoData" href="javascript:void(0)">→デモデータ</a></div>');

			$("#goDemoData").bind("click", function() {
				$("#mainContents").load(demoUrl, function(){
					initializeTopTotalingGraph();
				});
			})
		}
	}
});

