# client-invoice-batch-processing
A demo project for invoice parsing, business logic, and receipt creation.

This project is a culmination of Java, Batch, SQL, Design Pattern, and algorithmic knowledge from my experience so far as a Backend Developer.

## What is in this Demo?
- Parser Job
- Workflow Job (Buisness Logic)
- Receipt Job
  
These jobs cooperate independantly to receive invoices from a client by parsing a submitted csv file, validate that these are good invoices acceptable by the company, then put them in a finalized table and return a receipt to the client of their submitted invoices.

## Skills that built this project
- Spring Batch
  - As a pure Spring Batch backend service, this project was built to showcase the amount of features Spring Batch offers in easy to setup and maintain Batch Job exection to Database services and repositories
- Hibernate
  - I used hibernate obejcts to handle all the persistant obejcts, like the invoices.
  - This gave me an easy way to map csv -> object -> table and back
  - I could alternatively have used JDBC objects for more control and precise stack trace on objects
- H2
  - The easiest way to show some SQL knowledge while keeping the project completely standalone
  - I would actually prefer using a setup database for this to persist data
- Java
  - Honestly this project is so small I didn't fit many up to date java features
  - In the future I may add some depth to this demo to show off more java features
- Design Patterns
  - I choose to go with no endpoint services since this demo is inteded to show off purely backend knowledge
  - The way I would add a front end is by implementing a react front end that submits forms to endpoints using an MVC pattern that would put submitted files into the monitored directory
  - For the jobs themselves I decided to split them up and have them run effectively as Daemon jobs
  - Why I decided to split them up:
    -  They each perform such different tasks that it would be easier to update and maintain them seperately
    -  They each watch for different input and should handle that input independantly
    -  This is the main one: Performance! As each job commits changes, the next job would be able to start on the output from the previous
  - Lastly, for the Service I use for Invoice Types I used a standard Service-DAO pattern for accessing data in the database
    - I would add that I would probably add a cache service if I aditionally pulled Invoice Types when comparing in the error workflow
- Algorithms
  - Once Again, I have not added quite enough depth to this project to show off many algorithms
  - However I have implemented some errors that show some of the ways I safely and efficently process data, with few service calls and null pointer protection.

## Future Features
- More data, flesh out the invoice and client relationship and what the client could charge for and the data collected from an invoice submission
- With more data I will also add more errors to the error workflow
  - These errors would emphasize new algorithms and new java features
- True Daemon jobs
  - Currently they are triggered one by one by the monitoring service, so instead I would like them to be independantly running before hand and only the Parser job would receive the new file
-Way in the future: full-stack application front to back support for submitting forms to be processed, and put it out there running from a homelab.  
