Approach.
1. Used 1 activity and multi fragment for the application.
2. Used Android navigation to visually understand/design the flow of the app.
3. Used MVVM for core architecture of the project.
4. Used Retrofit for network api calls.
5. Used Recyclerview with Differ 
6. Glide for handling the image related operations. 
7. kotlin-android-extensions

Used MVVM for this particular sample application because it's quick to implement and takes less time in debugging.
Also keeps things organized so it's easy to add new features if required. One more reason to implement MVVM using
repository is the data source expansion in future. At the moment the data source is just the api but considering a localcache or
database to be added in future, it was be easier to attach to the existing architecture without making changes on the UI.

_________________________________
Possible Scope of Improvements.
_________________________________
1. Add room database to store the reminders
2. Match Details Page
3. Improving UI/UX 
4. Could have created multiple view-model for diff models.
5. Add Test cases and do a UI automation testing