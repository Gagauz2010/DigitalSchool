<?php
	require_once 'config.php';
	header( 'Content-Type: text/html; charset=utf-8' );
	mysql_set_charset( 'utf8' );
	$con = mysql_connect(mysql_host,mysql_user,mysql_password);
	if (!$con) {
		die("Connection error: " . mysql_error());
	}
	if (!mysql_select_db(mysql_database)) {
		die("DB selection error: " . mysql_error());
	}
	
	mysql_query ("set_client='utf8'");
	mysql_query ("set character_set_results='utf8'");
	mysql_query ("set collation_connection='utf8_general_ci'");
	mysql_query ("SET NAMES utf8");
	
	//default values for getting news
	$class = $_POST['class'];
	$acc = $_POST['access'];
	$user_id = $_POST['user_id'];
	
	//Values to know what to do
	$instructions = $_GET['instructions'];
	
	//update | delete | insert values
	$news_id = $_POST['news_id'];
	$news_head = $_POST['news_head'];
	$news_text = $_POST['news_text'];
	$news_category = $_POST['news_category'];
	$news_important = $_POST['news_important'];

	if(!$instructions){
		if($acc == '6'){
			$result = mysql_query("SELECT * FROM ( SELECT * FROM `news` ORDER BY `id` DESC LIMIT 50) a ORDER BY `id`");
		}
		else{
			$result = mysql_query("SELECT * FROM ( SELECT * FROM (SELECT * FROM `news` WHERE `category` = (SELECT `categories`.`id` FROM `categories` INNER JOIN `class` ON `categories`.`name` = `class`.`name` AND `class`.`id` = '".$class."') OR `category` = '1' OR `who_add` = '".$user_id."') a ORDER BY `id` DESC LIMIT 50) a ORDER BY `id`");
		}
		
		$output['news'] = array();
		while($row = mysql_fetch_assoc($result))
		{
			array_push($output['news'], $row); 
		}
		
		print (json_encode($output, JSON_UNESCAPED_UNICODE));
	} 
	else {
		$today = date("d/m/Y");
		if ($instructions == 'add'){
			//news_head || news_text || user_id || news_category || news_important
			$result = mysql_query("INSERT INTO news (`news_head`, `news_text`, `who_add`, `publishing_time`, `category`, `important`) VALUES ('".$news_head."', '".$news_text."', '".$user_id."', '".$today."', '".$news_category."', '".$news_important."')");
			
			if($result){
				echo('Success');
			}
			else{
				echo('Failure');
			}
		}
		
		if ($instructions == 'delete'){
			//news_id
			$result = mysql_query("DELETE FROM `news` WHERE `news`.`id` = '".$news_id."'");
			
			if($result){
				echo('Success');
			}
			else{
				echo('Failure');
			}
		}
		
		if ($instructions == 'update'){
			//news_id || news_head || news_text || user_id || news_category || news_important
			mysql_query("DELETE FROM `news` WHERE `news`.`id` = '".$news_id."'");
			$result = mysql_query("INSERT INTO news (`news_head`, `news_text`, `who_add`, `publishing_time`, `category`, `important`) VALUES ('".$news_head."', '".$news_text."', '".$user_id."', '".$today."', '".$news_category."', '".$news_important."')");
			
			if($result){
				echo('Success');
			}
			else{
				echo('Failure');
			}
		}
	}

	mysql_close($con);
?>