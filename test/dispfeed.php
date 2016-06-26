<a  href="login.php">Home</a>
	<br>
	<br>

<table  width="100%" border="1">
        <tr> 
          <td><strong><font color="#000000">ID</font></strong></td>
          <td><strong><font color="#000000">Name</font></strong></td>
          <td><strong><font color="#000000">E-mail Id</font></strong></td>
          <td><strong><font color="#000000">Description</font></strong></td>
          </tr>
<?php
$host='localhost';
$uname='root';
$pwd='';
$db="citybus";

mysql_connect($host,$uname,$pwd);
mysql_select_db($db);


//if ( isset( $_POST["busno"]))

$result=mysql_query("select * from fb");
$json=array();

if(mysql_num_rows($result)>0)
{	
	while($row=mysql_fetch_assoc($result))
	{echo '<body style="background-color:white">';

?>	 <tr> 
          <td><?php echo $row['id']; ?></td>
          <td><?php echo $row['userName']; ?></td>
          <td><?php echo $row['emailId']; ?></td>
          <td><?php echo $row['description']; ?></td>
          </tr>
<?php
	
}

	
}
else
{	echo '<body style="background-color:orange">';
	echo 'norecord';
}

?>
