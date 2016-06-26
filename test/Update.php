<html>
<head>
<title>Admin Panel</title>
</head>
<body bgcolor="red+yellow" align="center">
<a href="logout.php">Log out</a>
<a href="login.php">Home</a>
<div id="Frame0">
<h1 align="center">Bus Data Updatipon panel</h1>
    
<h3 align="center">Update Bus data</h3>
  </div>

<form action="upbus.php" method="POST">
 <br> <br>
Bus Stop <input type="text" name="mid">
<br> <br>
Latitude<input type=text name="lat"><br><br>
Longitute<input type=text name="long"><br><br><br>

<input type="submit" value="submit">
</form>
</body>
</html>