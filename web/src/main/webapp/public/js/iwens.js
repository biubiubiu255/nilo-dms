



if( typeof($) != 'undefined' ){

    var internalObject ={
        packing : function(name){
            console.log("正在进行序列化");
            var param = '';
            var inputAll = $(name).find("input");
            //console.log("found number:" + inputAll.length);
            inputAll.each(function(index, e){
                if ( param!='' ) param += "&";
                switch (inputAll[index].attr("toFormat")){
                    case 'date':
                        param += this.name + '=' + Date.parse(new Date(this.value)) / 1000;
                        break;
                    default:
                        param += this.name + '=' + this.value;
                        break;
                }
            });

            return param;
        }
    }

    $.extend(internalObject);
}



