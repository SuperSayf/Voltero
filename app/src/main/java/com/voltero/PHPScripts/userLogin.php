<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

$user_email = $_REQUEST['user_email'];
$user_password = $_REQUEST['user_password'];

// Get the hashed password from the database
$query = "SELECT user_password FROM users WHERE user_email = '$user_email'";
$result = $link->query($query);
$row = $result->fetch_assoc();
$hashed_password = $row['user_password'];

$user_type = array();

// Check if the hashed password matches the user's password
if (password_verify($user_password, $hashed_password)) {
    // Get the user_type from the database and output it
      $query = "SELECT user_type FROM users WHERE user_email = '$user_email'";
      $result = $link->query($query);
      $user_type = $result->fetch_assoc();
}

echo json_encode($user_type);

$link->close();

?>