<?php
    require("password.php");

    ini_set( 'display_errors', 1 );
    error_reporting( E_ALL );

    $to = $_POST["email"];

    $from = "noreply@eventblock.xyz";
 
    $subject = "Email confirmation";


    $emailHash = hexdec( md5($to) );

    $addr = 'http://eventblock.xyz/ConfirmEmail=' . $emailHash . '.php';

    $fp=fopen( $addr ,'w');
    fwrite($fp, 'data to be written');
    fclose($fp);

    move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $fp);

 
    $message = "please click this link to confirm your email address: " . $addr;
 
    $headers = "From:" . $from;
 
    mail($to,$subject,$message, $headers);
 
    $response = array();
    $response["success"] = true;
    
    echo json_encode($response);
?>