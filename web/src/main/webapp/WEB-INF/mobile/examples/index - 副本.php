<?php
session_start();
require_once("../dao/dao.php");
require_once("lib/function.php");
require_once("../lib/dbcache.php");   
$userdao = new DAO('users');
$user_id = 1;
$user_array = $userdao->GetFirstOrDefaultRow("id = 1");
$_SESSION['id'] = $user_array['id'];
$_SESSION['user_name'] = $user_array['user_name'];
$_SESSION['login_id'] = $user_array['login_id'];
$_SESSION['depart_id'] = $user_array['depart_id'];
$_SESSION['user_type'] = $user_array['user_type'];
$_SESSION['page_style'] = $user_array['page_style'];
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
<!--<link rel="apple-touch-icon-precomposed" sizes="114x114" href="">
<link rel="apple-touch-icon-precomposed" sizes="57x57" href=""> -->
<title>Mobile Phone System</title>
<link href="css/ionic.css" rel="stylesheet" type="text/css"/>
<link href="css/mp.css" type="text/css" rel="stylesheet" />  
</head>
<body>
<div class="wap_content"> 
   <div class="wap_top"><h2>ERP管理系统手机版</h2><a href="" title="通讯录" class="j_top_a5"></a></div>
   <?php
   foreach($index_func as $key=>$func_models){
        $func_model = $func_models['func_model'];     
        $func_items = $func_models['func_items'];
        //检查权限
        $power_flag = false;
        $power_array = array();
        foreach($func_items as $items=>$function){
            $func_id = $function['func_id'];
            $func_name = $function['func_name'];  
            $images = $function['images'];  
            $func_url = empty($function['erp_url']) ? $function['func_url'] : $function['erp_url'];
            $authorize = DbCache::GetUserUrlPower($user_id,null,$func_url);
            $func_url = 'view/'.$func_url;
            $is_authorized = empty($authorize[$func_url]) ? 0 : 1;
            $power_array[$func_id] = $is_authorized;
            if($is_authorized == 1){
                $power_flag = true;
            }
        }
        if(!$power_flag)continue;
        if($power_flag){   
   ?> 
   <div class="model_banner">
     <div class="model_banner_title"><i></i>销售管理</div> 
     <ul class="model_banner_ul">
   <?php
        }
        //遍历 权限
        $endl_flag = 0;
        foreach($func_items as $items=>$function){ 
            $css_class = '';
            if(++$endl_flag % 4 == 0){
                $css_class = 'model_banner_li_endl';
            }  
            $func_id = $function['func_id'];
            $func_name = $function['func_name'];  
            $images = $function['images'];  
            $func_url = $function['func_url'];
            echo $func_name.":".$power_array[$func_id]."<br/>";
            if(!empty($power_array[$func_id])){  
   ?>
       <a href="<?php echo $func_url;?>">
         <li><img src="images/<?php echo $images;?>" /><br/><?echo $func_name;?></li>
       </a>
   <?php
            }
        }    
        if($power_flag){        
   ?>
     </ul>  
     <div class="clear"></div>
  </div>
  <?php
        }
   }
  ?>

  
  <div class="j_bootm">
  <footer>
    <div class="w_current">
        <a href="">
            <i class="w_home"></i>
            <p>工作台</p>
        </a>
    </div>
    <div class="">
        <a href="">
            <i class="w_vshop"></i>
            <p>消息</p>
        </a>
    </div>
    <div class="">
        <a href="">
            <i class="w_frbj"></i>
            <p>日报</p>
        </a>
    </div>
    <div class="">
        <a href="">
            <i class="w_classify"></i>
            <p>统计报表</p>
        </a>
    </div>   
    <div class="">
        <a href="">
            <i class="w_f-cart"></i>
            <p>个人设置</p>
        </a>
    </div>
</footer>
</div>
</div>
</body>
</html>
