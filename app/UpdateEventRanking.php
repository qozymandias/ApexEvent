
<?php

    $con = mysqli_connect("mysql.hostinger.com", "u392533552_oscar", "Oscar191097", "u392533552_users");
    

    $response = array();
    $response["success"] = false;  
    

    $username = $_POST["username"];
    $event = $_POST["event"];
    $tokens = $_POST["tokens"];



    $statement = mysqli_prepare($con, "SELECT * FROM event_rankings_$event WHERE user = ?");

    mysqli_stmt_bind_param($statement, "s", $username);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    $count = mysqli_stmt_num_rows($statement);
    mysqli_stmt_close($statement);

    if($count >= 1) {
        
        $new_statement = mysqli_prepare($con, "UPDATE event_rankings_$event SET tokens = ? WHERE user = ?");
        mysqli_stmt_bind_param($new_statement, "is", $tokens, $username);
        mysqli_stmt_execute($new_statement);
        mysqli_stmt_close($new_new_statement); 


    } else {
        $new_new_statement = mysqli_prepare($connect, "INSERT INTO event_rankings_$event (event_name, user, tokens) VALUES (?, ?, ?)");
        mysqli_stmt_bind_param($new_new_statement, "ssi", $event, $username, $tokens);
        mysqli_stmt_execute($new_new_statement);
        mysqli_stmt_close($new_new_statement); 

    }

    $response["success"] = true;  

    echo json_encode($response);
?>