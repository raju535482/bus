<?php
error_reporting(0);
$host='localhost';
$uname='root';
$pwd='';
$db="citybus";

$con=mysql_connect($host,$uname,$pwd) or die("Connection Failed...");
mysql_select_db($db,$con) or die("Db Selection Failed...");


$id=$_POST['id'];
$stops=$_POST['stops'];

$flag['code']=0;

if($qry=mysql_query("insert into vastobapu(id,stops) values('$id','$stops')"))
{
$flag['code']=1;
}

echo json_encode($flag);
mysql_close($con);
?>
