// カテゴリ一覧
var searchCategoryAllList = [];
// 選択したカテゴリー
var searchCategoryChosenList = [];
// カテゴリのドロップダウンをリロード
var searchUpdateDropFlg = false;

const DEFALUT_TIMEOUT_GOODS_CATEGORY_SEARCH = 500;

// 初期化
$(function(){
    if (linkedSearchCategoryList && linkedSearchCategoryList !== 'null' && linkedSearchCategoryList !== 'undefined') {
        //選択したカテゴリー表示
        appendListInitRelation(linkedSearchCategoryList);
        // 選択したカテゴリー
        searchCategoryAllList = searchCategoryChosenList;
    }

});

// 検索テキストボックス：フォーカス・入力
function showDropDownSearchCaty() {

    // カテゴリ一覧取得
    doLoadAllCategoryRelation();
}

// カテゴリ一覧検索
function doSearchCategoryRelation(keySearch) {

    return new Promise(((resolve, reject) => {
        $.ajax({
            type: "POST",
            url: pkg_common.getAppComplementUrl() + "/goods/category/ajax",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            processData: false,
            contentType: false,
            dataType: "json",
            data: getConditionSearchCategorySettingRelation(),
            timeout: 30000
        })
            .done(function (data) {
                searchCategoryAllList = [];
                searchCategoryAllList = data;
                appendListDropDown(searchCategoryAllList);
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

// カテゴリ一覧取得
function doLoadAllCategoryRelation() {

    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/goods/category/ajax",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        processData: false,
        contentType: false,
        dataType: "json",
        data: getConditionSearchCategorySettingRelation(),
        timeout: 30000
    })
        .done(function (data) {
            if (!searchUpdateDropFlg) {
                searchCategoryAllList = data;
                appendListDropDownRelation(searchCategoryAllList);
            }
            $("#mySearchDropdown").show();
        })
        .fail(function (data) {
            if (data && data.status && data.status === 403) {
                location.href = '/admin/login/'
            }
        })
    ;
}

// ダイアログの項目にフォーム値から設定
function getConditionSearchCategorySettingRelation() {
    let categorySearch = $('#categoryInputSearch').val();

    let form = new FormData();
    form.append("categorySearch", categorySearch)
    return form
}

// 絞込用テキストボックス非表示
function hideDropDownSearchCaty(el) {
    let currentTarget = $(el);
    if (currentTarget.attr('id') == 'mySearchDropdown') {
        $("#mySearchDropdown").hide();
        // 選択されたカテゴリをモデルに更新
        doUpdateSelectedCategoryRelation();
        return;
    }
    requestAnimationFrame(() => {
        if (!currentTarget.siblings().is(document.activeElement)) {
            $("#mySearchDropdown").hide();
        }
    });
}

// カテゴリー入力にフォーカスする際
function onFilterDropDownSearchCaty(el) {
    setTimeout(async function () {
        let searchKeyWord = $(el).val();
        await doSearchCategoryRelation(searchKeyWord);

        filterDropDownCatyRelation(searchKeyWord);
    }, DEFALUT_TIMEOUT_GOODS_CATEGORY_SEARCH)
}
// 絞込用テキストボックス生成・表示
function filterDropDownCatyRelation(input) {
    let dropList, i;

    dropList = $("ul[id='mySearchDropdown'] li input");
    let displayNumber = dropList.length;

    for (i = 0; i < dropList.length; i++) {
        let txtValue = dropList[i].value;
        let categoryId = dropList[i].name;
        if (txtValue.toUpperCase().indexOf(input.toUpperCase()) > -1 || categoryId.toUpperCase().indexOf(input.toUpperCase()) > -1) {
            $(dropList[i]).parent().parent().attr('style', 'display: block;');
        } else {
            $(dropList[i]).parent().parent().attr('style', 'display: none;');
            displayNumber--;
        }
    }

    if (displayNumber <= 0) {
        $('#mySearchDropdown').css('border', 'none');
    } else {
        $('#mySearchDropdown').css('border', '1px solid #ddd');
    }
}

// カテゴリー入力する際
function onChangeSearchCheckboxCatyRelation(el) {
    let registCheck = el.checked;
    let categoryId = el.name;
    let categorySeq = el.id;
    let categoryName= el.value;
    if (registCheck) {
        searchCategoryChosenList.push({
            categorySeq: +categorySeq,
            categoryId: categoryId,
            registCategoryCheck: registCheck,
            categoryName: categoryName
        })
        searchCategoryChosenList.sort((a, b) => a.categoryName.localeCompare(b.categoryName))
        appendListRelation(searchCategoryChosenList);
        searchUpdateDropFlg = true;
    } else {
        searchCategoryChosenList = Array.from(searchCategoryChosenList.filter(item => item.categorySeq != categorySeq));
        searchCategoryChosenList.sort((a, b) => a.categoryName.localeCompare(b.categoryName))
        appendListRelation(searchCategoryChosenList, false);
        searchUpdateDropFlg = true;
    }
    Array.from(searchCategoryAllList).forEach(item => {
        if (item.categorySeq == categorySeq) {
            item.registCategoryCheck = registCheck;
        }
    });
}

// 選択されたカテゴリをモデルに更新
function doUpdateSelectedCategoryRelation() {

    let modifiedListCategory = {'categoryChosenList': searchCategoryChosenList};
    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/goods/registupdate/relationsearch/category/update/ajax",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(modifiedListCategory),
        timeout: 30000
    })
        .done(function (data) {
        })
        .fail(function (data) {
            if (data && data.status && data.status === 403) {
                location.href = '/admin/login/'
            }
        })
    ;
}

// 絞込用テキストボックスクローズ
function doRemoveCategorySettingRelation(el) {
    let listOfManual = $("#mySearchDropdown input");
    Array.from(listOfManual).forEach(item => {
        if (el.id == item.id) {
            item.checked = false;
        }
    });
    searchCategoryAllList.forEach(item => {
        if (item.categorySeq == el.id) {
            item.registCategoryCheck = false;
        }
    })
    searchUpdateDropFlg = true;
    $(el).parent().remove();
    searchCategoryChosenList = Array.from(searchCategoryChosenList.filter(item => item.categorySeq != el.id));
    doUpdateSelectedCategoryRelation();

}

// カテゴリー一覧のHTML構成生成
function appendListRelation(listCategory) {
    let listCategoryDisplay = '';
    Array.from(listCategory).forEach((item, index) => {
        listCategoryDisplay += '   <div class="category-label-item">\n' +
            '<span class="">' + item.categoryName + '</span>\n' +
            '    <button onclick="doRemoveCategorySettingRelation(this)" id="' + item.categorySeq + '" class="c-btn-icon"><i class="material-icons clear"></i></button>\n' +
            '    </div>' + '\n'
    })
    let listOfManual = $("#search_category_label_list .category-label-item");
    Array.from(listOfManual).forEach(el => $(el).remove());

    let firstItemBatch = $("#search_category_label_list .category-label-item-batch:first-of-type");
    if (firstItemBatch.length > 0) {
        $(listCategoryDisplay).insertBefore(firstItemBatch);
    } else {
        $('#search_category_label_list').append(listCategoryDisplay);
        Array.from(listCategory).forEach((item, index) => {
            document.getElementById(item.categorySeq).value = item.categoryName;
        })
    }
}

// 選択したカテゴリー表示
function appendListInitRelation(listCategory) {
    let listCategoryDisplay = '';
    let listCategoryAutoDisplay = '';
    Array.from(listCategory).forEach(item => {
        if (item.categoryType === "NORMAL" ) {
            listCategoryDisplay += '   <div class="category-label-item">\n' +
                '<span class="">' + item.categoryName + '</span>\n' +
                '    <button onclick="doRemoveCategorySettingRelation(this)" id="' + item.categorySeq + '" class="c-btn-icon"><i class="material-icons clear"></i></button>\n' +
                '    </div>' + '\n'
        }
        if (item.categoryType === "AUTO" ) {
            listCategoryAutoDisplay += '   <div class="category-label-item-batch">\n' +
                '<span class="">' + item.categorySeq + '</span>\n' +
                '    </div>' + '\n'
        }
    })
    let listOfManual = $("#search_category_label_list .category-label-item");
    Array.from(listOfManual).forEach(el => $(el).remove());

    let listOfAuto = $("#search_category_label_list .category-label-item-batch");
    Array.from(listOfAuto).forEach(el => $(el).remove());

    $('#search_category_label_list').append(listCategoryDisplay);
    $('#search_category_label_list').append(listCategoryAutoDisplay);
}

// 絞込用テキストボックス・カテゴリー一覧のHTML構成生成
function appendListDropDownRelation(listCategoryCheckbox) {

    if (listCategoryCheckbox.filter(item => item.registCategoryCheck == true).length == 0) {
        listCategoryCheckbox.sort((a, b) => a.categoryName.localeCompare(b.categoryName))
    } else {
        listCategoryCheckbox.sort((a, b) => b.registCategoryCheck - a.registCategoryCheck);
    }

    let listCategoryCheckBoxDisplay = '';
    Array.from(listCategoryCheckbox).forEach((item, index) => {
        if (item.registCategoryCheck) {
            listCategoryCheckBoxDisplay += ' <li>\n' +
                ' <label class="c-form-control"><input checked name="'+item.categoryId+'" id="' + item.categorySeq + '" onchange="onChangeSearchCheckboxCatyRelation(this)" type="checkbox"\n' +
                '             value=""><i></i>' + item.categoryName + '</label>\n' +
                '  </li>'
        } else {
            listCategoryCheckBoxDisplay += ' <li>\n' +
                ' <label class="c-form-control"><input name="'+item.categoryId+'" id="' + item.categorySeq + '" onchange="onChangeSearchCheckboxCatyRelation(this)" type="checkbox"\n' +
                '             value=""><i></i>' + item.categoryName + '</label>\n' +
                '  </li>'
        }
    })
    $('#mySearchDropdown').html('');
    $('#mySearchDropdown').append(listCategoryCheckBoxDisplay);

    Array.from(listCategoryCheckbox).forEach((item, index) => {
        $('input[name="'+item.categoryId + '"]').val(item.categoryName);
    })
}
