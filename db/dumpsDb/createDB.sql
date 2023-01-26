DROP DATABASE IF EXISTS "StackOverflow" WITH (FORCE);

CREATE DATABASE "StackOverflow"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'German_Germany.1252'
    LC_CTYPE = 'German_Germany.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

\connect StackOverflow;

CREATE UNLOGGED TABLE Users(
	Id Integer,
	Reputation Integer,
	DisplayName varchar(80),
	WebsiteUrl varchar(400),
	Location varchar(200),
	ProfileImageUrl varchar(400),
	AccountId Integer
);

CREATE UNLOGGED TABLE Votes(
	Id Integer,
	PostId Integer,
	VoteTypeId Integer,
	UserId Integer
);

CREATE UNLOGGED TABLE Posts(
	Id Integer,
	PostTypeId Integer,
	ParentId Integer,
	AcceptedAnswerId Integer,
	Score Integer,
	ViewCount Integer,
	Body Text,
	OwnerUserId Integer,
	Title varchar(200),
	Tags varchar(200)
);

\copy Users FROM 'csv/Users.csv' WITH DELIMITER ',' CSV HEADER ENCODING 'UTF8';
\copy Votes FROM 'csv/Votes.csv' WITH DELIMITER ',' CSV HEADER ENCODING 'UTF8';
\copy Posts FROM 'csv/Posts.csv' WITH DELIMITER ',' CSV HEADER ENCODING 'UTF8';

ALTER TABLE Users
ADD CONSTRAINT users_pk
PRIMARY KEY(Id);

ALTER TABLE Posts
ADD CONSTRAINT posts_pk
PRIMARY KEY(Id);

ALTER TABLE Votes
ADD CONSTRAINT votes_pk
PRIMARY KEY(Id);

CREATE INDEX ON Posts(owneruserid);
CREATE INDEX ON Votes(userid);
CREATE INDEX ON Votes(postid);
