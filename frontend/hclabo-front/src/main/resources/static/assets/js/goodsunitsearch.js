/*
 * 商品規格値検索クラス
 */
var Goodsunitsearch = function() {
	// 値はgoodsUnitSearchメソッドの中でセット
	this.ggcdObj;
	this.gcdObj;
	this.unitSelect1Obj;
	this.unitSelect2Obj;
}

/*
 * 商品規格値検索クラスのメソッド
 */
Goodsunitsearch.prototype = {

	/*
	 * 規格値2検索処理
	 */
	 goodsUnitSearchUnit2Return : function(ggcdObj, gcdObj, unitSelect1Obj, unitSelect2Obj) {
		goodsUnit2FromUnit1Search(ggcdObj, gcdObj, unitSelect1Obj, unitSelect2Obj);
	}

};

function goodsUnit2FromUnit1Search(ggcdObj, gcdObj, unitSelect1Obj, unitSelect2Obj) {

	// 規格値2が存在しない商品の場合は処理終了
	if (unitSelect2Obj == null || unitSelect2Obj == undefined) {
		return;
	}

	// 商品グループコードを取得
	var unitGgcd = ggcdObj.value;
	// 選択された規格値1の商品コードを取得
	var unit1Gcd = unitSelect1Obj.value;

	// 引数で渡されたobjectをインスタンス変数にセット
	setAddresInfoObjects(ggcdObj, gcdObj, unitSelect1Obj, unitSelect2Obj);

    // プレビュー画面の場合、プレビュー日時を取得
    var preTime = null;
    var target = document.getElementById("preTime");
    if(target){
        preTime = target.value;
    }

	// Ajax通信で、規格値2検索クラスを呼ぶ
	$.ajax({
		 type     : "GET"
		,url      : pkg_common.getAppComplementUrl() + "/searchGoodsUnit2"
		,dataType : "json"
		,data     : {"unitGgcd":unitGgcd, "unit1Gcd":unit1Gcd, "preTime":preTime}
		,timeout  : 30000
	})
		.done(function(response){setValue2(response);})
		.fail(function(response){setValue2(response);})
	;
}

/* Ajax用の規格値2検索クラスの戻り値をhtmlにセットします */
function setValue2(response) {
	if (response == null || response == undefined || response == '') {
		// 戻り値がない場合、セレクトボックスを初期化
		Goodsunitsearch.unitSelect2Obj.length = 0;
		Goodsunitsearch.unitSelect2Obj.options[0] = new Option("選択してください", "");
		Goodsunitsearch.unitSelect2Obj.selectedIndex = 0;
		return;
	}
	// セレクトボックスに値をセット
	var index = 0;
	Goodsunitsearch.unitSelect2Obj.length = 0;
	Goodsunitsearch.unitSelect2Obj.options[0] = new Option("選択してください", "");
	var i = 0;
	var keys =Object.keys(response);
	for(var key in keys){
		Goodsunitsearch.unitSelect2Obj.options[i + 1] = new Option(response[keys[key]], keys[key]);
		if (Goodsunitsearch.gcdObj != null && Goodsunitsearch.gcdObj.value == keys[key]) {
			index = i + 1;
		}
		i++;
	}
	// ページが商品コードを持っている場合、初期選択する
	Goodsunitsearch.unitSelect2Obj.selectedIndex = index;
}

/* 引数のobjectをインスタンス変数セット */
function setAddresInfoObjects(ggcdObj, gcdObj, unitSelect1Obj, unitSelect2Obj) {
	Goodsunitsearch.ggcdObj = ggcdObj;
	Goodsunitsearch.gcdObj = gcdObj;
	Goodsunitsearch.unitSelect1Obj = unitSelect1Obj;
	Goodsunitsearch.unitSelect2Obj = unitSelect2Obj;
}