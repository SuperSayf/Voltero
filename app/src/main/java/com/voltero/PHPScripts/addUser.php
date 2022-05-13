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

$sql = "INSERT INTO users(user_firstname, user_surname, user_password, user_email, user_address, user_type) VALUES(?,?,?,?,?,?);";
$stmt = $link->prepare($sql);
$stmt->bind_param("ssssss",$user_firstname,$user_surname,$user_password, $user_email, $user_address, $user_type);
$stmt->execute();

echo "sucessfully added user";

$stmt->close();
$link->close();
?>