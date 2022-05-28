<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

$user_email = $_REQUEST['user_email'];

$query = "SELECT * FROM sessions WHERE user_email = '$user_email'";
$result = $link->query($query);

$row = $result->fetch_assoc();
$session_id = $row['session_id'];

echo json_encode($session_id);

//$cart_query = "SELECT * FROM cart WHERE session_id = '$session_id';
//$res = $link->query($cart_query);

// Store the result in an array
//$cart = array();
//while($r = $result->fetch_assoc()) {
//    $cart[] = $r;
//}

// Check if the array is empty
//if(count($cart) == 0) {
    // Insert a new row into the sessions table
//    $q = "INSERT INTO cart (session_id) VALUES ('$session_id')";
//    $r = $link->query($q);
//    echo json_encode(array("message" => "Cart created"));
//} else {
//    echo json_encode(array("message" => "Cart already exists"));
//}


$link->close();

?>