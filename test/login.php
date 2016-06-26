<html>
<head>
<link rel="stylesheet" href="style.css" type="text/css">

<title>Admin Panel</title>

</head>
<body align="center" bgcolor="red+yellow">
<a href="logout.php">Log out</a>
<div id="Frame0">
<h1 align="center">Admin</h1>
 
<h3 align="center">Welcome</h3>
<br>
<br>
<br>
<a  href="busins.php">Insert Bus Data</a>
<br>
<br>
<br>
<a href="delete.php">Delete Bus Data</a>
<br>
<br>
<br>
<a href="Update.php">Update Bus Data</a>
<br>
<br>
<br>
<a href="dispfeed.php">Display User FeedBack</a>



</body>
</html>
<?php session_start(); /* Starts the session */

if(!isset($_SESSION['UserData']['Username'])){
	header("location:index.php");
	exit;
}
?>


