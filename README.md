# VoyagerX - Twitch Cohort 2 Android Project
## Overview
Blast off with VoyagerX! This app allows you to view and keep track of the latest and greatest SpaceX launches. Users have the ability to browse and filter launches. Enjoy all the photos, videos, and articles surrounding these monumental events. If you want to keep track of your favorite launches, you can do that too by signing up for an account. Customize your experience however you want, from personalizing your profile page to changing the text size of the UI. Conquer the final frontier with VoyagerX!

## Features
### Login/Registration
- New users register using their email and password. The form validates email addresses (valid email, email not in use) and passwords (length, password confirmation). 
- After registration, users can login to access the profile page as well as the favorite launches feature.

### Landing Screen
- The landing screen gives the user access to a list of recent SpaceX launch events.
- Users can view a list of launches with overview information including mission name, launch site, launch date and an image of the launch if one is available.
- Users can search and filter the list of launches by launch site and by media available on each launch event. 
- Clicking on a launch event card will take the user to the details screen for the given launch.

### Details Screen
- The Details screen allows users to see more information and interact with a specific launch.
- Users can favorite a launch, visit external resources (articles, videos, etc), and share the launch details with friends via text message.

### Profile/Edit Profile Screen
- The profile screens are only accessible to logged in users.
- Newly registered users will initially be greeted by a default avatar (containing the first letter of the word User), profile ownership, location, and bio.
- A list of SpaceX launches previously favorited via the Details page will be visible; however, if no launches have been saved, a message stating as such will populate.
- Users can tap on a launch event and be taken to the details screen for additional insights.
- Users can tap the edit profile pencil, navigate to their edit profile page, and update their name, location, and bio. These updates will be reflected on the Profile page.

### Settings
- Users can toggle between a starry background image or a solid background color within their user profile.
- Text size is customizable throughout the app with users being able to pick from Large, Medium, or Small font sizes.
- Users can specify whether they would like to receive notifications when upcoming launch events have been observed.

## Planning
We began the four week long project by designing prototypes of each screen. We provided feedback to each other and iterated over our designs. 
Additionally, we researched the SpaceX GraphQL API using the API schema documentation and the GraphQL playground. This allowed us to understand the available data and determine what would be useful for our use case.

## Architecture/Technologies Used
- [Material Design Component Library](https://material.io/develop/android) to build the UI according to Material UI guidelines.
- [View Binding](https://developer.android.com/topic/libraries/view-binding) to easily interact with views programmatically.
- [Hilt](https://dagger.dev/hilt/) for dependency injection.
- [Room](https://developer.android.com/jetpack/androidx/releases/room) for implementation of the local database.
- [Gson](https://github.com/google/gson) to convert complicated data types to JSON objects and vice versa.
- [Apollo](https://www.apollographql.com/) for fetching GraphQL queries.
- MVP Architecture to separate a fragment’s business logic from its UI logic.
- [Coroutines](https://developer.android.com/kotlin/coroutines) to make async calls to the SpaceX API and Room Database

## Future Development Goals
- Fully convert to MVP architecture
- Implementation of user profile picture
- Implementation of user notification of new launches
- Ability to retrieve more launches - pagination
- Full scale user registration/login system (encryption, etc.)


## Acknowledgements
This app was built over 4 weeks in April 2022 by Vivian Li, Terry Brown, Chris Bell, and Celina Rabe as part of the Onramp + Twitch Android Apprenticeship.
Our amazing mentor, Joel Camargo Jr., helped facilitate team communication, daily standups, sprint planning, as well as code reviews.


