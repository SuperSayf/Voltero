<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

//Parameters
$user_email = $_REQUEST["user_email"];
$rating_from = $_REQUEST["rating_from"];
$rating_stars = $_REQUEST["rating_stars"];
$rating_comment = $_REQUEST["rating_comment"];

// Add the new rating to the ratings table
$query = "INSERT INTO ratings (user_email, rating_from, rating_stars, rating_comment) VALUES ('$user_email', '$rating_from', '$rating_stars', '$rating_comment')";
// Execute the query
$result = $link->query($query);

// Check if the query was successful, then output a message
if ($result) {
    // Output as json object
    echo json_encode(array(
        "message" => "Rating added successfully"
    ));
} else {
    // Output as json object
    echo json_encode(array(
        "message" => "Rating not added"
    ));
}


$link->close();

?>