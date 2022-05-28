<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

$user_firstname = $_REQUEST['user_firstname'];
$user_surname = $_REQUEST['user_surname'];
$user_password = $_REQUEST['user_password'];
$user_email = $_REQUEST['user_email'];
$user_address = $_REQUEST['user_address'];
$user_type = $_REQUEST['user_type'];
$user_cell = $_REQUEST['user_cell'];

// If the email exists in the database, return an error
$query = "SELECT * FROM users WHERE user_email = '$user_email'";
$result = $link->query($query);
if ($result->num_rows > 0) {
    // echo "Email already exists" in a json object
    echo json_encode(array(
        "success" => "false",
        "message" => "Email already exists"
    ));
    exit;
} else {
    // Insert the new user into the database
    $query = "INSERT INTO users (user_firstname, user_surname, user_password, user_email, user_address, user_type, user_cell) VALUES ('$user_firstname', '$user_surname', '$user_password', '$user_email', '$user_address', '$user_type', '$user_cell')";
    $result = $link->query($query);
    if ($result) {
        // echo "User added successfully" in a json object
        echo json_encode(array(
            "success" => "true",
            "message" => "User added successfully"
        ));
    } else {
        // echo "Failed to add user" in a json object
        echo json_encode(array(
            "success" => "false",
            "message" => "Failed to add user"
        ));
    }
}

$link->close();
?>
