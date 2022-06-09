<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

$user_email = $_REQUEST['user_email'];

// Find and output the user_image from the users table where the user_email is user_email
$query = "SELECT user_image FROM users WHERE user_email = '$user_email'";
$result = $link->query($query);

// Check if the query is successful or not
if ($result) {
    // Output as a JSON object
    $row = $result->fetch_assoc();
    // If the user_image is not empty
    if ($row['user_image'] != "") {
        echo json_encode(array('success' => true, 'user_image' => $row['user_image']));
    } else {
        echo json_encode(array('success' => false));
    }
} else {
    // Output as a JSON object
    echo json_encode(array('success' => false));
}

$link->close();

?>