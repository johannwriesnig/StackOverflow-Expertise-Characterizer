setlocal enableDelayedExpansion
set file=createDB.sql
echo DROP DATABASE IF EXISTS "Expertise" WITH (FORCE); > %file%
echo: >> %file%
echo CREATE DATABASE "Expertise" >> %file%
echo    WITH  >> %file%
echo    OWNER = postgres >> %file%
echo    ENCODING = 'UTF8'>> %file%
echo    LC_COLLATE = 'German_Germany.1252' >> %file%
echo    LC_CTYPE = 'German_Germany.1252' >> %file%
echo    TABLESPACE = pg_default >> %file%
echo    CONNECTION LIMIT = -1 >> %file%
echo    IS_TEMPLATE = False; >> %file%
echo: >> %file%
echo \connect Expertise; >> %file%
echo: >> %file%
echo CREATE TABLE Users( >> %file%
echo	id SERIAL PRIMARY KEY, >> %file%
echo	stackId Integer, >> %file%
echo	stackDisplayName varchar(80), >> %file%
echo	gitLogin varchar(80), >> %file%
echo	profileImageUrl varchar(400), >> %file%
for /F "tokens=1,2 delims==" %%i in (../../config.properties) do (
	set line=%%i
	set tags=%%j
	if [!line!] == [tags] (
		for /F "tokens=* delims=," %%k in ("!tags!") do (
			for %%l in (%%k) do (
				echo %%l Numeric, >> %file%
			)
		)
	)

)
echo time TimeStamp); >> %file%
psql -U postgres -f createDB.sql
