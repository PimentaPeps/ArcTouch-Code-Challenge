# ArchTouch-Code-Challenge
Project for code challenge on ArchTouch


General
----------
Android project - gradle

This project uses libraries:

``````
*Room for database creation and management
*Apache commons for API calls
*Jackson's faster xml for object serialization/deserialization
*Picasso for Image loading and cache management
*Gson for Json
*Cardview for cardview support
*LFJ4 for logs
``````

It has 3 different flavors:
``````
mockFake - Fake network with memory cache
mockNetSimulation - Simulate network with delay
prod - No mocks
``````
With 2 different build types:
``````
Debug
Release
``````
Mock and MockIntegrated don’t use Release, it for test purpose only.

This project uses a local db (mysql) with Room to do persistency of some data. On network absence the user still can see movie information. At the time images still need network

It has a memory cache for faster searches and filtering.

User have four types of filter, and can search them by containing word. New filters can be added easily to the project.

Architecture pattern
----------

The project is structured on MVP-Clean architecture - UI/Presenter/Use Cases (Business)/Persistence-Data