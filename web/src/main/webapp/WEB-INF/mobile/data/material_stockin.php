<?php 
$date = $_POST['date'];
$min_time = $_POST['min_time'];
$max_time = $_POST['max_time'];
$ipc_id = $_POST['ipc_id'];

$queryWhere = "is_audit=1";
if(!empty($ipc_id)){
	$queryWhere.=" AND ipc_id='$ipc_id' ";
}
if(!empty($date)){
	$queryWhere.=" AND convert(varchar(10),weigh_time,120) = '$date'";
}

if(!empty($min_time)){
	$queryWhere.=" AND convert(varchar(20),weigh_time,120)>='$min_time'";
}

if(!empty($max_time)){
	$queryWhere.=" AND convert(varchar(20),weigh_time,120)<'$max_time'";
}


require_once(dirname(__FILE__).'/../../lib/pdo_conn.php');
$dbconn = pdo_conn::get_conn();
$query = "SELECT provider_name,material_name, cast(ISNULL(SUM(in_number),0)/1000 as  numeric(10,2)) AS in_number from ok_view_depotin  WHERE (".$queryWhere.")  group by provider_name,material_name order by  provider_name";
$result = $dbconn->get_result($query);
if(count($result) == 0){
    echo '<font color="red">没有找到任何数据，请重试！</font>';
    exit(0);
}
foreach($result as $row){
    $material_name = $row['material_name'];
    $provider_name = $row['provider_name'];
    $in_number = sprintf("%.2f", $row['in_number']);
?>
	<li class="half"><?php echo $provider_name;?></li>
    <li><?php echo $material_name;?></li>
    <li class="no_right_border tone"><?php echo $in_number;?></li>
<?php 
}
?>