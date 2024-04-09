$(document).ready(function(){

// サムネイル （メインスライダーより上に記載する）
const sliderThumbnail = new Swiper(".c-goodsviewer .c-goodsviewer__thumbnail-container", {
  slidesPerView: 4, // サムネイルの枚数　※必須（未設定だとSPスライダー見切れ画像を選択された場合、表示が崩れる）
  watchSlidesProgress: true, //スライダーの動きにスライドを追従させる
});

// メインスライダー
const swiper = new Swiper(".c-goodsviewer .c-goodsviewer__main-container", {
  loop: true, 
  navigation: {　
    nextEl: ".c-goodsviewer .swiper-button-next",
    prevEl: ".c-goodsviewer .swiper-button-prev",
  },
  pagination: {
    el: '.swiper-pagination',
    clickable: true
  },
  thumbs: {
    swiper: sliderThumbnail,
  },  
});

// スライダーが1枚の場合、ページネーションのエリアを狭く
if ($(".c-goodsviewer__main .swiper-pagination").hasClass('swiper-pagination-lock')) { 
  $(".p-product-detail__base").addClass("is-notPagenation");
}

// スライダーがnoimageの場合、余白を詰める
if ( $('.c-goodsviewer .noimage').length ) { 
  $(".p-product-detail__base").addClass("is-noimage");
}
  
});


// // 【モーダル内】サムネイル （メインスライダーより上に記載する）
// const sliderModalThumbnail = new Swiper(".c-goodsviewerModal .c-goodsviewerModal__thumbnail-container", {
//   slidesPerView: 10, // サムネイルの枚数
//   watchSlidesProgress: true, //スライダーの動きにスライドを追従させる
//   breakpoints: {
//     // 600px以上の場合
//     600: {
//       direction: "vertical", // スライダーの方向を縦に
//     },
//   },
// });

// // 【モーダル内】メインスライダー
// const swiperModal = new Swiper(".c-goodsviewerModal .c-goodsviewerModal__main-container", {
//   loop: true, // ループ
//   // 前後の矢印
//   navigation: {
//     nextEl: ".c-goodsviewerModal .swiper-button-next",
//     prevEl: ".c-goodsviewerModal .swiper-button-prev",
//   },
//   //サムネイル
//   thumbs: {
//     swiper: sliderModalThumbnail,
//   },
// });




// 定数定義
// const slide = document.querySelectorAll('.c-goodsviewer__main .swiper-slide');
// const modalClose = document.querySelectorAll('.js-close-button');
// const modal = document.querySelector('.js-sliderModal');
// const modalHTML = document.querySelector('html');

// メインスライダーの画像クリック時の処理
// slide.forEach(function (e) {
//   e.addEventListener('click', function () { 
//     let modalIndex = e.dataset.slideIndex;    // クリックしたらスライドのdata属性を取得
//     // console.log(modalIndex);
//     swiperModal.slideTo(modalIndex);          // モーダルのスライドをクリックされた番号のスライドにスライドする
//     modal.classList.add('is-active');         // モーダルにis-activeクラスを追加
//     modalHTML.style.overflowY = "hidden";
//   })
// })

// モーダルを閉じる処理
// modalClose.forEach(function (e) { 
//   e.addEventListener('click', function () { 
//     modal.classList.remove('is-active');
//     modalHTML.style.overflowY = "scroll";
//     slideIndex = "";

//     let modalActiveIndex = document.querySelector('.c-goodsviewerModal__main .swiper-slide-active').dataset.slideIndex;
//     // console.log(modalActiveIndex);
//     swiper.slideTo(modalActiveIndex); 
//   })
// })
