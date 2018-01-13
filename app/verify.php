<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Eventblock > Sign up</title>
    <link href="css/style.css" type="text/css" rel="stylesheet" />
</head>
<body>
    <!-- start header div --> 
    <div id="header">
        <h3>Eventblock > Sign up</h3>
    </div>
    <!-- end header div -->   
     
    <!-- start wrap div -->   
    <div id="wrap">
        <!-- start PHP code -->



        <?php


 
            echo '<div class="statusmsg">Connecting...</div>';


            $connect = mysqli_connect("mysql.hostinger.com", "u392533552_oscar", "Oscar191097", "u392533552_users") or die(mysqli_error());

            if(isset($_GET['email']) && !empty($_GET['email']) AND isset($_GET['hash']) && !empty($_GET['hash'])){
                // Verify data
                $email = $_GET['email']; // Set email variable
                $hash = $_GET['hash']; // Set hash variable
                
                echo '<div class="statusmsg">Searching...</div>';

                      
                if(found()){
                    // We have a match, activate the account
                    echo '<div class="statusmsg">Great Success!</div>';
                    update();
                    echo '<div class="statusmsg">Your account has been activated, you can now login</div>';
                    
                }else{
                    // No match -> invalid url or account has already been activated.
                    echo '<div class="statusmsg">The url is either invalid or you already have activated your account.</div>';
                }
                  
                             
            }else{
                // Invalid approach
                echo '<div class="statusmsg">Invalid approach, please use the link that has been send to your email.</div>';
            }


            function found() {
                global $connect, $email, $hash;
                $statement = mysqli_prepare($connect, "SELECT * FROM users WHERE email = ? AND hash = ? AND active='0'"); 
                mysqli_stmt_bind_param($statement, "ss", $email, $hash);
                mysqli_stmt_execute($statement);
                mysqli_stmt_store_result($statement);
                $count = mysqli_stmt_num_rows($statement);
                mysqli_stmt_close($statement); 
                if ($count > 0){
                    return true; 
                }else {
                    return false; 
                }
            }



            function update() {
                global $connect, $email, $hash;
                $statement = mysqli_prepare($connect, "UPDATE users SET active='1' WHERE email=? AND hash = ? AND active='0'"); 
                mysqli_stmt_bind_param($statement, "ss", $email, $hash);
                mysqli_stmt_execute($statement) or die(mysqli_error());
                mysqli_stmt_store_result($statement);
                $count = mysqli_stmt_num_rows($statement);
                mysqli_stmt_close($statement); 
                
            }

             
        ?>



        <!-- stop PHP Code -->
 
         
    </div>
    <!-- end wrap div --> 
</body>
</html>