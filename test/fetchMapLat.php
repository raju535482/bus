<?php
$host='localhost';
$uname='root';
$pwd='';
$db="citybus";

mysql_connect($host,$uname,$pwd);
mysql_select_db($db);
$src=$_POST['src'];
$des=$_POST['des'];

$result=mysql_query("select distinct lat,lng from bus where middlers='paldi'");

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

$result1=mysql_query("select distinct lat,lng from bus where middlers='rambaug'");

$json1=array();
if(mysql_num_rows($result1)>0)
{
	while($row1=mysql_fetch_assoc($result1))
	{
		$json1["des"][]=$row1;
	}
	echo json_encode($json1); 
	
}
else
{
	echo 'norecord';
}

?>
