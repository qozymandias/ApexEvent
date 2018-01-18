<?php

    $con = mysqli_connect("mysql.hostinger.com", "u392533552_oscar", "Oscar191097", "u392533552_users");

    $response = array();
    $response["success"] = false;  
    
    $username = $_POST["username"];

    $statement = mysqli_prepare($con, "SELECT tokens, days FROM users WHERE username = ?");
    mysqli_stmt_bind_param($statement, "s", $username);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $colTokens, $colDays);
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;  
        $response["tokens"] = $colTokens;
        $response["days"] = $colDays;
    }
    echo json_encode($response);
?>