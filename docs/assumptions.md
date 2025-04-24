## 📌 Assumptions Made During Implementation

- ### Award data category is available only in the CSV file

The OMDb API provides an Awards field but does not include specific award categories. Therefore, all "Best Picture"
award information is sourced exclusively from the academy_awards.csv file.

- ### Movie titles are assumed to be unique

The solution assumes that movie titles are unique identifiers when querying the OMDb API, and that no duplicate titles
exist across different years or entries.

- ### Only "Best Picture" entries are imported from the CSV file

During the data import process, only rows related to the "Best Picture" category are parsed and persisted into the
database. Other award categories are ignored as of now.

- ### Only user ratings are stored and listed in DB

Ratings available in OMDB API's are ignored, they are not listed and stored in database. 
