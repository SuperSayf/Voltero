<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

//Parameters
$user_email = $_REQUEST["user_email"];
$user_type = $_REQUEST["user_type"];

$output=array();

$query = "SELECT * FROM sessions WHERE user_email = '$user_email' AND cart_complete = 'true' AND session_complete = 'false'";
$result = $link->query($query);

if($user_type == "1") {
    // Find session_with where user_email = user_email and session_complete = "false"
    $query = "SELECT * FROM sessions WHERE user_email = '$user_email' AND cart_complete = 'true' AND session_complete = 'false'";
    $result = $link->query($query);
    $row = $result->fetch_assoc();
    $session_with = $row['session_with'];
    // Output as an array and also add session_ID to the array
    $output = array("user_email" => $session_with);
    $session_ID = $row['session_id'];
    $output = array_merge($output, array("session_ID" => $session_ID));

    // Output as an array
    echo json_encode($output);
} else {
   // Find user_email where session_with = user_email and session_complete = "false"
    $query = "SELECT * FROM sessions WHERE session_with = '$user_email' AND cart_complete = 'true' AND session_complete = 'false'";
    $result = $link->query($query);
    $row = $result->fetch_assoc();
    $user_email = $row['user_email'];
    $output = array("user_email" => $user_email);
    // Add session_ID to the array
    $session_ID = $row['session_id'];
    $output = array_merge($output, array("session_ID" => $session_ID));

    // Output as an array
    echo json_encode($output);
}

$link->close();

?>