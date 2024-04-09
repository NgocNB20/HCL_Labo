/**
 * スクロールハンドリング
 * @param element
 * @param heightStart
 * @param callback
 * @constructor
 */
function ScrollHandle(element, heightStart = 600, callback = () => {}) {
    let waiting = false;
    const heightAppend = heightStart;
    $(element).unbind("scroll");
    return $(element).on("scroll", async function (event) {
        if (($(event.target).scrollTop() > heightStart) && !waiting) {
            waiting = true;
            heightStart += heightAppend;
            await callback();
            waiting = false;
        }
    });
}