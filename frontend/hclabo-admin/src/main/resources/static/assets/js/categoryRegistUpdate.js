var SORT_MANUAL = '5';
var MAX_CATEGORY_CONDITION = 60;
var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');
var linkedGoodsModelItemsList = [];
var goodsGroupChecked = [];

if (linkedGoodsModelItems && linkedGoodsModelItems.length) {
  for (let i = 0; i < linkedGoodsModelItems.length; i++) {
    linkedGoodsModelItemsList.push(linkedGoodsModelItems[i].goodsGroupSeq);
  }
}

const ColumnType = {
  GOODS_GROUP_SEQ: '1',
  GOOD_NAME: '2',
  GOOD_TAG: '3',
  ICON: '4',
  TYPE: '5',
  PRICE: '6',
  ARRIVAL_DATE: '7',
  SELLABLE_STOCK: '8',
  STANDARD_NAME_1: '9',
  STANDARD_VALUE_1: '10',
  STANDARD_NAME_2: '11',
  STANDARD_VALUE_2: '12'
}

const OperatorType = {
  MATCH: '0',
  NOT_MATCH: '1',
  GREATER: '2',
  LESS: '3',
  START_ON: '4',
  END_ON: '5',
  CONTAINS: '6',
  NOT_CONTAINS: '7',
  SETTING: '8',
  NO_SETTING: '9'
}

// SETTING CATEGORY CONDITIONS
var conditionRowInitComponent =
  '<div class="edit" id="conditionRow0">\n' +
  '    <dd class="max_w550px">\n' +
  '        <select onchange="onChangeInput(this)" class="c-form-control mr15" name="categoryConditionItems[0].conditionColumn">\n' +
  '            <option value="" selected="selected">選択してください</option>\n' +
  '            #optionColumns \n' +
  '        </select>\n' +
  '        <span class="mr5">が</span>\n' +
  '        <input maxlength="100" class="c-form-control w150px mr15" name="categoryConditionItems[0].conditionValue">\n' +
  '        <select onchange="handleDisable(this)" class="c-form-control" name="categoryConditionItems[0].conditionOperator">\n' +
  '            <option value="" selected="selected">選択してください</option>\n' +
  '            #optionOperators \n' +
  '        </select>\n' +
  '    <button class="c-btn-icon mt5" onclick="doDeleteCondition(this)" type="button"><i class="material-icons clear"></i></button>\n' +
  '    </dd>\n' +
  '    <div class="c-error-txt mt5"></div>\n' +
  '</div>';

let optionColumns = '';
for (let property in conditionColumns) {
  optionColumns += '<option value="'+ property +'">'+ conditionColumns[property] +'</option>';
}

let optionOperators = '';
for (let property in conditionOperators) {
  optionOperators += '<option value="'+ property +'">'+ conditionOperators[property] +'</option>';
}

conditionRowInitComponent = conditionRowInitComponent.replaceAll('#optionColumns', optionColumns);
conditionRowInitComponent = conditionRowInitComponent.replaceAll('#optionOperators', optionOperators);


function handleDisable(element) {
  let elementFistName = element.name.split('.')[0]
  if (element.value === '8' || element.value === '9') {
    $(`input[name='${elementFistName}.conditionValue']`).attr('disabled', 'disabled')
    $(`input[name='${elementFistName}.conditionValue']`).val("")
  } else {
    $(`input[name='${elementFistName}.conditionValue']`).removeAttr('disabled')
  }
}

function onChangeInput(value) {
  let name = value.name.split('.')[0]
  let element = document.getElementsByName(name + '.conditionOperator')[0].value
  switch (value.value) {
    case ColumnType.GOODS_GROUP_SEQ:
    case ColumnType.GOOD_NAME:
      this.handleDisplay(name,
        [
          OperatorType.GREATER,
          OperatorType.LESS,
          OperatorType.SETTING,
          OperatorType.NO_SETTING,
        ],
        element);
      break;
    case ColumnType.GOOD_TAG:
      this.handleDisplay(name,
        [
          OperatorType.NOT_MATCH,
          OperatorType.GREATER,
          OperatorType.LESS,
          OperatorType.START_ON,
          OperatorType.END_ON,
          OperatorType.CONTAINS,
          OperatorType.NOT_CONTAINS,
          OperatorType.SETTING,
          OperatorType.NO_SETTING,
        ],
        element);
      break;
    case ColumnType.ICON:
      this.handleDisplay(name,
        [
          OperatorType.NOT_MATCH,
          OperatorType.GREATER,
          OperatorType.LESS,
          OperatorType.START_ON,
          OperatorType.END_ON,
          OperatorType.CONTAINS,
          OperatorType.NOT_CONTAINS
        ],
        element);
      break;
    case ColumnType.TYPE:
      this.handleDisplay(name,
        [
          OperatorType.GREATER,
          OperatorType.LESS,
          OperatorType.SETTING,
          OperatorType.NO_SETTING
        ],
        element);
      break;
    case ColumnType.PRICE:
    case ColumnType.ARRIVAL_DATE:
      this.handleDisplay(name,
        [
          OperatorType.START_ON,
          OperatorType.END_ON,
          OperatorType.CONTAINS,
          OperatorType.NOT_CONTAINS,
          OperatorType.SETTING,
          OperatorType.NO_SETTING
        ],
        element);
      break;
    case ColumnType.SELLABLE_STOCK:
      this.handleDisplay(name,
        [
          OperatorType.NOT_MATCH,
          OperatorType.START_ON,
          OperatorType.END_ON,
          OperatorType.CONTAINS,
          OperatorType.NOT_CONTAINS,
          OperatorType.SETTING,
          OperatorType.NO_SETTING,
        ],
        element);
      break;
    case ColumnType.STANDARD_NAME_1:
    case ColumnType.STANDARD_VALUE_1:
    case ColumnType.STANDARD_NAME_2:
    case ColumnType.STANDARD_VALUE_2:
      this.handleDisplay(name,
        [
          OperatorType.GREATER,
          OperatorType.LESS,
          OperatorType.SETTING,
          OperatorType.NO_SETTING
        ],
        element);
      break;
    default:
      this.handleDisplay(name, [], null)
      break;
  }
}

function handleDisplay(elementName, hidden, selected) {
  let element = document.getElementsByName(elementName + '.conditionOperator')
  element[0].options[0].removeAttribute('selected')
  let options = Array.from(element[0].options).map((item) => {
    $(item).removeAttr('selected')

    if (hidden.includes(item.value)) {
      $(item).attr('hidden', true)
    } else {
      $(item).removeAttr('hidden')
    }

    if (item.value === selected) {
      element[0].value = selected
      $(item).attr('selected', true)

      if (selected === '8' || selected === '9') {
        $(`input[name='${elementName}.conditionValue']`).attr('disabled', 'disabled')
        $(`input[name='${elementName}.conditionValue']`).val("")
      }
    }
    return item
  })
  if (selected !== null) {
    element[0].options[0].setAttribute('hidden', 'hidden')
  } else {
    element[0].options[0].removeAttribute('hidden')
    element[0].options[0].setAttribute('selected', 'selected')
  }
  return options
}

function addCategoryConditionAjax() {
  // Ajax
  $.ajax({
    type: "POST"
    , url: pkg_common.getAppComplementUrl() + "/goods/category/addCategoryCondition"
    , dataType: "json"
    , timeout: 30000
    , beforeSend: function (xhr) {
      xhr.setRequestHeader(header, token);
    }
  })
    .done(function () {})
    .fail(function (data) {
      if (data && data.status &&
        (data.status === 403 || (data.status === 200 && data.responseText.includes('login')))
      ) {
        location.href = pkg_common.getAppComplementUrl() + '/login/'
      }
    })
}

function removeCategoryConditionAjax(index) {
  // Ajax
  $.ajax({
    type: "POST"
    , url: pkg_common.getAppComplementUrl() + "/goods/category/removeCategoryCondition/" + index
    , beforeSend: function (xhr) {
      xhr.setRequestHeader(header, token);
    }
    , dataType: "json"
    , timeout: 30000
  })
    .done(function () {})
    .fail(function (data) {
      if (data && data.status &&
        (data.status === 403 || (data.status === 200 && data.responseText.includes('login')))
      ) {
        location.href = pkg_common.getAppComplementUrl() + '/login/'
      }
    })
}

function doAddCondition() {
  addCategoryConditionAjax();
  let lastConditionRow = $('div[id^="conditionRow"]:last');
  if (lastConditionRow.length === 0) {
    let prevSibling = $('.l-article:nth-of-type(3) .l-article_inner .edit');
    prevSibling.after(conditionRowInitComponent);
  } else {
    let incrementId = parseInt(lastConditionRow.prop("id").match(/\d+/g), 10) + 1;
    let countCondition = $('div[id^="conditionRow"]').length;
    if (countCondition < MAX_CATEGORY_CONDITION) {
      let nextConditionRow = lastConditionRow.clone().prop('id', 'conditionRow' + incrementId);
      nextConditionRow.find('input')[0].name = "categoryConditionItems["+ incrementId +"].conditionValue";
      nextConditionRow.find('select')[0].name = "categoryConditionItems["+ incrementId +"].conditionColumn";
      nextConditionRow.find('select')[1].name = "categoryConditionItems["+ incrementId +"].conditionOperator";
      nextConditionRow.find('input')[0].value = '';
      nextConditionRow.find('select')[0].value = '';
      nextConditionRow.find('input')[0].removeAttribute('disabled')
      Array.from(nextConditionRow.find('select')[1].options).map((item) => {
        $(item).removeAttr('hidden');
        $(item).removeAttr('selected')
        return item;
      })
      lastConditionRow.after(nextConditionRow);
    }

    if (countCondition >= MAX_CATEGORY_CONDITION - 1) {
      $('#addConditionBtn').hide();
    }
  }
}

function doDeleteCondition(el) {
  let indexRemove =  +$(el).parent().parent().prop('id').slice(-1);
  removeCategoryConditionAjax(indexRemove);
  $(el).parent().parent().remove();
  // reset index
  let conditionRow = $('div[id^="conditionRow"]');
  for (let i = 0; i < conditionRow.length; i++) {
    $(conditionRow[i]).attr('id', 'conditionRow'+ i );
    $(conditionRow[i]).find('input')[0].name = "categoryConditionItems["+ i +"].conditionValue";
    $(conditionRow[i]).find('select')[0].name = "categoryConditionItems["+ i +"].conditionColumn";
    $(conditionRow[i]).find('select')[1].name = "categoryConditionItems["+ i +"].conditionOperator";
  }
  if (conditionRow.length <= MAX_CATEGORY_CONDITION) {
    $('#addConditionBtn').show();
  }
}

function showCategoryConditionSettings() {
  if ($('#productSearchConditions').hasClass('display_none')) {
    $('#productSearchConditions').removeClass('display_none');
  }
}

function hideCategoryConditionSettings() {
  if (!$('#productSearchConditions').hasClass('display_none')) {
    $('#productSearchConditions').addClass('display_none');
  }
}

// SETTING GOODS
function deleteGoods(el) {
  let goodsGroupSeq = $(el).closest('tr').attr('goodsGroupSeq')
  $.ajax({
    type: "POST"
    , url: pkg_common.getAppComplementUrl() + "/goods/category/deleteGoodsAjax/" + goodsGroupSeq
    ,beforeSend: function (xhr) {
      xhr.setRequestHeader(header, token);
    }
    , timeout: 30000
  })
    .done(function () {
      $(el).closest('tr').remove();
      for ( let i = 0; i < linkedGoodsModelItems.length; i++) {
        if (linkedGoodsModelItems[i].goodsGroupSeq === +goodsGroupSeq) {
          linkedGoodsModelItemsList.splice(i, 1)
          linkedGoodsModelItems.splice(i, 1);
        }
      }
      // Update display description sort manual
      $('#goodsSortManualChecked').text('0個の商品の移動先：');
      $('#goodsSortManualTotal').text(' / ' + $('input:checkbox[name=goodsCheck]').length);
      let goodsList = $('#items_row tr td[name="key"]');
      for (let i = 0; i < goodsList.length; i++) {
          goodsList[i].innerText = i + 1;
      }
    })
    .fail(function (data) {
      if (data && data.status &&
        (data.status === 403 || (data.status === 200 && data.responseText.includes('login')))
      ) {
        location.href = pkg_common.getAppComplementUrl() + '/login/'
      }
    })
}

function doCheckGoods() {
  const numberChecked = $('input:checkbox[name=goodsCheck]:checked').length;
  const totalCheckbox = $('input:checkbox[name=goodsCheck]').length;
  $('#goodsSortManualChecked').text(numberChecked + '個の商品の移動先：');
  if (numberChecked > 0) {
    configShowManualSort();
  } else {
    configHideManualSort();
  }
}

function initSortable() {
  // ソートテーブルのドラッグ可能設定
  return $('#goodsView tbody').sortable({
    cursor: 'n-resize',
    items: 'tr',
    stop: function () {
      let goodsList = $('#items_row tr td[name="key"]');
      let goodsGroupSeqSortNew = [];
      for (let i = 0; i < goodsList.length; i++) {
        goodsGroupSeqSortNew.push(goodsList[i].getAttribute('goodsGroupSeq'));
        goodsList[i].innerText = i + 1;
      }
      $("#goodsGroupSeqSort").val(goodsGroupSeqSortNew);
    }
    // ドラッグ時にホイールマウスをスクロールできます
  }).draggable({
    scroll: true
  });
}

function disableSort() {
  $('#goodsView tbody').sortable('disable').draggable('disable');
}

function enableSort() {
  $('#goodsView tbody').sortable('enable').draggable('enable');
}

function showDragGoods(transparent = false) {
  if (transparent) {
    let elements = document.getElementsByClassName('drag');
    for (let i = 0; i < elements.length; i++) {
      elements[i].style.color = "#3b86ff";
    }
    return;
  }
  if ($('.drag').hasClass('display_none')) {
    $('.drag').removeClass('display_none');
  }
}

function hideDragGoods(transparent = false) {
  if (transparent) {
    let elements = document.getElementsByClassName('drag');
    for (let i = 0; i < elements.length; i++) {
      elements[i].style.color = "transparent";
    }
    return;
  }
  if (!$('.drag').hasClass('display_none')) {
    $('.drag').addClass('display_none');
  }
}

function showCheckGoods() {
  if ($('.check').hasClass('display_none')) {
    $('.check').removeClass('display_none');
  }
}

function hideCheckGoods() {
  if (!$('.check').hasClass('display_none')) {
    $('.check').addClass('display_none');
  }
}

function getGoodsSortAjax(params) {
  // Ajax
  return $.ajax({
    type: "GET"
    , url: pkg_common.getAppComplementUrl() + "/goods/category/getGoodsSortAjax"
    , dataType: "json"
    , data: params
    , timeout: 30000
  })
    .done(function (goodsSortAjaxResponse) {
      if (!goodsSortAjaxResponse || goodsSortAjaxResponse.length === 0) {
        return;
      }
      goodsGroupChecked = [];
      // Update data after sort
      const rows = $('#items_row tr');
      for (let index = 0; index < rows.length; index++) {
        let goodsInfo = goodsSortAjaxResponse[index];
        $(rows[index]).attr('goodsGroupSeq', goodsInfo['goodsGroupSeq']);
        $(rows[index]).find('td input[name="goodsCheck"]').val(goodsInfo['goodsGroupSeq']);
        $(rows[index]).find('td input[name="goodsCheck"]').prop('checked', false);
        $(rows[index]).find('td[name=key]').attr('goodsGroupSeq', goodsInfo['goodsGroupSeq']);
        let goodsGroupCodeEl = $(rows[index]).find('td[name=key]').next();
        goodsGroupCodeEl.text(goodsInfo['goodsGroupCode']);
        goodsGroupCodeEl.next().text(goodsInfo['goodsGroupName']);

        $("#goodsGroupSeqSort").val([]);
      }
      $('#checkAll').prop('checked', false);
    })
    .fail(function (data) {
      if (data.status === 400) {
        Array.from(data['responseJSON']).forEach(error => {
          $("#sortManualErrors").append(error['defaultMessage']);
        })
      }
      if (data && data.status &&
        (data.status === 403 || (data.status === 200 && data.responseText.includes('login')))
      ) {
        location.href = pkg_common.getAppComplementUrl() + '/login/'
      }
    })
}

function configShowManualSort() {
  if ($("#manualSortType").hasClass("display_none")) {
    $("#manualSortType").removeClass("display_none");
  }
  if (!$("#categoryGoodsSortType").hasClass("display_none")) {
    $("#categoryGoodsSortType").addClass("display_none");
  }
  hideDragGoods(true);
  disableSort();
}

function configHideManualSort() {
  if (!$("#manualSortType").hasClass("display_none")) {
    $("#manualSortType").addClass("display_none");
  }
  if ($("#categoryGoodsSortType").hasClass("display_none")) {
    $("#categoryGoodsSortType").removeClass("display_none");
  }
  showDragGoods(true);
  enableSort();
}

function doProductSearch() {
  $.ajax({
    type: "POST",
    url: pkg_common.getAppComplementUrl() + "/goods/category/doProductsSearchAjax",
    beforeSend: function (xhr) {
      xhr.setRequestHeader(header, token);
    },
    processData: false,
    contentType: false,
    dataType: "json",
    data: getConditionSearch(),
    timeout: 30000
  }).done(function (data) {
    setResultDataGoodsSearch(data);
  })
    .fail(function (data) {
      if (data.responseJSON && data.responseJSON.length > 0) {
        bindingErrorMessage(data.responseJSON)
      }
    })
}

function getConditionSearch() {
  let form = new FormData();
  form.append("searchKeyword", $("#searchKeywordDialog").val());
  form.append("limit", -1);
  return form
}

// 検索結果描画
function setResultDataGoodsSearch(data) {
  if (data) {
    let noResultItem = '<div class="l-inner_wrap" >\n' +
      '                   <div class="col wrap_flex">\n' +
      '                       <h4 class="c-pagetitle_h4 mr10">検索結果</h4>\n' +
      '                       <span class="as_end">全0件が該当しました</span>\n' +
      '                  </div>\n' +
      '               </div> '
    $("#products_search_result").html('');
    $("#product_search_dialog").dialog({
      buttons: [],
    })
    $("#products_search_result").append(noResultItem);
  }
  if (typeof data !== 'undefined' && data.length > 0) {
    let tableResultItems = ' <table id="result_table" class="c-tbl large60 tbl_relation_item">\n' +
      '    <thead>\n' +
      '    <tr>\n' +
      '        <th class="check w60px"></th>\n' +
      '        <th class="w60px">#' +
      '        <th class="w155px">商品管理番号' +
      '        <th class="w350px">商品名' +
      '        <th class="w155px">フロント表示状態' +
      '    </tr>\n' +
      '    </thead>\n' +
      '    <tbody id="items_row_products">\n' +
      '    \n' +
      '    </tbody>\n' +
      '</table> '
    $("#products_search_result").html('');
    $("#products_search_result").append(tableResultItems);
    data.forEach((val, index) => {

      let indexPlus = ++index;
      let goodsGroupCode;
      let goodsGroupName;
      let whatsnewDate;
      let goodsPrice;
      let goodsGroupSeq;
      let popularityCount;

      if (val.goodsGroupSubResponse != null) {
        goodsGroupCode = val.goodsGroupSubResponse.goodsGroupCode ? val.goodsGroupSubResponse.goodsGroupCode : ''
        goodsGroupName = val.goodsGroupSubResponse.goodsGroupName ? val.goodsGroupSubResponse.goodsGroupName : ''
        whatsnewDate = val.goodsGroupSubResponse.whatsnewDate ? val.goodsGroupSubResponse.whatsnewDate : ''
        goodsPrice = (val.goodsGroupSubResponse.goodsPrice || val.goodsGroupSubResponse.goodsPrice === 0) ? val.goodsGroupSubResponse.goodsPrice : ''
        goodsGroupSeq = val.goodsGroupSubResponse.goodsGroupSeq ? val.goodsGroupSubResponse.goodsGroupSeq : ''
      }

      if (val.goodsGroupDisplay != null) {
        popularityCount = val.goodsGroupDisplay.popularityCount
      }

      if (linkedGoodsModelItemsList.includes(goodsGroupSeq)) {
        let trData = '<tr>' +
          '<td class="check">\n' +
          '     <label class="c-form-control"><input checked onchange="onchangeCheckbox(name,this)" name="goodsModelItems[' + index + '].resultProductsCheck" value="true" type="checkbox"><i></i></label>\n' +
          '</td> ' +
          '<td class="w160px">' + indexPlus + '</td>' +
          '<td class="alphabetic">' + goodsGroupCode + '</td>' +
          '<td>' + goodsGroupName + '</td>' +
          '<td><button type="button" class="c-btn btn_back min_w100px">表示中</button></td>' +
          '<td style="display:none;">' + whatsnewDate + '</td>' +
          '<td style="display:none;">' + goodsPrice + '</td>' +
          '<td style="display:none;">' + popularityCount + '</td>' +
          '<td style="display:none;">' + goodsGroupSeq + '</td>' +
          '</tr>'
        $("#items_row_products").append(trData)
      } else {
        let trData = '<tr>' +
          '<td class="check">\n' +
          '     <label class="c-form-control"><input goodsGroupSeq="" onchange="onchangeCheckbox(name,this)" name="goodsModelItems[' + index + '].resultProductsCheck" value="true" type="checkbox"><i></i></label>\n' +
          '</td> ' +
          '<td class="w160px">' + indexPlus + '</td>' +
          '<td class="alphabetic">' + goodsGroupCode + '</td>' +
          '<td>' + goodsGroupName + '</td>' +
          '<td><button type="button" class="c-btn btn_back min_w100px">表示中</button></td>' +
          '<td style="display:none;">' + whatsnewDate + '</td>' +
          '<td style="display:none;">' + goodsPrice + '</td>' +
          '<td style="display:none;">' + popularityCount + '</td>' +
          '<td style="display:none;">' + goodsGroupSeq + '</td>' +
          '</tr>'
        $("#items_row_products").append(trData)
      }
    })

    $("#product_search_dialog").dialog({
      buttons: [
        {
          text: "確定",
          click: function () {
            let list = linkedGoodsModelItems
            let optionSort = $("#categoryGoodsSortCondition option:selected").val()

            $.ajax({
              type: "POST",
              url: pkg_common.getAppComplementUrl() + "/goods/category/doProductsAddAjax",
              beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
              },
              contentType: "application/json; charset=utf-8",
              processData: false,
              dataType: "json",
              data: JSON.stringify({optionSort : optionSort, goodsItems : list }),
              timeout: 30000
            })
              .done(function (data) {
                $("#product_search_dialog").dialog("close");
                $("#items_row_products").html('');
                setResultDataGoodsModelItems(data);
              })
              .fail(function (data) {
                if (data && data.status && data.status === 403) {
                  location.href = '/admin/login/'
                }
              })
          }
        }
      ]
    });
  }
}

function onFilterDropDownProductSetting() {
  setTimeout(filterListDropDown(), 500);
}

function filterListDropDown() {
  timeout(doProductSearch(), 30000)
}

function onchangeCheckbox(name, e) {
  let listOfEl = Array.from($(e).parent().parent().siblings());
  let value = $(e).is(":checked")
  /* List element: 0.index 1.goodsGroupCode 2.goodsGroupName 3.displayStatus 4.whatsnewDate 5. goodsPrice 6.popularityCount 7.goodsGroupSeq*/
  if (value) {
    linkedGoodsModelItemsList.push(+listOfEl[7].innerHTML)
    linkedGoodsModelItems.push({
      categorySeq: null,
      goodsGroupSeq: parseInt(listOfEl[7].innerHTML),
      manualOrderDisplay: null,
      goodsGroupCode: listOfEl[1].innerHTML,
      goodsGroupName: listOfEl[2].innerHTML,
      whatsnewDate: listOfEl[4].innerHTML,
      goodsPrice: parseInt(listOfEl[5].innerHTML),
      popularityCount: listOfEl[6].innerHTML,
      displayStatus: true,
      categoryId: null
    })
  } else {
    for ( let i = 0; i < linkedGoodsModelItems.length; i++) {
      if (linkedGoodsModelItems[i].goodsGroupSeq === +listOfEl[7].innerHTML) {
        linkedGoodsModelItemsList.splice(i, 1)
        linkedGoodsModelItems.splice(i, 1);
      }
    }
  }
}

function setResultDataGoodsModelItems(data) {
  if (data && data.length === 0) {
    let noResultItem = '<tr><div class="l-inner_wrap" >\n' +
      '                       <span class="as_end">このコレクションには商品がありません。\n' +'</span></br>\n' +
      '                       <span class="as_end">検索または閲覧して商品を追加してください。</span>\n' +
      '               </div></tr> '
    $("#items_row").html('');
    $("#items_row").append(noResultItem);
  }
  if (typeof data !== 'undefined' && data.length > 0) {

    let classDrag = $("#categoryGoodsSortCondition option:selected").val() == '5' ? 'drag w40px' : 'drag w40px display_none'
    let classCheck = $("#categoryGoodsSortCondition option:selected").val() == '5' ? 'check w40px' : 'check w40px display_none'
    let categoryType = categoryInputModel.categoryType ? categoryInputModel.categoryType : ''
    let dataDisplayStatus = categoryInputModel.displayStatus ? categoryInputModel.displayStatus : false

    let btnDeleteGoods = '';
    if (categoryType === '0') {
      btnDeleteGoods = '<button type="button" class="c-btn-icon" onclick="deleteGoods(this)"><i class="material-icons clear"></i></button>'
    }

    $("#items_row").empty();
    data.forEach((val, index) => {
      let indexPlus = ++index;
      let goodsGroupCode;
      let goodsGroupName;
      let goodsGroupSeq;

      if (val != null) {
        goodsGroupCode = val.goodsGroupCode ? val.goodsGroupCode : ''
        goodsGroupName = val.goodsGroupName ? val.goodsGroupName : ''
        goodsGroupSeq = val.goodsGroupSeq ? val.goodsGroupSeq : ''
      }

      let trDataDisplayStatus;
      if (dataDisplayStatus) {
        trDataDisplayStatus = '<button type="button" class="c-btn btn_back min_w100px">表示中</button>'
      } else {
        trDataDisplayStatus = '<button type="button" class="c-btn btn_del min_w100px">非表示</button>'
      }

      let trData = '<tr goodsGroupSeq="'+ goodsGroupSeq +'">' +
        '<td name="manual" class="'+ classDrag +'"><i class="material-icons menu"></i></td>' +
        '<td name="manual" class="'+ classCheck +'">\n' +
        '     <label class="c-form-control"><input type="checkbox" name="goodsCheck" onclick="doCheckGoods(this)" value='+ goodsGroupSeq +'><i></i></label>\n' +
        '</td> ' +
        '<td goodsGroupSeq="'+ goodsGroupSeq +'" name="key" class="number text-center">' + indexPlus + '</td>' +
        '<td class="alphabetic">' + goodsGroupCode + '</td>' +
        '<td>' + goodsGroupName + '</td>' +
        '<td>' + trDataDisplayStatus + '</td>' +
        '<td>'+ btnDeleteGoods +'</td>' +
        '</tr>'

      $("#items_row").append(trData);

      // Update display description sort manual
      $('#goodsSortManualChecked').text('0個の商品の移動先：');
      $('#goodsSortManualTotal').text(' / ' + $('input:checkbox[name=goodsCheck]').length);
    })
  }
}

// 1.1.4
// カテゴリー種別 change
$(function () {

  // フォームリセット
  function resetForm() {
    $("#products_search_result").html('');
    $("#globalMesDiv").html('');
    $("#globalMesDiv").attr('style', 'display:none');
    $(".ui-dialog-buttonpane").remove();
    $("#searchKeywordDialog").val('');
    $("#searchKeyword").val('');
  }

  // relation condition vs operator
  let conditionRow = $('div[id^="conditionRow"]');
  for (let i = 0; i < conditionRow.length; i++) {
    onChangeInput($(conditionRow[i]).find('select')[0]);
  }

  // check display category condition setting for category type AUTO
  $('input[type=radio][name=categoryType]').change(function () {
      if (this.value === '0') {
          hideCategoryConditionSettings();
      } else {
          showCategoryConditionSettings();
      }
  });

  // init sort goods
  initSortable();
  if (goodsSortDisplay != '5') {
    disableSort();
  }

  // check sort manual
  $("#categoryGoodsSortCondition").change(function () {
    if (this.value === SORT_MANUAL) {
      showDragGoods();
      showCheckGoods();
      enableSort();
    } else {
      hideDragGoods();
      hideCheckGoods();
      disableSort();
      // call Ajax
      let params = {
        optionSort: $("#categoryGoodsSortCondition option:selected").val(),
        goodsGroupChecked: $('#goodsView tbody').sortable('toArray', {attribute: 'goodsGroupSeq'}).join()
      }
      getGoodsSortAjax(params);
    }
  });

  // check sort manual position specification
  $("#categoryGoodsSortManualCondition").change(function () {
    if (this.value === '2') {
      document.getElementById("positionSpecification").disabled = false;
    } else {
      document.getElementById("positionSpecification").disabled = true;
    }
  })

  // for sort manual position
  let totalCheckbox = $('input:checkbox[name=goodsCheck]').length;
  $('#goodsSortManualChecked').text('0個の商品の移動先：');
  $('#goodsSortManualTotal').text(' / ' + totalCheckbox);

  // event add condition
  $("#btnAddCondition").on("click", function () {
    doAddCondition();
  })

  // event order manual
  $('#btnOrderManual').on("click", function () {
    $("input:checkbox[name=goodsCheck]:checked").each(function(){
      goodsGroupChecked.push($(this).val());
    });
    let params = {
      optionSort: $("#categoryGoodsSortCondition option:selected").val(),
      optionSortManual: $('#categoryGoodsSortManualCondition option:selected').val(),
      position: $('#positionSpecification').val(),
      goodsGroupChecked: goodsGroupChecked.join()
    }
    $("#sortManualErrors").html('');
    getGoodsSortAjax(params).then(() => {
      // reset display to sort manual
      configHideManualSort();
    });
  })

  $("#product_search_dialog_btn").click(function () {
    $("#product_search_dialog").dialog({
      modal: true,
      width: 950,
      title: "商品編集",
      position: {
        of: $("#searchKeyword"),
        my: 'left',
        at: 'center',
      },
      buttons: [],
      close: function (event, ui) {
        resetForm();
      }
    });
    let searchKeywordVal = $('#searchKeyword').val()
    if (searchKeywordVal) {
      $("#searchKeywordDialog").val(searchKeywordVal)
    }

    doProductSearch();

    $("#doSearch").click(function () {
      doProductSearch();
    })
  })

  $("#searchKeyword").change(function () {
    $("#product_search_dialog").dialog({
      modal: true,
      width: 950,
      title: "商品編集",
      position: {
        of: $("#searchKeyword"),
        my: 'left',
        at: 'center',
      },
      buttons: [],
      close: function (event, ui) {
        resetForm();
      }
    });
    let searchKeywordVal = $('#searchKeyword').val()
    $("#searchKeywordDialog").val(searchKeywordVal)

    doProductSearch();
  })

  $('#checkAll').on('change', function () {
    $('input:checkbox[name^="goodsCheck"]').prop('checked', $(this).prop('checked'));
    doCheckGoods();
  });
})
