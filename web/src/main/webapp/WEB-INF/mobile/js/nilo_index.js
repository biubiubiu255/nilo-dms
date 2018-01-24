$(function(){
        var opts = {
            url: '../controller/weixin_orders.php?action=NewEntry'
            , formId: 'orders-form'
        };
        
        $("div.select_contact_address").hide();
        $("div.select_thing_type").hide();
        $("div.select_append_service").hide();
        $("div.select_payment_type").hide();
        $("div.other_thing_type").hide();
        $("div.other_thing_weight").hide();
        $("div.input_express_num").hide();
        
        
        //国内快递单号
        $("a.select_inside_num").click(function() {
            $("div.input_express_num").toggle();
        });
        $("a.close_express_num").click(function() {
            $("div.input_express_num").toggle();
        });
        
        
        $("a.sure_express_num").click(function() {
            var number = $("div.input_express_num input").val();
            if($.trim(number) == ''){
                alert(tip_json.input_express_num);
                return ;
            }
            $("div.select_express_number").html(number); 
            $("[name=express_number]", $("#" + opts.formId)).val(number); 
            $("div.input_express_num").toggle();    
        });
        
        // 选择寄件人
        $("a.close_select_person").click(function() {
            $("div.select_contact_address").toggle();
        });
        
        $("div.select_thing_type .thing_type.thing_select a li").each(function(){
            $(this).click(function(){
                var thing_type = $(this).text();  
                $("div.select_thing_type .thing_type.thing_select a li").each(function(){
                    $(this).removeClass("selected");
                });
                $(this).addClass("selected");
                //赋值
                if(thing_type == '填其它' || thing_type == 'Input others'){
                    $("div.other_thing_type").show();   
                    $("div.other_thing_type input").val("");   
                }else{
                    $("div.other_thing_type").hide();
                    $("div.other_thing_type input").val(thing_type);   
                }  
            });
        });
        
        $("div.select_thing_type .thing_type.wight_select a li").each(function(){
            $(this).click(function(){
                var thing_weight = $(this).attr('value');
                $("div.select_thing_type .thing_type.wight_select a li").each(function(){
                    $(this).removeClass("selected");
                });
                $(this).addClass("selected");
                //赋值
                if(thing_weight == 0){
                    $("div.other_thing_weight").show();   
                    $("div.other_thing_weight input").val("");   
                }else{
                    $("div.other_thing_weight").hide();
                    $("div.other_thing_weight input").val(thing_weight);   
                }
            });
        });
         

        
        //$("#other_thing_type").click(function(){
//            alert('other_thing_type');
//        });
        var select_type = 'select_receive_person';
        $("a.select_receive_person").click(function() {
            var obj = $("a.select_person_list");
            if(obj.length < 2){
                window.location.href="new_address.php?type=receive&user_id="+weixin_user_id+"&lang="+tip_json.language_param;
                return;
            }
            $("div.select_contact_address").toggle();
            select_type = 'select_receive_person'; 
        });
        
        $("a.select_send_person").click(function() {
            var obj = $("a.select_person_list"); 
            if(obj.length < 1){
                window.location.href="new_address.php?type=send&user_id="+weixin_user_id+"&lang="+tip_json.language_param;
                return;
            }
            $("div.select_contact_address").toggle();
            select_type = 'select_send_person'; 
        });
                 
        //选中时间操作
        $("a.select_person_list").click(function() {
            var selected_id = $(this).attr('value');
            if(select_type == 'select_receive_person'){
                $("a.select_receive_person").html($(this).html());  
                $("[name=receive_address]", $("#" + opts.formId)).val(selected_id);   
            }else{
                $("a.select_send_person").html($(this).html()); 
                $("[name=sender_address]", $("#" + opts.formId)).val(selected_id);
            } 
            $("div.select_contact_address").toggle();
        });
        
        
        //选择物品类别
        $("a.select_thing_type").click(function() {
            $("div.select_thing_type").toggle();
        });
        
        $("a.close_thing_type").click(function() {
            $("div.select_thing_type").toggle();
        });
        
        $("a.select_thing_sure").click(function() {
                var weight = $("div.other_thing_weight input").val(); 
                var type = $("div.other_thing_type input").val(); 
                $("div.select_thing_type_tip").html(type);  
                $("div.select_thing_weight_tip").html(weight+'kg');  
                if(weight == '' || type == ''){
                    alert( tip_json.input_completed);
                    return;
                }
                $("[name=thing_type]", $("#" + opts.formId)).val(type);
                $("[name=thing_weight]", $("#" + opts.formId)).val(weight);
                $("div.select_thing_type").toggle(); 
        });
        
        //增值服务
        $("a.select_append_service").click(function() {
            alert(tip_json.look_forword);
            return;
            $("div.select_append_service").toggle();
        });
        $("a.close_append_service").click(function() {
            $("div.select_append_service").toggle();
        });   
        
        //付款方式   
        
        $("a.select_payment_type").click(function() {
            return;
            $("div.select_payment_type").toggle();
        });
        $("a.close_payment_type").click(function() {
            $("div.select_payment_type").toggle();
        }); 
        
         
        
        

        function checkform(){
            $("#" + opts.formId).validVal(opts);
            var sender_address = $("[name=sender_address]", $("#" + opts.formId)).val();
            var receive_address = $("[name=receive_address]", $("#" + opts.formId)).val();
            var thing_type = $("[name=thing_type]", $("#" + opts.formId)).val();
//            var uploadfile = $("[name=uploadfile]", $("#" + opts.formId)).val();
            var express_num = $("[name=express_number]", $("#" + opts.formId)).val();
            if(sender_address == ''){
                alert(tip_json.select_sender);
                $("div.select_contact_address").toggle();
                select_type = 'select_send_person'; 
                return false;
            }
            if(receive_address == ''){
                alert(tip_json.select_recever); 
                $("div.select_contact_address").toggle();
                select_type = 'select_receive_person';
                return false;
            }
            if(sender_address == receive_address){
                alert(tip_json.address_same); 
                return false;
            }
            if(thing_type == ''){
                alert(tip_json.select_goods); 
                $("div.select_thing_type").toggle();
                return false; 
            }
            
            if(express_num == ''){
               $("div.input_express_num").toggle();
               alert(tip_json.input_express_num); 
               return false; 
            }
            
            return true;
        }
  
        $("a.submit").click(function(event) {   
            var btn = $(this);
            if (checkform()) {
                btn.attr("disabled", "disabled");
                showMask();
                $("#" + opts.formId).submit();
                var refreshClock = setInterval(function() {
                    var targetobj = document.getElementById('post-form-target');
                    if (isEmpty(targetobj)) {
                        clearInterval(refreshClock);
                    } else {
                        var win = targetobj.contentWindow;
                        var result_json = win.document.body.innerText;
                        if (!isEmpty(result_json)) {
                            closeMask();
                            clearInterval(refreshClock);
                            var json = toObject(result_json);
                            if (json.result) {
                                alert(tip_json.orders_submitted);
                                window.location.href="index.php?lang="+tip_json.language_param;
                            } else {
                                showError(json.msg);
                                btn.removeAttr("disabled");
                            }
                        }
                    }
                }, 500);
            }
        });
        
        
        
        
        
});