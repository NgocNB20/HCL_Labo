var spSuffix = "_sp" //idで使用

$(function() {
	// Ajaxを実行
	execAjaxSideMenuSp('#sidemenuPageItemsSp-1', '#viewLevelSp');
});

function execAjaxSideMenuSp(appendId, viewLevelId){

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
	.done(function(data){ setSideMenuHtmlSp(data, appendId, cid, hsn); })
	.fail(function(data){ setSideMenuHtmlSp(data, appendId, cid, hsn); })
	;
}

function setSideMenuHtmlSp(resData, appendId, curCid, curHsn) {

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

            appendHtml = '<li class="l-category-sm__level1" data-hierarchy="1"><div class="l-category-sm__button">'
                    +   '<span class="l-category-sm__button-accordion js-accordion-button"><p class="c-textlink">'
                    +   '<svg viewBox="0 0 23 23" width="19"><use xlink:href="/assets/images/icon/search.svg#search"></use></svg>'
                    +   '<span>' + list[i].displayName + '</span>'
                    +   '</p></span></div><div id="' + list[i].cid + spSuffix + list[i].hierarchicalSerialNumber + '" class="l-category-sm__block js-accordion-block"></div></li>';
			$(appendId).append(appendHtml);
		}

		//　第二階層
		if( list[i].categoryLevel == "2" ){

			// ひとつ前のループが第一階層の場合（受領データはソート済みのため、以下の条件で、ループのひとつ前は必ず親になる）
			if( list[i - 1].categoryLevel == "1" ){
			    appendHtml =	'<ul class="l-category-sm__level2wrap" id="' + list[i].cidParent + spSuffix + list[i].hierarchicalSerialNumber.slice(0, 1) + '_parent"></ul>';
			    $('#' + list[i].cidParent + spSuffix + list[i].hierarchicalSerialNumber.slice(0, 1)).append(appendHtml);
			}

			appendHtml =		'<li class="l-category-sm__level2" data-hierarchy="2" id="' + list[i].cidParent + spSuffix + list[i].hierarchicalSerialNumber + '_parent"><div class="l-category-sm__button">';
			// 次のループが第三階層の場合、自身が親になる
			if (list[i + 1] && list[i + 1].categoryLevel == "3") {
			    appendHtml += '<span class="l-category-sm__button-accordion js-accordion-button">'
                        +		'<p class="c-textlink ' + selectedClassName + '">' + list[i].displayName + '</p>'
                        +	'</div>';
			} else {
			    appendHtml += '<div class="l-category-sm__button-anchor">'
                        +       '<a href="' + pkg_common.getAppComplementUrl() + '/goods/list?cid=' + list[i].cid + '&hsn=' + list[i].hierarchicalSerialNumber + '&isSide=true' + '">'
                        +		'<p class="c-textlink ' + selectedClassName + '">' + list[i].displayName + '</p>'
                        +	'</a></div>';
			}
			appendHtml += '</div></li>';

			$('#' + list[i].cidParent + spSuffix + list[i].hierarchicalSerialNumber.slice(0, 1) + '_parent').append(appendHtml);
		}

		//　第三階層
		if( list[i].categoryLevel == "3" ){

			// ひとつ前のループが第二階層の場合（受領データはソート済みのため、以下の条件で必ず親になる）
			if( list[i - 1].categoryLevel == "2" ){
			    appendHtml =	'<div class="l-category-sm__block js-accordion-block" style="height: 0px;"><ul class="l-category-sm__level3wrap" id="' + list[i].cidParent + spSuffix + list[i].hierarchicalSerialNumber.slice(0, 3) + '_parent' + '"></ul></div>';
			    $('#' + list[i-1].cidParent + spSuffix + list[i].hierarchicalSerialNumber.slice(0, 3) + '_parent').append(appendHtml);
			}

			appendHtml =		'<li class="l-category-sm__level3" data-hierarchy="3"><a class="l-category-sm__button-anchor" href="' + pkg_common.getAppComplementUrl() + '/goods/list?cid=' + list[i].cid + '&hsn=' + list[i].hierarchicalSerialNumber + '&isSide=true' + '">'
									+		'<p class="c-textlink ' + selectedClassName + '">' + list[i].displayName + '</p>'
									+	'</a></li>';
			$('#' + list[i].cidParent + spSuffix + list[i].hierarchicalSerialNumber.slice(0, 3) + '_parent').append(appendHtml);
		}
	}

    var baseDiv = document.querySelector('#sidemenuPageItemsSp-1');
	while(baseDiv.firstChild) {
        baseDiv.parentNode.insertBefore(baseDiv.firstChild, baseDiv);
    }
    baseDiv.remove();

    // 第三階層のアコーディオン登録
	accordionCategory('.l-category-sm .js-accordion-button');

	// プレビュー画面の場合はリクエストパラメータにプレビューキーとプレビュー日時を付与
    var preKey =  document.getElementById("preKey");
    var preTime = document.getElementById("preTime");
    if( preKey && preTime ){
    	$('#sidemenuPageItemsSp-1 li a').each(function() {
    		this.href += "&prekey=" + preKey.value + "&pretime=" + preTime.value;
    	});
    }
}

// 以下、アコーディオン用JS(2021/6/28時点では ロジカルサンより受領のbundle.jsを流用)
function accordionShow(btn) {
	var btnParent = btn.closest('.l-nav-slide--btn');
	var panel = btnParent.nextElementSibling;
	btnParent.classList.add('is-opened');
	panel.classList.add('is-opened');

	if (btnParent.closest('li').dataset.hierarchy > 1) {
		var addHeight = getHeight(panel.firstElementChild);
		for (var i = 1; i < btnParent.closest('li').dataset.hierarchy; ++i) {
			var parentPanel = btnParent.closest('[data-hierarchy="' + i + '"]').querySelector('.js-accordion-block');
			parentPanel.style.height = Number(parentPanel.style.height.replace('px', '')) + addHeight + 'px';
		}
	}
	panel.style.height = getHeight(panel.firstElementChild) + 'px';
}

function accordionHide(btn) {
	var btnParent = btn.closest('.l-nav-slide--btn');
	var panel = btnParent.nextElementSibling;
	btnParent.classList.remove('is-opened');
	panel.classList.remove('is-opened');

	if (btnParent.closest('li').dataset.hierarchy > 1) {
		var minusHeight = getHeight(panel.firstElementChild);
		for (var i = 1; i < btnParent.closest('li').dataset.hierarchy; ++i) {
			var parentPanel = btnParent.closest('[data-hierarchy="' + i + '"]').querySelector('.js-accordion-block');
			parentPanel.style.height = Number(parentPanel.style.height.replace('px', '')) - minusHeight + 'px';
		}
	}
	panel.style.height = 0;
}

var getHeight = function getHeight(target) {
	return target.clientHeight;
};
