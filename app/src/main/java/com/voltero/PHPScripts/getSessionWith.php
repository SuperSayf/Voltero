<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

//Parameters
$user_email = $_REQUEST["user_email"];
$user_type = $_REQUEST["user_type"];

$query = "SELECT * FROM sessions WHERE user_email = '$user_email' AND cart_complete = 'true' AND session_complete = 'false'";
$result = $link->query($query);

if($user_type == "1") {
    // Find session_with where user_email = user_email and session_complete = "false"
    $query = "SELECT * FROM sessions WHERE user_email = '$user_email' AND cart_complete = 'true' AND session_complete = 'false'";
    $result = $link->query($query);
    $row = $result->fetch_assoc();
    $session_with = $row['session_with'];
    echo json_encode(array("session_with" => $session_with));
} else {
   // Find user_email where session_with = user_email and session_complete = "false"
    $query = "SELECT * FROM sessions WHERE session_with = '$user_email' AND cart_complete = 'true' AND session_complete = 'false'";
    $result = $link->query($query);
    $row = $result->fetch_assoc();
    $user_email = $row['user_email'];
    echo json_encode(array("session_with" => $user_email));
}

$link->close();

?>