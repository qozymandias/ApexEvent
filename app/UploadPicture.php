<?php

    $connect = mysqli_connect("mysql.hostinger.com", "u392533552_oscar", "Oscar191097", "u392533552_users");
  

    $username = $_POST["name"];
    $image = $_POST["image"];

    $decodedImage = base64_decode["$image"];

    
    $statement = mysqli_prepare($con, "SELECT * FROM pictures WHERE username = ? AND image = ?");
    mysqli_stmt_bind_param($statement, "ss", $username, $decodedImage);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $username, $decodedImage);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;  
        $response["username"] = $name;
        $response["image"] = $decodedImage;
    }
    
    echo json_encode($response);
?>