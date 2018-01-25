<!DOCTYPE html>
<html>
<head></head>
<body>   
<div class="content">
    <form action="EventRegister.php" method="POST">
        <label>event_name: </label>
        <input name="event_name" type="text" size="25" />

        <label>description: </label>
        <input name="description" type="text" size="25" />

        <label>capacity: </label>
        <input name="capacity" type="text" size="255" />

        <label>start_time: </label>
        <input name="start_time" type="text" size="7" />

        <label>end_time: </label>
        <input name="end_time" type="text" size="255" />

        <label>is_free #: </label>
        <input name="is_free" type="text" size="12" />


        <label>venue_name: </label>
        <input name="venue_name" type="text" size="255" />

        <label>venue_lattitude: </label>
        <input name="venue_lattitude" type="text" size="7" />

        <label>venue_longitude: </label>
        <input name="venue_longitude" type="text" size="255" />

        <label>localized_multi_line_address_display #: </label>
        <input name="localized_multi_line_address_display" type="text" size="12" />


        <input name="mySubmit" type="submit" value="Submit!" />
    </form>
</div>
</body>
</html>