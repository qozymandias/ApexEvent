<?php

    $connect = mysqli_connect("mysql.hostinger.com", "u392533552_oscar", "Oscar191097", "u392533552_users");
    
    $event_name = $_POST["event_name"];
    $description = $_POST["description"];
    $capacity = $_POST["capacity"];
    $start_time = $_POST["start_time"];
    $end_time = $_POST["end_time"];  
    $is_free = $_POST["is_free"];
    $venue_name = $_POST["venue_name"];
    $venue_lattitude = $_POST["venue_lattitude"];
    $venue_longitude = $_POST["venue_longitude"];
    $localized_multi_line_address_display = $_POST["localized_multi_line_address_display"]; 




    function registerEvent() {
        global $connect, $event_name, $description, $capacity, $start_time, $end_time ,
            $is_free, $venue_name, $venue_lattitude, $venue_longitude , $localized_multi_line_address_display;



        $statement = mysqli_prepare($connect, "INSERT INTO events (event_name, description, capacity, start_time, end_time, is_free, venue_name, venue_lattitude, venue_longitude , localized_multi_line_address_display) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        mysqli_stmt_bind_param($statement, "ssisssssss", $event_name, $description, $capacity, $start_time, $end_time, $is_free, $venue_name, $venue_lattitude, $venue_longitude , $localized_multi_line_address_display);
        
        mysqli_stmt_execute($statement);
        mysqli_stmt_close($statement);     
    }


    $response = array();
    $response["success"] = false;  

    
    registerEvent();
    $response["success"] = true;
    
    echo json_encode($response);
?>