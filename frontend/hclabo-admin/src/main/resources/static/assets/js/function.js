let hexValue = $('.js-icon-color').val();
function onSubmitHandler(){
    $('.js-icon-color').val(hexValue);
    return errorCheckOnclickConfirmButton();
}
$(function(){
    //開閉メニュー
    $('.l-side-cat_parent li .submenu').hide();
    $(window).on('load', function(){
        if(!$('.l-mainwrap').hasClass('close')){
            $('.l-side-cat_parent li.active .acdon').addClass('active');
            $('.l-side-cat_parent li .acdon.active').next('.submenu').show();
        } else{
            $('.l-side-cat_parent li.active .acdon').addClass('active');
        }
    });
    $('.l-side-cat_parent .acdon').click(function(){
        if(!$('.l-mainwrap').hasClass('close')){
            if($(this).hasClass('active')){
                $(this).removeClass('active');
                $(this).next('.submenu').slideUp(450);
            } else{
                $(this).addClass('active');
                $(this).next('.submenu').slideDown(450);
            };
        };
    });
    //開閉ステータス
    var sideNavState = '';
    var listCookies = document.cookie.split(';');
    var lastCookie = listCookies[listCookies.length - 1]
    if (lastCookie.includes('open')) {
        sideNavState = '';
    } else if (lastCookie.includes('close')) {
        sideNavState = 'close';
    }
    $('.l-mainwrap').addClass(sideNavState);
    //サイドメニュー開閉
    $('.js_open').click(function(){
        $('.l-mainwrap').toggleClass('close');
        if(!$('.l-mainwrap').hasClass('close')){
            //cookieにもopenを書き込む
            document.cookie = "sidenav-state=open;path=/; max-age=86400;"//86400秒＝1日有効
            $('.l-side-cat_parent li .acdon.active').next('.submenu').show();
        } else {
            //cookieにもcloseを書き込む
            document.cookie = "sidenav-state=close;path=/; max-age=86400;"//86400秒＝1日有効
            $('.l-side-cat_parent li .submenu').hide();
        };
    });
    //要素がウインドウサイズより小さい場合の処理
    function pageheight() {
        var pagew = window.innerWidth;
        var windowsHeight01 = window.innerHeight;
        if(pagew <= 1600){
            $('.l-mainwrap').css({'min-height': windowsHeight01-17 + 'px'});
        } else{
            $('.l-mainwrap').css({'min-height': windowsHeight01 + 'px'});
        };
    };
    //追尾ボタンの処理
    function floatArea() {
        var scrollTop = $(window).scrollTop();
        var windowsHeight = window.innerHeight;
        var scroll = scrollTop + windowsHeight;
        let footerEl = $('.l-footer');
        var footerOffsetTop = 0;
        if (footerEl.length > 0) {
            footerOffsetTop = footerEl.offset().top;
        }
        var footerHeight = $('.l-footer').outerHeight();
        if (  scroll < footerOffsetTop ) {
            $('.l-pagebtn-nav').css('left', -$(window).scrollLeft());
            $('.l-pagebtn-nav').css({'bottom': '0','position':'fixed'});
        } else {
            $('.l-pagebtn-nav').css({'bottom': footerHeight + 'px','position':'absolute','left': '0'});
        }
    };
    $(window).on('load', function(){
        if($('.l-pagebtn-nav').length){
            $('body').addClass('fixd');
        } else{
            $('body').removeClass('fixd');
        };
    });
    $(window).on('load resize', function(){
        pageheight();
        setTimeout(function(){
            floatArea();
        }, 0);
    });
    $(window).scroll(function() {
        floatArea();
    });

    // スムーズスクロール
    const header = $('.l-content-header');
    $('a[href^="#"]').on('click', function() {
      const gap = header.outerHeight();
      const speed = 500;
      const href = $(this).attr("href");
      const target = $(href == "#" || href == "" ? "html" : href);
      const position = target.offset().top - gap;
      $("html, body").animate({ scrollTop: position }, speed, "swing");
      return false;
    });

    //開閉01
    $('.l-article_title:not(.no_slide)').click(function(){
      $(this).next('.l-article_inner').slideToggle();
      $(this).toggleClass('close');
    });

    ////開閉02
    $('.c-detail-warp .c-btn_add').next('.c-detail_inner:not(.active)').hide();
    $('.c-detail-warp .c-btn_add').click(function(){
      $(this).next('.c-detail_inner').slideToggle();
      $(this).next('.c-detail_inner').toggleClass('active');
      $(this).toggleClass('close');
    });

    //データピッカー
    $(document).ready(function($) {
        $( ".c-datepicker input" ).datepicker({
            changeMonth: true,
            changeYear: true,
            showOtherMonths: true,
            selectOtherMonths: true,
        });
    });
    $.datepicker.regional['ja'] = {
        currentText: '今日',
        monthNames: ['1','2','3','4','5','6','7','8','9','10','11','12'],
        monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
        dayNames: ['日曜日','月曜日','火曜日','水曜日','木曜日','金曜日','土曜日'],
        dayNamesShort: ['日','月','火','水','木','金','土'],
        dayNamesMin: ['日','月','火','水','木','金','土'],
        weekHeader: '週',
        dateFormat: 'yy/mm/dd',
        firstDay: 0,
        isRTL: false,
        showMonthAfterYear: true,
        yearSuffix: '年'};
    $.datepicker.setDefaults($.datepicker.regional['ja']);

    // タブ
    $('.tab_sty .tab_menu li:first').addClass('active');
    $('.tab_menu li').on('click', function(){
        if($(this).not('active')){
          // タブメニュー
          $(this).addClass('active').siblings('li').removeClass('active');
          // タブの中身
          var index = $('.tab_menu li').index(this);
          $('.tab_box > div').eq(index).addClass('active').siblings('div').removeClass('active');
        }
    });

    //住所検索
    $('.ajaxzip3').on('click', function(){
        AjaxZip3.zip2addr('zip','','pref','addr1','addr2');
        AjaxZip3.onSuccess = function() {
            $('.addr2').focus();
        };
        AjaxZip3.onFailure = function() {
            alert('郵便番号に該当する住所が見つかりません');
        };
        return false;
    });
    $('.ajaxzip3_02').on('click', function(){
        AjaxZip3.zip2addr('zip_02','','pref_02','addr1_02','addr2_02');
        AjaxZip3.onSuccess = function() {
            $('.addr2_02').focus();
        };
        AjaxZip3.onFailure = function() {
            alert('郵便番号に該当する住所が見つかりません');
        };
        return false;
    });
    //dialog関連
    $(document).on("click", ".ui-widget-overlay", function(){
        $(this).prev().find(".ui-dialog-content").dialog("close");
    });

    //アイコン選択
    $('.c-icon-list.edit .c-item-status').click(function(){
        $(this).toggleClass('off');
    });

    //商品説明
    $('.product_description .edit:nth-child(n + ' + (3 + 1) + ')').addClass('is-hidden').hide();
    $('.product_description .more .c-btn_add').on('click', function() {
        $('.product_description .edit.is-hidden').slice(0, 1).removeClass('is-hidden').show();
        if ($('.product_description .edit.is-hidden').length == 0) {
            $(this).parents('.more').fadeOut();
        }
    });
    //商品説明 商品詳細
    $('.product_description_details dl > div:nth-child(n + ' + (3 + 1) + ')').addClass('is-hidden').hide();
    $('.product_description_details .more .c-btn_add').on('click', function() {
        $('.product_description_details dl > div.is-hidden').slice(0, 8).removeClass('is-hidden').show();
            if ($('.product_description_details dl > div.is-hidden').length == 0) {
            $(this).parents('.more').fadeOut();
        }
    });

    //受注連携設定
    $('.order_coordination .edit:nth-child(n + ' + (2 + 1) + ')').addClass('is-hidden').hide();
    $('.order_coordination .more .c-btn_add').on('click', function() {
        $('.order_coordination .edit.is-hidden').slice(0, 1).removeClass('is-hidden').show();
        if ($('.order_coordination .edit.is-hidden').length == 0) {
            $(this).parents('.more').fadeOut();
        }
    });
    //受注連携設定 商品詳細
    $('.order_coordination_details dl > div:nth-child(n + ' + (2 + 1) + ')').addClass('is-hidden').hide();
    $('.order_coordination_details .more .c-btn_add').on('click', function() {
        $('.order_coordination_details dl > div.is-hidden').slice(0, 8).removeClass('is-hidden').show();
            if ($('.order_coordination_details dl > div.is-hidden').length == 0) {
            $(this).parents('.more').fadeOut();
        }
    });

    //検索結果
    $('#checkall').on('change', function () {
        if ($(this).prop('checked')) {
            $('input:checkbox[name="result01"]').prop('checked', true);
            var cnt = $('#search_result tbody input:checkbox:checked').length;
            $('.count_menu').css({'display': 'flex'});
            $('.resultcount').text( cnt  + '件 選択済' );
        } else {
            $('input:checkbox[name="result01"]').prop('checked', false);
            $('.count_menu').hide();
        }
    });
    $('input:checkbox[name="result01"]').change(function() {
        $('.count_menu').css({'display': 'flex'});
        var cnt = $('#search_result tbody input:checkbox:checked').length;
        $('.resultcount').text( cnt + '件 選択済' );
        if (cnt < 1) {
            $('.count_menu').hide();
        }
    }).trigger('change');

    //ニュース登録更新
    $('input[name="news"]').on('change', function() {
        var parentsDd = $(this).parents('dt').siblings('dd');
        var input_radio_value = $('.c-form-control input[name="news"]:checked').val();
        if(input_radio_value === 'url'){
            parentsDd.find('input').prop('disabled', false);
            $('.news_details').find('input').prop('disabled', true);
        } else if(input_radio_value === 'details'){
            parentsDd.find('input').prop('disabled', false);
            $('.news_url').find('input').prop('disabled', true);
        };
    });

    //フリーエリア検索
    $('input[name="freearea01"]').on('change', function() {
        var input_radio_value = $('.c-form-control input[name="freearea01"]:checked').val();
        if(input_radio_value === 'specification'){
            $(this).parents('li.wrap_flex_center').find('input[type="text"]').prop('disabled', false);
        } else {
            $('.specified_time').find('input[type="text"]').prop('disabled', true);
        };
    });

    //カテゴリタグ
    $('.tbl_cat_list .cat01 .c-open-btn').on('click', function(){
        $(this).toggleClass('close');
        $('.tbl_cat_list .cat01_sub').slideToggle();
    });
    $('.tbl_cat_list .cat02 .c-open-btn').on('click', function(){
        $(this).toggleClass('close');
        $('.tbl_cat_list .cat02_sub').slideToggle();
    });
    $('.tbl_cat_list .cat03 .c-open-btn').on('click', function(){
        $(this).toggleClass('close');
        $('.tbl_cat_list .cat03_sub').slideToggle();
    });
    $('.tbl_cat_list tbody tr:nth-child(2n)').addClass('stripe');
    hexValue = $('.js-icon-color').val();
    //アイコン登録用カラーチップ
    if ($('.js-icon-color').length) {
        $('.js-icon-color').minicolors({
            swatches: ['#ef9394', '#eebd8e', '#89dbb6', '#9eb9e6', '#fcef8a', '#bfa998', '#8e8e8e', '#e31616', '#e05b09', '#00b562', '#2f6bcc', '#ffe200', '#77481f', '#000'],
            defaultValue: '',
            value:'',
            change: function (hex) {
                // Check colorCode entered
                if (hex && !/^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$/i.test(hex)) {
                    // Invalid colorCode, set default color
                    $(this).minicolors('value', '');
                    $(this).val(hex);
                }
            },
            hide: function () {
                let hex = hexValue
                // Check colorCode entered
                if (hex && !/^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$/i.test(hex)) {
                    // Invalid colorCode, set default color
                    $(this).minicolors('value', '');
                    $(this).val(hex);
                }
            }
        });
        if (hexValue && !/^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$/i.test(hexValue)) {
            // Invalid colorCode, set default color
            $('.js-icon-color').minicolors('value', '');
            $('.js-icon-color').val(hexValue);
        }
    }
    //アイコンカラーチップ
    $('.js-icon-color-btn').each(function(){
        var color = $(this).val();
        $(this).css('background-color', color);
    });
    $('.js-icon-color-btn').on('click',function(){
        var color = $(this).val();
        $('.js-icon-color').val(color).trigger('keyup.minicolors');
    });
    //アイコンイメージ表示
    if($('.js-icon-sample').length) {
        var icon_text = $('.js-icon-text').val();
        if(!icon_text) {icon_text = 'サンプル';}
        var icon_bg_color = $('.js-icon-color').val();
        if (hexValue && !/^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$/i.test(hexValue)) {
            icon_bg_color = '#ffffff';
        }
        if(!icon_bg_color) {icon_bg_color = '#ffffff';}
        var icon_text_color = blackOrWhite(icon_bg_color);

        $('.js-icon-sample').text(icon_text).css({
            backgroundColor: icon_bg_color,
            color: icon_text_color,
        });
        $('.js-icon-text').on('change',function(){
            var changetext = $(this).val();
            if(!changetext) {changetext = 'サンプル';}
            $('.js-icon-sample').text(changetext);
        });
        $('.js-icon-color').on('change',function(){
            var bgcolor = $(this).val();
            hexValue = bgcolor;
            if (!/^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$/i.test(bgcolor)) {
                bgcolor = '#ffffff';
            }
            if(!bgcolor) {bgcolor = '#ffffff';}
            var textcolor = blackOrWhite(bgcolor);
            $('.js-icon-sample').css({
                backgroundColor: bgcolor,
                color: textcolor,
            });
        });
    }

    //アイコン背景色によるテキスト色変更
    if($('.js-icon-textcolor').length) {
        $('.js-icon-textcolor').each(function(){
            var bgcolor = $(this).css('background-color');
            bgcolor = rgbTo16(bgcolor);
            var textcolor = blackOrWhite (bgcolor);
            $(this).css('color',textcolor);
        });
    }
    function blackOrWhite ( hexcolor ) {
        var r = parseInt( hexcolor.substr( 1, 2 ), 16 ) ;
        var g = parseInt( hexcolor.substr( 3, 2 ), 16 ) ;
        var b = parseInt( hexcolor.substr( 5, 2 ), 16 ) ;

        return ( ( ( (r * 299) + (g * 587) + (b * 114) ) / 1000 ) < 128 ) ? "white" : "black" ;
    }

    function rgbTo16(col){
        return "#" + col.match(/\d+/g).map(function(a){return ("0" + parseInt(a).toString(16)).slice(-2)}).join("");
    }
});
