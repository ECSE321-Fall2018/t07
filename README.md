# t07
Springboot backend implementation for the ride-sharing service. 

HOW TO USE:
1. Using a REST client, send a GET request to "/instructions" to be provided with a list of actions
2. Sending the associated request for any of those instructions will provide you with the required parameters that must be entered (if necessary)

NOTE:
- Persistence is handled in the application through direct injection to the postgreSQL database. Database contents are saved automatically after all changes because of this.

### Changelog
Nov. 1, 2018: (Android, Passenger, Backend) Added Email Uniqueness Check on Login
- Since we are using email and password for logging in, then the email needs to be unique
- Added backend functionality to the /users/search REST endpoint that allows searching for email
- This endpoint returns "valid" if there are no others, and "invalid" if that email already exists
- On the android sign up page when pressing sign up it now first checks for validity
- If it is not a valid email address a small pop up says that it is already taken
- Also added 2 JUnit tests to check helper functions
- Passenger profile page showing passengers' trip (both past and upcoming at this stage)

Nov. 1, 2018: (Android, Passenger) Added Ability To Sort Trip Search
- Now on the Trip Search page there is a box with a spinner that allows you to change between different sorting modes
- These include departure time, price, available seats and total duration
- Selecting one will immediately update the listview below it
- Also some minor bug fixes and layout updates

OCt. 31, 2018: (Android, Passenger) Trip Details Page Fully Implemented
- Now when in the trip search results page and you select one of the displayed trips it brings you to the specific details for that trip
- This page properly gathers all the backend data and displays it nicely for the user
- There is a Join Trip button that allows them to be added onto the passengers list in the back end
- If they are already on the list it will know and display as Leave Trip button which will allow them to leave
- Home button added in corner that directs you back to the search page
- The user ID is properly transferred around between all pages so that you always have access to it

Oct. 31, 2018: (Android, Passenger) TripSearchResults functionality added. Trip_Details functionality specimen added. 
- ListView shows the search result. Sorting functionality yet to be added. 
- Modified some SQL command of the backend to return the matching row from BOTH trip_table and user_table. 
- TripSearchResult passes the corresponding JSON data (for the one pressed down) to Trip_Details as an argument, so just use JSONObjects command to pull out whatever data you need.
- Profile page is able to show personal information.

Oct. 30, 2018: (Android, Passenger) Sign Up Page functionality added, toolbar layour restructuring
- Back end connectivity was added to the sign up page so that it adds a new user to the database
- Note that we still need to check to make sure that their email is unique on signup
- Changed how the menu bar/toolbar works for all layouts: made it not show the toolbar by default in the manifest (as it was resulting in a double)
- Added the toolbar manually to each content layout file and removed from the activity layout files
- Toolbar can now be given titles and buttons at the top
- Button added on the Trip Search page in the top right corner to go to the profile page (however mem issues are preventing it from working at the moment)
- Log in or sign up now brings the user to the trip search page first

Oct. 29, 2018: (Android, Passenger) Completed user login
- The login view now operates with backend to perform user authentiction. 
- For successful attempt, the app transitions to the profile view. Otherwise, stays in the login view. 
- When transitioning to the profile view, the activity 'Userlogin' passes userid value (int) to 'Profile_page'. 

Oct. 29, 2018: (Android, Passenger) Completed Trip Search Page for Passenger App
- The Trip Search Page layout is now complete
- Includes textedit boxes for the departure and destination locations
- Uses the DatePickerFragment to allow the user to easily select a date
- String output was reformatted to fit the proper Java LocalDate format to be used later
- Spinner is also properly implemented with string values for 1, 2, 3, 4+ available seats
- This string will have to be parsed when searched (because of the 4+ option)

Oct. 28, 2018: (Backend) Added user authentication function in the backend. 
- Usage: Send a POST request to "/users/auth?email={id}&password={password}"
- Returns in JSON [{"userid":x}] if exists. [{"userid":-1}] if does not exist. 

Oct. 25, 2018: (Android, Passenger) Add login and signup page for Passenger
- Switch between existing pages
- Is able to reject login or signup request if desired info is not filled (need further implementation)

Oct. 7, 2018: (Backend) Fixed unintended cases for Join/Leave
- Previously you could Join a trip even if your passenger ID was already on the list, adding you twice and taking another seat
- Now it checks if your ID is already on the list and if you are, then you aren't added
- You can now only Leave a trip if your ID is already on the list, or else previously it would reduce the available seats even though no one was taken off the list
- Added HOW TO USE at the top of the README

Oct. 6, 2018: (Backend) Added JUnit tests for User and Trip classes
- 4 JUnit tests were added
- One to test some of the getters and setters of the User class
- One to test some of the getters and setters of the Trip class
- One to test the update driver/passenger rating methods of the User class
- One to test the isValidFormat function in Controller properly works with date formats

Oct. 6, 2018: (Backend) Bug fixing for most API Endpoints
- Altered the user search to work with any number of names separated by spaces
- Fixed the trip search so that it properly searches for the destination in the array within the table column
- Changed the method in the service class used by the delete endpoints to be sqlInsert so that it runs with the update commmand and doesn't return seemingly an error when it is working fine
- Changed search methods and create methods to use .toLowerCase() for certain strings so that data isn't stored in the database with a random collection of upper/lower case if the user does so, and allows for searches that wont fail
- Cleaned up leftover commented code that has long since been abandoned
- Added further comments to sections for readability

Oct. 5, 2018: (Backend) Fixing DB issue of the updates on Oct. 4. 
- Added service class to handle the DB - so that injection does not fail.  

Oct. 4, 2018: (Backend) Joining and Leaving Trips:
- Added two methods that use POST mapping to allow the user to add or join trips
- They take 2 inputs: userID and tripID
- The associated userID will be added or removed from the trip associated with the tripID (depending on the method you use)
- The available seats on the trip also change according to joining/leaving
- Was not able to test functionality of this yet, waiting on reconfigure of trip_table column's data type (See Issue #13)

Oct.4, 2018: (Backend) Adding Users/Trips and Deleting Users/Trips:
- Added a public method in the user and trip classes that takes all of their data, formulates a SQL query and inserts the row into the associated table (there appears to still be some errors in this portion, getting a NULL Pointer Exception)
- Added methods in the main application that delete a user or trip from the associated table based on ID
- Reconfigured the Trip class to use LocalDate and LocalTime instead of previous setup since it works natively with PostgreSQL

Oct.3, 2018: (Backend) Search component: 
- Changed the URI for searching trips to "/trips/search". Returns usage if no queries given. 
Usage: Send a POST request to "/trips/search?dep={departure_location}&dest={destination}&date={departure_date}&seats={seats_required}"
- Added the user search to "/users/search". Returns usage if no queries given. 
Queries: id (userid), keyword (firstname, lastname, or both but separated using space)
Usage: Send a POST request to "/users/search?id={id}" or "/users/search?keyword={name}"

Oct.2, 2018: (Backend) Added REST GET Method to create new Trip and User objects
- Each method currently takes a lot of input (all that is necessary to create a trip or user) and formats it as appropriate then creates an object from this
- This does not yet connect with the database table to create a new entry, it simply creates an instance of the object
- These methods also output a confirmation message with the name of the user or trip departure and destination locations so you know it worked
- Also added a Request method with \instructions to show how you can make a new user or trip since it requires a lot of input

Oct.1, 2018: (Backend) Added Persistence, service, model and controller packages
Change package structures:
add package model, controller, persistence, service packages inside RideShare package. Model is consisted of trip and user class. This modification is made to reduce the amount of reload when importing these two classes.
Add "Passenger ID" field in trip constructor
Add Persistence class:
method to connect database on Heroku to code
method to return newly changed trip
this is not completed yet
Change import package "util.date" to "sql.date", in the case of exchanging data "date" with PostgreSQL
Lots of work isn't finished yet, but I still want to make one commit. If you read and find any problem please tell me

Sep.30, 2018: (Backend) Added Trip and User classes
- Both classes have constructors to create an object of that type with all the user input required
- Both have getters and setters for the fields that I believe are necessary to access/alter, this can be easily expanded upon if we discover we need to
- Other methods are also included, like ones to add/remove users from a trip or to update the average driver/passenger rating of a user
- No connection has been made yet to the database or the RESTful API, just the creation of the classes

Sep.29, 2018: (Backend) It reads the database now. 
- / returns Hello world. 
- /users returns the entire list of user_table from the DB. 
- /trips returns the entire list of trip_table from the DB. 
- Todo: Create a getter/setter class based on the current code. 
