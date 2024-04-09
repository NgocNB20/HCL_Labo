function CharCount(element = document, attrName = 'maxLength', position = 'after', warningAt = 0) {
  if (!element) {
    return;
  }
  let textAreaList = element.getElementsByTagName('TEXTAREA');
  for (let textArea of textAreaList) {
    let maxLength = +textArea.getAttribute(attrName);
    addDescriptionMaxLength(textArea, maxLength, position, warningAt);
  }
}

function addDescriptionMaxLength(elementArea, maxLength, position, warningAt) {
  if (!maxLength || maxLength === -1) {
    return;
  }
  let inputLengthInit = elementArea.value.length;
  const defaultLabel = '<div class="c-text--xxs c-text-area__count-number"> '+ inputLengthInit +' / '+ maxLength +' </div>';

  if (position === 'before') {
    $(elementArea).before(defaultLabel);
    setLabel($(elementArea).prev(), maxLength, inputLengthInit, warningAt);
    $(elementArea).on('input', function() {
      let label = $(elementArea).prev();
      setLabel(label, maxLength, this.value.length, warningAt);
      warningInput($(elementArea), maxLength, this.value.length);
    });
  } else if (position === 'after') {
    $(elementArea).after(defaultLabel);
    setLabel($(elementArea).next(), maxLength, inputLengthInit, warningAt);
    $(elementArea).on('input', function() {
      let label = $(elementArea).next();
      setLabel(label, maxLength, this.value.length, warningAt);
      warningInput($(elementArea), maxLength, this.value.length);
    });
  }
}

function setLabel(el, maxLength, inputLength, warningAt) {
  let pattern = inputLength + ' / ' + maxLength;
  el.text(pattern);
  warningDescription(el, maxLength, inputLength, warningAt);
}

function warningDescription(el, maxLength, inputLength, warningAt) {
  if (warningAt >= maxLength - inputLength) {
    el.attr('style', 'color: red');
  } else {
    el.attr('style', '');
  }
}

function warningInput(el, maxLength, inputLength) {
  if (maxLength <= inputLength) {
    el.addClass('border-warning');
  } else {
    el.removeClass('border-warning');
  }
}
