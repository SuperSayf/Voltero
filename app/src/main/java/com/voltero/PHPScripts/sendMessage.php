<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

$session_id = $_REQUEST['session_id'];
$user_email = $_REQUEST['user_email'];
$msg_content = $_REQUEST['msg_content'];
$msg_seen = $_REQUEST['msg_seen'];

// Insert the message into the database
$query = "INSERT INTO messages (session_id, user_email, msg_content, msg_seen) VALUES ('$session_id', '$user_email', '$msg_content', '$msg_seen')";
$result = mysqli_query($link, $query);

// Check if the query was successful
if ($result) {
    // Echo as a JSON object
    echo json_encode(array('success' => true));
} else {
    // Echo as a JSON object
    echo json_encode(array('success' => false));
}

$link->close();

?>