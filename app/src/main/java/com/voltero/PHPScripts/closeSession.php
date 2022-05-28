<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

//Parameters
$user_email = $_REQUEST["user_email"];
$session_with = $_REQUEST["session_with"];

// Find in the sessions table where user_email = '$user_email' and session_with = '$session_with' and session_complete = "false" and update the session_complete to "true"
$query = "UPDATE sessions SET session_complete = 'true' WHERE user_email = '$user_email' AND session_with = '$session_with' AND cart_complete = 'true' AND  session_complete = 'false'";
$result = $link->query($query);

// Count the number of rows where the session_with = user_email and session_complete = "false"
$query = "SELECT * FROM sessions WHERE user_email = '$user_email' AND cart_complete = 'true' AND session_complete = 'false'";
$result = $link->query($query);

// Store the result in an array
$sessions = array();
while($row = $result->fetch_assoc()) {
    $sessions[] = $row;
}

// Check if the array is empty
if(count($sessions) == 0) {
    echo json_encode(array("message" => "success"));
} else {
    echo json_encode(array("message" => "error"));
}


$link->close();

?>