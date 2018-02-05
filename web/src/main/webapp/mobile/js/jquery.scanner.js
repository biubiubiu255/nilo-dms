(function($) {
    $.scanner = function(func,param,debug) {

        if (typeof (android) == 'undefined' && !debug) {
            showError('Please install app to operate.');
            return;
        }

        afterScanCallBack = func;
        if($('div.scanner').length > 0) {
            $('div.scanner').unbind('click').bind('click', function (e) {
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