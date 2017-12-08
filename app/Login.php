<?php
    require("password.php");

    $con = mysqli_connect("mysql.hostinger.com", "u392533552_oscar", "Oscar191097", "u392533552_users");
    
    $username = $_POST["username"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM users WHERE username = ?");
    mysqli_stmt_bind_param($statement, "s", $username);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $colUserID, $colName, $colUsername, $colAge, $colPassword);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        if (password_verify($password, $colPassword)) {
            $response["success"] = true;  
            $response["name"] = $colName;
            $response["age"] = $colAge;
            $response["username"] = $colUsername;
        }
    }

    echo json_encode($response);
?>