<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

//Parameters
$user_email = $_REQUEST["user_email"];

// Find in the sessions table where the row with user_email = '$user_email' and session_with = "XXX" and session_complete = "false" and update the session_with to '$session_with'
$query = "UPDATE sessions SET cart_complete = 'true' WHERE user_email = '$user_email' AND session_with = 'XXX' AND cart_complete = 'false' AND session_complete = 'false'";
$result = $link->query($query);

// Find the number of rows where user_email = '$user_email' and session_with = '$session_with' and session_complete = "false"
$query = "SELECT * FROM sessions WHERE user_email = '$user_email' AND cart_complete = 'true' AND  session_complete = 'false'";
$result = $link->query($query);

$row = $result->fetch_assoc();

echo json_encode($row);

$link->close();

?>