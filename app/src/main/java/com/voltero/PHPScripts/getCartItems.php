<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

$user_email = $_REQUEST["user_email"];

// Finds session_id corresponding to user_email
$query = "SELECT session_id FROM sessions WHERE user_email = '$user_email' AND session_complete = 'false' ";
$result = $link->query($query);

// Array with session_id, session_id is then extracted into variable 
$row = $result->fetch_assoc();
$session_id = $row[session_id];

// Return every row in the cartItem table as a json array
$query = "SELECT * FROM cartItem WHERE cart_session_id = $session_id";
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