<?php
$host='localhost';
$uname='root';
$pwd='';
$db="citybus";

mysql_connect($host,$uname,$pwd);
mysql_select_db($db);

$result=mysql_query("select distinct middlers,lat,lng from bus");

$json=array();
if(mysql_num_rows($result)>0)
{
	while($row=mysql_fetch_assoc($result))
	{
		$json["src"][]=$row;
	}
	echo json_encode($json); 
	
}
else
{
	echo 'norecord';
}

?>
