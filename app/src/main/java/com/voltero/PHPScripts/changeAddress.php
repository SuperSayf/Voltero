<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

$user_email = $_REQUEST['user_email'];
$user_address = $_REQUEST['user_address'];

// Change the user_address in the users table
$query = "UPDATE users SET user_address = '$user_address' WHERE user_email = '$user_email'";
$result = $link->query($query);

// Check if the query was successful
if ($result) {
    // Output as a json object
    echo json_encode(array('result' => 'success'));
} else {
    // Output as a json object
    echo json_encode(array('result' => 'failure'));
}

$link->close();

?>