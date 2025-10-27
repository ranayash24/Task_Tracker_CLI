# Task Tracker CLI

## Overview
Task Tracker CLI is a simple command line interface (CLI) tool used to track and manage your tasks. It allows you to add, update, delete, and organize tasks into three states: todo, in-progress, and done. The project helps you practice your programming skills such as working with the filesystem, handling user inputs, and building CLI applications without using any external libraries.

## Features
- Add new tasks  
- Update existing tasks  
- Delete tasks  
- Mark tasks as in progress or done  
- List all tasks  
- List tasks by status (todo, in-progress, done)  
- Store all tasks in a JSON file  
- Automatically create the JSON file if it does not exist  
- Handle invalid commands or missing inputs gracefully  

## Task Properties
Each task has the following properties:
- **id**: Unique identifier for the task  
- **description**: Short description of what needs to be done  
- **status**: Current status of the task (todo, in-progress, done)  
- **createdAt**: Date and time when the task was created  
- **updatedAt**: Date and time when the task was last updated  

Example of how a task is stored in the JSON file:
```json
{
  "id": 1,
  "description": "Buy groceries",
  "status": "todo",
  "createdAt": "2025-10-27T12:00:00",
  "updatedAt": "2025-10-27T12:00:00"
}
