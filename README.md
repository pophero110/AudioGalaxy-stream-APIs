# Audio Galaxy Streaming API
Streaming Music App That Is Out of This World!

## Tables of Contents

- [Project Planning](#project-planning)
- [Project Overview](#project-overview)
- [User Stories](#user-stories)
- [MVP](#mvp-------completed-------not-completed)
- [ERD](#erd)
- [REST Endpoints](#rest-endpoints)
- [How To Install Dependencies](#how-to-user-audio-galaxy-streaming-api)
- [Favorite Parts](#favorite-parts)
- [Wins & Hurdles](#wins--hurdles)
- [Languages Used](#languages-used)
- [Works Sited](#works-sited)

***

## Project Overview

Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus vitae ultricies ligula. Nulla ac 
nunc et nunc maximus tincidunt. Fusce elementum auctor neque non pharetra. Sed tristique lobortis mauris, 
nec fringilla odio finibus nec. Curabitur lobortis bibendum quam, ac suscipit ligula commodo eu. Mauris in 
ligula malesuada, venenatis ex id, iaculis nunc. Integer gravida ante sed lorem posuere sollicitudin. 
Vestibulum accumsan euismod dolor, ut pellentesque justo consequat nec. Nam tristique, odio et dignissim 
consectetur, sem felis tristique odio, sit amet eleifend odio ligula vitae ex.
***

## Project Planning

### Progress Snapshot 1
![Kanban Board 1](./img/kanban1.png)<br><br>

### Progress Snapshot 2
![Kanban Board 2](./img/kanban2.png)<br><br>

### Progress Snapshot 3
![Kanban Board 3](./img/kanban3.png)<br><br>


[View Planning Kanban Board](https://github.com/users/pophero110/projects/7)

***

## User Stories

#### <ins>Unauthenticated User</ins>
- As a user, I'm able to create an account and access public resources so I'm able to log in<br>
- As a user, I'm able to log in to my account so I can access authenticated resources.

#### <ins>Authenticated User</ins>

#### User
- As a user, I'm able to update my profile such as username so I can correct any typo in the username
- As a user, I'm able to delete my account so I can remove my information from the system.

#### Playlist
- As a user, I'm able to get a list of playlists from my library so I can listen to a specific playlist
- As a user, I'm able to search other users' public playlists to add as one of my playlists.
- As a user, I'm able to create a playlist so I can organize my songs and find them easily
- As a user, I'm able to update my playlist to give it another name or make that playlist public or private.
- As a user, I'm able to delete a playlist to remove it from my library.

#### Song
- As a user, I'm able to add songs to playlists
- As a user, I'm able to delete songs from a specific playlist.
- As a user, I'm able to get a list of songs so I can see the random 50 songs.
- As a user, I'm able to search for a song by title, artist name, or genre in order to add it to my playlist

***

## MVP ( - ✓ - Completed / - ✗ - Not Completed)

1. Should consist of 3 models.  - ✓
2. The environment settings should be using Spring Profiles.  - ✓
3. Use a combination of Spring Security and JWT Tokens.  - ✓
4. The routes should be protected by JWT except for login/register.  - ✓
5. A minimum of 1 REST API point that is fully CRUD operable.  - ✗
6. API endpoints must be able to perform CRUD based on business use-case.  - ✗
7. Provide CRUD routes that were built based on rest conventions to be a part of the API.  - ✓
8. Have graceful exception handling in place.  - ✓
9. In the event that an  exception occurs, you should send error message to user.  - ✓
10. Code on different branches during development.  - ✓
11. Utilize KISS & DRY Principals.  - ✗
12. Write code that is test driven with TDD Approach.  - ✓ 
13. Unit test the controller & service classes with MockMVC.  - ✓
14. Write Doc Strings for each method. - ✗
15. Provide REST Endpoints. - ✓

***
## ERD

#### User & Playlist ERD <br>
![User & Playlist ERD](./img/User_Library_ERD.jpg)<br><br>

#### Playlist & Song ERD <br>
![Playlist & Song ERD](./img/Playlist_Song-ERD.jpg)<br><br>
[ERD Resource Link](https://drive.google.com/file/d/1aKHH1yrHc_hTh5BsFtUyS7XPm4ydv1Rg/view?usp=sharing)

***

## Rest Endpoints

#### <ins>Playlist Endpoints</ins>
| Request Type | URL               | Functionality     | Access  |   |
|--------------|-------------------|-------------------|---------|---|
| POST         | api/playlists     | Create Playlist   | private |   |
| GET          | api/playlists     | Get all Playlists | private |   |

#### <ins>User Endpoints</ins>
| Request Type | URL              | Functionality | Access  |   |
|--------------|------------------|---------------|---------|---|
| POST         | api/users/       | Register      | public  |   |
| POST         | api/users/login/ | Login         | public  |   |
| PUT          | api/users/       | Update User   | Private |   |

<br>

***

## How To Install Dependencies

#### List of dependencies used
 - SpringBoot
   - Starter REST
   - Dev Tools
   - JPA
   - Starter Security
   - Plugin/Mavin
 - H2 Database
 - Junit
 - JWT Token
 - JJWT Jackson

#### How to install dependencies
Copy and paste the code below into your pom.xml file.  Once copied right mouse click on your pom.xml file and select
Maven and then Reload project.  This will install the dependecnies.
<br><br>

```

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-rest</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>

		<!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api -->
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-api</artifactId>
			<version>0.11.5</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-impl -->
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-impl</artifactId>
			<version>0.11.5</version>
			<scope>runtime</scope>
		</dependency>
		<!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-jackson -->
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-jackson</artifactId>
			<version>0.11.5</version>
			<scope>runtime</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>




```
***

## Favorite Parts

***

## Wins & Hurdles

#### <ins>Wins</ins>
Through collaboration, we reached a consensus on the following list, which we proudly consider to be our wins for this project. 

- We each acquired valuable experience and knowledge while conducting pair programming.
- We recognize the importance of efficient time management when collaborating with a team.
- We developed the ability to effectively priority user stories, enabling us to successfully reach the Minimum Viable Product (MVP).
- We are proud of the significant progress we made within the set timeframe we had.

#### <ins>Hurdles</ins>
As a team, we encountered the following list of hurdles during this project.

 - <b>Working as a team with varying levels of experience:</b> We faced the challenge of leveraging each team member's 
 strengths and effectively assigning tasks accordingly.

 - <b>Testing controllers without authentication:</b> One of our obstacles was finding a way to test the 
controllers without the need for authentication.

 - <b>Project management using Git:</b> We encountered difficulties in managing the project collaboratively using Git.
<br>

***

## Languages Used
 - Java
 - Spring Boot

***

## Works Sited
We utilized this [tool](https://app.diagrams.net/) for creating ERD diagrams - [Google Diagrams](https://app.diagrams.net/)