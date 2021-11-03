# Project Estimation  
Authors: Semeraro Lorenzo, Ernesto Cristian, Marino Vincenzo, Matees Mihai Alexandru

Date: 23/04/2021

Version: 1.0

<br />

# Contents
- [Project Estimation](#project-estimation)
- [Contents](#contents)
- [Estimate by product decomposition](#estimate-by-product-decomposition)
    - [](#)
- [Estimate by activity decomposition](#estimate-by-activity-decomposition)
    - [](#-1)
  - [Gantt Diagram](#gantt-diagram)
      - [(5 days per week, 8 hours per day, 4 people)](#5-days-per-week-8-hours-per-day-4-people)

<br />

# Estimate by product decomposition
### 
|             | Estimate                        |             
| ----------- | ------------------------------- |  
|NC =  Estimated number of classes to be developed|25|             
|A = Estimated average size per class, in LOC|100| 
|S = Estimated size of project, in LOC (= NC * A)|2500|
|E = Estimated effort, in person hours (here use productivity 10 LOC per person hour)|250|   
| C = Estimated cost, in euro (here use 1 person hour cost = 30 euro) |7500| 
| Estimated calendar time, in calendar weeks (Assume team of 4 people, 8 hours per day, 5 days per week ) |1.6|               

<br />

# Estimate by activity decomposition
### 
|         Activity name    | Estimated effort (person hours)   |             
| ----------- | ------------------------------- | 
|Requirements | 112 |
|Design | 50 |
|Coding | 250 |
|Testing | 300 |
###

<br />

## Gantt Diagram
#### (5 days per week, 8 hours per day, 4 people)


```plantuml
Saturday are closed
Sunday are closed

Project starts 2021-04-05

[Requirements] lasts 8 days
Note bottom
Requirements and Design
are partially parallel
endnote

[Design] lasts 4 days
[Coding] lasts 8 days
[Testing] lasts 10 days

[Requirements] starts 2021-04-05
[Design] starts at [Requirements]'s end
[Coding] starts at [Design]'s end
[Testing] starts at [Coding]'s end
```
