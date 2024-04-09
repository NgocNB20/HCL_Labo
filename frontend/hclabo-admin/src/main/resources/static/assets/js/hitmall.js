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
}
