<?php
require_once(dirname(__FILE__).'/lib/authorize.php'); 
?>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta content="telephone=no" name="format-detection" />
<meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=2.0"/>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no">
<meta name="keywords" content="#" />
<meta name="description" content="#" />
<title>合同管理</title>
<link href="css/ionic.css" rel="stylesheet" type="text/css"/>
<link href="css/mp.css" type="text/css" rel="stylesheet" />
<script src="js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/functions.js"></script>
<script type="text/javascript" src="js/mobile.js"></script>
<script type="text/javascript" src="js/mobile_valid.js"></script>
<script type="text/template" id="customers_tmplate">
    <li>
        <a href="javascript:void(0);" id="customers_a_{id}" text="{customers_name}" value="{id}">
            <div class="banner_center" style="border:none;">
                <h2>{customers_name}</h2>
            </div>
        </a>
    </li>
</script>

<script type="text/template" id="pact_tmplate">
    <li id="pact_li_{id}" is_audit="{is_audit}" is_locked = "{is_locked}">
    <!-- <a href="javascript:void(0);" class="ckh_lock">锁定</a>
    <a href="javascript:void(0);" class="ckh_unlock">解锁</a> -->
    <?php 
        $authorize->GetCustomizeButton('pact/LockPact','ckh_lock','锁定');
        $authorize->GetCustomizeButton('pact/UnLockPact','ckh_unlock','解锁');
    ?>
    <a href="javascript:void(0);">
        <div class="banner_center">
            <h2>{pact_name}</h2>
            <p>
                编码：{pact_bm}<span style=" float:right;">{creator}/{write_time}</span>
            </p>
            <p>
                地址：{project_address} 
            </p>
            <p>
                客户名称：{customers_name} 
            </p>
            <p>
                签订日期：2016-09-06 
            </p>
            <p>
                联系人：{contact_man}
                <span style=" float:right;">联系电话：{contact_phone}</span>
            </p>
        </div>
    </a>
    <div class="banner_bottom">
        <p><?php $authorize->GetPowerGroup('pact');?></p>
    </div>
    </li>
</script>

<script type="text/javascript">
    $(function(){
        function lockPact(json){
            //console.dir(json.button.attr('id'));
            if(json.button.length == 0)return;
            if(json.attr_value == 1){  //remove 反审核按钮 
                $('#'+json.button.attr('id')).remove();
            }else{
                json.button.unbind("click").bind("click",{id:json.id,object:json.object},function(){
                    if(confirm('确定要锁定合同 ？此合同对应的工地将无法调度生产！！')){
                        var locked_id = json.id;
                        var mbObject = json.object;
                        var locked = 1;
                        var model = mbObject.DataJson.model;
                        var lockListId = model+'_li_'+locked_id;
                        var lockUrl = mbObject.DataJson.controller+'?action=LockPact&model='+model;
                        ajaxRequest(lockUrl,{id:locked_id},false,function(response){
                            if(response.result){
                                $('#'+lockListId).attr('is_locked',1);
                                mbObject.updateModifyRecord(locked_id);                   
                                msgbox(response.msg);
                            }else{
                                showError(response.msg);
                            }    
                        });
                    }
                });
            }
        }
        
        function unLockPact(json){
        //{id:id,object:mbObject,button:button,attr_value:attr_value};
            if(json.button.length == 0)return;
            if(json.attr_value == 0){  //remove 反审核按钮  
                $('#'+json.button.attr('id')).remove(); 
            }else{
                json.button.unbind("click").bind("click",{id:json.id,object:json.object},function(){
                    if(confirm('确定要解锁合同 ？')){
                        var locked_id = json.id;
                        var mbObject = json.object;
                        var locked = 0;
                        var model = mbObject.DataJson.model;
                        var lockListId = model+'_li_'+locked_id;
                        var lockUrl = mbObject.DataJson.controller+'?action=UnLockPact&model='+model;
                        ajaxRequest(lockUrl,{id:locked_id},false,function(response){
                            if(response.result){
                                $('#'+lockListId).attr('is_locked',0);
                                mbObject.updateModifyRecord(locked_id);                   
                                msgbox(response.msg);
                            }else{
                                showError(response.msg);
                            }    
                        });
                    }
                });
            }
        }
         
        
        var mobile = new MobileData({
            model : 'pact'
            ,viewModel:'view_pact'
            ,templateId:'pact_tmplate'
            ,appendId:'append_pact_id'
            ,controller:'../controller/pact.php'
            ,formId:'pact-form'
            ,searchId:'pact-search'
            ,buttonEvents:[
                {class_name:'ckh_lock',attr_name:'is_locked',handler:lockPact}
                ,{class_name:'ckh_unlock',attr_name:'is_locked',handler:unLockPact}
            ]
        });    

        mobile.initComboSelect({
            scopeId:'group_customers_id'
            ,valueField:'customers_id'
            ,textField:'customers_name'
            ,options:{
                model:'customers'
                ,templateId:'customers_tmplate'
                ,appendId:'append_customers_id'
                ,searchId:'customers-search'
                ,autoLoad:false
            }
        });        
    });
</script>
</head>
<body>
<div class="wap_content"> 
   <div class="wap_top"><a href="user_center.php" title="返回" class="wap_top_back"></a>
   <h2>合同明细</h2>
   <?php $authorize->GetTopAddButton('pact'); ?>
   </div>
   <div class="search_banner">
        <div class="search_content" id="pact-search">
            <div class="search_input">
                <i></i>
                <input type="text" placeholder="输入合同名称或编号关键字" searchParam="pact_name like '%{keywords}%' OR pact_bm like '%{keywords}%'" class="search_input_field keywords"/>
            </div>
            <div class="search_button"><input type="button" value="搜索" class="search_input_button submit"/></div>
        </div>
   </div>     
   <div class="banner_content">
        <ul id = 'append_pact_id'></ul>
        <div class="append_more"></div> 
   </div>
</div> 


 
<form class="form_window" id="pact-form">
<div class="form_content">
        <div class="wap_top"><a href="javascript:void(0);"  onclick="$('#pact-form').hide();" class="wap_top_close"></a>
       <h2>{oprate}合同信息</h2>
       </div>
        <div class="banner_content">
        <input type="hidden" name="id" />
        <ul class="one_banner">
        <li>
            <label>合同编号</label>
            <input type='text' placeholder="自动编号请留空" maxlength='50' class='input_value' name='pact_bm' />
        </li>
        <li>
            <label>合同名称</label>
            <input type='text' maxlength='50' class='input_value required' name='pact_name' required="required" />
        </li>
        <li id="group_customers_id">
            <label>客户名称</label>
            <input type='text' readonly="true" class='input_value combo_group_select' name='customers_name' required="required"/>
            <span class="group"><img src="images/search.png" /></span>
            <input type="hidden" name="customers_id" class="combo_group_select" />
        </li>
        <li>
            <label>项目地址</label>
            <input type='text' maxlength='50' class='input_value' name='project_address' />
        </li>
        <li>
            <label>总方量</label>
            <input type='number' class='input_value required' name='intending_cube' required="required" />
        </li>
        <li>
            <label>合同签订日期</label>
            <input type='date' class='input_value required' name='signup_date' required="required" />
        </li>
        <li>
            <label>合同终止日期</label>
            <input type='date' class='input_value required' name='end_date' required="required" />
        </li>
        <li>
            <label>供货日期</label>
            <input type='date' class='input_value' name='supply_date' />
        </li>
        <li><label>联系人</label><input type='text' maxlength='5' class='input_value' name='contact_man' /></li>
        <li><label>联系电话</label><input type='number' maxlength='13' class='input_value' name='contact_phone' /></li>
        <li><label>备注</label><input type='text' maxlength='200' class='input_value' name='other_notice' /></li>
        
        </ul> 
        <div class="form_bottom"></div>
        </div>
        <div class="bottom_a_button"><a href="javascript:void(0);" class="submit">提交</a></div>
</div>
</form>




<div class="group_customers_id" style="display:none;">
 <div class="bottom_window_mask"></div>
 <div class="bottom_window_content" style="height:80%;">
   <div class="bottom_window_topbutton">
   <a href="javascript:void(0);" onClick='$("div.group_customers_id").hide();' title="关闭" class="bottom_window_close"></a>                   <h4>请选择客户名称</h4>
   </div>
   <div class="search_content" id="customers-search" style="margin-top:0px;">
        <div class="search_input">
            <i></i>
            <input type="text" placeholder="输入客户名称关键字" searchParam="customers_name like '%{keywords}%'" 
                class="search_input_field keywords" />
        </div>
        <div class="search_button"><input type="button" value="搜索" class="search_input_button submit"/></div>
    </div>
   <div class="bottom_window" style="height:80%; max-height:400px; overflow:scroll;">
           <div class="banner_content">
            <ul id = 'append_customers_id' ></ul>
            <div class="append_more"></div> 
       </div>     
   </div>   
   </div>
</div>


  
</body>
</html>
