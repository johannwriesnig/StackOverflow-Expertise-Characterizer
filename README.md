# StackOverflow Expertise Characterizer

The Characterizer application is a Java program that computes a [StackOverflow](https://stackoverflow.com/)-Users expertise for some given topics.

Given some StackOverflow ids, the application uses the [StackExchange-Api](https://api.stackexchange.com/) to retrieve the users' profile data. With this data, it then utilizes the [GitHub-Api](https://docs.github.com/en/rest?apiVersion=2022-11-28) to find the users' potential [GitHub](https://github.com/) accounts. After this first phase, it then makes use of the [StackOverflow-Data-Dumps](https://archive.org/details/stackexchange) to determine the users' expertise for some given tags. Additionally, it will fetch repositories of found Github accounts and analyze their quality, thus their expertise, if they can be associated with certain tags and are written in Java or Python. Next, the users' expertises computed for StackOverflow and Github are used together, to determine final tag expertise scores that range between 1 and 5. Finally, the scores are mapped to textual descriptions and displayed in the GUI. 

Expertise-Legend:

| Score | Description |
|-------|-------------|
| 1-1.99| 'Beginner', limited knowledge or experience |
| 2-2.99| 'Intermediate', some knowledge or experience |
| 3-3.99| 'Proficient', good knowledge or experience |
| 4-5| 'Expert', high level of knowledge or experience |

## Requirements

StackOverflow-Data-Dumps: The data dumps are used to compute a users' expertise on StackOverflow. You need to download [Users.xml](https://archive.org/download/stackexchange/stackoverflow.com-Users.7z), [Posts.xml](https://archive.org/download/stackexchange/stackoverflow.com-Posts.7z) and [Votes.xml](https://archive.org/download/stackexchange/stackoverflow.com-Votes.7z). <br>
PostgreSQL: The application uses a [PostgreSQL](https://www.python.org/downloads/) database, that stores the StackOverflow-Data-Dumps.<br>
Python: In order to analyze Python project metrics, the application uses Radon which is run with [Python](https://www.python.org/downloads/). ***To enable Python projects analysis make sure python is added as path-variable, thus can be called as 'python' via cmd. Since Radon may not work with older Python versions check Radons [ReadMe-Requirements](https://github.com/rubik/radon/blob/master/README.rst).*** <br>
GitHub-Api-Token: For increased rate limits, a personal access token for the GitHub-Api is used.                                                                 [Here](https://docs.github.com/en/enterprise-server@3.4/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token) is a nice and simple step-by-step guide to create one. <br>
StackExchange-Api-Key: For increased rate limits, a personal key for the Stack-Exchange-Api is used. Click [here](https://stackapps.com/apps/oauth/register) and register your application. You can type placeholders in every field. At the end, check 'Enable Client Side OAuth Flow' and 'Register Your Application'. You will then get to see your key. <br>

***Hint:*** You can use the program without any API token/key but be aware that the API rate limits will prevent you from doing many requests! Also, it is not necessary to download Python if you don't want to analyze any Python projects.

## Setup 

#### Creating Databases

At first, place Posts.xml, Users.xml and Votes.xml (unzipped) in db/dumpsDb/xml. Then you can call the Characterizer-Application with additional parameter c. The program tries to find db/dumpbsDb/xml/(Posts.xml|Votes.xml|Users.xml) and converts existing ones into CSV and remove unneeded columns. You can find the .csv files in db/dumpsDb/csv/ and delete the .xml files, since they are not needed from now on. Having the .csv files in db/dumpsDb/csv and Postgresql installed you can use the init.cmd script in db/dumpsDb to create the dumps-database.<br>
<br>
Next, navigate to db/expertiseDb/. The init.cmd creates a database that is used to store the users' expertise. Before running the script set the tags in the config.properties file. Based on these tags the application will later compute the users' expertises and the script adapts the createDB.sql file to contain them in the Users table. ***Tags are delimited by commas e.g. 'tags=java,python,spring'.***

#### Install Radon

Unzip the tools.zip folder. Then navigate to tools/radon-master/ and run 'python setup.py install'(You can also find the installation description under tools/README.md). ***Make sure you keep the hierarchy 'tools/radon-master/radon'.***

#### Set config.properties

Values -> <br>

dumpsDB.user: user of your dumps database, e.g. 'postgres'. <br>
dumpsDB.password: password of your dumps database, e.g. '123'. <br>
dumpsDB.url: url of your dumps database, e.g. ' ***jdbc:postgresql://*** localhost:5432/StackOverflow'. <br>

expertiseDB.user: user of your dumps database, e.g. 'postgres'. <br>
expertiseDB.password: password of your dumps database, e.g. '123'. <br>
expertiseDB.url: url of your dumps database, e.g. ***jdbc:postgresql://*** localhost:5432/Expertise. <br>

git.token: your GitHub-Api token. <br>
git.max.repo.size.mb: the maximum size in MB a repository is allowed to have to be downloaded and analyzed, e.g. '200'.<br>

stack.key: your Stack-Api key. <br>

tags: expertise is computed for set tags, e.g. 'java,python,spring'. <br>


## Usage

Start the application by calling it with the config.properties as an argument. A GUI comes up where you can type in user ids delimited by commas. Press start and the program starts running. After some time, the users and their expertises are displayed and the collected data is also stored in the expertise-database. 

