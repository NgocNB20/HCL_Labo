window.addEventListener("load", (function () {
    setTimeout(function () {
        let el = document.querySelectorAll('[data-relation-inputs="el"]');
        if (el) {
            el.forEach(function (e) {
                let pr = e.querySelector('[data-relation-inputs="parent"]');
                if (pr && pr.checked) {
                    pr.dispatchEvent(new Event('change'));
                }
            })
        }
    }, 100)
}))

$(document).ready(function(){
    // Set value elements on click of the button
    $('button[name="reset"]').click(function(){
    	$('#formreset').find('input[type=text],select,input[type=checkbox]').each(function() {
    		$(this).val('');
		})

    	$('#formreset').find('input[type=checkbox]').each(function() {
    		$(this).prop('checked', false);
    		$(this).attr('value', true);
    	})

    	// reset element for responsive
    	$('#formresetsm').find('input[type=text],select,input[type=checkbox]').each(function() {
    		$(this).val('');
		})

    	$('#formresetsm').find('input[type=checkbox]').each(function() {
    		$(this).prop('checked', false);
    		$(this).attr('value', true);
    	})
    });
});

