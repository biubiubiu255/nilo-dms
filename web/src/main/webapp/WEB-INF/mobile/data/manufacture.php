<?php
if(!isset($_POST['delivery_bm']) || !isset($_POST['product_cube'])){
	echo '<font color="red">数据获取失败，无效的请求！</font>';
	exit(0);
}
$delivery_bm = $_POST['delivery_bm'];
$list_cube = $_POST['product_cube'];
require_once(dirname(__FILE__).'/../../lib/pdo_conn.php'); 
$dbconn = pdo_conn::get_conn();
$query = "select ok_view_manufactureitems_material.*,convert(varchar(20),ok_view_manufactureitems_material.write_time,120) as product_time,ok_manufacture.plate_value,ok_manufacture.product_cube from ok_view_manufactureitems_material left join ok_manufacture on ok_view_manufactureitems_material.manufacture_id = ok_manufacture.id where delivery_bm = '$delivery_bm' order by plate_value ASC";
$result = $dbconn->get_result($query);
if(count($result) == 0){
	echo '<font color="red">没有找到任何数据，请重试！</font>';
	exit(0);
}

$total_cube = 0;
$last_plate_value = 0;
foreach($result as $row){
	$plate_value = $row['plate_value'];
	$product_cube = $row['product_cube'];
	$depot_name = $row['depot_name'];
	$material_name = $row['material_name'];
	$formula_value = $row['formula_value'];
	$actual_value = $row['actual_value'];
	$error_value = $row['error_value'];
	$product_time =  date('H:i:s',strtotime($row['product_time']));
	if($last_plate_value != 0 && $last_plate_value != $plate_value){
		echo '</table>';
	}
	if($last_plate_value != $plate_value){
		echo "<p>第".$plate_value."盘，方量: ".$product_cube." m³ ,投料时间：".$product_time." </p>";
		echo '<table cellpadding="0" cellspacing="0" class="pf_div1">';
		echo '<tr>
			<td>筒仓名称</td>
			<td>材料名称</td>
			<td>配方值</td>
			<td>投料值</td>
			<td>误差</td>
		  </tr>';
		$last_plate_value = $plate_value;
		$total_cube += $product_cube;
	}
	echo '<tr>
			<td>'.$depot_name.'</td>
			<td>'.$material_name.'</td>
			<td>'.$formula_value.'</td>
			<td>'.$actual_value.'</td>
			<td>'.$error_value.'</td>
		  </tr>';
}
echo '</table>';
if(floatval($total_cube) == floatval($list_cube)){
	echo '<norequest/>';	
}
?>
 
