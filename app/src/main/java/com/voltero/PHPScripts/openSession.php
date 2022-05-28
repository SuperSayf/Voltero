<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

//Parameters
$user_email = $_REQUEST["user_email"];

// Count the number of rows where the session_with = user_email and session_complete = "false"
$query = "SELECT * FROM sessions WHERE user_email = '$user_email'  AND cart_complete = 'false' AND session_complete = 'false'";
$result = $link->query($query);

// Store the result in an array
$sessions = array();
while($row = $result->fetch_assoc()) {
    $sessions[] = $row;
}

// Check if the array is empty
if(count($sessions) == 0) {
    // Insert a new row into the sessions table
    $query = "INSERT INTO sessions (user_email, session_with, cart_complete, session_complete) VALUES ('$user_email', 'XXX', 'false', 'false')";
    $result = $link->query($query);
    echo json_encode(array("message" => "Session created"));
} else {
    echo json_encode(array("message" => "Session already exists"));
}


$link->close();

?>