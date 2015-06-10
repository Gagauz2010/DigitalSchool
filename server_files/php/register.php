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
	$fio = $_POST['fio'];
	$phone = $_POST['phone'];
	$code = $_GET['code'];

	if (!$code){
		$result = mysql_query("SELECT id FROM users WHERE email = '".$email."'");
		$row = mysql_fetch_array($result);
		$data = $row[0];
		if (!$data)
		{
			$result = mysql_query("INSERT INTO users (email, password, fio, phone) VALUES ('".$email."','".$password."','".$fio."', '".$phone."')");
			$last_id = mysql_insert_id();

			$confirmation_code = md5(uniqid(rand()));
			$temp_result = mysql_query("INSERT INTO users_temp (guid, confirmation_code) VALUES ('".$last_id."', '".$confirmation_code."')");
		
			$message = "From: Digital School <gaga-ya@hotmail.com>\r\n";
			$message .= "Reply-To: gaga-ya@hotmail.com\r\n";
			$message .= "Для завершения регистрации перейдите по ссылке http://dsc.freevar.com/register.php?code=".$confirmation_code."\r\n\r\n";
			$message .= "Спасибо за использование нашего приложения!";
			
			if (mail("$email", "Подтверждение регистрации", "$message", "Цифровая школа")) {
				echo "Message sent!";
			} else {
				echo "ERROR sending message.";
			}
		}
		else
			echo ("Email already registered");	
	}
	else{
		$update_id = mysql_query("SELECT guid FROM users_temp WHERE confirmation_code = '".$code."'");
		$row = mysql_fetch_array($update_id);
		$data = $row[0];
		if ($data){
			$update = mysql_query("DELETE FROM users_temp WHERE confirmation_code='".$code."'");
			echo mysql_error();
			$update = mysql_query("UPDATE users SET access=1 WHERE id=".$data);
			echo mysql_error();
			echo "Email успешно подтвержден!";
		}
		else{
			echo ("Возможно ссылка является устаревшей");
		}
		
		//echo ($data);
	}

	mysql_close($con);
?>	