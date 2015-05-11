<?php
session_start();
?>
<!DOCTYPE html>
<html>
<body>

<?php

var$host = "localhost";
$dbname = "SEA";
$username = "postgres";
$password = "RENeR2015DB";

// // $conn_string = "host=localhost port=5432 dbname=SEA user=postgres password=RENeR2015DB";
// $dbh = new PDO("pgsql:dbname=$dbname;host=$host", $username, $password ); 
// // $db = pg_pconnect($conn_string) or die('Could not connect: ' . pg_last_error());

// // $var="User', email='test";

// $statement = $db->prepare("SELECT * FROM user");
// $statement->execute();
// $result = $statement->frtchAll();

// var_dump($result);


// $b->bindParam(":var",$var);
// $b->execute();

// $db->pg_query_params($dbconn, 'SELECT * FROM shops WHERE name = $1', array("Joe's Widgets"));
echo "Session variables are set.";
?>

</body>
</html>