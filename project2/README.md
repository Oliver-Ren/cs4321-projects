# CS 4321 Project 2 #

In this project, we implemented a simple interpreter for SQL statements which takes in a database and a file containing several SQL queries then process and evaluate each SQL query on the database.

## Project Setup

_How do I, as a developer, start working on the project?_ 

1. _What dependencies does it have (where are they expressed) and how do I install them?_
2. _How can I see the project working before I change anything?_

## Interpreter / Harness

## Join Logic

We categorize conditions from the WHERE clause into:
	1. Comparison between constants.
	2. Inner-table select condition.
	3. Inter-table join condition.

At the start of parsing, all conditions are extracted by recursively breaking up the AND expressions. Then for each table we record: 1) its select conditions, and 2) its join condition with all of the __preceding__ tables. This way, we could move along the FROM tables, and focus only on relevant conditions when a new table needs to be joined with the current left sub-tree. And of course, constant conditions are handled before everything.

## Testing

_How do I run the project's automated tests?_

### Unit Tests

1. `rake spec`

### End-to-End Tests

1. _Run other local services / provide credentials for external services._
2. `rake spec:integration`

## Deploying

### _How to setup the deployment environment_

- _Required heroku addons, packages, or chef recipes._
- _Required environment variables or credentials not included in git._
- _Monitoring services and logging._

### _How to deploy_

## Troubleshooting & Useful Tools

_Examples of common tasks_

> e.g.
> 
> - How to make curl requests while authenticated via oauth.
> - How to monitor background jobs.
> - How to run the app through a proxy.

## Contributing changes

- _Internal git workflow_
- _Pull request guidelines_
- _Tracker project_
- _Google group_
- _irc channel_
- _"Please open github issues"_
