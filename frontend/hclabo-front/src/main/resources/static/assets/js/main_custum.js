'use strict'

// babel polyfill
// import '@babel/polyfill'　//★ babelの設定なので非表示のまま

// ---------------------
//  define
// ---------------------

// import EL from '/assets/js/constant/elements'
let EL = {
  HTML: document.getElementsByTagName('html')[0],
  BODY: document.getElementsByTagName('body')[0],
  HEADER: document.getElementsByTagName('header')[0],
  MAIN: document.getElementsByTagName('main')[0],
  FOOTER: document.getElementsByTagName('footer')[0],
  NAV: document.getElementsByTagName('nav')[0],
  // MAINWRAP: document.querySelector('#l-mainwrap'), //使われていない。不要かも。
  STICKY: document.querySelectorAll('.sticky')
}

// 【add comment】'/assets/js/constant/define'
const DEFINE = { 
  BREAKPOINT: 899,
  SCROLL_OFFSET_LG: -50,
  SCROLL_OFFSET_SM: -80,
  SCROLL_DURATION_LG: 900,
  SCROLL_DURATION_SM: 1200,
  SCROLL_EASING_LG: 'easeInOutQuart',
  SCROLL_EASING_SM: 'easeInOutQuart'
}

// ---------------------
// helper
// ---------------------

// import closetPolyfill from './helper/polyfillCloset' // IE11用にJS「Closet」を利用できるようにする対応なので非表示のままにする

// import hmb from './helper/hmb'
  /**
   * ハンバーガーメニューの処理を提供します
   */
  function hmb() {
    const func = {
      isActive: false,
      deviceType: getDeviceType(),

      HMB: document.querySelector('.l-hamburger'),
      HMBBG: document.querySelector('.l-hamburger__back'),
      HMBCLOSE: document.querySelector('.l-hamburger__close'),
      HMBCLOSE2: document.querySelector('.l-hamburger__close2'),

      /**
       * init
       */
      init: () => {
        if (func.HMB) {
          func.HMB.addEventListener('click', func.switchShowHide, false)
          func.HMBCLOSE.addEventListener('click', func.switchShowHide, false)
          func.HMBCLOSE2.addEventListener('click', func.switchShowHide, false)
          window.addEventListener('resize', func.resize, false)
        }
      },

      /**
       * show
       */
      show: () => {
        if (EL.HTML.classList.contains('is-search-active')) EL.HTML.classList.remove('is-search-active')
        func.isActive = true
        EL.NAV.style.visibility = ''
        EL.HTML.classList.add('is-nav-active')
      },

      /**
       * hide
       */
      hide: () => {
        func.isActive = false
        EL.HTML.classList.remove('is-nav-active')
      },

      /**
       * switchShowHide
       */
      switchShowHide: () => {
        func.isActive ? func.hide() : func.show()
      },

      /**
       * resize
       */
      // 【元】resize: debounce(150, () 
      // 【追記】debounceを利用することで、負荷対策しているが、node.js経由のプラグインなので利用しない方法に書き換え
      resize: () => {
        if (func.deviceType !== getDeviceType()) {
          func.deviceType = getDeviceType()
          func.hide()

          if (func.deviceType === 'lg') {
            EL.NAV.style.visibility = ''
          }
        }
      }

    }

    func.init()
  }

// import search from './helper/search'
  function search() {
    const func = {
      isActive: false,
      deviceType: getDeviceType(),

      SCH: document.querySelector('.js-search-btn'),
      SCHBG: document.querySelector('.l-header-search div'),

      /**
       * init
       */
      init: () => {
        if (func.SCH) {
          func.SCH.addEventListener('click', func.switchShowHide, false)
          func.SCHBG.addEventListener('click', func.switchShowHide, false)
          window.addEventListener('resize', func.resize, false)
        }
      },

      /**
       * show
       */
      show: () => {
        func.isActive = true
        EL.NAV.style.visibility = ''
        EL.HTML.classList.add('is-search-active')
      },

      /**
       * hide
       */
      hide: () => {
        func.isActive = false
        EL.HTML.classList.remove('is-search-active')
      },

      /**
       * switchShowHide
       */
      switchShowHide: () => {
        func.isActive ? func.hide() : func.show()
      },

      /**
       * resize
       */
      // 【元】resize: debounce(150, () 
      // 【追記】debounceを利用することで、負荷対策しているが、node.js経由のプラグインなので利用しない方法に書き換え
      resize: () => {
        if (func.deviceType !== getDeviceType()) {
          func.deviceType = getDeviceType()
          func.hide()

          if (func.deviceType === 'lg') {
            EL.NAV.style.visibility = ''
          }
        }
      }

    }

    func.init()
  };

// import smSearch from './helper/sm-search'
  /**
   * スマホ用検索の処理を提供します
   */
  function smSearch() { 
      const func = {
        isActive: false,
        deviceType: getDeviceType(),
    
        HMB: document.querySelector('.js-search-open'),
        CLOSE: document.querySelector('.js-search-close'),
        HMBBG: document.querySelector('.c-search__back'),
    
        /**
         * init
         */
        init: () => {
          if (func.HMB) {
            func.HMB.addEventListener('click', func.switchShowHide, false)
            func.CLOSE.addEventListener('click', func.switchShowHide, false)
            func.HMBBG.addEventListener('click', func.switchShowHide, false)
            window.addEventListener('resize', func.resize, false)
          }
        },
    
        /**
         * show
         */
        show: () => {
          func.isActive = true
          EL.HTML.classList.add('is-c-search-active')
        },
    
        /**
         * hide
         */
        hide: () => {
          func.isActive = false
          EL.HTML.classList.remove('is-c-search-active')
        },
    
        /**
         * switchShowHide
         */
        switchShowHide: () => {
          func.isActive ? func.hide() : func.show()
        },
    
        /**
         * resize
         */
      // 【元】resize: debounce(150, () 
      // 【追記】debounceを利用することで、負荷対策しているが、node.js経由のプラグインなので利用しない方法に書き換え
        resize: () => {
          if (func.deviceType !== getDeviceType()) {
            func.deviceType = getDeviceType()
            func.hide()
          }
        }
    
      }
    
      func.init()
  };


// import uaDataset from './helper/uaDataset'   // UA判定（プラグイン利用）
  /**
   * UA情報を<html>タグにdatasetとして追加します
   * 文字列にスペースが付く場合はハイフンで繋がれます
   */
  function uaDataset() { 
    const ua = UAParser()
    const uaString = {
      browserName: ua.browser.name.toLowerCase().replace(' ', '-'),
      browserVersion: ua.browser.major,
      browserEngine: ua.engine.name.toLowerCase().replace(' ', '-'),
      osName: ua.os.name.toLowerCase().replace(' ', '-'),
      type: (typeof ua.device.type !== 'undefined') ? ua.device.type.toLowerCase().replace(' ', '-') : 'laptop'
    }
    EL.HTML.dataset.browser = uaString.browserName
    EL.HTML.dataset.browserversion = uaString.browserVersion
    EL.HTML.dataset.browserengine = uaString.browserEngine
    EL.HTML.dataset.os = uaString.osName
    EL.HTML.dataset.type = uaString.type
  }



// import sweetScrollInit from './helper/sweetScrollInit'　//スクロールのプラグイン。利用しなくても大丈夫そう。

//import getDocumentH from '/assets/js/helper/getDocumentHeight'
  /**
   * documentの高さを取得します
   * @return document height
   */
  function getDocumentH() { 
    let documentH = document.body.clientHeight - window.innerHeight

    if (getDeviceType() === 'sm') {
      //documentH = documentH - EL.FOOTER.clientHeight + 0
    }
    return documentH;
  }

// import isTouchSupport from './helper/isTouchSupport'
  /**
   * タッチサポート判定を行います
   */
  function isTouchSupport() { 
    const isTouchSupport = (window.ontouchstart === null)
    EL.HTML.dataset.touchsupport = isTouchSupport;
  }

// import getOrientation from './helper/getOrientation' // デバイスの回転向き判定。今は要らない気がするので、非表示でいこうと思う。

// import getClassName from './helper/getClassName'
  /**
   * 特定の要素のクラスを取得して文字列で返します
   * @return string
   */
  function getClassName(target)  {
    const className = target.classList[0];
    return className;
  }

// import getDeviceType from './helper/getDeviceType'
  /**
   * breakpointとウインドウサイズを比較してlgかsmか返します
   * @return string 'lg' or 'sm'
   */
  function getDeviceType() { 
    const windowWidth = window.innerWidth;
    const deviceType = windowWidth > DEFINE.BREAKPOINT ? 'lg' : 'sm';
    return deviceType;
  }

// import accordion from './helper/accordion'
  /**
   * アコーディオンの処理を行います
   */
	function accordionCategory(targetClass) {

		document.querySelectorAll(targetClass).forEach(btn => {
			btn.addEventListener('click', () => {
				btn.closest('.l-category__button , .l-category-sm__button').classList.contains('is-opened') ? accordionHide(btn) : accordionShow(btn)
			})
		})

		const getHeight = target => {
			return target.clientHeight
		}

		function accordionShow(btn) {
			const btnParent = btn.closest('.l-category__button , .l-category-sm__button')
			const panel = btnParent.nextElementSibling
			btnParent.classList.add('is-opened')
			panel.classList.add('is-opened')
			if (btnParent.closest('li').dataset.hierarchy > 1) {
				const addHeight = getHeight(panel.firstElementChild)
				for (let i = 1; i < btnParent.closest('li').dataset.hierarchy; ++i) {
					const parentPanel = btnParent.closest('[data-hierarchy="' + i + '"]').querySelector('.js-accordion-block')
					console.log(parentPanel);
					parentPanel.style.height = Number(parentPanel.style.height.replace('px', '')) + addHeight + 'px'
				}
			}
			panel.style.height = getHeight(panel.firstElementChild)  + 'px'
		}
		function accordionHide(btn) {
			const btnParent = btn.closest('.l-category__button , .l-category-sm__button')
			const panel = btnParent.nextElementSibling
			btnParent.classList.remove('is-opened')
			panel.classList.remove('is-opened')
			if (btnParent.closest('li').dataset.hierarchy > 1) {
				const minusHeight = getHeight(panel.firstElementChild)
				for (let i = 1; i < btnParent.closest('li').dataset.hierarchy; ++i) {
					const parentPanel = btnParent.closest('[data-hierarchy="' + i + '"]').querySelector('.js-accordion-block')
					parentPanel.style.height = Number(parentPanel.style.height.replace('px', '')) - minusHeight + 'px'
				}
			}
			panel.style.height = 0
		}
	}


// import selectbox from './helper/selectbox' // セレクトタグのカスタマイズ版。利用していないので非表示でOK

// import relationInputs from './helper/relationInputs'
  /**
   * フォームの入力項目の連動を制御
   */
  function relationInputs() {
    const func = {

      el: document.querySelectorAll('[data-relation-inputs="el"]'),
      childs: document.querySelectorAll('[data-relation-inputs="child"]'),

      /**
       * init
       */
      init: () => {
        if (!func.el) return
        func.disableChild()
        func.el.forEach(el => {
          const parent = el.querySelector('[data-relation-inputs="parent"]')
          const child = el.querySelector('[data-relation-inputs="child"]')

          if (!parent || !child) return
          parent.addEventListener('change', e => {
            func.resetChild(child)
          }, false)
        })
      },

      /**
       * resetChild
       */
      resetChild: (child) => {
        func.childs.forEach(el => {
          const select = el.querySelectorAll('select')
          const input = el.querySelectorAll('input')
          const selectDiv = el.querySelectorAll('.c-selectbox')
          if (el === child) {
            select.forEach(el => {
              el.disabled = false
            })
            input.forEach(el => {
              el.disabled = false
            })
            selectDiv.forEach(el => {
              el.classList.remove('disable')
            })
          } else {
            select.forEach(el => {
              el.disabled = true
              el.selectedIndex = 0
            })
            input.forEach(el => {
              el.disabled = true
              el.checked = false
              if(el.type != "hidden"){
                el.value = ''
              }
            })
            selectDiv.forEach(el => {
              func.resetSelectBox(el)
            })
            selectDiv.forEach(el => {
              el.classList.add('disable')
              el.classList.remove('is-open')
            })
          }
        })
      },

      /**
       * disableChild
       */
      // LS元ソース
      // disableChild: () => {
      //   func.childs.forEach(el => {
      //     el.querySelectorAll('select').forEach(el => {
      //       el.disabled = true
      //     })
      //     el.querySelectorAll('input').forEach(el => {
      //       el.disabled = true
      //     })
      //     el.querySelectorAll('.c-selectbox').forEach(el => {
      //       el.classList.add('disable')
      //     })
      //   })
      // },
      disableChild: function disableChild() {
        func.el.forEach(function (el) {
          var parent = el.querySelector('[data-relation-inputs="parent"]');
          var child = el.querySelector('[data-relation-inputs="child"]');
          if (!parent || !child) return;

          if (!parent.checked) {
            child.querySelectorAll('select').forEach(function (el) {
              el.disabled = true;
            });
            child.querySelectorAll('input').forEach(function (el) {
              el.disabled = true;
            });
            child.querySelectorAll('.c-selectbox').forEach(function (el) {
              el.classList.add('disable');
            });
          }
        });
      },


      /**
       * resetSelectbox
       */
      resetSelectBox: (el) => {
        if (el.querySelector('.c-selectbox__list li.is-select')) el.querySelector('.c-selectbox__list li.is-select').classList.remove('is-select')

        const def = el.querySelector('.c-selectbox__list li')
        def.classList.add('is-select')

        el.querySelector('.c-selectbox--text').innerHTML = def.querySelector('.c-selectbox--text').innerHTML
      }
    }

    func.init()
  }

// import favorite from './helper/favorite'
  /**
   * お気に入りボタンの処理を提供します
   */
  function favorite() {
    const func = {
      FAVBTN: document.querySelectorAll('.js-btn-favorite'),

      /**
       * init
       */
      init: () => {
        func.FAVBTN.forEach(btn => {
          btn.addEventListener('click', (e) => {
            e.preventDefault()
            func.switchShowHide(btn)
          })
        })
      },

      /**
       * show
       */
      show: (btn) => {
        btn.classList.add('is-active')
      },

      /**
       * hide
       */
      hide: (btn) => {
        btn.classList.remove('is-active')
      },

      /**
       * switchShowHide
       */
      switchShowHide: (btn) => {
        btn.classList.contains('is-active') ? func.hide(btn) : func.show(btn)
      }

    }

    func.init()
  }

// import password from './helper/password'
  /**
   * パスワードの処理を提供します
   */
  function password() {
    const func = {
      INPUT: document.querySelectorAll('.js-password-input'),
      BUTTON: document.querySelectorAll('.js-password-btn'),

      /**
       * init
       */
      init: () => {
        func.BUTTON.forEach(btn => {
          btn.addEventListener('click', () => {
            func.switchShowHide(btn)
          })
        })
      },

      /**
       * show
       */
      show: (target) => {
        target.previousElementSibling.type = 'text'
        target.classList.add('is-show')
      },

      /**
       * hide
       */
      hide: (target) => {
        target.previousElementSibling.type = 'password'
        target.classList.remove('is-show')
      },

      /**
       * switchShowHide
       */
      switchShowHide: (target) => {
        target.classList.contains('is-show') ? func.hide(target) : func.show(target)
      }
    }

    func.init()
  }




// ---------------------
// plugins
// ---------------------
// import objectFitImages from 'object-fit-images'
// import picturefill from 'picturefill'
// import Stickyfill from 'stickyfilljs'
// import { throttle, debounce } from 'throttle-debounce'
// import 'nodelist-foreach-polyfill'
// import MatchHeight from 'matchheight'


// ---------------------
// page scripts
// ---------------------

const breakW = 600;

// import pageNameTop from './page/top'
function pageNameTop() {

    //mv
    const mvSwiper = new Swiper('.p-top__mv', {
      slidesPerView: 'auto',
      centeredSlides: true,
      effect: 'slide',
      loop: true,
      speed: 500,
      autoplay: {
        delay: 5000
      },
      pagination: {
        el: '.top__mv--pagination',
        clickable: true
      },
      navigation: {
        nextEl: '.top__mv--next',
        prevEl: '.top__mv--prev'
      },
      spaceBetween: 4,

      breakpoints: {
        // 600px以上の場合
        600: {
          spaceBetween: 10,
        }
      },
    })


    // recommend
    const recommendSwiper = new Swiper('.p-top__recommend .js-product-swiper', {
      slidesPerView: 'auto',
      effect: 'slide',
      loop: false,
      speed: 500,

      breakpoints: {
        // 600px以上の場合
        600: {
          navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
          },
        }
      },
    })



    //banner
    // if (window.innerWidth >= 600) {
    //   const bnrSwiper = new Swiper('.p-top__banner-swiper', {
    //     slidesPerView: 'auto',
    //     effect: 'slide',
    //     loop: false,
    //     speed: 500,

    //     navigation: {
    //       nextEl: '.top__banner-next',
    //       prevEl: '.top__banner-prev'
    //     }
    //   })
    // }

    // sellwell
    const sellwellSwiper = new Swiper('.p-top__sellswell .js-product-swiper', {
      slidesPerView: 'auto',
      effect: 'slide',
      loop: false,
      speed: 500,

      breakpoints: {
        // 600px以上の場合
        600: {
          // navigationを外に出している場合は固有のクラス名を指定する。（他のスライダーに干渉してしまう為）
          navigation: {
            nextEl: '.js-product-swiper-next',
            prevEl: '.js-product-swiper-prev'
          },
        }
      },
    })

    // special
    const special1Swiper = new Swiper('.p-top__special .js-product-swiper', {
      slidesPerView: 'auto',
      effect: 'slide',
      loop: false,
      speed: 500,

      breakpoints: {
        // 600px以上の場合
        600: {
          navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
          },
        }
      },
    })

    // topics
    const topicsSwiper = new Swiper('.p-top__topics-swiper', {
      slidesPerView: 'auto',
      effect: 'slide',
      loop: false,
      speed: 500,

      breakpoints: {
        // 600px以上の場合
        600: {
          navigation: {
            nextEl: '.top__topics-next',
            prevEl: '.top__topics-prev',
          },
        }
      },
    })

    // aside
    if (window.innerWidth >= 600) {
      const asideSwiper = new Swiper('.p-top__aside-swiper', {
        slidesPerView: 'auto',
        effect: 'slide',
        loop: false,
        speed: 500,

        breakpoints: {
          // 600px以上の場合
          600: {
            navigation: {
              nextEl: '.top__aside-next',
              prevEl: '.top__aside-prev',
            },
          }
        },
      })
    }



    // const rankItems = document.querySelectorAll('.c-product--slider2 .js-product-swiper')
    // const rankSwiper = Array(document.querySelectorAll('.c-product--slider2 .js-product-swiper').length)

    // rankItems.forEach(function (elem, index) {
    //     rankSwiper[index] = new Swiper(elem, {
    //       slidesPerView: 'auto',
    //       effect: 'slide',
    //       speed: 500,

    //       breakpoints: {
    //         // 600px以上の場合
    //         600: {
    //           navigation: {
    //             nextEl: '.js-product-swiper2-next',
    //             prevEl: '.js-product-swiper2-prev'
    //           }
    //         }
    //       },

    //     })

    // })

    //元ソース
    // window.addEventListener('resize',
    //   // ▼【元】debounce(150, () => {
    //   () => {
    //     rankItems.forEach(function (elem, index) {
    //       if (window.innerWidth <= 600) {
    //         if (rankSwiper[index]) {

    //         } else {
    //           rankSwiper[index] = new Swiper(elem, {
    //             slidesPerView: 'auto',
    //             effect: 'slide',
    //             speed: 500,
    //             pagination: {
    //               el: elem.querySelector('.swiper-pagination'),
    //               clickable: true
    //             }
    //           })
    //         }
    //       } else {
    //         if (rankSwiper[index]) {
    //           rankSwiper[index].destroy()
    //           rankSwiper[index] = undefined
    //         }
    //       }
    //     })
    //   },
    //   false
    // )
  }

// import pageNameProductDetail from './page/product-detail'
function pageNameProductDetail() {

    // mainImg
    // const thumbList = document.querySelector('.p-product-detail__base--thumb')
    // const mainSwiper = new Swiper('.p-product-detail__base--mainimg', {
    //   slidesPerView: 1,
    //   effect: 'slide',
    //   loop: true,
    //   speed: 500,
    //   navigation: {
    //     nextEl: '.p-product-detail__base--mainimg .swiper-button-next',
    //     prevEl: '.p-product-detail__base--mainimg .swiper-button-prev'
    //   }
    // })
    // mainSwiper.on('slideChange', function () {
    //   thumbList.querySelector('div.is-current').classList.remove('is-current')
    //   thumbList.querySelectorAll('div')[mainSwiper.realIndex].classList.add('is-current')

    //   // 【追記：納品時に掃除すること】
    //   const currentBtn = thumbList.querySelector('div.is-current');       // 選択中サムネイル
    //   const currentBtnPos = currentBtn.getBoundingClientRect().left;      // 選択中サムネイルの座標位置
    //   const currentBtnRightPos = currentBtnPos + currentBtn.clientWidth;　// 選択中サムネイルの幅を含めた座標位置
    //   const deviceWidth = document.body.clientWidth;                      // デバイスの幅
    //   const contentsWidth = thumbList.clientWidth;                        // コンテンツの幅
    //   const thmbWidth = thumbList.querySelector('.button').clientWidth;   // サムネイルの幅
    //   const contentsMargin = deviceWidth - contentsWidth;                 // 余白の幅
    //   const thmbScroll = thumbList.scrollLeft;                            // サムネイル部分のスクロール量

    //   console.log('カレントの座標位置：' + currentBtnPos);
    //   console.log('カレント右側の座標位置：' + currentBtnRightPos);

    //   console.log('デバイス幅：' + deviceWidth);
    //   console.log('コンテンツ幅：' + contentsWidth);
    //   console.log('コンテンツとデバイス幅の余白：' + contentsMargin);
    //   console.log('サムネイルの幅：' + thmbWidth);

    //   console.log('サムネイル部分のスクロール量：' + thmbScroll);

    //   if (currentBtnRightPos > contentsWidth) {
    //     console.log('はみ出しているよ');
    //     //thumbList.scrollBy(currentBtnRightPos - deviceWidth + contentsMargin , 0);
    //     // thumbList.scrollBy(currentBtnRightPos - deviceWidth + thmbWidth + (contentsMargin * 2) , 0);
    //     thumbList.scrollBy({
    //       left: currentBtnRightPos - deviceWidth + thmbWidth + (contentsMargin * 2),
    //       behavior: 'smooth'
    //     });
    //   } else if (currentBtnPos <= thumbList.getBoundingClientRect().left) {
    //     console.log('左側にはみ出しているよ');
    //     //thumbList.scrollBy( (currentBtnPos-12) , 0);
    //     thumbList.scrollBy({
    //       left: currentBtnPos - 12,
    //       behavior: 'smooth'
    //     });
    //   }






    // })
    // thumbList.querySelectorAll('div')[mainSwiper.realIndex].classList.add('is-current')
    // thumbList.querySelectorAll('div').forEach(THUMB => {
    //   THUMB.addEventListener('click', () => {
    //     const selectIndex = [].slice.call(thumbList.querySelectorAll('div')).indexOf(THUMB)
    //     mainSwiper.slideToLoop(selectIndex)
    //   })
    // })

    // 【追記】viewed
    // const viewedSwiper = new Swiper('.p-product-detail__viewed .js-product-swiper', {
    //   slidesPerView: 'auto',
    //   effect: 'slide',
    //   loop: false,
    //   speed: 500,

    //   breakpoints: {
    //     // 600px以上の場合
    //     600: {
    //       // navigationを外に出している場合は固有のクラス名を指定する。（他のスライダーに干渉してしまう為）
    //       navigation: {
    //         nextEl: '.js-product-swiper-next',
    //         prevEl: '.js-product-swiper-prev'
    //       },
    //     }
    //   },
    // })

    // favorite & sellswell
    const goodsdetailSwiper = new Swiper('.p-product-detail__related .js-product-swiper , .p-product-detail__favorite .js-product-swiper , .p-product-detail__viewed .js-product-swiper', {
      slidesPerView: 'auto',
      effect: 'slide',
      loop: false,
      speed: 500,

      breakpoints: {
        // 600px以上の場合
        600: {
          navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
          },
        }
      },
    })






    // relation
    const rankItems = document.querySelectorAll('.c-product--relation')
    const rankSwiper = Array(document.querySelectorAll('.c-product--relation').length)
    rankItems.forEach(function (elem, index) {
      if (window.innerWidth <= 768.9) {
        rankSwiper[index] = new Swiper(elem, {
          slidesPerView: 'auto',
          effect: 'slide',
          speed: 500,
          pagination: {
            el: elem.querySelector('.swiper-pagination'),
            clickable: true
          }
        })
      }
    })
    window.addEventListener('resize',
    // ▼【元】debounce(150, () => {
      () => {
      rankItems.forEach(function (elem, index) {
        if (window.innerWidth <= 768.9) {
          if (rankSwiper[index]) {

          } else {
            rankSwiper[index] = new Swiper(elem, {
              slidesPerView: 'auto',
              effect: 'slide',
              speed: 500,
              pagination: {
                el: elem.querySelector('.swiper-pagination'),
                clickable: true
              }
            })
          }
        } else {
          if (rankSwiper[index]) {
            rankSwiper[index].destroy()
            rankSwiper[index] = undefined
          }
        }
      })
    }),
    false;

  }

// import pageNameCart from './page/cart'
  function pageNameCart() {
    const rankItems = document.querySelectorAll('.c-product--relation')
    const rankSwiper = Array(document.querySelectorAll('.c-product--relation').length)
    rankItems.forEach(function (elem, index) {
      if (window.innerWidth <= 768.9) {
        rankSwiper[index] = new Swiper(elem, {
          slidesPerView: 'auto',
          effect: 'slide',
          speed: 500,
          pagination: {
            el: elem.querySelector('.swiper-pagination'),
            clickable: true
          }
        })
      }
    })
    window.addEventListener('resize',
      // ▼【元】debounce(150, () => {
      () => {
        rankItems.forEach(function (elem, index) {
          if (window.innerWidth <= 768.9) {
            if (rankSwiper[index]) {

            } else {
              rankSwiper[index] = new Swiper(elem, {
                slidesPerView: 'auto',
                effect: 'slide',
                speed: 500,
                pagination: {
                  el: elem.querySelector('.swiper-pagination'),
                  clickable: true
                }
              })
            }
          } else {
            if (rankSwiper[index]) {
              rankSwiper[index].destroy()
              rankSwiper[index] = undefined
            }
          }
        })
      }),
      false;
  }


// import pageNameSearch from './page/search'
// require('svgxuse')

// ---------------------
// getDeviceType
// ---------------------
  let deviceType = getDeviceType()


// ---------------------
// getDocumentH
// ---------------------

  let documentH = getDocumentH()

/**
 * getScrollPos
 */
const getScrollPos = function getScrollPos() {
  const y = window.pageYOffset;

  // add class is-scroll
  if (y > 1) {
    if (!EL.HTML.classList.contains('is-scroll')) {
      EL.HTML.classList.add('is-scroll')
    }
  } else {
    EL.HTML.classList.remove('is-scroll')
  }

  // add class is-footer
  if (documentH <= y) {
    if (!EL.HTML.classList.contains('is-footer')) {
      EL.HTML.classList.add('is-footer')
    }
  } else {
    EL.HTML.classList.remove('is-footer')
  }
}

/**
 * first
 */
const first = function first() { // 【メモ】コメント表示(07/18)
  // set ua dataset
  uaDataset()

  // set touch support dataset
  isTouchSupport();

  //   // closet polyfill.
  //   closetPolyfill()

  //   // Math height
  //   MatchHeight.init()

  //   // Polyfill object-fit
  //   objectFitImages()

  //   // Polyfill picturefill
  //   picturefill()

  //   // stickyfilljs
  //   Stickyfill.add(EL.STICKY)

  //   // getOrientation
  //   getOrientation()

  // hmb
  hmb();

  // search modal
  search();
  smSearch();

  //   // selectbox
  //   selectbox()

  // favorite
  favorite();

  // password
  password();

//   // sweetScroll
  const sweetScroll = new SweetScroll({
  });



}

/**
 * init
 */
const init = function init() {// 【メモ】コメント表示(07/18)
  // get body className
  const className = getClassName(EL.BODY);

  // getScrollPos
  getScrollPos();

  // top
  if (className.endsWith('top')) {
    pageNameTop();
  }

  // product-detail
  if (className.endsWith('product-detail')) {
    pageNameProductDetail();
  }

  // cart
  if (className.endsWith('cart')) {
    pageNameCart();
  }

  //form
  relationInputs();

//   // search
//   if (className.endsWith('search')) {
//     pageNameSearch()
//   }
}

/**
 * DOMCONTENTLOADED
 */
// 【メモ】コメント表示(07/18)
// 【メモ】first関数をここでロード
window.addEventListener('DOMContentLoaded', first);

/**
 * LOAD
 */
// 【メモ】コメント表示(07/18)
// 【メモ】init関数をここでロード
window.addEventListener('load', init);

/**
 * SCROLL
 */
// 【元】 window.addEventListener('scroll', throttle(150, getScrollPos), false);
// 【追記】throttleを利用することで、負荷対策。利用しなくてもできるけど、毎度、スクロール位置を取得するので、負荷がかかっている。
//        このプラグインで、イベント時の処理回数を減らしている。
//        node.js経由のプラグインなので利用しない方法に書き換え
window.addEventListener('scroll', getScrollPos, false);

/**
 * RESIZE
 */
// window.addEventListener('resize',
//   debounce(150, () => {
//     // LGとSMで切り替わる時
//     if (deviceType !== getDeviceType()) {
//       deviceType = getDeviceType()

//       // documentH更新
//       documentH = getDocumentH()
//     }
//   }),
//   false
// )
