<?php
$host='localhost';
$uname='root';
$pwd='';
$db="citybus";

mysql_connect($host,$uname,$pwd);
mysql_select_db($db);


//if ( isset( $_POST["busno"]))
//$busno=$_POST['busno'];

$result=mysql_query("select distinct busNo from bus");
$json=array();
echo '{"busno":';
if(mysql_num_rows($result)>0)
{
	while($row=mysql_fetch_assoc($result))
	{
		array_push($json,$row);
	}
	echo json_encode($json); 
	echo '}';
}
else
{
	echo 'norecord';
}


?>
