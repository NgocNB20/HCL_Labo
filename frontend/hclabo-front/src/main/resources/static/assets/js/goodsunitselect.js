/**
 * 規格プルダウン制御
 */
var goodsUnitSelect = {
	/* 規格1選択リストのエレメントID */
	unitSelect1Id : "unitSelect1",
	/* 規格2選択リストのエレメントID */
	unitSelect2Id : "unitSelect2"
}

var goodsUnitSearch = new Goodsunitsearch();

$(function(){
	// 規格1のチェンジイベント登録
	$("#" + goodsUnitSelect.unitSelect1Id).change(function() {
		callGoodsUnit2SearchFunc($(this).get(0), true);
		setWishlistButton();
	});

	// 規格2のチェンジイベント登録
	$("#" + goodsUnitSelect.unitSelect2Id).change(function() {
		$("#gcd").val($(this).val());
		setWishlistButton();
	});

	var unitSelect1 = $("#" + goodsUnitSelect.unitSelect1Id).get(0);
	var unitSelect2 = $("#" + goodsUnitSelect.unitSelect2Id).get(0);
	// 規格１が選択済みかつ規格２が存在する場合、初期設定を行う（マイリスト、ブラウザバックなど）
	if (unitSelect2 != null && unitSelect1 != null && unitSelect1.value != null && unitSelect1.value != '') {
		callGoodsUnit2SearchFunc(unitSelect1, false);
	}
});

function callGoodsUnit2SearchFunc(unitSelect1, isUnit1Select) {
	var gcd = $("#gcd").get(0);
	var unitSelect2 = $("#" + goodsUnitSelect.unitSelect2Id).get(0);

	if (isUnit1Select) {
		// 規格１を再選択した場合、gcdを初期化する
		gcd.value = "";
	}
	if (unitSelect2 == null ) {
		// 規格２が存在しない場合、gcdに規格１の値を設定する
		gcd.value = unitSelect1.value;
		setWishlistButton();
	}

	// 規格２を取得し、洗い替える
	goodsUnitSearch.goodsUnitSearchUnit2Return(
		$("#ggcd").get(0),
		gcd,
		unitSelect1,
		unitSelect2
	);
}

function setWishlistButton() {
	if ($("#wishlistGoodsCodeList").val() != "") {
		var preKey =  document.getElementById("preKey");
		if(!preKey){
			if ($("#gcd").val() != "") {
				if($("#wishlistBtn").hasClass("is-active")){
					$("#wishlistBtn").removeClass("is-active");
				}
				$("#wishlistBtn").attr("style", "display:inline-flex");
				$("#wishlistRegistered").attr("style", "display:none");
				var array = $("#wishlistGoodsCodeList").val().split(",");
				for (var i = 0; i < array.length; i++) {
					if ($("#gcd").val() == array[i]) {
						$("#wishlistBtn").attr("style", "display:none");
						$("#wishlistRegistered").attr("style", "display:inline-flex");
					}
				}
			}
		}
	}
}