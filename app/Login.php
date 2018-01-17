<?php
    require("password.php");

    $con = mysqli_connect("mysql.hostinger.com", "u392533552_oscar", "Oscar191097", "u392533552_users");
    
    $username = $_POST["username"];
    $password = $_POST["password"];

    if(filter_var($username, FILTER_VALIDATE_EMAIL)) {
        // valid address
        $statement = mysqli_prepare($con, "SELECT * FROM users WHERE email = ?");
    }
    else {
        // invalid address
        $statement = mysqli_prepare($con, "SELECT * FROM users WHERE username = ?");
    }
    
    
    mysqli_stmt_bind_param($statement, "s", $username);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $colUserID, $colName, $colUsername, $colAge, $colPassword, $colEmail, $colHash, $colActive, $col_fb_id, $colTokens, $colDays);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        if (password_verify($password, $colPassword)) {
            $response["success"] = true;  
            $response["name"] = $colName;
            $response["age"] = $colAge;
            $response["username"] = $colUsername;
            $response["email"] = $colEmail;
            $response["hash"] = $colHash;
            $response["active"] = $colActive;
            $response["fb_id"] = $col_fb_id;
            $response["tokens"] = $colTokens;
            $response["days"] = $colDays;
        }
    }

    echo json_encode($response);
?>