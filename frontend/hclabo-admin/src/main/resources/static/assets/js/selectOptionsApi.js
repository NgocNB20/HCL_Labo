// ダイアログ表示
function SelectOptionsApi(elementId, fieldMappingId, fieldMappingName, options = {initValue: [], method: 'GET', url: '', urlUpdate: ''}) {

    const elementJquery = $('#' + elementId);
    const elementDropDownId = elementId + 'DropDown';
    const elementSelectedId = elementId + 'Selected';

    // 選択したカテゴリー
    let categorySearchChosenList = [];

    if (linkedCategoryList != null) {
        if(linkedCategoryList && categorySearchChosenList.length === 0) {
            categorySearchChosenList = linkedCategoryList;
        }
    }

    $('<div id="' + elementSelectedId +'" class="category-label-wrap pt10"></div>').insertAfter(elementJquery);
    $('<ul id="' + elementDropDownId +'" tabIndex="-1" class="dropdown-content"></ul>').insertAfter(elementJquery);

    const elementDropDownJquery = $('#' + elementDropDownId);
    const elementSelectedJquery = $('#' + elementSelectedId);
    const elementMappingJquery = $('#' + fieldMappingId);
    const elementMappingNameJquery = $('#' + fieldMappingName);

    const token = $('#_csrf').attr('content');
    const header = $('#_csrf_header').attr('content');
    const NO_RESULT_MSG = '<div id="noResult" style="background-color: transparent; margin: 0!important;"></div>\n';
    const DEFAULT_TIMEOUT_KEYPRESS = 500;
    const DEFAULT_LIMIT = 20;

    let allTagList = [];
    let registList = [...options.initValue];
    let allCheckBoxTagList = [];
    let updateTagDropDownFlag = false;
    let dropDownSearchFunction = null;

    // ひもづいている商品タグの取得
    function doLoadGoodsRegisteredTag() {

        // ひもづいている商品タグのHTML構成生成
        renderRegisteredTagList(registList);
    }

    // 商品タグ一覧所得
    function doLoadAllTag(params) {
        return new Promise((resolve, reject) => {
            $.ajax({
                type: options.method,
                url: options.url,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                dataType: "json",
                data: {
                    ...params
                },
                timeout: 30000,
            })
                .done(function (data) {
                    let resultList = [];
                    data['categoryList'].forEach(category => {
                        let exist = false;
                        for (let i = 0; i < registList.length; i++){
                            const registItem = registList[i];
                            if (registItem.value === category['categoryId']) {
                                exist = true;
                                break;
                            }
                        }
                        if (!exist) {
                            resultList.push({
                                value: category['categoryId'],
                                label: category['categoryName']
                            })
                        }
                    })
                    resolve({
                        totalPage: data['pageInfo']['totalPages'],
                        items: resultList
                    });
                })
                .fail(function (data) {
                    if (data && data.status && data.status === 403) {
                        location.href = '/admin/login/'
                    }
                    reject();
                });
        })
    }

    // 絞込用テキストボックス表示する時チェックボックス操作際
    function onChangeCheckboxTag(event) {
        let el = event.target;
        let registCheck = el.checked;
        let value = el.value;
        let nameVal = el.getAttribute('nameVal')
        let itemCheck = {
            value: value,
            label: nameVal
        }
        if (registCheck) {
            registList.push(itemCheck);
            // リストからタグ削除
            removeItemFormList(allTagList, itemCheck);

            // ひもづいている商品タグのHTML構成生成
            renderRegisteredTagList(registList);

            categorySearchChosenList.push({
                categoryId: value,
                registCategoryCheck: registCheck,
                categoryName: nameVal
            })

            updateTagDropDownFlag = true;
            elementJquery.val('');
        } else {
            // リストからタグ削除
            removeItemFormList(registList, itemCheck);
            addTag(allTagList, itemCheck);

            // ひもづいている商品タグのHTML構成生成
            renderRegisteredTagList(registList);
            updateTagDropDownFlag = true;

            categorySearchChosenList = Array.from(categorySearchChosenList.filter(item => item.categoryId != itemCheck.value));
        }
        categorySearchChosenList.sort((a, b) => a.categoryName.localeCompare(b.categoryName));

        if (options.urlUpdate != '') {
            doUpdateSelectedCategory();
        }

        Array.from(allCheckBoxTagList).forEach(item => {
            if (item.value === itemCheck.value) {
                item.registCheck = registCheck;
            }
        })
        updateGoodsTagOnModel(registList);

    }

    // リストからタグ削除
    function doRemoveTagSetting(event) {
        let el = event.currentTarget;
        // リストからタグ削除
        let itemRemove = {
            value: el.value,
            label: el.getAttribute('nameVal')
        }
        removeItemFormList(registList, itemRemove);
        updateGoodsTagOnModel(registList);
        addTag(allTagList, itemRemove);

        categorySearchChosenList = Array.from(categorySearchChosenList.filter(item => item.categoryId != itemRemove.value));
        categorySearchChosenList.sort((a, b) => a.categoryName.localeCompare(b.categoryName))

        // ひもづいている商品タグのHTML構成生成
        renderRegisteredTagList(registList);

        if (options.urlUpdate != '') {
            doUpdateSelectedCategory();
        }
    }

    // ひもづいている商品タグのHTML構成生成
    function renderRegisteredTagList(tagList) {
        elementSelectedJquery.html('');
        $('#' + elementSelectedId +" .remove-selected").unbind('click');
        tagList.sort((a, b) => a.label.localeCompare(b.label));
        let registeredTagListDisplay = '';

        tagList.forEach((item, index) => {
            registeredTagListDisplay += ' <div class="category-label-item">\n' +
                '                               <span id="category-label-item-'+ index +'">' + item.label + '</span>\n' +
                '                              <button type="button" value="' + item.value + '" class="c-btn-icon remove-selected"><i class="material-icons clear"></i></button>\n' +
                '                         </div>'
        });

        elementSelectedJquery.append(registeredTagListDisplay);

        tagList.forEach((item, index) => {
            $("#category-label-item-" + item.index).text(item.label)
        })

        setTimeout(() => {
            $('#' + elementSelectedId +" .remove-selected").on("click", doRemoveTagSetting);
        })
    }

    // 絞込用テキストボックス表示
    async function initDropDownTagSetting() {
        if (updateTagDropDownFlag) {
            allCheckBoxTagList = allCheckBoxTagList.sort((a, b) => b.registCheck - a.registCheck);
            updateTagDropDownFlag = false;
        }
        let searchKeywords = elementJquery.val();
        let params = {
            limit: DEFAULT_LIMIT,
            searchKeywords: searchKeywords,
        }
        const {items} = await doLoadAllTag(params);
        appendTagListDropDown(registList, items);
        elementDropDownJquery.show();
    }

    // 絞込用テキストボックス非表示
    function hideDropDownTagSetting(elementTarget) {
        let currentTarget = elementTarget;
        if (currentTarget.attr('id') === elementDropDownId) {
            elementDropDownJquery.hide();
            return;
        }
        requestAnimationFrame(() => {
            if (!currentTarget.siblings().is(document.activeElement)) {
                elementDropDownJquery.hide();
            }
        });

    }

    // 絞込用テキストボックス表示処理
    function onFilterDropDownTagSetting(event) {
        $('#noResult').remove();
        elementDropDownJquery.show();
        clearTimeout(dropDownSearchFunction);
        dropDownSearchFunction = setTimeout(async function () {
            let keyWord = elementJquery.val();
            await doSearchKeyWordGoodsTag(keyWord);

            filterListDropDown(keyWord, event);

        }, DEFAULT_TIMEOUT_KEYPRESS);
    }

    // 絞込用テキストボックス生成・表示
    function filterListDropDown(input, event) {
        let dropList, dropListString = [], i;
        dropList = $("#" + elementDropDownId + " li input");
        let displayNumber = dropList.length;
        for (i = 0; i < dropList.length; i++) {
            let txtValue = dropList[i].value;
            let nameValue = dropList[i].getAttribute('nameVal');
            dropListString.push(txtValue);
            if (txtValue.toUpperCase().includes(input.toUpperCase()) || nameValue.toUpperCase().includes(input.toUpperCase())) {
                $(dropList[i]).parent().parent().attr('style', 'display: block;');
            } else {
                $(dropList[i]).parent().parent().attr('style', 'display: none;');
                displayNumber--;
            }
        }

        if (displayNumber <= 0) {
            let firstRowResult = $("#" + elementDropDownId + " li:first-of-type");
            $(NO_RESULT_MSG).insertBefore(firstRowResult);
            elementDropDownJquery.css('border', 'none');
        } else {
            $('#noResult').remove();
            elementDropDownJquery.css('border', '1px solid #ddd');
        }

    }

    // 絞込用テキストボックス表示
    function appendTagListDropDown(registListTag, allListTag) {
        $("#" + elementDropDownId + " .checkbox-edit").unbind("change");

        if (allListTag.length === 0) {
            return;
        }

        allCheckBoxTagList = [];
        registListTag.forEach(item => {
            allCheckBoxTagList.push({
                registCheck: true,
                label: item.label,
                value: item.value
            })
        })
        allListTag.forEach(item => {
            if(jQuery.inArray(item, registListTag) === -1) {
                allCheckBoxTagList.push({
                    registCheck: false,
                    label: item.label,
                    value: item.value
                })
            }
        })

        if (registListTag.length === 0) {
            allCheckBoxTagList.sort((a, b) => a.label.localeCompare(b.label))
        }

        let listTagCheckBoxDisplay = '';

        Array.from(allCheckBoxTagList).forEach((item, index) => {
            if (item.registCheck) {
                listTagCheckBoxDisplay += ' <li id="' + item.value + '-' + index + '-li">\n' +
                    ' <label class="c-form-control category-input-name"><input checked type="checkbox" class="checkbox-edit"\n' +
                    '             value="'+ item.value +'"' +
                    '             nameVal=""' +
                    '><i></i>' + item.label + '</label>\n' +
                    '  </li>'
            } else {
                listTagCheckBoxDisplay += ' <li id="' + item.value + '-' + index + '-li">\n' +
                    ' <label class="c-form-control category-input-name"><input type="checkbox" class="checkbox-edit"\n' +
                    '             value="'+ item.value +'"' +
                    '             nameVal=""' +
                    '><i></i>' + item.label + '</label>\n' +
                    '  </li>'
            }
        })
        elementDropDownJquery.html('');
        elementDropDownJquery.append(listTagCheckBoxDisplay);

        allCheckBoxTagList.forEach((item, index) => {
            $("#" + item.value + '-' + index + '-li').find('.category-input-name input')[0].setAttribute('nameVal', item.label);
        });

        setTimeout(() => {
            $("#" + elementDropDownId + " .checkbox-edit").on("change", onChangeCheckboxTag);
        })

    }

    // リストからタグ削除
    function removeItemFormList(array, item) {
        for (let i = 0; i <array.length; i++) {
            if (array[i].value === item.value) {
                array.splice(i, 1);
                break;
            }
        }
    }

    function addTag(array, item) {
        let exist = false;
        for (let i = 0; i < array.length; i++) {
            if (array[i].value === item.value) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            array.push({...item})
        }
    }

    // AJAX : キーワード処理による商品タグ検索
    async function doSearchKeyWordGoodsTag(keywords) {
        const {items} = await doLoadAllTag({
            searchKeywords: keywords,
            limit: DEFAULT_LIMIT
        });
        appendTagListDropDown(registList, items);
    }

    // モデルの値に反映
    function updateGoodsTagOnModel(registList) {
        let ids = registList.map(regist => regist.value)
        let labels = registList.map(regist => regist.label)
        elementMappingJquery.val(ids.join(','))
        elementMappingNameJquery.val(labels.join(','))
    }

    elementJquery.on({
        focus: initDropDownTagSetting,
        blur: () => hideDropDownTagSetting(elementJquery),
        keyup: onFilterDropDownTagSetting
    })

    elementDropDownJquery.on({
        blur: () => hideDropDownTagSetting(elementDropDownJquery)
    });

    // ひもづいている商品タグの取得
    doLoadGoodsRegisteredTag();

    // 選択されたカテゴリをモデルに更新
    function doUpdateSelectedCategory() {

        let modifiedListCategory = {'categoryChosenList': categorySearchChosenList};
        $.ajax({
            type: "POST",
            url: options.urlUpdate,
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

}