<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

$user_email = $_REQUEST['user_email'];
$user_new_email = $_REQUEST['user_new_email'];


// Change the user_email in the users table if the user_new_email is not already in the users table
$query = "SELECT * FROM users WHERE user_email = '$user_new_email'";
$result = $link->query($query);

// Check if the query was successful
if ($result) {
    // Check if the user_new_email is already in the users table
    if ($result->num_rows > 0) {
        // Output as a json object
        echo json_encode(array('result' => 'failure'));
    } else {
        // Change the user_email in the users table
        $query = "UPDATE users SET user_email = '$user_new_email' WHERE user_email = '$user_email'";
        $result = $link->query($query);
        
        // Check if the query was successful
        if ($result) {
            // Output as a json object
            echo json_encode(array('result' => 'success'));
        } else {
            // Output as a json object
            echo json_encode(array('result' => 'failure'));
        }
    }
} else {
    // Output as a json object
    echo json_encode(array('result' => 'failure'));
}


$link->close();

?>