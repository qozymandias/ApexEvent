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


    function eventAvailable() {
        global $connect, $event_name;

        $statement = mysqli_prepare($connect, "SELECT * FROM events WHERE event_name = ?"); 
        mysqli_stmt_bind_param($statement, "s", $event_name);
        mysqli_stmt_execute($statement);
        mysqli_stmt_store_result($statement);
        $count = mysqli_stmt_num_rows($statement);
        mysqli_stmt_close($statement); 
        if ($count < 1){
            return true; 
        }else {
            return false; 
        }
    }


    function createTable() {

        global $connect, $event_name;

        $event_name = preg_replace('/\s+/', '', $event_name);
        $event_name = preg_replace('/[^a-zA-Z0-9]/', '', $event_name);

        $statement = mysqli_prepare($connect, "CREATE TABLE event_rankings_$event_name (
                                      id int(6) NOT NULL,
                                      event_name varchar(60) COLLATE utf8_unicode_ci NOT NULL,
                                      user varchar(60) COLLATE utf8_unicode_ci NOT NULL,
                                      tokens bigint(20) NOT NULL
                                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;");
        mysqli_stmt_execute($statement);
        mysqli_stmt_close($statement); 
    }


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

    if(eventAvailable()) {
        registerEvent();
        createTable();
        $response["success"] = true;
    }
    
    echo json_encode($response);
?>