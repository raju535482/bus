<?php
	$host='localhost';
	$uname='root';
	$pwd='';
	$db="citybus";

	$con = mysql_connect($host,$uname,$pwd) or die("connection failed");
	mysql_select_db($db,$con) or die("db selection failed");
	 
	$name=$_POST['bno'];
 
	$flag['code']=0;
	 
	if($r=mysql_query("DELETE FROM bus WHERE busNO= '$name'",$con))
	{
		echo "Success";
		header("location:delete.php");
		exit;
	}
	else{
		
		$flag['code']=0;
		
	}
	 
	print(json_encode($flag));
	mysql_close($con);
?>
