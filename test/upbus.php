<?php
	$host='localhost';
	$uname='root';
	$pwd='';
	$db="citybus";

	$con = mysql_connect($host,$uname,$pwd) or die("connection failed");
	mysql_select_db($db,$con) or die("db selection failed");
	 
	
	$pass=$_POST['mid'];
	$lat=$_POST['lat'];
	$long=$_POST['long'];

	$flag['code']=0;
		 
	if($r=mysql_query("UPDATE bus SET lat = '$lat', lng = '$long' WHERE middlers = '$pass'",$con))
	{
		echo "Success";
		header("location:Update.php");
		exit;

	}
	else
	{
		echo "failure";
	}
	 
	mysql_close($con);
?>
