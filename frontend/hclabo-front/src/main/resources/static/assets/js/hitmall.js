/*-----------------------------------------------------------
 *
 * hitmall.js
 *
 * @author k.harada
 * @date 2012.08.20
 *----------------------------------------------------------*/

/* 画面共通クラス */
pkg_common = new function() {

	/* private */

	var _APP_COMPLEMENT_URL = "#appComplementUrl";
	var _STATIC_COMPLEMENT_URL = "#staticComplementUrl";
	var _GOODS_IAMGE_PATH = "#goodsImagePath";
	
	var _appComplementUrl = undefined;
	var _staticComplementUrl = undefined;
	var _goodsImagePath = undefined;

	/* public */

	this.getAppComplementUrl = function() {
		if (!_appComplementUrl) {
			_appComplementUrl = $(_APP_COMPLEMENT_URL).val();
			if (!_appComplementUrl)
				_appComplementUrl = "";
		}
		return _appComplementUrl;
	};

	this.getStaticComplementUrl = function() {
		if (!_staticComplementUrl) {
			_staticComplementUrl = $(_STATIC_COMPLEMENT_URL).val();
			if (!_staticComplementUrl)
				_staticComplementUrl = "";
		}
		return _staticComplementUrl;
	};
	
	this.getGoodsImagePath = function() {
		if (!_goodsImagePath) {
			_goodsImagePath = $(_GOODS_IAMGE_PATH).val();
			if (!_goodsImagePath)
				_goodsImagePath = "";
		}
		return _goodsImagePath;
	};
	
	this.getComplementedAppPath = function (url) {
		return this.getAppComplementUrl() + url;
	};
	
	this.getComplementedStaticPath = function (url) {
		return this.getStaticComplementUrl() + url;
	};
	this.getComplementedGoodsImagePath = function (url) {
		return this.getGoodsImagePath() + url;
	};

	/* エラースタイル設定 */	
	this.errorStyleRadioOrCheckbox = function (componentName, errorStyleClass) {

		// 親タグを取得
		var parent = $("input[name='" + componentName + "']").parent();
		
		// label タグなら、もう１階層上を取得
		if (parent[0].nodeName == "LABEL") {
			parent = parent.parent();
		}
		
		// スタイルの設定
		parent.addClass(errorStyleClass);
	};
}

/**
 * 別ウインドウを立ち上げる
 * 開いた窓は、スクロール可能、リサイズ可能
 *
 * @param url 表示したい画面のURL　※?newwindow=true　は呼び元でつけてください。
 * @param winId 表示するウィンドウの名前　未指定の場合はnullをセットする（複数画面が立ち上がる）
 * @param width 表示窓の横幅　未指定の場合は600
 * @param height 表示窓の高さ　未指定の場合は600
 */
function openWindow1(url, winId , width, height) {

	// ウィンドウの名前が指定されていなければ、nullをセット
	if ( !windowName ){
		windowName = null;
	}
	if ( !width || typeof width != "number" ){
		width = 600;
	}
	if ( !height || typeof height != "number" ){
		height = 600;
	}

	var option = "width=" + width + "," + "height=" + height + "," + "menubar=no,status=no,scrollbars=yes,resizable=yes";
	return openWindow2(url, winId, option);
}

/**
 * 別ウインドウを立ち上げる
 *
 * @param url 表示したい画面のURL　※?newwindow=true　は呼び元でつけてください。
 * @param winId 表示するウィンドウの名前　未指定の場合はnullをセットする（複数画面が立ち上がる）
 * @param option 付加情報
 */
function openWindow2(url, winId, option) {
	return window.open(url, winId, option);
}

/**
 * 別ウインドウを立ち上げる
 * <pre>
 * 親ウインドウの真ん中に、指定したサイズの別ウインドウを開きます。
 * サブアプリ・ページスコープが消えてしまうのを防止するため、newwindow=trueを付与します。
 * スクロール有無・リサイズ可否指定可能。
 * </pre>
 *
 * @param url 表示したい画面のURL　※?newwindow=true　は自動で付与するので不要
 * @param winId 表示するウィンドウの名前　未指定の場合はnullをセットする（複数画面が立ち上がる）
 * @param width 表示窓の横幅　未指定の場合は810
 * @param height 表示窓の高さ　未指定の場合は850
 * @param scroll スクロールバー有無　yes or no　未指定の場合はyes
 * @param resize リサイズ可否　yes or no　未指定の場合はyes
 */
function openWindow3(url, winId, width, height, scroll, resize) {

	// ウィンドウの名前が指定されていなければ、nullをセット
	if ( !winId ){
		winId = null;
	}

	if ( !width || typeof width != "number" ){
		width = 810;
	}
	if ( !height || typeof height != "number" ){
		height = 850;
	}
	if ( !scroll ){
		scroll = "yes";
	}
	if ( !resize ){
		resize = "yes";
	}

	// 表示位置算出
	var x = (screen.width - width) / 2;
	var y = (screen.height - height) / 2;

	url = url + "?newwindow=true";
	var option = "screenX=" + x + ",screenY=" + y + ",left=" + x + ",top=" + y + ",width=" + width + ",height=" + height + ",menubar=no,status=no,scrollbars=" + scroll + ",resizable=" + resize;

	return openWindow2(url, winId, option);
}

/**
 * 画面を閉じる
 */
function closeWindow () {
	window.close();
}

/**
 * 印刷する
 */
function printWindow () {
	window.print();
}