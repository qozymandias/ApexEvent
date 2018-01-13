<?php
    ini_set( 'display_errors', 1 );
    error_reporting( E_ALL );

    $to = $_POST["email"];

    $name = $_POST["username"];
    $password = $_POST["password"];

    $from = "noreply@eventblock.xyz";
 
    $subject = "Email confirmation";


    $emailHash = md5($to);

 
    $message = '
 
    Thanks for signing up!
    Your account has been created, you can login with the following credentials after you have activated your account by pressing the url below.
     
    ------------------------
    Username: '.$name.'
    Password: '.$password.'
    ------------------------
     
    Please click this link to activate your account:
    http://eventblock.xyz/verify.php?email='.$to.'&hash='.$emailHash.'
     
    ';
     
    $headers = "From:" . $from;
 
    mail($to,$subject,$message, $headers);
 
    $response = array();
    $response["success"] = true;
    
    echo json_encode($response);
?>