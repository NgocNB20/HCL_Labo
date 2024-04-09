// カテゴリ一覧
var categorySearchAllList = [];
// 選択したカテゴリー
var categorySearchChosenList = [];
// カテゴリのドロップダウンをリロード
var categorySearchUpdateDropFlg = false;

const DEFALUT_TIMEOUT_CATEGORY_SEARCH = 500;

// 初期化
$(function(){
    if (linkedCategoryList && linkedCategoryList !== 'null' && linkedCategoryList !== 'undefined') {
        // 選択したカテゴリー表示
        appendListInit(linkedCategoryList);
        // 選択したるカテゴリー
        categorySearchAllList = categorySearchChosenList;
        if(linkedCategoryList && categorySearchChosenList.length === 0) {
            categorySearchChosenList = linkedCategoryList;
        }
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
            url: pkg_common.getAppComplementUrl() + "/goods/category/ajax",
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
                categorySearchAllList = [];
                categorySearchAllList = data;
                if (linkedCategoryList) {
                    Array.from(linkedCategoryList).forEach(linkedCategoryItem => {
                        Array.from(categorySearchAllList).forEach(categorySearchAllItem => {
                            if (linkedCategoryItem.categorySeq == categorySearchAllItem.categorySeq) {
                                categorySearchAllItem.registCategoryCheck = linkedCategoryItem.registCategoryCheck;
                            }
                        })
                    });
                }
                appendListDropDown(categorySearchAllList);
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
        url: pkg_common.getAppComplementUrl() + "/goods/category/ajax",
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
            if (!categorySearchUpdateDropFlg) {
                categorySearchAllList = data;
                if (linkedCategoryList) {
                    Array.from(linkedCategoryList).forEach(linkedCategoryItem => {
                        Array.from(categorySearchAllList).forEach(categorySearchAllItem => {
                            if (linkedCategoryItem.categorySeq == categorySearchAllItem.categorySeq) {
                                categorySearchAllItem.registCategoryCheck = linkedCategoryItem.registCategoryCheck;
                            }
                        })
                    });
                }
                appendListDropDown(categorySearchAllList);
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
    }, DEFALUT_TIMEOUT_CATEGORY_SEARCH)
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
    let categoryId = el.name;
    let categoryName= el.value;
    if (registCheck) {
        categorySearchChosenList.push({
            categoryId: categoryId,
            categorySeq: +categorySeq,
            registCategoryCheck: registCheck,
            categoryName: categoryName
        })
        categorySearchChosenList.sort((a, b) => a.categoryName.localeCompare(b.categoryName))
        appendList(categorySearchChosenList);
        categorySearchUpdateDropFlg = true;
    } else {
        categorySearchChosenList = Array.from(categorySearchChosenList.filter(item => item.categorySeq != categorySeq));
        categorySearchChosenList.sort((a, b) => a.categoryName.localeCompare(b.categoryName))
        appendList(categorySearchChosenList, false);
        categorySearchUpdateDropFlg = true;
    }
    Array.from(categorySearchAllList).forEach(item => {
        if (item.categorySeq == categorySeq) {
            item.registCategoryCheck = registCheck;
        }
    });
}

// 選択されたカテゴリをモデルに更新
function doUpdateSelectedCategory() {

    let modifiedListCategory = {'categoryChosenList': categorySearchChosenList};
    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/goods/category/update/ajax",
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
        });
    linkedCategoryList = categorySearchChosenList;
}

// 絞込用テキストボックスクローズ
function doRemoveCategorySetting(el) {
    let listOfManual = $("#myDropdown input");
    Array.from(listOfManual).forEach(item => {
        if (el.id == item.id) {
            item.checked = false;
        }
    });
    categorySearchAllList.forEach(item => {
        if (item.categorySeq == el.id) {
            item.registCategoryCheck = false;
        }
    });
    categorySearchUpdateDropFlg = false;
    $(el).parent().remove();
    categorySearchChosenList = Array.from(categorySearchChosenList.filter(item => item.categorySeq != el.id));
    linkedCategoryList = Array.from(linkedCategoryList.filter(item => item.categorySeq != el.id));
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

// 選択したカテゴリー表示
function appendListInit(listCategory) {
    let listCategoryDisplay = '';
    let listCategoryAutoDisplay = '';
    Array.from(listCategory).forEach(item => {
        listCategoryDisplay += '   <div class="category-label-item">\n' +
            '<span class="categorySearchList">' + item.categoryName + '</span>\n' +
            '    <button onclick="doRemoveCategorySetting(this)" id="' + item.categorySeq + '" class="c-btn-icon"><i class="material-icons clear"></i></button>\n' +
            '    </div>' + '\n'
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

    Array.from(listCategoryCheckbox).forEach((item, index) => {
        document.getElementById(item.categorySeq).value = item.categoryName;
    });

}
