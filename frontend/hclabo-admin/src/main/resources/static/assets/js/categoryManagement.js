// カテゴリ一覧
var categoryAllList = [];
// ひもづいているカテゴリー（手動のみ）
var categoryChosenList = [];
// カテゴリのドロップダウンをリロード
var updateDropFlg = true;

// 初期化
$(function(){
    if (linkedCategoryList && linkedCategoryList !== 'null' && linkedCategoryList !== 'undefined') {
        // ひもづいているカテゴリー表示
        appendListInit(linkedCategoryList);
        // ひもづいているカテゴリー（手動のみ）
        categoryChosenList = Array.from(linkedCategoryList.filter(item => item.categoryType != "AUTO"));
        categoryAllList = categoryChosenList;
    }

});

// 検索テキストボックス：フォーカス・入力
function showDropDownCaty() {

    // カテゴリ一覧取得
    doLoadAllCategory();
}

// カテゴリ一覧検索
function doSearchCategory(keySearch) {

    return new Promise(((resolve, reject) => {
        $.ajax({
            type: "POST",
            url: pkg_common.getAppComplementUrl() + "/goods/registupdate/category/ajax",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            processData: false,
            contentType: false,
            dataType: "json",
            data: getConditionSearchCategorySetting(),
            timeout: 30000
        })
            .done(function (data) {
                categoryAllList = [];
                categoryAllList = data;
                appendListDropDown(categoryAllList);
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
function doLoadAllCategory() {

    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/goods/registupdate/category/ajax",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        processData: false,
        contentType: false,
        dataType: "json",
        data: getConditionSearchCategorySetting(),
        timeout: 30000
    })
        .done(function (data) {
            categoryAllList = data;
            if (updateDropFlg) {
                appendListDropDown(categoryAllList);
            }
            $("#myDropdown").show();
        })
        .fail(function (data) {
            if (data && data.status && data.status === 403) {
                location.href = '/admin/login/'
            }
        })
    ;
}

// ダイアログの項目にフォーム値から設定
function getConditionSearchCategorySetting() {
    let categorySearch = $('#categoryInputSearch').val();

    let form = new FormData();
    form.append("categorySearch", categorySearch)
    return form
}

// 絞込用テキストボックス非表示
function hideDropDownCaty(el) {
    let currentTarget = $(el);
    if (currentTarget.attr('id') == 'myDropdown') {
        $("#myDropdown").hide();
        // 選択されたカテゴリをモデルに更新
        doUpdateSelectedCategory();
        return;
    }
    requestAnimationFrame(() => {
        if (!currentTarget.siblings().is(document.activeElement)) {
            $("#myDropdown").hide();
        }
    });
}

// カテゴリー入力にフォーカスする際
function onFilterDropDownCaty(el) {
    setTimeout(async function () {
        let searchKeyWord = $(el).val();
        await doSearchCategory(searchKeyWord);

        filterDropDownCaty(searchKeyWord);
    }, DEFAULT_TIMEOUT_KEYPRESS)
}
// 絞込用テキストボックス生成・表示
function filterDropDownCaty(input) {
    let dropList, i;

    dropList = $("ul[id='myDropdown'] li input");
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
        $('#myDropdown').css('border', 'none');
    } else {
        $('#myDropdown').css('border', '1px solid #ddd');
    }
}

// カテゴリー入力する際
function onChangeCheckboxCaty(el) {
    let registCheck = el.checked;
    let categorySeq = el.id;
    let categoryName= el.value;
    if (registCheck) {
        categoryChosenList.push({
            categorySeq: +categorySeq,
            registCategoryCheck: registCheck,
            categoryName: categoryName
        })
        categoryChosenList.sort((a, b) => a.categoryName.localeCompare(b.categoryName))
        appendList(categoryChosenList);
        updateDropFlg = true;
    } else {
        categoryChosenList = Array.from(categoryChosenList.filter(item => item.categorySeq != categorySeq));
        categoryChosenList.sort((a, b) => a.categoryName.localeCompare(b.categoryName))
        appendList(categoryChosenList, false);
        updateDropFlg = true;
    }
    Array.from(categoryAllList).forEach(item => {
        if (item.categorySeq == categorySeq) {
            item.registCategoryCheck = registCheck;
        }
    });
}

// 選択されたカテゴリをモデルに更新
function doUpdateSelectedCategory() {

    let modifiedListCategory = {'categoryChosenList': categoryChosenList};
    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/goods/registupdate/category/update/ajax",
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
function doRemoveCategorySetting(el) {
    let listOfManual = $("#myDropdown input");
    Array.from(listOfManual).forEach(item => {
        if (el.id == item.id) {
            item.checked = false;
        }
    });
    categoryAllList.forEach(item => {
        if (item.categorySeq == el.id) {
            item.registCategoryCheck = false;
        }
    })
    updateDropFlg = true;
    $(el).parent().remove();
    categoryChosenList = Array.from(categoryChosenList.filter(item => item.categorySeq != el.id));
    doUpdateSelectedCategory();

}

// カテゴリー一覧のHTML構成生成
function appendList(listCategory) {
    let listCategoryDisplay = '';
    Array.from(listCategory).forEach(item => {
        listCategoryDisplay += '   <div class="category-label-item">\n' +
            '<span class="">' + item.categoryName + '</span>\n' +
            '    <button onclick="doRemoveCategorySetting(this)" id="' + item.categorySeq + '" class="c-btn-icon"><i class="material-icons clear"></i></button>\n' +
            '    </div>' + '\n'
    })
    let listOfManual = $("#category_label_list .category-label-item");
    Array.from(listOfManual).forEach(el => $(el).remove());

    let firstItemBatch = $("#category_label_list .category-label-item-batch:first-of-type");
    if (firstItemBatch.length > 0) {
        $(listCategoryDisplay).insertBefore(firstItemBatch);
    } else {
        $('#category_label_list').append(listCategoryDisplay);
    }
}

// ひもづいているカテゴリー表示
function appendListInit(listCategory) {
    let listCategoryDisplay = '';
    let listCategoryAutoDisplay = '';
    Array.from(listCategory).forEach(item => {
        if (item.categoryType === "NORMAL" ) {
            listCategoryDisplay += '   <div class="category-label-item">\n' +
                '<span class="">' + item.categoryName + '</span>\n' +
                '    <button onclick="doRemoveCategorySetting(this)" id="' + item.categorySeq + '" class="c-btn-icon"><i class="material-icons clear"></i></button>\n' +
                '    </div>' + '\n'
        }
        if (item.categoryType === "AUTO" ) {
            listCategoryAutoDisplay += '   <div class="category-label-item-batch">\n' +
                '<span class="">' + item.categoryName + '</span>\n' +
                '    </div>' + '\n'
        }
    })
    let listOfManual = $("#category_label_list .category-label-item");
    Array.from(listOfManual).forEach(el => $(el).remove());

    let listOfAuto = $("#category_label_list .category-label-item-batch");
    Array.from(listOfAuto).forEach(el => $(el).remove());

    $('#category_label_list').append(listCategoryDisplay);
    $('#category_label_list').append(listCategoryAutoDisplay);
}

// 絞込用テキストボックス・カテゴリー一覧のHTML構成生成
function appendListDropDown(listCategoryCheckbox) {

    if (listCategoryCheckbox.filter(item => item.registCategoryCheck == true).length == 0) {
        listCategoryCheckbox.sort((a, b) => a.categoryName.localeCompare(b.categoryName))
    } else {
        listCategoryCheckbox.sort((a, b) => b.registCategoryCheck - a.registCategoryCheck);
    }

    let listCategoryCheckBoxDisplay = '';
    Array.from(listCategoryCheckbox).forEach(item => {
        if (item.registCategoryCheck) {
            listCategoryCheckBoxDisplay += ' <li>\n' +
                ' <label class="c-form-control"><input checked name="'+item.categoryId+'" id="' + item.categorySeq + '" onchange="onChangeCheckboxCaty(this)" type="checkbox"\n' +
                '             value=""><i></i>' + item.categoryName + '</label>\n' +
                '  </li>'
        } else {
            listCategoryCheckBoxDisplay += ' <li>\n' +
                ' <label class="c-form-control"><input name="'+item.categoryId+'" id="' + item.categorySeq + '" onchange="onChangeCheckboxCaty(this)" type="checkbox"\n' +
                '             value=""><i></i>' + item.categoryName + '</label>\n' +
                '  </li>'
        }
    })
    $('#myDropdown').html('');
    $('#myDropdown').append(listCategoryCheckBoxDisplay);

    Array.from(listCategoryCheckbox).forEach((item) => {
        document.getElementById(item.categorySeq).value = item.categoryName;
    })
}
