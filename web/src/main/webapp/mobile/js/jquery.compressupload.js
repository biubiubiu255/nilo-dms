(function($){
$.fn.compressandupload=function(compressoption){
    var deferred = $.Deferred();
    $(this).change(function(){
       
            var file = $(this).get(0).files[0]
            task_compress_and_then_upload(file,compressoption).then(function(result){
                deferred.resolve(result)
            })
        
        
    })
    return  deferred 
}
/**
 * 上传并压缩核心代码,依赖jquery
 *@param file : 需要上传的文件对象 
 * @param compressoption:压缩和上传参数,是一个对象
 * {maxWidth:int,压缩后图片的最大宽度,默认400
 * maxHeight:int,压缩后图片的最大高度,默认400
 * quality:float,压缩图片质量,0-1之间小数,默认0.92
 * serveurl:string,后端处理上传的连接地址,
 * minSize:int,小于这个大小的文件都不压缩,
 * onpickup:function,当文件选中即进行的回调,我们一般使用 e.target.result 
 * 这是该文件的base64编码,
 * 类似于这样,data:image/png;base64,iVBORw0
 * 我们可以使用用来做预览,
 * postarg:{}//用户自定义的上传参数,如用户id等
 * base64key:string,后端接收base64文件编码参数的名字,默认为base64data
 * }
 * @result:该函数封装了jquery 的promis 对象,因此采用then的方式,无返回
 * 
 **/
function  task_compress_and_then_upload(file,compressoption){
    var deferred = $.Deferred();
    task_compress(file,compressoption).then(function(result){
                    if(result.status==200){
                        task_upload(result.data,compressoption.serveurl,compressoption.postarg,compressoption.base64key)
                        .then(function(res){
                            deferred.resolve(res)
                        }) 
                    }else{
                        deferred.resolve(result)
                    }
    })
    return deferred;
}
//压缩核心函数
//科普一下File对象
/*
{lastModified:1511236246031
lastModifiedDate:"Tue Nov 21 2017 11:50:46 GMT+0800 (中国标准时间) "
name:"0.首页 - 副本.png"
size:2552293 //文件大小
type:"image/png" //文件类型
webkitRelativePath:""
}*/
function task_compress(file,compressoption){
    //定义promis
    var deferred = $.Deferred();
    //过滤一下,
    if (file.type.indexOf("image") ==-1) {
        deferred.resolve({"status":400,"msg":"当前文件类型暂不支持"}) 
        return deferred;
    }
    //用文件流对象读取文件
    var reader = new FileReader();
    var image = new Image();
    
    //定义流对象事件
    reader.onload = function(e) {
        //小于200kb的都不压缩
        if(typeof compressoption.onpickup=="function"){
            compressoption.onpickup(e)
        }
       if(file.size<compressoption.minSize||200*1024){
            deferred.resolve({"status":200,"data":e.target.result,"msg":""}) 
            return deferred;
       }else{
        //大于200kb的先预览到图片上
        image.src = e.target.result;
       }
       
      
    }
    //开始读取事件
    reader.readAsDataURL(file);
   
    // 缩放图片需要的canvas
    var canvas = document.createElement('canvas');
    var context = canvas.getContext('2d');
 
    // 定义image 事件 
    //base64地址图片加载完毕后
    image.onload = function () {
        console.log("image on load")
        // 图片原始尺寸
        var originWidth = this.width;
        var originHeight = this.height;
        
 
        // 最大尺寸限制
        var maxWidth = compressoption.maxWidth||400, maxHeight = compressoption.maxHeight||400;
        // 目标尺寸
        var targetWidth = originWidth, targetHeight = originHeight;
        // 图片尺寸超过400x400的限制
        if (originWidth > maxWidth || originHeight > maxHeight) {
            if (originWidth / originHeight > maxWidth / maxHeight) {
                // 更宽，按照宽度限定尺寸
                targetWidth = maxWidth;
                targetHeight = Math.round(maxWidth * (originHeight / originWidth));
            } else {
                targetHeight = maxHeight;
                targetWidth = Math.round(maxHeight * (originWidth / originHeight));
            }
        }
            
        // canvas对图片进行缩放
        canvas.width = targetWidth;
        canvas.height = targetHeight;
        // 清除画布
        context.clearRect(0, 0, targetWidth, targetHeight);
        // 图片压缩
        context.drawImage(image, 0, 0, targetWidth, targetHeight);
        // canvas压缩并上传
        var dataURL = canvas.toDataURL(compressoption.mime||"image/jpeg",compressoption.quality||0.92);
        deferred.resolve({"status":200,"data":dataURL,"msg":""})
     };  
   
    return deferred
}
/**
 * @param dataurl:文件的base64编码
 * @param serveurl:后端接收上传的地址
 * @param postarg:后端接收的其他参数
 *@param base64key:后端接收base64编码的参数名称
 **/
function task_upload(dataurl,serveurl,postarg,base64key){
    var deferred = $.Deferred();
    var data = {}
    for(var i in postarg||{}){
        data[i] = urldecode(postarg[i]);
    }
    data[base64key||"base64data"]=dataurl
    $.ajax({
        "url":serveurl|| "/attach/upload",
        async:true,
        method:"post",
        data:data,
        success:function(result){
            deferred.resolve(result)
        },
        error(xhr,status,error){
            deferred.resolve({"status":400,"msg":"服务器繁忙请稍后再试"})
        }
        });
    return deferred
}	
})(jQuery)
