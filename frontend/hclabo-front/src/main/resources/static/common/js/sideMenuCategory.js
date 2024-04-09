
$(function() {
	// Ajaxを実行
	execAjaxSideMenu('#sidemenuPageItems-1', '#viewLevel');
});

function execAjaxSideMenu(appendId, viewLevelId){

	var viewLevel = $(viewLevelId).val();
	var cid = (new URL(document.location)).searchParams.get('cid');
	if(!cid){
		cid = "";
	}
    // プレビュー画面の場合、プレビュー日時を取得
    var preTime = null;
    var target = document.getElementById("preTime");
    if(target){
        preTime = target.value;
    }

	var hsn = (new URL(document.location)).searchParams.get('hsn');
	if(!hsn){
		hsn = "";
	}

	// Ajax通信を行う
	$.ajax({
		 type			:	"GET"
		,url			:	pkg_common.getAppComplementUrl() + "/getCategoryJsonData"
		,data		 	:	{"viewLevel":viewLevel, "cid":cid, "preTime":preTime}
		,dataType 	:	"json"
		,timeout		:	30000
	})
	.done(function(data){ setSideMenuHtml(data, appendId, cid, hsn); })
	.fail(function(data){ setSideMenuHtml(data, appendId, cid, hsn); })
	;
}

function setSideMenuHtml(resData, appendId, curCid, curHsn) {

	if (!resData) {
		return;
	}

	var list = resData;
	if(list == null || list.length <= 0){
		$(appendId).css({display: 'none'});
		$(appendId+'_hideTarget').css({display: 'none'});
		return;
	}

	//カテゴリーツリー生成
	for(var i = 0, size = list.length; i < size ; i++){

		var appendHtml = '';
		var selectedHtml = '';
		var selectedClassName = '';

		if(list[i].cid == curCid && list[i].hsn == curHsn){
			selectedClassName = 'is-current';
			selectedHtml = ' class="' + selectedClassName + '" ';
		}

        // TODO: 未確定部分あり #36
		//　第一階層
		if(list[i].categoryLevel == "1"){

			if (list[i].categoryFileName != null) {
				appendHtml =		'<li class="l-category__level1" data-hierarchy="1">'
				// + '<a href="' + pkg_common.getAppComplementUrl() + '/goods/list?cid=' + list[i].cid + '&hsn=' + list[i].hierarchicalSerialNumber + '&isSide=true' + '"' + selectedHtml + '>'
					+		'<span><svg viewBox="0 0 19.2 17.5" style="fill:#a0a0a0;" class="categoryIcon">'
					+			'<use xlink:href="' + pkg_common.getAppComplementUrl() + list[i].categoryFileName + '"></use>'
					+		'</svg></span>'
					+		'<div class="l-category__button"><p>' + list[i].displayName + '</p></div>'
				//	+	'</a>'
					+   '<div id="' + list[i].cid + list[i].hierarchicalSerialNumber + '" class="l-category__block js-accordion-block"></div></li>';
			} else {
				appendHtml = '<li class="l-category__level1" data-hierarchy="1">'
					+ '<div class="l-category__button">'
					// + '<a href="' + pkg_common.getAppComplementUrl() + '/goods/list?cid=' + list[i].cid + '&hsn=' + list[i].hierarchicalSerialNumber + '&isSide=true' + '"' + selectedHtml + '>'
					+ '<p>' + list[i].displayName + '</p></div>'
					+ '<div id="' + list[i].cid + list[i].hierarchicalSerialNumber + '" class="l-category__block js-accordion-block"></div></li>';
			}

			$(appendId).append(appendHtml);
		}

		//　第二階層
		if( list[i].categoryLevel == "2" ){

			// ひとつ前のループが第一階層の場合（受領データはソート済みのため、以下の条件で、ループのひとつ前は必ず親になる）
			if( list[i - 1].categoryLevel == "1" ){
			    appendHtml =	'<ul class="l-category__level2wrap" id="' + list[i].cidParent + list[i].hierarchicalSerialNumber.slice(0, 1) + '_parent"></ul>';
			    $('#' + list[i].cidParent + list[i].hierarchicalSerialNumber.slice(0, 1)).append(appendHtml);
			}

			appendHtml =		'<li class="l-category__level2" data-hierarchy="2" id="' + list[i].cidParent + list[i].hierarchicalSerialNumber + '_parent"><div class="l-category__button">';
			// 次のループが第三階層の場合、自身が親になる
			if (list[i + 1] && list[i + 1].categoryLevel == "3") {
			    appendHtml += '<span class="l-category__button-accordion js-accordion-button">'
                        +		'<p class="c-textlink ' + selectedClassName + '">' + list[i].displayName + '</p>'
                        +	'</div>';
			} else {
			    appendHtml += '<div class="l-category__button-anchor">'
                        +       '<a href="' + pkg_common.getAppComplementUrl() + '/goods/list?cid=' + list[i].cid + '&hsn=' + list[i].hierarchicalSerialNumber + '&isSide=true' + '">'
                        +		'<p class="c-textlink ' + selectedClassName + '">' + list[i].displayName + '</p>'
                        +	'</a></div>';
			}
			appendHtml += '</div></li>';

			$('#' + list[i].cidParent + list[i].hierarchicalSerialNumber.slice(0, 1) + '_parent').append(appendHtml);
		}

		//　第三階層
		if( list[i].categoryLevel == "3" ){

			// ひとつ前のループが第二階層の場合（受領データはソート済みのため、以下の条件で必ず親になる）
			if( list[i - 1].categoryLevel == "2" ){
			    appendHtml =	'<div class="l-category__block js-accordion-block" style="height: 0px;"><ul class="l-category__level3wrap" id="' + list[i].cidParent + list[i].hierarchicalSerialNumber.slice(0, 3) + '_parent' + '"></ul></div>';
			    $('#' + list[i-1].cidParent + list[i].hierarchicalSerialNumber.slice(0, 3) + '_parent').append(appendHtml);
			}

			appendHtml =		'<li class="l-category__level3" data-hierarchy="3"><a class="l-category__button-anchor" href="' + pkg_common.getAppComplementUrl() + '/goods/list?cid=' + list[i].cid + '&hsn=' + list[i].hierarchicalSerialNumber + '&isSide=true' + '">'
									+		'<p class="c-textlink ' + selectedClassName + '">' + list[i].displayName + '</p>'
									+	'</a></li>';
			$('#' + list[i].cidParent + list[i].hierarchicalSerialNumber.slice(0, 3) + '_parent').append(appendHtml);
		}
	}

    // 第三階層のアコーディオン登録
	accordionCategory('.l-category .js-accordion-button');
	// 第二階層のボタンのうち、自身の配下の第三階層に選択中のカテゴリがあればアコーディオンを開く
    document.querySelectorAll('.l-category .js-accordion-button').forEach(btn => {
        var parentCategory = btn.closest('.l-category__level2');
        var currentText = parentCategory.getElementsByClassName('c-textlink is-current');
        if (currentText.length) {
            btn.dispatchEvent(new PointerEvent('click'));
        }
	});

	// プレビュー画面の場合はリクエストパラメータにプレビューキーとプレビュー日時を付与
    var preKey =  document.getElementById("preKey");
    var preTime = document.getElementById("preTime");
    if( preKey && preTime ){
    	$('#sidemenuPageItems-1 li a').each(function() {
    		this.href += "&prekey=" + preKey.value + "&pretime=" + preTime.value;
    	});
    }
}
