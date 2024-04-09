var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');
var allTagList = [], registTagList = [];
var registTagListTemp = [];
var allCheckBoxTagList = [];
var newlyAddedPopupList = [];
var updateTagDropDownFlag = false;
var DEFAULT_TIMEOUT_KEYPRESS = 500;
var DEFAULT_GOODS_TAG_LIMIT = 20;

var LOADING_SPINNER = '<div class="spinner-load"></div>\n' +
                    '<span class="loading-txt">読み込み中...</span>'

var NO_RESULT_MSG = '<span>タグが見つかりません。</span>\n'

// サーバー側でタグロード中
var pageCurrent = 1;
var totalPages = 1;

// タイマーアウト制御
var popupSearchFunction = null;
var dropDownSearchFunction = null;

// ダイアログ表示
$(function () {
    $("#goods_tag_management").click(function () {
        allTagList = [];
        newlyAddedPopupList = [];
        registTagListTemp = [...registTagList];
        // 商品タグ一覧所得
        doLoadGoodsTagPage().then(function () {
            displayMessageNoResult();

            let heightElement = 40;
            let elementStartCallServer = 80;
            new ScrollHandle($("#allListTagWrap"), heightElement * elementStartCallServer, doLoadGoodsTagPage);
        })
        $("#goods_tag_management_dialog").dialog({
            modal: true,
            width: 600,
            title: "商品タグ管理",
            position: {
                my: 'top',
                at: 'top+40'
            },
            buttons: [
                 {
                    text: '確定',
                    click: function () {
                        doAddTagSetting();
                    }
                }],
            close: function (event, ui) {
                $('#newtags_added_wrap').attr('style', 'display: none;');
                $('#newtags_added_wrap table #newAddedTagList').html('')
                $('#search_goods_tag_popup').val('');
                removeRecommendAddTagButtonPopup();
                registTagList = [...registTagListTemp];
                newlyAddedPopupList = [];
                pageCurrent = 1;
                allTagList = [];
                clearDataTableRow();
            }
        });

    });

    // ひもづいている商品タグの取得
    doLoadGoodsRegisteredTag();
});

function displayMessageNoResult() {
    if ($('#goods_tag_management_dialog #registListTag input[type=checkbox]').length > 0) {
        $('#goods_tag_management_dialog #registListTagWrap .no-result-tags-wrap').html('');
    } else {
        showNoResultSearchRegisteredList();
    }

    if ($('#goods_tag_management_dialog #allListTag input[type=checkbox]').length > 0) {
        $('#goods_tag_management_dialog #allListTagWrap .no-result-tags-wrap').html('');
    } else {
        showNoResultSearchAllList();
    }
}


// 商品タグ管理ダイアログ表示
async function doLoadGoodsTagPage(condition = {}) {
    $('#goodstag_input_search').val('');
    if (pageCurrent > totalPages) {
        return;
    }
    // 無制限設定
    let params = {
        limit: 100,
        page: pageCurrent,
        ...condition
    }
    const {tags, totalPage} = await doLoadAllTag(params);
    allTagList = allTagList.concat(tags.filter(item => !registTagList.includes(item) && !allTagList.includes(item)));
    // 商品タグ一覧表示
    loadListTag(allTagList, registTagList);
    // 現在ページ更新
    pageCurrent++;
    totalPages = totalPage;
}

// ひもづいている商品タグの取得
function doLoadGoodsRegisteredTag() {

    // 商品タグリスト取得
    registTagList = $('input[class="goodsTagList"]').map(function(){
        return this.value
    }).get();

    // ひもづいている商品タグのHTML構成生成
    appendRegisteredTagList(registTagList);
}

// 商品タグ一覧所得
function doLoadAllTag(params) {

    return new Promise(((resolve, reject) => {
        $.ajax({
            type: "GET",
            url: pkg_common.getAppComplementUrl() + "/goods/registupdate/tags/ajax",
            dataType: "json",
            data: {
               ...params
            },
            timeout: 30000
        })
            .done(function (data) {
                let tagsInfo = data['goodsTagList'].map(goodsTagInfo => goodsTagInfo['goodsTag']);
                resolve({
                    totalPage: data['pageInfo']['totalPages'],
                    tags: tagsInfo.filter(item => !registTagList.includes(item))
                });
            })
            .fail(function (data) {
                if (data && data.status && data.status === 403) {
                    location.href = '/admin/login/'
                }
                reject();
            });
    }))
}

// 選択一覧でチェックイベント
function onAddGoodsTag(el) {
    // タグ追加
    registTagList.push(el.value);

    // リストからタグ削除
    removeItemFormList(allTagList, el.value);

    // 商品タグ一覧表示
    loadListTag(allTagList, registTagList);

    displayMessageNoResult();
}

// チェック外す時
function onRemoveGoodsTag(el) {
    // リストからタグ削除
    removeItemFormList(registTagList, el.value);

    // タグ追加
    addTag(allTagList, el.value);

    // 商品タグ一覧表示
    loadListTag(allTagList, registTagList);

    displayMessageNoResult();
}

// 商品タグ管理確定時
function doAddTagSetting() {
    // 商品タグをひもづく
    registTagList = registTagList.concat(newlyAddedPopupList);

    // ひもづいている商品タグのHTML構成生成
    appendRegisteredTagList(registTagList);

    // モデルの値に反映
    updateGoodsTagOnModel(registTagList);

    registTagListTemp = [...registTagList];

    // モーダル閉じる
    $("#goods_tag_management_dialog").dialog("close");
}
// リストからタグ削除
function doRemoveTagSetting(el) {
    // リストからタグ削除
    removeItemFormList(registTagList, el.value);
    updateGoodsTagOnModel(registTagList);
    addTag(allTagList, el.value);

    // ひもづいている商品タグのHTML構成生成
    appendRegisteredTagList(registTagList);
}
// タグ追加ボタン処理
function doAddToAllTagList(el) {
    let additionalTag = el.value;
    registTagList.push(additionalTag);
    updateTagDropDownFlag = true

    // ひもづいている商品タグのHTML構成生成
    appendRegisteredTagList(registTagList);
    updateGoodsTagOnModel(registTagList);
    removeRecommendAddTagButton();
    $('#goodstag_input_search').val('');
}

// タグ追加ボタン処理 （商品タグ管理）
function doAddRecommendTagPopup(el) {
    let newestAddedTagPopup = el.value;
    newlyAddedPopupList.push(newestAddedTagPopup);
    let newlyItem = '<tr>\n' +
        '                <td>\n' +
        '                    <label class="c-form-control"><input checked onchange="removeNewlyAddedPopup(this)" id="newestAddedTagPopup" value="" type="checkbox"><i></i>'+newestAddedTagPopup+'</label>\n' +
        '                </td>\n' +
        '            </tr>';

    $('#newtags_added_wrap table #newAddedTagList').append(newlyItem);
    document.getElementById('newestAddedTagPopup').value = newestAddedTagPopup;

    $('#newtags_added_wrap').attr('style', 'display: block;');
    $('#search_goods_tag_popup').val('');
    onFilterGoodsTagPopupSearch($('#search_goods_tag_popup'));
    removeRecommendAddTagButtonPopup();
}

// 新しく追加された商品タグを削除
function removeNewlyAddedPopup(el) {
    $(el).parent().parent().parent().remove();
    removeItemFormList(newlyAddedPopupList, el.value)

    let newlyAddedListCount = $('#newAddedTagList tr').length;
    if (newlyAddedListCount === 0) {
        $('#newtags_added_wrap').attr('style', 'display: none;')
    }
}

// 商品タグ一覧表示
function loadListTag(allTagList, registTagList) {
    hideLoadingSpinner();
    clearDataTableRow();

    let allTagItems = ''
    allTagList.forEach((item, index) => {
        allTagItems += '<tr>\n' +
            '                <td>\n' +
            '                    <label class="c-form-control"><input onchange="onAddGoodsTag(this)" id="onAddGoodsTag-'+ index +'" value="" type="checkbox"><i></i>'+item+'</label>\n' +
            '                </td>\n' +
            '            </tr>';
    });
    $('#goods_tag_management_dialog #allListTag').append(allTagItems);
    allTagList.forEach((item, index) => {
        document.getElementById('onAddGoodsTag-'+index).value = item;
    });

    let registTagItems = ''

    registTagList.forEach((item, index) => {
        registTagItems += '<tr>\n' +
            '                <td>\n' +
            '                    <label class="c-form-control"><input checked onchange="onRemoveGoodsTag(this)" id="registTagItem-'+index+'" value="" type="checkbox"><i></i>'+item+'</label>\n' +
            '                </td>\n' +
            '            </tr>';
    });
    $('#goods_tag_management_dialog #registListTag').append(registTagItems);
    registTagList.forEach((item, index) => {
        document.getElementById('registTagItem-'+index).value = item;
    });
}

// ひもづいている商品タグのHTML構成生成
function appendRegisteredTagList(tagList) {
    clearDataTagLabel();
    tagList.sort((a, b) => a.localeCompare(b));
    let registeredTagListDisplay = '';
    tagList.forEach((item, index )=> {
        registeredTagListDisplay += ' <div class="category-label-item">\n' +
            '                               <span>' + item + '</span>\n' +
            '                              <button id="registeredTagListDisplay-'+ index +'" value="" onclick="doRemoveTagSetting(this)" class="c-btn-icon"><i class="material-icons clear"></i></button>\n' +
            '                         </div>'
    });

    $('#registered_tag_setting').append(registeredTagListDisplay);

    tagList.forEach((item, index )=> {
        document.getElementById('registeredTagListDisplay-'+index).value = item;
    })
}

// 絞込用テキストボックス表示
async function initDropDownTagSetting() {
    if (updateTagDropDownFlag) {
        allCheckBoxTagList = allCheckBoxTagList.sort((a, b) => b.registCheck - a.registCheck);
        updateTagDropDownFlag = false;
    }
    let goodsTagInput = $('#goodstag_input_search').val();
    let orderField = $('#search_goods_tag_sorting').val();
    let params = {
        limit: DEFAULT_GOODS_TAG_LIMIT,
        goodsTag: goodsTagInput,
        orderField: orderField,
    }
    const {tags} = await doLoadAllTag(params);
    appendTagListDropDown(registTagList, tags);
}

// 絞込用テキストボックス非表示
function hideDropDownTagSetting(el) {
    let currentTarget = $(el);
    if (currentTarget.attr('id') == 'tagDropDown') {
        $("#tagDropDown").hide();
        return;
    }
    requestAnimationFrame(() => {
        if (!currentTarget.siblings().is(document.activeElement)) {
            $("#tagDropDown").hide();
        }
    });

}

// 並べ替えイベント リスナー（商品タグ管理）
function onChangeSortingPopupSearch(el) {
   let sortVal = el.value;
   let keyWord = $('#search_goods_tag_popup').val();
    doSearchKeyWordGoodsTagPopUp(keyWord, sortVal).finally(function () {});
}

// 絞込用テキストボックス表示処理
function onFilterDropDownTagSetting(el, event) {
    $("#tagDropDown").show();
    clearTimeout(dropDownSearchFunction);
    dropDownSearchFunction = setTimeout(async function () {
        let keyWord = $(el).val().replace(/\s/g, '');
        await doSearchKeyWordGoodsTag(keyWord);

        filterListDropDown(keyWord, event);

        displayMessageNoResult();
    }, DEFAULT_TIMEOUT_KEYPRESS);
}

//絞込用テキストボックス表示処理 （商品タグ管理）
function onFilterGoodsTagPopupSearch(el) {
    clearTimeout(popupSearchFunction);
    popupSearchFunction = setTimeout(async function () {
        let keyWord = $(el).val();
        let orderField = $('#search_goods_tag_sorting').val();
        await doSearchKeyWordGoodsTagPopUp(keyWord, orderField);

        filterListPopupSearch(keyWord);

        displayMessageNoResult();
    }, DEFAULT_TIMEOUT_KEYPRESS);
}

// 絞込用テキストボックス生成・表示
function filterListDropDown(input, event) {
    let dropList, dropListString = [], i;
    let isEnterPressed = event.keyCode === 13;
    dropList = $("ul[id='tagDropDown'] li input");
    for (i = 0; i < dropList.length; i++) {
        let txtValue = dropList[i].value;
        dropListString.push(txtValue);
        if (txtValue.toUpperCase().indexOf(input.toUpperCase()) > -1) {
            $(dropList[i]).parent().parent().attr('style', 'display: block;');
        } else {
            $(dropList[i]).parent().parent().attr('style', 'display: none;');
        }
    }
    displayRecommendAddTagButton(input);

    if (dropListString.includes(input)) {
        removeRecommendAddTagButton();
    }
    // Enter キーを押すと製品タグがリンクされます
    if (isEnterPressed && dropListString.includes(input) && !registTagList.includes(input)) {

        // ケース 1 : 製品タグは利用可能ですが、リンクされていません
        registTagList.push(input);
        // リストからタグ削除
        removeItemFormList(allTagList, input);
        // ひもづいている商品タグのHTML構成生成
        appendRegisteredTagList(registTagList);
        updateTagDropDownFlag = true;
        $("#goodstag_input_search").val('');
        $("#tagDropDown").hide();
    } else if (isEnterPressed && dropListString.includes(input) && registTagList.includes(input)) {

        // ケース 2: 商品タグが利用可能でリンクされている
        $("#goodstag_input_search").val('');
        $("#tagDropDown").hide();
    } else if (isEnterPressed) {

        // ケース 3: 商品タグがデータベースに含まれておらず、リンクされていない
        let recommendAddBtn = $("ul[id='tagDropDown'] li[id='recommend_add_tag_btn'] .c-btn_add");
        if (recommendAddBtn) {
            recommendAddBtn.trigger('onclick');
            $("#tagDropDown").hide();
        }
    }

}

// 商品タグ管理のためのリストタグのフィルタリング
function filterListPopupSearch(keySearch) {
    let filterList, filterListString = [], i;

    let tempRegistRowEl = $('#goods_tag_management_dialog #registListTag tr input');
    let tempAllRowEl = $('#goods_tag_management_dialog #allListTag tr input');
    filterList = [...tempRegistRowEl, ...tempAllRowEl];
    for (i = 0; i < filterList.length; i++) {
        let txtValue = filterList[i].value
        filterListString.push(txtValue);
        if (txtValue.toUpperCase().indexOf(keySearch.toUpperCase()) > -1) {
            $(filterList[i]).parent().parent().parent().attr('style', 'display: block;');
        } else {
            $(filterList[i]).parent().parent().parent().attr('style', 'display: none;');
        }
    }
    displayRecommendAddTagButtonPopup(keySearch);

    if (filterListString.includes(keySearch) || newlyAddedPopupList.includes(keySearch)) {
        removeRecommendAddTagButtonPopup();
    }

}

// タグ追加ボタン表示
function displayRecommendAddTagButton(inputSearch) {
    removeRecommendAddTagButton();
    if (inputSearch) {
        let firstRowResult = $("ul[id='tagDropDown'] li:first-of-type");
         let recommendAddTagLI = ' <li id="recommend_add_tag_btn" class="recommend-add-tag">\n' +
            ' <div class="c-btn_add" onclick="doAddToAllTagList(this)"\n' +
            '             id="displayRecommendAddTagButton" th:value=""><i></i><span>' + '追加【' + inputSearch + '】' + '</span></div>\n' +
            '  </li>'
        $(recommendAddTagLI).insertBefore(firstRowResult);
        document.getElementById('displayRecommendAddTagButton').value = inputSearch;
    }
}

// タグ追加ボタン表示（商品タグ管理）
function displayRecommendAddTagButtonPopup(inputSearch) {
    removeRecommendAddTagButtonPopup();
    if (inputSearch) {
        let recommendAddTagLI = ' <li id="recommend_add_tag_btn" class="recommend-add-tag">\n' +
            ' <div class="c-btn_add" onclick="doAddRecommendTagPopup(this)"\n' +
            '             id="doAddRecommendTagPopup" th:value=""><i></i><span>' + '追加【' + inputSearch + '】' + '</span></div>\n' +
            '  </li>'
        $("ul[id='recommend_add_tag_btn_popup']").append(recommendAddTagLI).attr('style','display: block;');
        document.getElementById('doAddRecommendTagPopup').value = inputSearch;
    }
}

// タグ追加ボタン非表示
function removeRecommendAddTagButton() {
    let recommendAddTagLI = $('#recommend_add_tag_btn');
    if (recommendAddTagLI.length > 0) {
        recommendAddTagLI.remove();
    }
}

// タグ追加ボタン非表示（商品タグ管理）
function removeRecommendAddTagButtonPopup() {
    $('#recommend_add_tag_btn_popup').html('').attr('style','display: none;');
}

// 絞込用テキストボックス表示
function appendTagListDropDown(registListTag, allListTag) {
    if (allListTag.length === 0) return;
    $("#tagDropDown").show();
    allCheckBoxTagList = [];
    registListTag.forEach(item => {
        allCheckBoxTagList.push({
            registCheck: true,
            label: item
        })
    })
    allListTag.forEach(item => {
        if(jQuery.inArray(item, registListTag) == -1) {
            allCheckBoxTagList.push({
                registCheck: false,
                label: item
            })
        }
    })

    if (registListTag.length == 0) {
        allCheckBoxTagList.sort((a, b) => a.label.localeCompare(b.label))
    }

    let listTagCheckBoxDisplay = '';
    Array.from(allCheckBoxTagList).forEach((item, index) => {
        if (item.registCheck) {
            listTagCheckBoxDisplay += ' <li>\n' +
                ' <label class="c-form-control"><input id="onChangeCheckboxTag-'+ index +'" checked onchange="onChangeCheckboxTag(this)" type="checkbox"\n' +
                '             value=""><i></i>' + item.label + '</label>\n' +
                '  </li>'
        } else {
            listTagCheckBoxDisplay += ' <li>\n' +
                ' <label class="c-form-control"><input id="onChangeCheckboxTag-'+ index +'" onchange="onChangeCheckboxTag(this)" type="checkbox"\n' +
                '             value=""><i></i>' + item.label + '</label>\n' +
                '  </li>'
        }
    })
    $('#tagDropDown').html('');
    $('#tagDropDown').append(listTagCheckBoxDisplay);

    Array.from(allCheckBoxTagList).forEach((item, index) => {
        document.getElementById('onChangeCheckboxTag-' + index).value = item.label;
    })
}

// 絞込用テキストボックス表示する時チェックボックス操作際
function onChangeCheckboxTag(el) {
    let registCheck = el.checked;
    let tagLabel= el.value;
    if (registCheck) {
        registTagList.push(tagLabel);
        // リストからタグ削除
        removeItemFormList(allTagList, el.value);

        // ひもづいている商品タグのHTML構成生成
        appendRegisteredTagList(registTagList);
        updateTagDropDownFlag = true;
        $("#goodstag_input_search").val('');
    } else {
        // リストからタグ削除
        removeItemFormList(registTagList, tagLabel);
        addTag(allTagList, tagLabel);

        // ひもづいている商品タグのHTML構成生成
        appendRegisteredTagList(registTagList);
        updateTagDropDownFlag = true;
    }
    Array.from(allCheckBoxTagList).forEach(item => {
        if (item.label == tagLabel) {
            item.registCheck = registCheck;
        }
    })
    updateGoodsTagOnModel(registTagList);

}

// クリア
function clearDataTableRow() {
    $('#goods_tag_management_dialog #allListTag').html('');
    $('#goods_tag_management_dialog #registListTag').html('');

}

// クリア
function clearDataTagLabel() {
    $('#registered_tag_setting').html('');
}

// リストからタグ削除
function removeItemFormList(array, item) {
    let removeIndex = array.indexOf(item);
    if (removeIndex !== -1) {
        array.splice(removeIndex, 1);
    }
}

function addTag(array, item) {
    let exist = array.indexOf(item);
    if (exist === -1) {
        array.push(item)
    }
}

// ローディングアニメーションを表示
function showLoadingSpinner() {
    $('#goods_tag_management_dialog #allListTagWrap table').attr('style', 'display: none;')
    $('#goods_tag_management_dialog #registListTagWrap table').attr('style', 'display: none;')
    $('#goods_tag_management_dialog #allListTagWrap .spinner-wrap').html('').append(LOADING_SPINNER);
    $('#goods_tag_management_dialog #registListTagWrap .spinner-wrap').html('').append(LOADING_SPINNER);
}

// ローディングアニメーションを隠す
function hideLoadingSpinner() {
    $('#goods_tag_management_dialog #registListTagWrap .spinner-wrap').html('');
    $('#goods_tag_management_dialog #allListTagWrap .spinner-wrap').html('');
    $('#goods_tag_management_dialog #allListTagWrap table').attr('style', 'display: table;')
    $('#goods_tag_management_dialog #registListTagWrap table').attr('style', 'display: table;')
}

// 結果のないメッセージを表示
function showNoResultSearchAllList() {
    $('#goods_tag_management_dialog #allListTagWrap .no-result-tags-wrap').html('').append(NO_RESULT_MSG);

}
function showNoResultSearchRegisteredList() {
    $('#goods_tag_management_dialog #registListTagWrap .no-result-tags-wrap').html('').append(NO_RESULT_MSG);
}

// 結果のないメッセージを非表示にする
function hideNoResultSearchMessage() {
    $('#goods_tag_management_dialog #allListTagWrap .no-result-tags-wrap').html('');
    $('#goods_tag_management_dialog #registListTagWrap .no-result-tags-wrap').html('');

}

// AJAX : キーワード処理による商品タグ検索
function doSearchKeyWordGoodsTag(keySearch) {
    return new Promise(((resolve, reject) => {
        $.ajax({
            type: "GET",
            url: pkg_common.getAppComplementUrl() + "/goods/registupdate/tags/ajax",
            dataType: "json",
            data: {
                "goodsTag": keySearch,
                "limit" : DEFAULT_GOODS_TAG_LIMIT
            },
            timeout: 30000
        })
            .done(function (data) {

                let tagsInfo = data['goodsTagList'].map(goodsTagInfo => goodsTagInfo['goodsTag']);
                const tagDropDownList = tagsInfo.filter(item => !registTagList.includes(item));
                appendTagListDropDown(registTagList, tagDropDownList);
                resolve();
            })
            .fail(function (data) {
                if (data && data.status && data.status === 403) {
                    location.href = '/admin/login/'
                }
                reject();
            });
    }));
}

// AJAX : キーワード処理による商品タグ検索（商品タグ管理）
async function doSearchKeyWordGoodsTagPopUp(keySearch, orderBy) {
    allTagList = [];
    pageCurrent = 1;
    totalPages = 1;

    hideNoResultSearchMessage();
    showLoadingSpinner();
    let params = {
        goodsTag: keySearch,
        orderField: orderBy,
    }

    // 商品タグ一覧所得
    doLoadGoodsTagPage(params).then(function () {
        displayMessageNoResult();

        let heightElement = 40;
        let elementStartCallServer = 80;
        new ScrollHandle($("#allListTagWrap"), heightElement * elementStartCallServer, () => {doLoadGoodsTagPage(params)});
    })
}

// モデルの値に反映
function updateGoodsTagOnModel(registTagList) {

    let modifiedListGoodsTag = {'tagChosenList': registTagList};
    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/goods/registupdate/goodstag/update/ajax",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(modifiedListGoodsTag),
        timeout: 30000
    })
        .done(function (data) {
        })
        .fail(function (data) {
            if (data && data.status && data.status === 403) {
                location.href = '/admin/login/'
            }
        });

}