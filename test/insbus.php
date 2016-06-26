<?php
error_reporting(0);
$host='localhost';
$uname='root';
$pwd='';
$db="citybus";

$con=mysql_connect($host,$uname,$pwd) or die("Connection Failed...");
mysql_select_db($db,$con) or die("Db Selection Failed...");


$id=$_POST['bno'];
$stops=$_POST['mid'];
$start=$_POST['src'];
$end=$_POST['des'];
$lat=$_POST['lat'];
$lng=$_POST['lng'];

$flag['code']=0;

if($qry=mysql_query("insert into bus values(NULL,'$id','$start','$end','$stops','$lat','$lng')"))
{
echo "Success";
header("location:busins.php");
exit;
}

echo json_encode($flag);
mysql_close($con);
?>
