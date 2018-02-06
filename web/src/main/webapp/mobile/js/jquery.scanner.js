(function($) {
    $.scanner = function(func,param,debug) {

        if (typeof (android) == 'undefined' && !debug) {
            showError('Please install app to operate.');
            return;
        }

        afterScanCallBack = func;
        if($('span.scanner').length > 0) {
            $('span.scanner').unbind('click').bind('click', function (e) {
                android.startScan();
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