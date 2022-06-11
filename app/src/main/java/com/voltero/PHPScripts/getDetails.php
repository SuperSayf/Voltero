<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

//Parameters
$user_email = $_REQUEST["user_email"];


// Check is the user_type in users is "shopper" or "volunteer"
$query = "SELECT user_type FROM users WHERE user_email = '$user_email'";
$result = $link->query($query);

// Check if it is a shopper or volunteer
if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $user_type = $row["user_type"];
    }
}

// If the user_type is "shopper", then output all the row in users when user_email matches the user_email parameter
if ($user_type == "1") {
    $query = "SELECT user_firstname, user_surname, user_address, user_type, user_cell, user_image FROM users WHERE user_email = '$user_email'";
    $result = $link->query($query);
    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            $response[] = $row;
        }
    }
} else {
    // If the user_type is "volunteer", then output all the row in users when user_email matches the user_email parameter and also get the average rating_stars in the ratings table using the rating_from from the ratings table
    $query = "SELECT users.user_firstname, users.user_surname, users.user_address, users.user_type, users.user_cell, users.user_image, AVG(ratings.rating_stars) AS average_rating FROM users INNER JOIN ratings ON users.user_email = ratings.user_email WHERE users.user_email = '$user_email' GROUP BY users.user_email";
    $result = $link->query($query);
    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            $response[] = $row;
        }
    }
}

// Output the JSON
echo json_encode($response);

$link->close();

?>