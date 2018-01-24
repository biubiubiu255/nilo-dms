// JavaScript Document
function MobileData(DataJson){
	DataJson = $.extend({},MobileData.defaults, DataJson); 	
	if(isEmpty(DataJson.controller)){
        DataJson.controller='../controller/basecontroller.php';
    }
    //find url setting
	if(DataJson.findUrl == ''){
	    DataJson.findUrl= '../controller/basecontroller.php?model='+DataJson.model+'&action=Find';	
	    if(DataJson.viewModel != ''){
		    DataJson.findUrl='../controller/basecontroller.php?model='+DataJson.viewModel+'&action=Find';	
	    }
    }
	this.requestIng = false;
	this.DataJson = DataJson;
	this.isComboSelect = DataJson.isComboSelect;
	var mbObject = this;


	if(mbObject.isComboSelect){
		var scrollWindow = $("div."+mbObject.DataJson.scopeId).find("div.bottom_window");
		var nScrollHight = 0; //滚动距离总长(注意不是滚动条的长度)
		var nScrollTop = 0;   //滚动到的当前位置
		var nDivHight = 420;
		// div内滚动事件处理。。
		//alert(mbObject.DataJson.scopeId);
		scrollWindow.scroll(function(){
			nScrollHight = $(this)[0].scrollHeight;
			nScrollTop = $(this)[0].scrollTop;
			//console.dir(nScrollTop +" "+ nDivHight +" "+ nScrollHight);
			if(nScrollTop + nDivHight >= nScrollHight){
				if(mbObject.requestIng) return;
				var page = mbObject.DataJson.page;
				var total = mbObject.DataJson.total;
				if (total == 0 && page > 1) {
					$('#'+mbObject.DataJson.appendId).parent().find('.append_more').html('没有找到任何数据');
				}else if(total > 0 && total <= mbObject.DataJson.pageSize){
					$('#'+mbObject.DataJson.appendId).parent().find('.append_more').html('');	
				} else if (page * (mbObject.DataJson.pageSize-1) < mbObject.DataJson.total) {
					$('#'+mbObject.DataJson.appendId).parent().find('.append_more').html('上移，加载更多数据');
					mbObject.requestIng = true;
					mbObject.paginate({clearHtml:false,appendParam:mbObject.DataJson.postParams});
				} else {
					$('#'+mbObject.DataJson.appendId).parent().find('.append_more').html('已经没有了');
				}
			}
				 
		});
	}else{

		$(window).scroll(function() {
			var scrollTop = $(this).scrollTop();
			var scrollHeight = $(document).height();
			var windowHeight = $(this).height();
			if (scrollTop + windowHeight >= scrollHeight) {
				if(mbObject.requestIng) return;
				var page = mbObject.DataJson.page;
				var total = mbObject.DataJson.total;
				if (total == 0 && page > 1) {
					$('#'+mbObject.DataJson.appendId).parent().find('.append_more').html('没有找到任何数据');
				}else if(total > 0 && total <= mbObject.DataJson.pageSize){
					$('#'+mbObject.DataJson.appendId).parent().find('.append_more').html('');	
				} else if (page * (mbObject.DataJson.pageSize-1) < mbObject.DataJson.total) {
					$('#'+mbObject.DataJson.appendId).parent().find('.append_more').html('上移，加载更多数据');
					mbObject.requestIng = true;
					mbObject.paginate({clearHtml:false,appendParam:mbObject.DataJson.postParams});
				} else {
					$('#'+mbObject.DataJson.appendId).parent().find('.append_more').html('已经没有了');
				}
			}
		});	
	}
	
	
	//自动加载数据
	if(DataJson.autoLoad){
		this.paginate({clearHtml:true});	
	}
	
	//搜索功能实现
	var searchId = DataJson.searchId;
	if($('#'+searchId).length > 0){
		$("#"+searchId).find('.submit').unbind("click").bind("click",{ object:this } ,this.startSearch);
	}
	
	//增加功能实现
	var addButton = DataJson.model+'-add-button';
	if($('#'+addButton).length > 0){
		$("#"+addButton).bind("click",{ object:this } ,this.newRecord);
	}
	
	//form title template 	
	var formId = mbObject.DataJson.formId; 
	if(isEmpty(this.formTitleTemplate) && !isEmpty(formId)){
		var headTitle = $('#'+formId).find('h2');
		this.formTitleTemplate = headTitle.html();
	}	
}


/*
set form title
*/
MobileData.prototype.setFormTitle = function(title){
	var formId = this.DataJson.formId;
	var headTitle = $('#'+formId).find('h2').html(title);	
};


MobileData.prototype.resetForm = function(){
	var formId = this.DataJson.formId;
};

MobileData.prototype.getMobileData=function(){
    return $('#' + this.DataJson.appendId);
};

MobileData.prototype.appendNewRecord = function(primary_id){
	var mbObject = this;
	var findUrl = this.DataJson.findUrl;
    var idField = mbObject.DataJson.idField;
    var searchParam = idField+" = '"+primary_id+"'";
    //新建之前移除已经存在的list
    var model = mbObject.DataJson.model;
    var existsId = $('#'+model+'_li_'+primary_id);
    
	ajaxRequest(findUrl,{
		parameters:escape(searchParam)
	},false,function(response){
		if(response.rows){
            //console.dir(existsId);
            //console.dir(existsId.length); 
            if(existsId.length > 0){
                existsId.remove(); 
            }
			response.appendType = 'prepend';
			mbObject.formatterData(response);
		}else{
			mbObject.paginate({clearHtml:true,appendParam:mbObject.DataJson.postParams});		
		}
	});
};


/*
编辑数据
*/
MobileData.prototype.editData = function(e){
	var edit_id = e.data.id;	
	var mbObject = e.data.object;
	var formId = mbObject.DataJson.formId; 
    if(!isEmpty(e.data.formId)){
        formId = e.data.formId;
    }
	var model = mbObject.DataJson.model;
    
    var editListId = model+'_li_'+edit_id;
    var is_audit = $('#'+editListId).attr('is_audit');
    if(is_audit == 1){
        showWarning('已经审核的记录不允许修改！');
        return;
    }
    
	//显示表单
	$("#"+formId).show();
	$("#"+formId+" .form_content").stop().animate({width: '100%'});

	if(isEmpty(e.data.formId)){
        var headTitle = $('#'+formId).find('h2');
        var title= mbObject.formTitleTemplate.replace('{oprate}','编辑');
        headTitle.html(title);
    }
	mbObject.readOnlyField(true);
	//请求数据，重新设置表单
	var idField = mbObject.DataJson.idField;
	var findUrl = mbObject.DataJson.findUrl;
	if(!isEmpty(mbObject.DataJson.editGetUrl)){
		findUrl = mbObject.DataJson.editGetUrl;	
		idField = 'id';
	}
	var searchParam = idField+" = '"+edit_id+"'";
	ajaxRequest(findUrl,{
		parameters:escape(searchParam)
	},false,function(response){
		if(response.rows){
			if(response.total > 0){
				$("#"+formId).triggerHandler("loadForm",response.rows[0]);
				if(!isEmpty(mbObject.DataJson.editRecord)){
					var afterLoadForm = mbObject.DataJson.editRecord.afterLoadForm;
					if(!isEmpty(afterLoadForm)){
						invokeCallBack(afterLoadForm, formId);			
					}
				}
                if(!isEmpty(e.data.afterLoadForm)){
                    invokeCallBack(e.data.afterLoadForm, response.rows[0]);
                }
			}else{
				//showError('数据获取失败，请重试！');
			}
		}
	});
	//after load form
	var postUrl = mbObject.DataJson.controller+'?action=EditEntry&model='+mbObject.DataJson.model
	if(!isEmpty(e.data.postUrl)){
        postUrl = e.data.postUrl;
    }
    var defaultOptions = {
        submitUrl:postUrl
		,showMsg: true
		,successMsg: ''
		,formId: formId
		,beforeSubmit:function(formId){ return true;}
		,callback:function(response){}
		,object:mbObject
		,action:'edit'
		,editId:edit_id
    };
	var options = $.extend(defaultOptions,mbObject.DataJson.editRecord); 
	$("#" + formId).validVal(options);
	
};

MobileData.prototype.updateModifyRecord = function(primary_id){
	var mbObject = this;
	var findUrl = this.DataJson.findUrl;
	var idField = mbObject.DataJson.idField;
	var searchParam = idField+" = '"+primary_id+"'";
	ajaxRequest(findUrl,{
		parameters:escape(searchParam)
	},false,function(response){
		if(response.rows){
			response.appendType = 'modify';
			response.primary_id = primary_id;
			mbObject.formatterData(response);
		}else{
			mbObject.paginate({clearHtml:true});		
		}
	},false);
};


//使指定字段 readOnly
MobileData.prototype.readOnlyField = function(value){
    try{
        if(value){
            value = 'readonly';
        }
        $('[altReadOnly="true"]',$('#'+this.DataJson.formId)).each(function(){
            $(this).attr('readonly',value);
        });
    }catch(e){
        showError('禁用/启用字段: '+e);
    }
};



/*
添加方法事件执行
*/
MobileData.prototype.newRecord = function(e){
	var mbObject = e.data.object;
	var formId = mbObject.DataJson.formId;
    if(!isEmpty(e.data.formId)){
        formId = e.data.formId;
    } 
	$("#"+formId).show();
	$("#"+formId+" .form_content").stop().animate({width: '100%'});
	$("#"+formId).triggerHandler("resetForm");
    if(isEmpty(e.data.formId)){ 
	    var headTitle = $('#'+formId).find('h2');
	    var title= mbObject.formTitleTemplate.replace('{oprate}','新建');
	    headTitle.html(title);
    }
	mbObject.readOnlyField(false);
	//after load form
	if(!isEmpty(mbObject.DataJson.newRecord)){
		var afterLoadForm = mbObject.DataJson.newRecord.afterLoadForm;
		if(!isEmpty(afterLoadForm)){
			invokeCallBack(afterLoadForm, formId);			
		}
	}
    //自定义load事件
    if(!isEmpty(e.data.afterLoadForm)){
        invokeCallBack(e.data.afterLoadForm, mbObject);
    }
    
	var postUrl = mbObject.DataJson.controller+'?action=NewEntry&model='+mbObject.DataJson.model;
	if(!isEmpty(e.data.postUrl)){
        postUrl = e.data.postUrl;
    }
    var beforePostValid = function(formId){ return true;};
    if(!isEmpty(e.data.beforeSubmit)){
        beforePostValid = e.data.beforeSubmit;
    }
    var defaultAction = 'new';
    if(!isEmpty(e.data.action)){
        defaultAction = e.data.action;
    } 
    var defaultCallBack = function(response){};
    if(!isEmpty(e.data.callback)){
        defaultCallBack = e.data.callback;
    }
    
    var defaultOptions = {
        submitUrl:postUrl
		,showMsg: true
		,successMsg: ''
		,formId: formId
		,beforeSubmit:beforePostValid
		,callback:defaultCallBack
		,object:mbObject
		,action:defaultAction
    };
	var options = $.extend(defaultOptions,mbObject.DataJson.newRecord); 
	$("#" + formId).validVal(options);
};




/*
打开指定表单
*/
MobileData.prototype.openFormWindow = function(json){
	var mbObject = json.mbObject;
	var formId = json.formId; 
	$("#"+formId).show();
	$("#"+formId+" .form_content").stop().animate({width: '100%'});
	if(!isEmpty(json.formTitle)){
		var headTitle = $('#'+formId).find('h2');
		headTitle.html(json.formTitle);
	}
	if(!isEmpty(json.afterOpenWindow)){
		invokeCallBack(json.afterOpenWindow, formId);			
	}
	if(isEmpty(json.postUrl))return;
	
	$("#"+formId).triggerHandler("resetForm");
	//after load form
	if(!isEmpty(json.afterLoadForm)){
		invokeCallBack(json.afterLoadForm, formId);			
	}
	var postUrl = json.postUrl;
	var defaultOptions = {
        submitUrl:postUrl
		,showMsg: true
		,successMsg: ''
		,formId: json.formId
		,beforeSubmit:function(formId){ return true;}
		,callback:function(response){}
		,object:mbObject
		,action:'open'
    };
	var options = $.extend(defaultOptions,json); 
	$("#" + formId).validVal(options);
};


/**
 * form表单初始化、提交
 */
MobileData.prototype.initSubmitForm = function(json){
	var mbObject = json.mbObject;
	var formId = json.formId; 
	if(!isEmpty(formId)){
		mbObject.DataJson.formId = formId;
	}
	if(!isEmpty(json.beforeInitForm)){
		invokeCallBack(json.beforeInitForm, formId);			
	}
	if(isEmpty(json.postUrl))return;
	if(json.resetForm){
		$("#"+formId).triggerHandler("resetForm");
	}
	var postUrl = json.postUrl;
	var defaultOptions = {
        submitUrl:postUrl
		,showMsg: true
		,successMsg: ''
		,formId: json.formId
		,beforeSubmit:function(formId){ return true;}
		,callback:function(response){}
		,object:mbObject
		,warningMsgType:'tips'
		,action:'submit'
    };
	var options = $.extend(defaultOptions,json); 
	$("#" + formId).validVal(options);
};


MobileData.prototype.getFormFieldValue = function (fieldName) {
    var field = $('#'+this.DataJson.formId).find(":input[name='" + fieldName + "']");
    //console.dir(this.DataJson.formId+" "+fieldName);
    if (isEmpty(field)) {
        showError('没有找到 ' + fieldName + " 的表单项!");
        return "";
    }
    if (field.is("input:radio") || field.is("input:checkbox")) {
        return field.attr('checked');
    } else {
        return $.trim(field.val());
    }
};


MobileData.prototype.getFormField = function (fieldName) {
    var field = $('#'+this.DataJson.formId).find(":input[name='" + fieldName + "']");
    if (isEmpty(field)) {
        showError('没有找到 ' + fieldName + " 的表单项!");
        return "";
    }
    return field;
};


MobileData.prototype.setFormFieldValue = function (fieldName, value) {
    var field = $('#'+this.DataJson.formId).find(":input[name='" + fieldName + "']");   
    if (field.length == 0) {
        showError('没有找到 ' + fieldName + " 的表单项!");
    }
    if (field.is("input:radio") || field.is("input:checkbox")) {
        field.attr('checked', value);
    } else {
        field.val(value);
    }
};




//使指定字段 readOnly
MobileData.prototype.setFormFieldReadOnly = function(field_name,boolValue){
    var field = this.getFormField(field_name);
    if(!isEmpty(field)){
        $(field).attr('readonly',boolValue); 
    }else{
        showError('禁用/启用字段: '+e); 
    }
};


/**
 * 初始化顶部 日期 tab
 */
MobileData.prototype.initDateTab = function (init_id, clickfunction,initDate) {
	var mbObj = this;
	var now_date = GetCurrentTime('YYYY-MM-DD');
	if(!isEmpty(initDate)){
		now_date = initDate;
	}
	var totalTabNum = Math.floor($(window).width()/85);
	if(totalTabNum < 3) totalTabNum = 3; 
	var firstDiffDay = -Math.floor(totalTabNum / 2);
	for(var i= firstDiffDay ,j=0;j<= totalTabNum;i++,j++){
		var mydate = getMyDate(now_date,i);	
		if(mydate == now_date){
			$('#'+init_id).append('<a href="javascript:void(0);" position="a'+j+'" date="'+mydate+'" class="current">'+getMyDate(mydate,0,'MM-DD')+'</a>');
		}else{
			$('#'+init_id).append('<a href="javascript:void(0);" position="a'+j+'" date="'+mydate+'">'+getMyDate(mydate,0,'MM-DD')+'</a>');
		}
	} 
	var jsonParam = {date:now_date,initId:init_id,callback:clickfunction,mbObject:mbObj};
	$('#'+init_id).find('a').unbind("click").bind("click",jsonParam,function(e){
		var initIdParam = e.data.initId;
		var mbObject = e.data.mbObject;
		var func = e.data.callback;
		$('#'+initIdParam).find('a').removeClass('current');	
		var click_date = $(this).attr('date');
		$(this).addClass('current');
        var position= $(this).attr('position');
        invokeCallBack(clickfunction, {date:click_date});	
        if(position == 'a0' || position == 'a'+totalTabNum){
        	$('#'+initIdParam).html('');
        }
        mbObject.initDateTab(initIdParam,func,click_date);
	});
	return now_date;
};



/*
搜索方法事件执行
*/
MobileData.prototype.startSearch = function(e){
	var mbObject = e.data.object;
	var searchId = mbObject.DataJson.searchId;
	var inputField = $("#"+searchId).find('.keywords');
	var keywords = inputField.val();
	var searchRule = inputField.attr('searchParam');
 	var searchParam = '';
	var initSearchParam = mbObject.DataJson.initSearchParam;
	if($.trim(keywords) != ''){
		searchParam = searchRule.replace(/{keywords}/g,keywords);
		if(initSearchParam != ''){
			searchParam = initSearchParam +' AND ('+searchParam+')';				
		}
	}
	//alert("searchParam:"+searchParam+"\n initSearchParam:"+initSearchParam);
	mbObject.DataJson.searchParam = searchParam;
	mbObject.paginate({
		clearHtml:true
	});	
	var afterSearch = mbObject.DataJson.afterSearch;
	if(!isEmpty(afterSearch)){
		invokeCallBack(afterSearch, mbObject.DataJson.formId);			
	}
};


/*
手动删除数据
*/
MobileData.prototype.removeData=function(options){
	var defaultOptions = {
        removeUrl:this.DataJson.controller+'?action=DelEntry&model='+this.DataJson.model
        ,callback: function(){}
		,deleteId:'0'
    };  
	var del_id = options.deleteId;
    options = $.extend(defaultOptions,options); 
	var callback = options.callback;
	var removeUrl = options.removeUrl;
	if(confirm('确定要删除 ？')){
		ajaxRequest(removeUrl,{
			id:del_id
		},false,function(response){
			invokeCallBack(callback, response);   
		});
	}
};

/*
删除数据，绑定方法自动删除
*/
MobileData.prototype.deleteData = function(e){
	var del_id = e.data.id;
	var mbObject = e.data.object;
	var model = mbObject.DataJson.model;
	var removeListId = model+'_li_'+del_id;
	var is_audit = $('#'+removeListId).attr('is_audit');
	if(is_audit == 1){
		showWarning('已经审核的记录不允许删除！');
		return;
	}
	
	var removeUrl = mbObject.DataJson.controller+'?action=DelEntry&model='+model;
	if(confirm('确定要删除 ？')){
		ajaxRequest(removeUrl,{
			id:del_id
		},false,function(response){
			if(response.result){
				$('#'+removeListId).remove();	
				msgbox(response.msg);
			}else{
                showError(response.msg);
            }    
		});
	}
};


//审核数据
MobileData.prototype.auditData = function(e){
    var audit_id = e.data.id;
    var mbObject = e.data.object;
	var audit_value = e.data.audit;
    var model = mbObject.DataJson.model;
    var auditListId = model+'_li_'+audit_id;
    var is_audit = $('#'+auditListId).attr('is_audit');
    if(is_audit == 1 && audit_value == 1){
        showWarning('该记录已审核！');
        return;
    }
	
	if(is_audit == 0 && audit_value == 0){
        showWarning('该记录未审核，无需反审核！');
        return;
    }
	var auditUrl = mbObject.DataJson.controller+'?action=AuditEntry&model='+model;
	var audit_name = '审核';
	var postParam = {id:audit_id,is_audit:1,audit_advice:'同意'};
	if(!isEmpty(mbObject.DataJson.auditUrl)){
		auditUrl = mbObject.DataJson.auditUrl;
	}
	if(audit_value == 0){
		audit_name = '反审核';
		postParam = {id:audit_id,is_audit:0};
		if(!isEmpty(mbObject.DataJson.unAuditUrl)){
			auditUrl = mbObject.DataJson.unAuditUrl;
		}
	}
    if(confirm('确定要 '+audit_name+'？')){
        ajaxRequest(auditUrl,postParam,false,function(response){
            if(response.result){
                $('#'+auditListId).attr('is_audit',audit_value);
				mbObject.updateModifyRecord(audit_id);                   
                msgbox(response.msg);
            }else{
                showError(response.msg);
            }    
        });
    }
};


MobileData.prototype.reload = function(options) {  
    var defaultOptions = {
        findUrl:this.DataJson.findUrl
        ,appendId:this.DataJson.appendId
        ,searchParam:this.DataJson.searchParam  
        ,appendParam:{}
        ,callback: function(){}
        ,defaultFormatter:true
        ,templateId:this.DataJson.templateId
        ,clearHtml:true
        ,loadMask:true
        ,authorized:1
    };
    options = $.extend(defaultOptions,options);   
    var mbObject = this; 
    mbObject.paginate({clearHtml:options.clearHtml,searchParam:options.searchParam,loadMask:options.loadMask});   
};

/*
分页查询
*/
MobileData.prototype.paginate = function(options) {
	var defaultOptions = {
        findUrl:this.DataJson.findUrl
		,appendId:this.DataJson.appendId
        ,searchParam:""
		,appendParam:{}
		,callback: function(){}
		,defaultFormatter:true
		,templateId:this.DataJson.templateId
		,clearHtml:true
        ,loadMask:true
		,authorized:1
    };
	options = $.extend(defaultOptions,options); 
	var url = options.findUrl;
	var mbObject = this;
    if (options.clearHtml) {
        mbObject.DataJson.page = 1;
        $('#' + options.appendId).html('');
		$('#'+mbObject.DataJson.appendId).parent().find('.append_more').html('');
    }
	mbObject.DataJson.clearHtml = false;
    var callback = options.callback;
	var searchParam = options.searchParam;
	//没有指定条件就按上次查询的条件
	if(isEmpty(searchParam)){
		searchParam = mbObject.DataJson.searchParam;	
	}else{
		mbObject.DataJson.searchParam = searchParam;	
	}
    var param = escape(searchParam);
	//初始条件判断,初始条件不空，搜索条件空，则按初始条件。
	if(!isEmpty(mbObject.DataJson.initSearchParam) && isEmpty(mbObject.DataJson.searchParam)){
		param = escape(mbObject.DataJson.initSearchParam);		
	}
    var page = mbObject.DataJson.page;
    var rows = mbObject.DataJson.pageSize;
    var sort = mbObject.DataJson.sortName;
    var order = mbObject.DataJson.sortOrder;
    var authorized = mbObject.DataJson.authorized;
	//alert("postparam:"+param);
    var params = {"parameters": param, "page": page, "rows": rows, "sort": sort, "order": order,"authorized":authorized};
	params = $.extend(params,defaultOptions.appendParam); 
	params = $.extend(params,mbObject.DataJson.postParams); 
	$('#'+mbObject.DataJson.appendId).parent().find('.append_more').html('玩命加载中，请稍后……');
	if(options.loadMask){showMask();}
    var xhr = $.ajax({
        url: url,
        type: 'POST',
        data: params,
        traditional: true,
        success: function(json) {
            try {
                var response = toObject(json);
				var total = response.total;
                if (options.clearHtml && total == 0) {
					$('#'+mbObject.DataJson.appendId).parent().find('.append_more').html('没有找到任何数据');
                }else if(total > 0 && total <= mbObject.DataJson.pageSize){
					$('#'+mbObject.DataJson.appendId).parent().find('.append_more').html('');	
				} else{
					$('#'+mbObject.DataJson.appendId).parent().find('.append_more').html('上移，加载更多数据');	
				}
				if(response.Relogin == "yes"){
					var msg = response.msg;
					$('#'+mbObject.DataJson.appendId).parent().find('.append_more').html(msg);
					showWarning(msg);
					window.location.href='index.php';
				}
				if(!options.defaultFormatter)return;
				mbObject.formatterData(response);
				mbObject.DataJson.total = response.total;
				mbObject.DataJson.page = mbObject.DataJson.page + 1;
				if (callback) {
                    invokeCallBack(callback, response);
                }
            } catch (e) {
                showError(response.msg);
            }
			mbObject.requestIng = false;
            closeMask();
        },error: function (textStatus) {
    		//showError('ERROR: 请求发生异常，请重试!');
        },
        complete: function (XMLHttpRequest,status) {
            if(status == 'timeout') {
                 //xhr.abort();    超时后中断请求
                //showError('网络超时，请刷新!');
            }
        }
    });
};



/*
	替换template中的定义的变量
*/
MobileData.prototype.formatTemplate = function (dta, tmpl) {  
	var format = {  
		name: function(x) {  
			return x  
		}  
	};  
	return tmpl.replace(/{(\w+)}/g, function(m1, m2) {  
		if (!m2)  
			return ""; 
		var value = dta[m2] == 'undifined' ? "":dta[m2];
		return (format && format[m2]) ? format[m2](dta[m2]) : value;  
	});  
};

MobileData.prototype.dicValueFomatter = function (gethtml) {
	var start  = gethtml.indexOf('dicValueFormat');
	var searchHtml = gethtml;
    if(start != -1){
    	while (true){
            var tempStr = searchHtml.substring(start);
            var end =tempStr.indexOf(')');
            var formatterFunc = tempStr.substring(0,end+1);
            searchHtml = tempStr.substr(end);
            if(!isEmpty(formatterFunc)) {
                var get_value ;
                jQuery(get_value = toObject(formatterFunc));
                if(!isEmpty(get_value)) {
                    gethtml = gethtml.replace(formatterFunc, get_value);
                }
            }
            start  = searchHtml.indexOf("dicValueFormat");
            if(start == -1){
            	break;
			}

		}
    }
    return gethtml;
};
/*
	按template进行附加，并对权限按钮进行控制。
*/
MobileData.prototype.formatterData = function (response) {
	var mbObject = this;
	var model = mbObject.DataJson.model;
	var rows = response.rows;
	var templateId = this.DataJson.templateId;
	var appendId = this.DataJson.appendId;
	var html = $('#'+templateId).html();
	var arr = [];  
	var ids = [];
	$.each(rows, function(i, o){ 
		var gethtml = mbObject.formatTemplate(o, html);

        gethtml = mbObject.dicValueFomatter(gethtml);
        //替换函数
        if(gethtml.indexOf('renderBool') != -1){
            gethtml = gethtml.replace(/renderBool\(0\)/g,'<font color="red">否</font>');
            gethtml = gethtml.replace(/renderBool\(1\)/g,'<font color="green">是</font>');
        }
		//替换函数
        if(gethtml.indexOf('renderAudit') != -1){
            gethtml = gethtml.replace(/renderAudit\(0\)/g,'<font color="red">未审核</font>');
            gethtml = gethtml.replace(/renderAudit\(1\)/g,'<font color="green">已审核</font>');
        }
		
		//配比状态特殊处理
		if(gethtml.indexOf('FORMULA_STATUS') != -1){
			var formula_audit = o.formula_audit;
			var formula_status = o.formula_status;
			if(formula_status == '0'){
				gethtml = gethtml.replace('FORMULA_STATUS','<font color="red">配比待下发</font>');
			}else{
				if(parseInt(formula_status) != parseInt(formula_audit)){
					gethtml = gethtml.replace('FORMULA_STATUS','<font color="blue">配比待审核</font>');
				}else{
					gethtml = gethtml.replace('FORMULA_STATUS','<font color="green">配比已下发</font>');
				}
			}
        }
		
		//配比状态特殊处理
		if(model == 'delivery' && gethtml.indexOf('DELIVERY_STATUS') != -1){
			var is_active = o.is_active;
			 
			if(is_active == '0'){
				gethtml = gethtml.replace('DELIVERY_STATUS','<i class="red">作废</i>');
			}else{
				gethtml = gethtml.replace('DELIVERY_STATUS','<i class="blue">有效</i>');
			}
        }
		
		
		if(model == 'depotin'){
			var in_number = o.in_number;
			var tone_value = (in_number/1000).toFixed(2);
			gethtml = gethtml.replace('IN_NUMBER',tone_value);
		}
		
		if(model == 'metage'){
			var net_weigh = o.net_weigh;
			var tone_value = (net_weigh/1000).toFixed(2);
			gethtml = gethtml.replace('NET_WEIGHT',tone_value);
		}
		
		//车辆状态、车辆类型处理
		if(model == 'car'){
			if(gethtml.indexOf('renderCarStatus') != -1){
				var car_status = o.car_status;
				switch(car_status){
					case '0':
						gethtml = gethtml.replace(/renderCarStatus\(0\)/g,'<font color="green">可调度</font>');
						break;
					case '1':
						gethtml = gethtml.replace(/renderCarStatus\(1\)/g,'<font color="red">出车</font>');
						break;
					case '2':	
						gethtml = gethtml.replace(/renderCarStatus\(2\)/g,'<font color="blue">休息</font>');
						break;
					default:	
						gethtml = gethtml.replace(/renderCarStatus\(3\)/g,'维修');
						break;
				}
			}
			if(gethtml.indexOf('renderCarType') != -1){
				var car_type = o.car_type;
				switch(car_type){
					case '0':
						gethtml = gethtml.replace(/renderCarType\(0\)/g,'搅拌车');
						break;
					case '1':
						gethtml = gethtml.replace(/renderCarType\(1\)/g,'泵车');
						break;
					case '2':	
						gethtml = gethtml.replace(/renderCarType\(2\)/g,'铲车');
						break;
					default:	
						gethtml = gethtml.replace(/renderCarType\(3\)/g,'机动车');
						break;
				}
			}
		}
		
		
		
		arr.push(gethtml);  
		var idField = mbObject.DataJson.idField;
		var id = o[idField];
		ids.push(id);
	});  
	if(response.appendType == 'prepend'){
		$('#'+appendId).prepend(arr.join(''));
	}else if(response.appendType == 'modify'){
		var primary_id = response.primary_id;
		var appendListId = model+"_li_"+primary_id;
		$('#'+appendListId).html(arr.join(''));
	}else{
		$('#'+appendId).append(arr.join(''));
	}
	var model = mbObject.DataJson.model;
	
	//如果没有权限处理banner
	if($('#'+appendId).find('a').length > 0){
		$.each(ids, function(n, id){ 
			var template_del_id = model+'_delete_'+id;
			var template_edit_id = model+'_edit_'+id;
            var template_audit_id = model+'_audit_'+id;
            var template_unaudit_id = model+'_unaudit_'+id;
			if($("#"+template_del_id).length > 0){
                $("#"+template_del_id).unbind("click").bind("click",{id:id,object:mbObject},mbObject.deleteData);    
            }
            if($("#"+template_edit_id).length > 0){	
			    $("#"+template_edit_id).unbind("click").bind("click",{id:id,object:mbObject},mbObject.editData);
            }
            
            //审核与反审核处理
            var is_audit = $('#'+model+'_li_'+id).attr('is_audit');
            if($("#"+template_audit_id).length > 0){  
                if(is_audit == 1){  //remove 审核按钮  
                    $("#"+template_audit_id).remove();
                }else{
                    $("#"+template_audit_id).unbind("click").bind("click",{id:id,object:mbObject,audit:1},mbObject.auditData);
                }
            }
            if($("#"+template_unaudit_id).length > 0){    
                if(is_audit == 0){  //remove 反审核按钮  
                    $("#"+template_unaudit_id).remove();
                }else{
                    $("#"+template_unaudit_id).unbind("click").bind("click",{id:id,object:mbObject,audit:0},mbObject.auditData);
                }
            }
            //处理自定义事件
            var buttonEvents = mbObject.DataJson.buttonEvents;
            for(var i=0;i<buttonEvents.length;i++){
                var buttonJson = buttonEvents[i];
                var class_name = buttonJson.class_name;
                var attr_name = buttonJson.attr_name;
                var handler = buttonJson.handler;  
                var button = $('#'+model+'_li_'+id).find('a.'+class_name); 
                var button_id = class_name+'-button-'+id;
                button.attr('id',button_id);
                var attr_value = $('#'+model+'_li_'+id).attr(attr_name); 
                if(button.length > 0 && !isEmpty(handler)){
                    var paramJson = {id:id,object:mbObject,button:button,attr_value:attr_value};
                    invokeCallBack(handler, paramJson);
                }
            }
		});
		/*//如果没有任何权限
		$('#append_project_id').find('.banner_bottom').css('display','none'); 
		*/
	}
	
	//如果为iscomboselect，则绑定 combo select事件,获取属性字段
	if(mbObject.isComboSelect){
		$.each(ids, function(n, id){ 
			var appendListId = model+"_a_"+id;
			var getText = $('#'+appendListId).attr('text');
			var getValue = $('#'+appendListId).attr('value');
			$("#"+appendListId).unbind("click").bind("click",{value:getValue,text:getText,object:mbObject},function(e){
				var value = e.data.value;
				var text = e.data.text;
				var object = e.data.object;
				var valueField = object.valueField;
				var textField = object.textField;	
				var scopeId = object.scopeId;	  
				var vField = $("#"+scopeId).find("input[name='"+valueField+"']");
				var tField = $("#"+scopeId).find("input[name='"+textField+"']");
				if(vField.length > 0){
					vField.val(value);
				}
				if(tField.length > 0){
					tField.val(text);
				}
				$("div."+scopeId).hide();	
                //处理附加设定值
                var appendSetValue = mbObject.DataJson.appendSetValue;
                if(!isEmpty(appendSetValue) && appendSetValue.length > 0){
                    for(i=0;i<appendSetValue.length;i++){
                        var json = appendSetValue[i];
                        //{field_id:'customers_id',attr_name:'customers_id'}
                        var field_id = json.field_id;
                        var attr_name = json.attr_name;
                        var attr_value = $('#'+appendListId).attr(attr_name);
                        if(attr_value != 'undefined'){
                            $('#'+field_id).val(attr_value);
                        }
                    }    
                }
                //处理回调函数
                var paramJson = {id:id,list_id:appendListId};
                invokeCallBack(mbObject.DataJson.onClick, paramJson);																		
			});	
		});
	}
};


MobileData.prototype.initComboSelect = function (initOpts) {
	var group_id = initOpts.scopeId;
	var inited = false;
	var iniMb = null;
	var selector = $('#'+group_id+" .group");
	if(!isEmpty(initOpts.clickBindId)){
		selector = $('#'+initOpts.clickBindId);
	}
//	console.dir(selector);
	selector.unbind("click").bind("click",{},function(){
		if($(this).attr('readonly') == 'readonly') return;
		$("div."+group_id).toggle();
		if(!inited){
			//赋值scopeId,便于内部判断。
			initOpts.options.scopeId = group_id;
			initOpts.options.isComboSelect = true;
			iniMb = new MobileData(initOpts.options);
			iniMb.valueField = initOpts.valueField;
			iniMb.textField = initOpts.textField;
			iniMb.scopeId = initOpts.scopeId;
			//设置下拉框init初始化查询条件
			var searchId = initOpts.options.searchId;
			//beforeLoad之前函数
			if(!isEmpty(initOpts.options.beforeLoad)){
				invokeCallBack(initOpts.options.beforeLoad, {scopeId:scopeId,searchId:searchId});	
			}
			var initSearchParam = "";
			if(!isEmpty($('#'+searchId).find('.keywords').attr('initSearchParam'))){
				initSearchParam = $('#'+searchId).find('.keywords').attr('initSearchParam')	
			}
			inited = true;
			iniMb.paginate({clearHtml:true,searchParam:initSearchParam});
		}else{
			//清理搜索条件
			var searchId = iniMb.DataJson.searchId;
			var scopeId = iniMb.DataJson.searchId;
            var initSearchParam = "";
			if($('#'+searchId).length > 0){
				var inputField = $("#"+searchId).find('.keywords');
				//beforeLoad之前函数
				if(!isEmpty(initOpts.options.beforeLoad)){
					invokeCallBack(initOpts.options.beforeLoad, {scopeId:scopeId,searchId:searchId});	
				}
				//设置下拉框init初始化查询条件
				inputField.val('');
				if(!isEmpty($('#'+searchId).find('.keywords').attr('initSearchParam'))){
					initSearchParam = $('#'+searchId).find('.keywords').attr('initSearchParam');
				}
			}
            iniMb.paginate({clearHtml:true,searchParam:initSearchParam});
		}
	});
};




MobileData.defaults = {
	page: 1
    , pageSize: 10
    , total: 0
    , findUrl:''
	, editGetUrl:''
	, auditUrl:''
	, unAuditUrl:''
	, idField:'id'
    , sortName: 'id'
    , sortOrder: 'desc'
	, initSearchParam:''
    , searchParam: ''
    , authorized:0
	, appendId: ''
   	, clearHtml: true	
	, postParams:{}
	, autoLoad:true
	, model:''
	, formId:''
	, searchId:''  
	, viewModel:''
	, loadMask:true
	,isComboSelect:false
    ,buttonEvents:[]
};

