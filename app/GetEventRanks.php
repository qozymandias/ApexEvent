<?php


    $con = mysqli_connect("mysql.hostinger.com", "u392533552_oscar", "Oscar191097", "u392533552_users");   


    $event = $_POST["event"];



    $event = preg_replace('/\s+/', '', $event);

    $event = preg_replace('/[^a-zA-Z0-9]/', '', $event);



    $statement = mysqli_query($con, "SELECT * FROM event_rankings_$event");



    while($row = mysqli_fetch_array($statement)) {

        $response[] = $row;

    }

    



    echo json_encode(array('data' => $response));
?>