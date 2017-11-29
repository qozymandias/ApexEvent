<?php
    $con = mysqli_connect("localhost", "id3776519_eventblockusers", "Oscar191097", "id3776519_users");
    
    $name = $_POST["name"];
    $age = $_POST["age"];
    $username = $_POST["username"];
    $password = $_POST["password"];

    $statement = mysqli_prepare($con , "INSERT INTO Users(name,username,age,password) VALUES(?,?,?,?)");
    mysqli_stmt_bind_param($statement, "ssis", $name, $username, $age, $password);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>
