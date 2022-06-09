<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

//Parameters
$user_email = $_REQUEST["user_email"];

// If the user_email is in the session_with column, then the user is in a session
$query = "SELECT * FROM sessions WHERE session_with = '$user_email' AND session_complete = 'false'";
$result = $link->query($query);

// Check if the user is in a session
if($result->num_rows > 0) {
    // Output as an array
    echo json_encode(array("inSession" => "true"));
} else {
    // Output as an array
    echo json_encode(array("inSession" => "false"));
}

$link->close();

?>