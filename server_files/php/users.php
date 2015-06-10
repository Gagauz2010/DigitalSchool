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
	
	$id = $_POST['id'];

	$result = mysql_query("SELECT users.fio, users.phone, access.info, class.name, users.last_login FROM users INNER JOIN access ON users.access = access.index INNER JOIN class ON users.class_id = class.id WHERE users.id = '".$id."'");
	$data = mysql_fetch_array($result);

	$output = array('fio' => $data['fio'], 'phone' => $data['phone'], 'class' => $data['name'], 'access' => $data['info'], 'last_login' => $data['last_login']); 
	print (json_encode($output, JSON_UNESCAPED_UNICODE));

	mysql_close($con);
?>	