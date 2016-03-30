# POI
Android POI app.
Persistence : SQLite database. Database has two tables for POIs and for paths to POI-photo. 
Model : POI class contains fields as columns in database and ArrayList<String> to store path for photos;
Main screen contains list of POIs (ListView with mulpitlpe choice mode),due to context menu there's
possibility to select one and go to edit_POI activity. 
Edit_POI activity : scrollable layout contains necessary items 
to edit POi and includes fragment with GoogleMap to set marker and define coordinates,toolbar contains buttons to save or
operation. 
Add_POI activity : scrollable layout contains editable necessary items for creating new POI, includes fragment with GoogleMap 
to set marker and define coordinates and button
"Add photo" - due to system content provider its possible to pick photo from gallery.
Options menu : deleteSelected - delete selected POIs; deleteAll - delete all POIs; map - show map with markers of all POIs.
