<?php
$index_func = array(
	'1' => array(
		func_model => '销售管理'
		,func_items=>array(
			'1'=>array(func_id => 168,func_name =>'客户管理',images =>'customers.png',func_url =>'customers.php'),
			'2'=>array(func_id => 174,func_name =>'工程管理',images =>'project.png',func_url =>'project.php'),
			'3'=>array(func_id => 639,func_name =>'合同管理',images =>'contract.png',func_url =>'contract.php',erp_url=>'pact.php'),
			'4'=>array(func_id => 657,func_name =>'销售统计',images =>'sales_statistic.png',func_url =>'sales_statistic.php',erp_url=>'report_sails.php')
		)
	),
	'2' => array(
		func_model => '生产计划管理'
		,func_items=>array(
			'1'=>array(func_id => 197,func_name =>'生产任务',images =>'tasklist.png',func_url =>'tasklist.php',erp_url=>'task.php'),
			'2'=>array(func_id => 190,func_name =>'任务排班',images =>'taskplan.png',func_url =>'taskplan.php',erp_url=>'taskplan.php'),
			'3'=>array(func_id => 516,func_name =>'生产记录',images =>'productlist.png',func_url =>'productlist.php',erp_url=>'manufacture.php'),
			'4'=>array(func_id => 1031,func_name =>'生产统计',images =>'product_statistic.png',func_url =>'product_statistic.php',erp_url=>'manufacture_report.php')
		)
	),'3' => array(
		func_model => '生产调度管理'
		,func_items=>array(
			'1'=>array(func_id => 222,func_name =>'车辆管理',images =>'carlist.png',func_url =>'carlist.php',erp_url=>'car_driver.php'),
			'2'=>array(func_id => 1015,func_name =>'调度看板',images =>'scheduler_info.png',func_url =>'scheduler_info.php',erp_url=>'scheduler.php'),
			'3'=>array(func_id => 1015,func_name =>'实时调度',images =>'scheduler.png',func_url =>'scheduler.php'),
		    '4'=>array(func_id => 504,func_name =>'送货单',images =>'stockin.png',func_url =>'delivery.php')
			//,'5'=>array(func_id => 1031,func_name =>'统计报表',images =>'scheduler_statistic.png',func_url =>'scheduler_statistic.php',erp_url=>'manufacture_report.php')
		)
	),'4' => array(
		func_model => '配合比管理'
		,func_items=>array(
			'1'=>array(func_id => 954,func_name =>'施工配比',images =>'formula.png',func_url =>'formula.php'),
			'2'=>array(func_id => 1037,func_name =>'客户配比',images =>'recipe.png',func_url =>'formula_customer.php'),
			'3'=>array(func_id => 1118,func_name =>'施工配比库',images =>'formulalib.png',func_url =>'formulalib.php',erp_url=>'formulacons_ref.php'),
			'4'=>array(func_id => 439,func_name =>'客户配比库',images =>'recipelib.png',func_url =>'recipelib.php',erp_url=>'formulacons_ref_out.php')
		)
	),'5' => array(
		func_model => '原材料管理'
		,func_items=>array(
			'1'=>array(func_id => 544,func_name =>'筒仓库存',images =>'inventory.png',func_url =>'inventory.php',erp_url=>'stock_status.php'),
			'2'=>array(func_id => 575,func_name =>'入库明细',images =>'stockin.png',func_url =>'stockin.php',erp_url=>'depotin.php'),
			'3'=>array(func_id => 1042,func_name =>'消耗统计',images =>'consume.png',func_url =>'consume_statistic.php',erp_url=>'report_material_cost.php'),
			'4'=>array(func_id => 2037,func_name =>'入库统计',images =>'stockin_statistic.png',func_url =>'stockin_statistic.php',erp_url=>'report_view_depotin.php'),
			'5'=>array(func_id => 1409,func_name =>'过磅明细',images =>'ponderation.png',func_url =>'ponderation.php',erp_url=>'metage.php'),
			'6'=>array(func_id => 1428,func_name =>'过磅统计',images =>'ponder_statistic.png',func_url =>'ponder_statistic.php',erp_url=>'report_metage.php')	
		)
	)
);
?>