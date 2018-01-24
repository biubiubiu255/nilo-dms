<?php 
$date = $_POST['date'];
$min_time = $_POST['min_time'];
$max_time = $_POST['max_time'];
$ipc_id = $_POST['ipc_id'];

$queryWhere = "1=1";
if(!empty($ipc_id)){
	$queryWhere.=" AND ipc_id='$ipc_id' ";
}
if(!empty($date)){
	$queryWhere.=" AND convert(varchar(10),delivery_time,120) = '$date'";
}

if(!empty($min_time)){
	$queryWhere.=" AND convert(varchar(20),delivery_time,120)>='$min_time'";
}

if(!empty($max_time)){
	$queryWhere.=" AND convert(varchar(20),delivery_time,120)<'$max_time'";
}


require_once(dirname(__FILE__).'/../../lib/pdo_conn.php');
$dbconn = pdo_conn::get_conn();
$query = "SELECT type_name,material_name,cast(ISNULL(SUM(actual_value),0)/1000 as  numeric(10,4)) as actual_value,cast(ISNULL(SUM(formula_value),0)/1000 as  numeric(10,4)) as formula_value,cast((ISNULL(SUM(actual_value),0)-ISNULL(SUM(formula_value),0))/ISNULL(SUM(formula_value),0)*100 as  numeric(10,4)) as diff_value from ok_view_product_material  WHERE (".$queryWhere.")  group by type_name,material_name  order by type_name asc";
$result = $dbconn->get_result($query);
if(count($result) == 0){
    echo '<font color="red">没有找到任何数据，请重试！</font>';
    exit(0);
}
foreach($result as $row){
    $material_name = $row['material_name'];
    $formula_value = sprintf("%.2f", $row['formula_value']);
    $actual_value = sprintf("%.2f", $row['actual_value']); 
    $diff_value = sprintf("%.2f", $row['diff_value']);
?>
	<li class="material" style="width:30%;"><?php echo $material_name;?></li>
    <li><?php echo $formula_value;?></li>
    <li><?php echo $actual_value;?></li>
    <li class="no_right_border tone"  style="width:20%;"><?php echo $diff_value;?></li>
<?php 
}
?>