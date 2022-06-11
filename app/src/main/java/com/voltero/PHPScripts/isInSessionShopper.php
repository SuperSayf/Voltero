<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

//Parameters
$user_email = $_REQUEST["user_email"];

// If the user_email parameter is in the user_email column, and the session_complete column is false, and the session_with column is not equal to 'XXX' then the user is in a session
$query = "SELECT * FROM sessions WHERE user_email = '$user_email' AND session_complete = 'false' AND session_with != 'XXX'";
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