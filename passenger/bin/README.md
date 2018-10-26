# t07
Springboot backend implementation for the ride-sharing service. 

### Changelog
Sep.29, 2018: It reads the database now. 
- / returns Hello world. 
- /users returns the entire list of user_table from the DB. 
- /trips returns the entire list of trip_table from the DB. 
- Todo: Create a getter/setter class based on the current code. 

Sep.30, 2018: Added Trip and User classes
- Both classes have constructors to create an object of that type with all the user input required
- Both have getters and setters for the fields that I believe are necessary to access/alter, this can be easily expanded upon if we discover we need to
- Other methods are also included, like ones to add/remove users from a trip or to update the average driver/passenger rating of a user
- No connection has been made yet to the database or the RESTful API, just the creation of the classes
