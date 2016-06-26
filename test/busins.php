<html>
<head>
<title>Admin Panel</title>
</head>
<body bgcolor="red+yellow" align="center">
<a href="logout.php">Log out</a>
<a href="login.php">Home</a>

<div id="Frame0">
<h1 align="center">Bus Data Insertion panel</h1>
    
<h3 align="center">Insert New Bus deatils</h3>
  </div>
<form action="insbus.php" method="POST">
<table id="table1"; cellspacing="5px" cellpadding="5%"; align="center">
<tr>
<td align="right" class="style1">BusNo:</td>
	<td class="style1"><input type=text name="bno"></td>
</tr>
<tr>
<td align="right">Source:</td>
	<td><input type=text name="src"></td>
</tr>
<tr>
<td  align="right">Destination:</td>
 <td><input type=text name="des"></td>
</tr>
<tr>
<td align="right">Stops:</td>
<td><input type=text name="mid"></td>
</tr>
<tr>
<td align="right">Latitude:</td>
<td><input type=text name="lat"></td>
</tr>
<tr>
<td align="right">Longitute:</td>
<td><input type=text name="lng"></td>
</tr>
</table> 
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
<input type="submit" value="submit">
</form>
</body>
</html>
<?php session_start(); /* Starts the session */

if(!isset($_SESSION['UserData']['Username'])){
	header("location:index.php");
	exit;
}
?>


