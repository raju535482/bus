<?php
error_reporting(0);
$host='localhost';
$uname='root';
$pwd='';
$db="citybus";

$con=mysql_connect($host,$uname,$pwd) or die("Connection Failed...");
mysql_select_db($db,$con) or die("Db Selection Failed...");


$id=$_POST['un'];
$stops=$_POST['mail'];
$des=$_POST['des'];

$flag['code']=0;

if($qry=mysql_query("insert into fb(userName,emailId,description) values('$id','$stops','$des')"))
{
$flag['code']=1;
}

echo json_encode($flag);
mysql_close($con);
?>
