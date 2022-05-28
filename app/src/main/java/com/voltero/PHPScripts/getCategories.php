<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

// Return every row in the categories table as a json array
$query = "SELECT * FROM categories";
$result = $link->query($query);

// Create an array to hold all the rows
$rows = array();

// Add each row into the array
while($row = $result->fetch_assoc()) {
    $rows[] = $row;
}

// Encode the array as a json object and echo it
echo json_encode($rows);

$link->close();

?>