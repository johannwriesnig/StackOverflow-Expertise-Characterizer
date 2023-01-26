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
 
CREATE TABLE Users( 
id Integer PRIMARY KEY, 
stackId varchar(80), 
gitId varchar(80), 
profileImageUrl varchar(400) 
,java Numeric 
,python Numeric 
,hibernate Numeric 
,spring Numeric 
,bottle Numeric 
,flask Numeric 
,django Numeric 
,gwt Numeric 
); 
