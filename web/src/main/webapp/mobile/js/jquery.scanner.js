(function($) {
    $.scanner = function(func,param,debug) {

        /*if (typeof (android) == 'undefined' && !debug) {
            showError('Please install app to operate.');
            return;
        }*/

        afterScanCallBack = func;

        if($('span.scanner').length > 0) {
            $('span.scanner').unbind('click').bind('click', function (e) {
                android.startScan();
            });
        }
        
        $('.scanner_banner img').click(function () {
			android.startScan();
		});
        if($('.scanner_banner span').length > 0 ) {
            $('.scanner_banner span').unbind('click').bind('click', function (e) {
                var input_code = $('.scanner_banner input').val();
                if(isEmpty(input_code)){
                    showError('please input code or scan code!');
                    $('.scanner_banner input').focus();
                    return;
                }
                //invokeCallBack(afterScanCallBack(input_code));
                afterScan(input_code);
            });
        }
        
        
        
        if($('div.scanner img').length > 0) {
            $('div.scanner img').unbind('click').bind('click', function (e) {
                android.startScan();
            });
        }
        if($('div.scanner span').length > 0 && $('div.scanner').find('input').length > 0) {
            $('div.scanner span').unbind('click').bind('click', function (e) {
                var input_code = $('div.scanner').find('input').val();
                if(isEmpty(input_code)){
                    showError('please input code or scan code!');
                    $('div.scanner').find('input').focus();
                    return;
                }
                //invokeCallBack(afterScanCallBack(input_code));
                afterScan(input_code);
            });
        }
        
        
        if(param == 1){
            if(debug){
                invokeCallBack(afterScanCallBack);
                return;
            }
            android.startScan();
        }
    };
})(jQuery);



var afterScanCallBack = null;
function afterScan(scanResult){
    invokeCallBack(afterScanCallBack,scanResult);
}