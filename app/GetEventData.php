
<?php

    $con = mysqli_connect("mysql.hostinger.com", "u392533552_oscar", "Oscar191097", "u392533552_users");
    

    $response = array();
    $response["success"] = false;  
    

    $event = $_POST["event"];
    $username = $_POST["username"];

    $tokens = 0;

    $event = preg_replace('/\s+/', '', $event);
    $event = preg_replace('/[^a-zA-Z0-9]/', '', $event);

    $statement = mysqli_prepare($con, "SELECT tokens FROM event_rankings_$event WHERE user = ?");

    mysqli_stmt_bind_param($statement, "s", $username);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);

    mysqli_stmt_bind_result($statement, $tokens);

    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true; 
        $response["tokens"] = $tokens;

    }     
    

    echo json_encode($response);
?>