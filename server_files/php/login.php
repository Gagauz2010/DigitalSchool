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

	$email = $_POST['email'];
	$password = $_POST['password'];
	$result = mysql_query("SELECT id, access, class_id FROM users WHERE email='".$email."' AND password='".$password."'");
	
	$data = mysql_fetch_array($result);

	if($data['id']){
		$output = array('id' => $data['id'], 'access' => $data['access'], 'class' => $data['class_id']);
		$update = mysql_query("UPDATE `users` SET `last_login`=(NOW()+INTERVAL 8 HOUR) WHERE `email`='".$email."'");
		echo (json_encode($output, JSON_UNESCAPED_UNICODE));
	}
	else{
		echo("User not found");
	}

	mysql_close($con);
?>	