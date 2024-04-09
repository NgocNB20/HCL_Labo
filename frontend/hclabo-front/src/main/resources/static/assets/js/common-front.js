$(window).on('load', function () {

  /* -----------------------------------------
  追従ヘッダー
  ------------------------------------------*/
  let headerCommon = $('.l-header__common').outerHeight();
  let freeAreaHeader1 = $('.free-area-header1').outerHeight();

  $(window).scroll(function () {
    if (!$('.free-area-header1').length) {

      if ($('html').hasClass('is-scroll')) {
        $('.l-header__common').css({'position':'fixed','box-shadow':'0 3px 3px 0 rgba(0, 0, 0, .1)'});
        $('.l-header__global').css('padding-top', headerCommon);
      } else {
        $('.l-header__common').css({'position':'relative','box-shadow':'none'});
        $('.l-header__global').css('padding-top', '0');
      }

    } else {

      let headerScroll = $(window).scrollTop();

      if (headerScroll > freeAreaHeader1) {
        $('.l-header__common').css({'position':'fixed','box-shadow':'0 3px 3px 0 rgba(0, 0, 0, .1)'});
        $('.l-header__global').css('padding-top', headerCommon);
      } else {
        $('.l-header__common').css({'position':'relative','box-shadow':'none'});
        $('.l-header__global').css('padding-top', '0');
      }
    }
  });
});


$(function () {

  const headerCommon = $('.l-header__common').outerHeight();

  /* -----------------------------------------
  グローバル ネストメニュー
  ------------------------------------------*/
  $('.l-header__global-has').on({
    'mouseenter': function () { 
      $(this).children('.l-header__global-menu').addClass('is-open');
    },
    'mouseleave': function () { 
      $(this).children('.l-header__global-menu').removeClass('is-open');
    }
  });


  /* -----------------------------------------
  アコーディオン(categoryjson以外)
  ------------------------------------------*/
  $('.js-accordion-arrow').on('click', function () {
    $(this).next().slideToggle(300);
    $(this).toggleClass('is-opened', 300);
  });

  // TOPのみお知らせを開いた状態に
  $('.p-top .l-header__member-information dd').css('display', 'block');
  $('.p-top .l-header__member-information dt').addClass('is-defaultOpen,is-opened');
  $('.p-top .l-header__member-information dt').on('click', function () { 
    $(this).removeClass('is-defaultOpen');
  })

  /* -----------------------------------------
  もっと見る
  ------------------------------------------*/
  // p-top__banner用
  $('.js-limit').each(function () {
    let limitNum = $(this).data('limit');
    let target = $(this).find('.swiper-wrapper .swiper-slide');
    let moreButton = $(this).siblings('.p-top__banner-more').children('.js-more');

    if (window.innerWidth < 600) {
      $(target).slice(limitNum).hide();
    }

    $(moreButton).on('click', function () {
      $(this).parents('.p-top__banner').find('.swiper-slide').slice(limitNum).slideDown('fast');
      $(this).parent('.p-top__banner-more').hide();
    })

  });

  // p-top__news用 または その他
  $('.js-limit').each(function () {
    let limitNum = $(this).data('limit');
    let target = $(this).find('.js-limit-unit');
    let moreButton = $(this).siblings('.js-more-wrap').children('.js-more');

    $(target).slice(limitNum).hide();

    $(moreButton).on('click', function () {
      $(this).parent('.js-more-wrap').siblings('.js-limit').find('.js-limit-unit').slice(limitNum).slideDown('fast');
      $(this).parent('.js-more-wrap').hide();
    })

  });

  /* -----------------------------------------
  モーダル処理
  ------------------------------------------*/
  const modal_close = $(".js-modal-close");

  $('.js-modal').each(function () {
    $(this).on('click', function (e) {
      e.preventDefault();
      let target = $(this).data('target');
      let modal = document.getElementById(target);
      let scroll = $(window).scrollTop();
      $(modal).fadeIn();
      $('body').addClass('is-modal-opened');
    })
  });

  modal_close.on('click', function (e) {
    e.preventDefault();
    $('.c-modal').fadeOut( function () { 
      $('body').removeClass('is-modal-opened');
    });
  });

  $('.c-modal__contents').on('click', function (e) {
    e.stopPropagation();
  });

  /* -----------------------------------------
  ページ内アンカー
  ------------------------------------------*/
  $('.js-anchor[href^="#"]').click(function(){
    // スクロールの速度
    var speed = 600; // ミリ秒
    // アンカーの値取得
    var href= $(this).attr("href");
    // 移動先を取得
    var target = $(href == "#" || href == "" ? 'html' : href);
    // 移動先を調整
    var position = target.offset().top - headerCommon;
    // スムーススクロール
    $('body,html').animate({scrollTop:position}, speed, 'swing');
    return false;
  });
  

  /* -----------------------------------------
  // スクロールのドラッグ有効化
  ------------------------------------------*/
  jQuery.prototype.mousedragscrollable = function () {
    let target;
    $(this).each(function (i, e) {
      $(e).mousedown(function (event) {
        event.preventDefault();
        target = $(e);
        $(e).data({
          down: true,
          move: false,
          x: event.clientX,
          y: event.clientY,
          scrollleft: $(e).scrollLeft(),
          scrolltop: $(e).scrollTop(),
        });
        return false;
      });
      $(e).click(function (event) {
        if ($(e).data("move")) {
          return false;
        }
      });
    });
    $(document)
      .mousemove(function (event) {
        if ($(target).data("down")) {
          event.preventDefault();
          let move_x = $(target).data("x") - event.clientX;
          let move_y = $(target).data("y") - event.clientY;
          if (move_x !== 0 || move_y !== 0) {
            $(target).data("move", true);
          } else {
            return;
          }
          $(target).scrollLeft($(target).data("scrollleft") + move_x);
          $(target).scrollTop($(target).data("scrolltop") + move_y);
          return false;
        }
      })
      .mouseup(function (event) {
        $(target).data("down", false);
        return false;
      });
  };
  $(".simplebar-content-wrapper").mousedragscrollable();


  /* -----------------------------------------
  一覧ページ アイコンが無い場合
  ------------------------------------------*/
  $('.c-product__item-icon').each(function () {
    
    if ( !($(this).find('span').length)) {
      $(this).css('margin', '0');
      } 
  })


  /* -----------------------------------------
  一覧ページ 並び順 オプション表示
  ------------------------------------------*/
  // $('.c-sortbox--title').each(function () { 
  //   $(this).on('click', function () {
  //     $(this).siblings('.c-sortbox--list').toggleClass('js-sort-open');
  
  //     var otherList = $(this).parent('.c-sortbox__wrap').siblings('.c-sortbox__wrap').children('.c-sortbox--list');  
  //     if (otherList.hasClass('js-sort-open')) { 
  //       otherList.removeClass('js-sort-open');
  //     }
  //   });
  // })

  // $('html , body').on('click', function (e) {
  //   if (!$(e.target).closest('.c-sortbox--title').length) {
  //     $('.c-sortbox--list').removeClass('js-sort-open');
  //   }
  // });


  /* -----------------------------------------
  ポップアップ
  ------------------------------------------*/
  $('.js-pop-linkpay').click(function (e) {
    e.preventDefault();

    let url = '/guide/pop_linkpay';
    let winId = 'linkpay';

    window.open(url, winId, 'width=800,height=500,top=100,left=100,toolbar=yes,menubar=yes,scrollbars=yes');
    
  });

  $('.js-pop-close').click(function () { 
    window.open('','_self').close();
  })



  /* -----------------------------------------
  商品詳細 数量カウンター
  ------------------------------------------*/
  // ボタンクリック時
  // $('.js-count-quantity button').on('click', function (e) {
  //   e.preventDefault();

  //   let fildName = $(this).attr('data-field');
  //   let type = $(this).attr('data-type');
  //   let $input = $('input[name="' + fildName + '"]');
  //   let carrentVal = parseInt($input.val());

  //   if (!isNaN(carrentVal)) {         // 入力値が数値の場合
  //     if (type == 'minus') {           // マイナスボタンの場合
  //       if (carrentVal > $input.attr('min')) {   // ∟ 入力値 > 最小値
  //         $input.val(carrentVal - 1).change();
  //       }
  //       if (parseInt($input.val()) == $input.attr('min')) {    // ∟ 入力値 = 最小値
  //         $(this).attr('disabled', true);
  //       }
  //     } else if (type == 'plus') {
  //       if (carrentVal < $input.attr('max')) {
  //         $input.val(carrentVal + 1).change();
  //       }
  //       if (parseInt($input.val()) == $input.attr('max')) {
  //         $(this).attr('disabled', true);
  //       }
  //     }
  //   } else {
  //     $input.val(0);
  //   }
  // });

  // $('.input_result').on('focusin', function () {
  //   $(this).data('oldValue', $(this).val());
  // });
  // $('.input_result').on('change', function () {
  //   let minValue = parseInt($(this).attr('min'));
  //   let maxValue = parseInt($(this).attr('max'));
  //   let valueCurrent = parseInt($(this).val());

  //   if (valueCurrent >= minValue) {
  //     $('.minus_button').removeAttr('disabled');
  //   } else { 
  //     $(this).val($(this).data('oldValue'));
  //   }
  //   if (valueCurrent <= maxValue) {
  //     $('.plus_button').removeAttr('disabled');
  //   } else { 
  //     $(this).val($(this).data('oldValue'));
  //   }
  // });


});






/* 
  ブレイクポイント切り替えポイントで再読み込み
*/

const breakPoint = 600;
let resizeFlag;

window.addEventListener('load',()=>{
  if( breakPoint < window.innerWidth){
    resizeFlag = false;
  }else{
    resizeFlag = true;
  }
  resizeWindow();
},false);

const resizeWindow = () =>{
  window.addEventListener('resize',()=>{
    if( breakPoint < window.innerWidth && resizeFlag){
      window.location.reload();
      resizeFlag = false;
    } 
    else if ( breakPoint >= window.innerWidth && !(resizeFlag)) {
      window.location.reload();
      resizeFlag = true;
    }
  },false);
}
 



