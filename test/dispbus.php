<?php
$host='localhost';
$uname='root';
$pwd='';
$db="citybus";

mysql_connect($host,$uname,$pwd);
mysql_select_db($db);


//if ( isset( $_POST["busno"]))
$busno=$_POST['busno'];

$result=mysql_query("select * from bus where busNo='$busno'");

$json=array();

if(mysql_num_rows($result)>0)
{
	while($row=mysql_fetch_assoc($result))
	{
		$json["businfo"][]=$row;
	}
	echo json_encode($json);
}
else
{
	echo 'norecord';
}

?>
