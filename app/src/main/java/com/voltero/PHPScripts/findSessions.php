<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

//In the sessions table, select where the session_with is XXX and session_complete = "false", and inner join with the users table to get the user_firstname and user_lastname
$query = "SELECT * FROM sessions INNER JOIN users ON sessions.user_email = users.user_email WHERE session_with = 'XXX' AND cart_complete = 'true' AND session_complete = 'false'";
$result = $link->query($query);
$output=array();

//Output all the sessions which are found in a json array
while($row = $result->fetch_assoc()) {
    $output[]=$row;
    // Add a message to the row
    $output[count($output)-1]["message"] = "Found";
}

//If no sessions are found, output an empty json array with a message
if($result->num_rows == 0) {
    // Make and output a json array
    $output[]=array("message"=>"No sessions found");

    //Output the json array
    echo json_encode($output);
} else {
    echo json_encode($output);
}


$link->close();

?>