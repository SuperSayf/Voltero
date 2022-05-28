<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

$user_email = $_REQUEST["user_email"];
$grc_name = $_REQUEST["grc_name"];
$grc_image = $_REQUEST["grc_image"];
$change_type = $_REQUEST["change_type"];

// Finds session_id corresponding to user_email
$query = "SELECT session_id FROM sessions WHERE user_email = '$user_email' AND session_complete = 'false' ";
$result = $link->query($query);

// Array with session_id, session_id is then extracted into variable 
$row = $result->fetch_assoc();
$session_id = $row[session_id];

// Check if a grc_name is already in table
$query = "SELECT * FROM cartItem WHERE grc_name = '$grc_name' AND cart_session_id = '$session_id' ";
$result = $link->query($query);

// Store the result in an array
$quantity = array();
while($row = $result->fetch_assoc()) {
    $quantity[] = $row;
}

if($change_type == "add") {
    // Check if the array is empty
    if(count($quantity) == 0) {
       // Insert a new row into the sessions table
       $query = "INSERT INTO cartItem (cart_session_id, grc_name, grc_image, grc_quantity) VALUES ('$session_id', '$grc_name', '$grc_image', '1')";
       $result = $link->query($query);
       echo json_encode(array("message" => "Item added"));
    } else {
       $query = "UPDATE cartItem SET grc_quantity = grc_quantity + 1 WHERE cart_session_id = '$session_id' AND grc_name = '$grc_name'";
       $result = $link->query($query);
       echo json_encode(array("message" => "Item quntity increased"));
    }
} else {
    // Buttons for increasing and decreasing
    $query = "SELECT grc_quantity FROM cartItem WHERE grc_name = '$grc_name' AND cart_session_id = '$session_id' ";
    $result = $link->query($query);
    
    $row = $result->fetch_assoc();
    $grc_amt = $row[grc_quantity];

    echo json_encode($change_type);

    if($grc_amt == 1 || $change_type == "removeAll") {
       $query = "DELETE FROM cartItem WHERE cart_session_id = '$session_id' AND grc_name = '$grc_name' ";
       $result = $link->query($query);
       echo json_encode(array("message" => "Item removed"));
    } else {
       $query = "UPDATE cartItem SET grc_quantity = grc_quantity - 1 WHERE cart_session_id = '$session_id' AND grc_name = '$grc_name'";
       $result = $link->query($query);
       echo json_encode(array("message" => "Item quntity decreased"));
    }
}

$link->close();

?>