<?php
$host='localhost';
$uname='root';
$pwd='';
$db="citybus";

mysql_connect($host,$uname,$pwd);
mysql_select_db($db);


//if ( isset( $_POST["busno"]))
$busno=$_POST['mid'];

$result=mysql_query("select distinct busNo from bus where middlers='$busno'");

$json=array();

if(mysql_num_rows($result)>0)
{
	while($row=mysql_fetch_assoc($result))
	{
		$json["busno"][]=$row;
	}
	echo json_encode($json);
}
else
{
	echo 'norecord';
}

?>
