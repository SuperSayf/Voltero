<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

//Parameters
$grc_name = $_REQUEST["grc_name"];

$query = "SELECT * FROM groceries WHERE grc_name = '$grc_name'";
$result = $link->query($query);

$rows = array();

while($row = $result->fetch_assoc()) {
    $rows[] = $row;
}

echo json_encode($rows);

$link->close();

?>