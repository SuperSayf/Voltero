<?php
$username = "s2430888";
$password = "s2430888";
$database = "d2430888";
$link  = new mysqli("127.0.0.1", $username, $password, $database);

$session_id = $_REQUEST['session_id'];
$user_email = $_REQUEST['user_email'];

// Check if the message from the sender is in the database and not seen
$query = "SELECT * FROM messages WHERE session_id = '$session_id' AND user_email = '$user_email' AND msg_seen = 'false'";
$result = mysqli_query($link, $query);

// If the query was successful, check if there are any rows returned, and if so, echo as a JSON object of the msg_content
if ($result) {
    if (mysqli_num_rows($result) > 0) {
        $row = mysqli_fetch_assoc($result);
        echo json_encode(array('found' => true, 'msg_content' => $row['msg_content']));

        // Update the message to seen
        $query = "UPDATE messages SET msg_seen = 'true' WHERE session_id = '$session_id' AND user_email = '$user_email'";
        $result = mysqli_query($link, $query);

    } else {
        echo json_encode(array('found' => false));
    }
} else {
    echo json_encode(array('success' => false));
}


$link->close();

?>