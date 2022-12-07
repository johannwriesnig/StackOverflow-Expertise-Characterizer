DROP DATABASE IF EXISTS "Expertise" WITH (FORCE);

CREATE DATABASE "Expertise"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'German_Germany.1252'
    LC_CTYPE = 'German_Germany.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

\connect Expertise;

CREATE UNLOGGED TABLE Users(
	Id Integer PRIMARY KEY,
	SoName varchar(80),
	GhName varchar(80),
	ProfileImageUrl varchar(400)
);


