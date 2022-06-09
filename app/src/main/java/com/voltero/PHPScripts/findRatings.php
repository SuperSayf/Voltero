<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

//Parameters
$user_email = $_REQUEST["user_email"];

// Find all the rows in the ratings table where the user_email matches the user_email parameter and also find the user_firstnsame and user_lastname in the users table of using the rating_from from the ratings table
$query = "SELECT ratings.user_email, ratings.rating_from , ratings.rating_stars, ratings.rating_comment, users.user_firstname AS rators_firstname, users.user_surname AS rators_surname, users.user_image AS rators_image FROM ratings INNER JOIN users ON ratings.rating_from = users.user_email WHERE ratings.user_email = '$user_email'";

// Execute the query
$result = $link->query($query);

// Check if the query was successful
if ($result) {
    // Check if there are any rows returned
    if ($result->num_rows > 0) {
        // Loop through the rows and add them to the response array
        while ($row = $result->fetch_assoc()) {
            $response[] = $row;
        }
    }
}

// Inverse the response array and output the JSON
$response = array_reverse($response);
echo json_encode($response);

$link->close();

?>