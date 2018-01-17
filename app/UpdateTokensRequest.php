
<?php

    $con = mysqli_connect("mysql.hostinger.com", "u392533552_oscar", "Oscar191097", "u392533552_users");
    

    $response = array();
    $response["success"] = false;  
    

    $username = $_POST["username"];
    $tokens = $_POST["tokens"];
    $days = $_POST["days"];

    $statement = mysqli_prepare($con, "UPDATE users SET tokens = ? WHERE username=?");
    mysqli_stmt_bind_param($statement, "is", $tokens, $username);
    mysqli_stmt_execute($statement);
    
    $statement1 = mysqli_prepare($con, "UPDATE users SET days = ? WHERE username=?");
    mysqli_stmt_bind_param($statement1, "is", $days, $username);
    mysqli_stmt_execute($statement1);


    $response["success"] = true;  

    echo json_encode($response);
?>