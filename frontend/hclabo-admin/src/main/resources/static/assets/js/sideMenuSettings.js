/*!
 * Nestable jQuery Plugin - Copyright (c) 2012 David Bushell - http://dbushell.com/
 * Dual-licensed under the BSD or MIT licenses
 */
;(function($, window, document, undefined)
{
    var hasTouch = 'ontouchstart' in document;

    /**
     * Detect CSS pointer-events property
     * events are normally disabled on the dragging element to avoid conflicts
     * https://github.com/ausi/Feature-detection-technique-for-pointer-events/blob/master/modernizr-pointerevents.js
     */
    var hasPointerEvents = (function()
    {
        var el    = document.createElement('div'),
            docEl = document.documentElement;
        if (!('pointerEvents' in el.style)) {
            return false;
        }
        el.style.pointerEvents = 'auto';
        el.style.pointerEvents = 'x';
        docEl.appendChild(el);
        var supports = window.getComputedStyle && window.getComputedStyle(el, '').pointerEvents === 'auto';
        docEl.removeChild(el);
        return !!supports;
    })();

    var defaults = {
        listNodeName    : 'ol',
        itemNodeName    : 'li',
        rootClass       : 'dd',
        listClass       : 'dd-list',
        itemClass       : 'dd-item',
        dragClass       : 'dd-dragel',
        handleClass     : 'dd-handle-side-menu',
        collapsedClass  : 'dd-collapsed',
        placeClass      : 'dd-placeholder',
        noDragClass     : 'dd-nodrag',
        emptyClass      : 'dd-empty',
        group           : 0,
        maxDepth        : 10,
        threshold       : 20
    };

    function Plugin(element, options)
    {
        this.w  = $(document);
        this.el = $(element);
        this.options = $.extend({}, defaults, options);
        this.init();
    }

    Plugin.prototype = {

        init: function()
        {
            var list = this;

            list.reset();

            list.el.data('nestable-group', this.options.group);

            list.placeEl = $('<div class="' + list.options.placeClass + '"/>');

            var onStartEvent = function(e)
            {
                var handle = $(e.target);
                if (!handle.hasClass(list.options.handleClass)) {
                    if (handle.closest('.' + list.options.noDragClass).length) {
                        return;
                    }
                    handle = handle.closest('.' + list.options.handleClass);
                }

                if (!handle.length || list.dragEl) {
                    return;
                }

                list.isTouch = /^touch/.test(e.type);
                if (list.isTouch && e.touches.length !== 1) {
                    return;
                }

                e.preventDefault();
                list.dragStart(e.touches ? e.touches[0] : e);
            };

            var onMoveEvent = function(e)
            {
                if (list.dragEl) {
                    e.preventDefault();
                    list.dragMove(e.touches ? e.touches[0] : e);
                }
            };

            var onEndEvent = function(e)
            {
                if (list.dragEl) {
                    e.preventDefault();
                    list.dragStop(e.touches ? e.touches[0] : e);
                }
            };

            if (hasTouch) {
                list.el[0].addEventListener('touchstart', onStartEvent, false);
                window.addEventListener('touchmove', onMoveEvent, false);
                window.addEventListener('touchend', onEndEvent, false);
                window.addEventListener('touchcancel', onEndEvent, false);
            }

            list.el.on('mousedown', onStartEvent);
            list.w.on('mousemove', onMoveEvent);
            list.w.on('mouseup', onEndEvent);

        },

        serialize: function()
        {
            var data,
                depth = 0,
                list  = this;
            step  = function(level, depth)
            {
                var array = [ ],
                    items = level.children(list.options.itemNodeName);
                items.each(function(i)
                {
                    var li   = $(this),
                        item = $.extend({}, li.data(), {serialNumber : i + 1}),
                        sub  = li.children(list.options.listNodeName);
                    if (sub.length) {
                        item.categoryTreeDtoList = step(sub, depth + 1);
                    }
                    i++;
                    array.push(item);
                });
                return array;
            };
            data = step(list.el.find(list.options.listNodeName).first(), depth);
            return {
                categoryId: "dummy",
                categoryName: "ダミーカテゴリー",
                displayName: "ダミーカテゴリー",
                categoryTreeDtoList: data
            };
        },

        serialise: function()
        {
            return this.serialize();
        },

        reset: function()
        {
            this.mouse = {
                offsetX   : 0,
                offsetY   : 0,
                startX    : 0,
                startY    : 0,
                lastX     : 0,
                lastY     : 0,
                nowX      : 0,
                nowY      : 0,
                distX     : 0,
                distY     : 0,
                dirAx     : 0,
                dirX      : 0,
                dirY      : 0,
                lastDirX  : 0,
                lastDirY  : 0,
                distAxX   : 0,
                distAxY   : 0
            };
            this.isTouch    = false;
            this.moving     = false;
            this.dragEl     = null;
            this.dragRootEl = null;
            this.dragDepth  = 0;
            this.hasNewRoot = false;
            this.pointEl    = null;
        },

        dragStart: function(e)
        {
            var mouse    = this.mouse,
                target   = $(e.target),
                dragItem = target.closest(this.options.itemNodeName);

            this.placeEl.css('height', dragItem.height());

            mouse.offsetX = e.offsetX !== undefined ? e.offsetX : e.pageX - target.offset().left;
            mouse.offsetY = e.offsetY !== undefined ? e.offsetY : e.pageY - target.offset().top;
            mouse.startX = mouse.lastX = e.pageX;
            mouse.startY = mouse.lastY = e.pageY;

            this.dragRootEl = this.el;

            this.dragEl = $(document.createElement(this.options.listNodeName)).addClass(this.options.listClass + ' ' + this.options.dragClass);
            this.dragEl.css('width', dragItem.width());

            dragItem.after(this.placeEl);
            dragItem[0].parentNode.removeChild(dragItem[0]);
            dragItem.appendTo(this.dragEl);

            $(document.body).append(this.dragEl);
            this.dragEl.css({
                'left' : e.pageX - mouse.offsetX,
                'top'  : e.pageY - mouse.offsetY
            });
            // total depth of dragging item
            var i, depth,
                items = this.dragEl.find(this.options.itemNodeName);
            for (i = 0; i < items.length; i++) {
                depth = $(items[i]).parents(this.options.listNodeName).length;
                if (depth > this.dragDepth) {
                    this.dragDepth = depth;
                }
            }
        },

        dragStop: function(e)
        {
            var el = this.dragEl.children(this.options.itemNodeName).first();
            el[0].parentNode.removeChild(el[0]);
            this.placeEl.replaceWith(el);

            this.dragEl.remove();
            this.el.trigger('change');
            if (this.hasNewRoot) {
                this.dragRootEl.trigger('change');
            }
            this.reset();
        },

        dragMove: function(e)
        {
            var list, parent, prev, next, depth,
                opt   = this.options,
                mouse = this.mouse;

            this.dragEl.css({
                'left' : e.pageX - mouse.offsetX,
                'top'  : e.pageY - mouse.offsetY
            });

            // mouse position last events
            mouse.lastX = mouse.nowX;
            mouse.lastY = mouse.nowY;
            // mouse position this events
            mouse.nowX  = e.pageX;
            mouse.nowY  = e.pageY;
            // distance mouse moved between events
            mouse.distX = mouse.nowX - mouse.lastX;
            mouse.distY = mouse.nowY - mouse.lastY;
            // direction mouse was moving
            mouse.lastDirX = mouse.dirX;
            mouse.lastDirY = mouse.dirY;
            // direction mouse is now moving (on both axis)
            mouse.dirX = mouse.distX === 0 ? 0 : mouse.distX > 0 ? 1 : -1;
            mouse.dirY = mouse.distY === 0 ? 0 : mouse.distY > 0 ? 1 : -1;
            // axis mouse is now moving on
            var newAx   = Math.abs(mouse.distX) > Math.abs(mouse.distY) ? 1 : 0;

            // do nothing on first move
            if (!mouse.moving) {
                mouse.dirAx  = newAx;
                mouse.moving = true;
                return;
            }

            // calc distance moved on this axis (and direction)
            if (mouse.dirAx !== newAx) {
                mouse.distAxX = 0;
                mouse.distAxY = 0;
            } else {
                mouse.distAxX += Math.abs(mouse.distX);
                if (mouse.dirX !== 0 && mouse.dirX !== mouse.lastDirX) {
                    mouse.distAxX = 0;
                }
                mouse.distAxY += Math.abs(mouse.distY);
                if (mouse.dirY !== 0 && mouse.dirY !== mouse.lastDirY) {
                    mouse.distAxY = 0;
                }
            }
            mouse.dirAx = newAx;

            /**
             * move horizontal
             */
            if (mouse.dirAx && mouse.distAxX >= opt.threshold) {
                // reset move distance on x-axis for new phase
                mouse.distAxX = 0;
                prev = this.placeEl.prev(opt.itemNodeName);
                // increase horizontal level if previous sibling exists and is not collapsed
                if (mouse.distX > 0 && prev.length && !prev.hasClass(opt.collapsedClass)) {
                    // cannot increase level when item above is collapsed
                    list = prev.find(opt.listNodeName).last();
                    // check if depth limit has reached
                    depth = this.placeEl.parents(opt.listNodeName).length;
                    if (depth + this.dragDepth <= opt.maxDepth) {
                        // create new sub-level if one doesn't exist
                        if (!list.length) {
                            list = $('<' + opt.listNodeName + '/>').addClass(opt.listClass);
                            list.append(this.placeEl);
                            prev.append(list);
                        } else {
                            // else append to next level up
                            list = prev.children(opt.listNodeName).last();
                            list.append(this.placeEl);
                        }
                    }
                }
                // decrease horizontal level
                if (mouse.distX < 0) {
                    // we can't decrease a level if an item preceeds the current one
                    next = this.placeEl.next(opt.itemNodeName);
                    if (!next.length) {
                        parent = this.placeEl.parent();
                        this.placeEl.closest(opt.itemNodeName).after(this.placeEl);
                    }
                }
            }

            var isEmpty = false;

            // find list item under cursor
            if (!hasPointerEvents) {
                this.dragEl[0].style.visibility = 'hidden';
            }
            this.pointEl = $(document.elementFromPoint(e.pageX - document.body.scrollLeft, e.pageY - (window.pageYOffset || document.documentElement.scrollTop)));
            if (!hasPointerEvents) {
                this.dragEl[0].style.visibility = 'visible';
            }
            if (this.pointEl.hasClass(opt.handleClass)) {
                this.pointEl = this.pointEl.parent(opt.itemNodeName);
            }
            if (this.pointEl.hasClass(opt.emptyClass)) {
                isEmpty = true;
            }
            else if (!this.pointEl.length || !this.pointEl.hasClass(opt.itemClass)) {
                return;
            }

            // find parent list of item under cursor
            var pointElRoot = this.pointEl.closest('.' + opt.rootClass),
                isNewRoot   = this.dragRootEl.data('nestable-id') !== pointElRoot.data('nestable-id');

            /**
             * move vertical
             */
            if (!mouse.dirAx || isNewRoot || isEmpty) {
                // check if groups match if dragging over new root
                if (isNewRoot && opt.group !== pointElRoot.data('nestable-group')) {
                    return;
                }
                // check depth limit
                depth = this.dragDepth - 1 + this.pointEl.parents(opt.listNodeName).length;
                if (depth > opt.maxDepth) {
                    return;
                }
                var before = e.pageY < (this.pointEl.offset().top + this.pointEl.height() / 2);
                parent = this.placeEl.parent();
                // if empty create new list to replace empty placeholder
                if (isEmpty) {
                    list = $(document.createElement(opt.listNodeName)).addClass(opt.listClass);
                    list.append(this.placeEl);
                    this.pointEl.replaceWith(list);
                }
                else if (before) {
                    this.pointEl.before(this.placeEl);
                }
                else {
                    this.pointEl.after(this.placeEl);
                }
                if (!this.dragRootEl.find(opt.itemNodeName).length) {
                    this.dragRootEl.append('<div class="' + opt.emptyClass + '"/>');
                }
                // parent root list has changed
                if (isNewRoot) {
                    this.dragRootEl = pointElRoot;
                    this.hasNewRoot = this.el[0] !== this.dragRootEl[0];
                }
            }
        }

    };

    $.fn.nestable = function(params)
    {
        var lists  = this,
            retval = this;

        lists.each(function()
        {
            var plugin = $(this).data("nestable");

            if (!plugin) {
                $(this).data("nestable", new Plugin(this, params));
                $(this).data("nestable-id", new Date().getTime());
            } else {
                if (typeof params === 'string' && typeof plugin[params] === 'function') {
                    retval = plugin[params]();
                }
            }
        });

        return retval || lists;
    };

})(window.jQuery || window.Zepto, window, document);

const updateOutput = function (e) {
    const list = e.length ? e : $(e.target),
        output = list.data('output');
    if (output === undefined) {
        return
    }
    if (window.JSON) {
       list.nestable('serialize');
        output.val(window.JSON.stringify(list.nestable('serialize')));
    } else {
        output.val('This browser does not support for JSON');
    }
};
$(document).ready(function () {
    let nestable = $('#nestable');
    updateOutput(nestable.data('output', $('#nestable-output')));
    nestable.nestable().on('change', updateOutput);
});

function updateData() {
    updateOutput($('#nestable').data('output', $('#nestable-output')));
}

function textChange(item) {
    let display = $(item).val();
    $(item).closest('li').data('displayName', display);
    updateOutput($('#nestable').data('output', $('#nestable-output')));
}

function addMenu(data, initVal) {
    let menuData = '<li class="dd-item" id="' + data.categoryId + '-' + initVal + '-li' +
        '" data-category-id="' + data.categoryId +
        '" data-category-name="' + change(data.categoryName) +
        '" data-display-name="' + change(data.categoryName) + '">' +
        '<div class="dd-handle-side-menu dd3-handle"><i class="material-icons menu"></i></div>' +
        '<div class="dd3-content-side-menu">' +
        '<div class="category-draggable-items">' +
        '<div class="l-section">' +
        '<label class="c-form-control">' +
        '<input type="checkbox" id="' + data.categoryId + '-' + initVal + '" onclick="inputCheck(this)"><i></i>' +
        '<span id="' + data.categoryId + '-' + initVal + '-id">' + data.categoryId + ' (' + data.categoryName + ')' + '</span></label>' +
        '</div>' +
        '<div class="l-section">' +
        '<div class="l-section category-input-name">' +
        '<dt>表示名</dt>' +
        '<dd>' + '<input class="c-form-control" id="'+initVal+'" value="" onKeyUp="textChange(this)">' + '</dd>' + '</div>' +
        '</div>' + '</div>' + '</div>' + '</li>';
    $("#menu-list").append(menuData);
    document.getElementById(initVal).value = data.categoryName;
}
function addCategory(data) {
    let categoryData = '<tr id="' + data.categoryId + '-tr">' +
        '<td class="check">' +
        '<label class="c-form-control">' +
        '<input type="checkbox" name="resultItems-' + data.categoryId + '" id="' + data.categoryId + '-category' + '" value="true"><i></i>' +
        '</label>' +
        '</td>' +
        '<td class="cat_id"><span id="' + data.categoryId + '-id">' + data.categoryId + '</span>' +
        '</td>' +
        '<td class="cat_name">' +
        '<span id="' + data.categoryId + '-name">' + data.categoryName + '</span></td>' +
        '</tr>';
    $("#category-list").append(categoryData);
}
function addItems() {
    let registType = $('#registType').val();
    let initId = $('#initId');
    let initVal = initId.val();
    let alarmFlag = false;
    $('#category-list input:checked').each(function() {
        alarmFlag = true;
        const data = {
            categoryId: $('#' + $(this).attr('id').split('-category')[0] + '-id').text(),
            categoryName: $('#' + $(this).attr('id').split('-category')[0] + '-name').text(),
        };
        addMenu(data, initVal)
        initVal++;
        if (registType !== "0") {
            $('#' + $(this).attr('id').split('-category')[0] + '-tr').remove();
        } else {
            this.checked = false;
        }
    }, this);
    initId.val(initVal);
    if (!alarmFlag) {
        alert("カテゴリー一覧からカテゴリーを選択してください");
    }
}

function delItems() {
    let registType = $('#registType').val();
    let items = [];
    let alarmFlag = false;
    $('#menu-list input:checked').each(function () {
        alarmFlag = true;
        if (registType !== "0") {
            const data = {
                categoryId: $('#' + $(this).attr('id') + '-id').text(),
                categoryName: $('#' + $(this).attr('id') + '-name').text()
            };
            addCategory(data)
        }
        items.push($(this).attr('id') + '-li');
    });
    items.reverse().forEach(item => $('#' + item).remove());
    if (!alarmFlag) {
        alert("サイドメニュー構成からカテゴリ―を選択してください");
    }
}

function inputCheck(item) {
    let id = $(item).attr('id');
    let li_id = $('#' + id + '-li');
    if (item.checked === false) {
        let checked_input = $('#' + id);
        let parent_id = checked_input.closest('ol').parent().closest('li').attr('id');
        if (parent_id !== null && parent_id !== undefined) {
            let input_id = parent_id.split('-li')[0];
            if ($('#' + input_id).is(":checked")) {
                checked_input.prop( "checked", true )
                return;
            }
        }
    }
    let checked = item.checked;
    li_id.find('input[type=checkbox]').each(function () {
        this.checked = checked !== false;
    }, this);
}

$(function () {
    if ($('.unitItemsError').length > 0) {
        $('#unitItemsError').removeAttr('style');
    }
});

function change(myString){
    return myString
        .replace(/&/g, "&amp;")
        .replace(/>/g, "&gt;")
        .replace(/</g, "&lt;")
        .replace(/"/g, "&quot;");
}
