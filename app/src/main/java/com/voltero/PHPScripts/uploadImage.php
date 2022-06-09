<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

$user_email = $_POST['user_email'];
$user_image = $_POST['user_image'];

// Update the user_image to the database where the user_email is user_email
$query = "UPDATE users SET user_image = '$user_image' WHERE user_email = '$user_email'";
$result = $link->query($query);

// Check if the query is successful or not
if ($result) {
    // Output as a JSON object
    echo json_encode(array('success' => true));
} else {
    // Output as a JSON object
    echo json_encode(array('success' => false));
}

$link->close();

?>