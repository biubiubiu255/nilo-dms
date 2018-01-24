<?php
session_start();
header("Content-Type:text/html;charset=UTF-8");
if (empty($_SESSION['id'])) {
    echo '<script type="text/javascript">alert("会话已过期,请重新登陆!");location.href="index.php";</script>';
    exit(0);
}
$session_id = $_SESSION['id'];
require_once(dirname(__FILE__).'/../../lib/dbcache.php'); 
require_once(dirname(__FILE__).'/../../lib/pdo_conn.php'); 
require_once(dirname(__FILE__).'/function.php');  

class Authorize{
    private $buttonArray;
    private $jsonner;
    private $connection;

    function get_connection() {
        if (empty($this->connection))
            return pdo_conn::get_conn();
        return $this->connection;
    }
    
    
    function getConfigValue($KEY){
        try{
            $connection = $this->get_connection();
            $sql = "SELECT config_value from ok_config where config_key = '$KEY'";
            $value = $connection->get_cell_value($sql);
            return trim($value);
        }catch(Exception $e){
            return null;
        }
    }
	
	
	/**
	 * @desc html select控件 $viewhelp->HtmlSelect(array(
	 model=>'concretegrade',name=>'concretegrade_id'
	 ,valueField=>'id',displayField=>'concrete_grade'
	 ,required=>true,editable=>false,style=>""
	 ,queryWhere=>'',topRow=>30,defaultValue=>'',defalutDisplayValue=>''
	 ));
	 model, name ,valueField ,displayField为必须项
	 */
	function HtmlSelect($array){
		$model = $array['model'];
		if(empty($array['model'])){
			throw new Exception("mode不能为空");
		}
		$table_name = table_prefix.$model;
		$valueField = $array['valueField'];
		$displayField = $array['displayField'];
		$name = $array['name'];
		$style = $array['style'];
		$editable = $array['editable'];
		$queryWhere = $array['queryWhere'];
		$required = $array['required'];
		$onchange = ' ';
		if(!empty($array['onChange'])){
			$onchange=' change="'.$array['onChange'].'" ';
		}
		$html = '<select class="input_value"'.$onchange.'name="'.$name.'"';
		if(!empty($array['id'])){
			$html.=' id="'.$array['id'].'"';
		}
		if($required){
			$html .= ' required = "true" ';
		}
		$html.= empty($style) ? '':' style="'.$style.'"';
		$html.= ' >';
		$topRow = empty($array['topRow']) ? 100 : $array['topRow'];
		$fields = $valueField;
		if($valueField != $displayField){
			$fields .= ",".$displayField;
		}
		$sql = "select top $topRow $fields from $table_name ";
		if(!empty($queryWhere)){
			$conn=pdo_conn::get_conn();
			$sql .= " WHERE ".$queryWhere;
			$result = $conn->get_result($sql);
		}else{
			if(DbCache::IsStoredTable($table_name)){
				$value=DbCache::GetTableInMem($table_name);
				if($value!==false){
					$result=$value;
				}else{
					$conn=pdo_conn::get_conn();
					$result = $conn->get_result($sql);
					//DbCache::SetTableInMem($table_name, $result);
				}
			}else {
				$conn=pdo_conn::get_conn();
				$result = $conn->get_result($sql);
			}
		}
         $defaultDisplayValue = $array['defalutDisplayValue'];     
        if(!isset($array['defalutDisplayValue'])) {
         $defaultDisplayValue = $array['defaultDisplayValue'];
        }
        $defaultValue = $array['defalutValue'];
        if(!isset($array['defalutValue'])){
          $defaultValue =  $array['defaultValue'];
        }
		if(isset($defaultValue)){
			$html.= '<option value="'.$defaultValue.'">'.$defaultDisplayValue.'</option>';
		}
		foreach($result as $row){
			$html.= '<option value="'.$row[$valueField].'">'.$row[$displayField].'</option>';
		}
		$html.= '</select>';
		echo $html;
	}

	/**
	 * @desc 字典下拉控件
	 $viewhelp->DicSelect(array(root=>"car_status",name=>"car_status" ,required=>true,editable=>false,defalutValue=>"0",defalutDisplayValue=>"car_id"))
	 name为必须项,表单里面某个文本框的那么属性
	 root为字典的根节点
	 required为是必须填的项
	 editable是否可编辑
	 */
	function DicSelect($array){
		$model = "dic";
		$root = $array['root'];
		$table_name = table_prefix.$model;
		$valueField = empty($array['valueField'])?"dic_value":$array['valueField'];
		$displayField = empty($array['displayField'])?"dic_name":$array['displayField'];
		$name = empty($array['name'])?$array['root']:$array['name'];
		$style = $array['style'];
		$editable = $array['editable'];
		$required = $array['required'];
		$onchange = ' ';
        $class = ' class="input_value" ';
        if(!empty($array['plain'])){
            $class='';
        }
		if(!empty($array['onChange'])){
		  $onchange=' change="'.$array['onChange'].'" ';
		}
 
        $idx = $array['selectedIndex'];
 
		$html = '<select'.$class.$onchange.'name="'.$name.'"';
		if(!empty($array['id'])){
			$html.=' id="'.$array['id'].'"';
		}
		if($required){
			$html .= ' required = "true" ';
		}
		if(empty($editable) || !$editable){
			$html .= ' editable="false" ';
		}else{
			$html .= ' editable = "true"';
		}
		$html.= empty($style) ? '':' style="'.$style.'"';
		$html.= ' >';
		$result=DbCache::GetDicCache($root);
		if(isset($array['defalutValue'])){
			$html.= '<option value="'.$array['defalutValue'].'">'.$array['defalutDisplayValue'].'</option>';
		}
		foreach($result as $row){
			$html.= '<option value="'.$row[$valueField].'">'.$row[$displayField].'</option>';                                                                  
		}
		$html.= '</select>';
		echo $html;
	}
	
	
    
    function FindAllButtons($node = 0, $session_id) {
        $this->buttonArray = DbCache::GetAllButtons($session_id, $node);
    }
    
    function GetPower($func_url){
        global $session_id;
        $authorize = DbCache::GetUserUrlPower($session_id,null,$func_url);
        return empty($authorize[$func_url]) ? 0 : 1;
    }

    
    /**
    * @desc <a href="javascript:void(0)" class="top_button_add" id="model-add-button"></a>
    */
    function GetTopAddButton($model){
        $func_url = $model."/NewEntry";
        if($this->GetPower($func_url)){
            echo '<a href="javascript:void(0)" class="top_button_add" id="'.$model.'-add-button"></a>';
        }
    }
    
    
    function GetCustomizeButton($func_url,$css_name,$button_name,$button_id='',$style=''){
        if($this->GetPower($func_url)){
            echo '<a href="javascript:void(0);" id="'.$button_id.'" class="'.$css_name.'" style="'.$style.'">'.$button_name.'</a>';
        }    
    }
	
	
    /**
    * @desc 获取修改、删除、审核、反审核权限。
    */
    function GetPowerGroup($model,$param=array('edit','delete','audit','unaudit'),$id_field='id'){
        foreach($param as $button){
            if($button =='edit'){
                $func_url = $model."/EditEntry";
                if($this->GetPower($func_url)){
                    echo '<a href="javascript:void(0);" id="'.$model.'_edit_{'.$id_field.'}" class="ckh_edit">修改</a>';
                }    
            }
            if($button == 'delete'){
                $func_url = $model."/DelEntry";
                if($this->GetPower($func_url)){
                    echo '<a href="javascript:void(0);" id="'.$model.'_delete_{'.$id_field.'}" class="ckh_delete">删除</a>';
                }
            }
            if($button =='audit'){
                $func_url = $model."/AuditEntry";
                if($this->GetPower($func_url)){
                    echo '<a href="javascript:void(0);" id="'.$model.'_audit_{'.$id_field.'}" class="ckh_audit">审核</a>';
                }
            }
            if($button =='unaudit'){
                $func_url = $model."/FreeAuditEntry";
                if($this->GetPower($func_url)){
                    echo '<a href="javascript:void(0);" id="'.$model.'_unaudit_{'.$id_field.'}" class="ckh_unaudit">反审核</a>';
                }
            }
        }      
    }
    
    /*
     * 混凝土报警         
     */
    function ConcreteWarning($total_weight){
        $maxValue = $this->getConfigValue('concrete_maxwarn');
        $minValue = $this->getConfigValue('concrete_minwarn');
        $total_weight = floatval($total_weight);
        if ($total_weight > floatval($maxValue) || $total_weight < floatval($minValue)) {
            return '<font color="red">'.$total_weight.'</font';
        }
        return $total_weight;
    }
    
    /*
     * 砂浆报警    
     */
    function MortarWarning($total_weight){
        $maxValue = $this->getConfigValue('mortar_maxwarn');
        $minValue = $this->getConfigValue('mortar_minwarn');
        $total_weight = floatval($total_weight);
        if ($total_weight > floatval($maxValue) || $total_weight < floatval($minValue)) {
            return '<font color="red">'.$total_weight.'</font';
        }
        return $total_weight;
    }
    
}

$authorize = new Authorize();   

?>