1) Start ipc-daemon2:

 ipc-daemon2.exe &


2) Create a new Postgres database in the data folder:

 initdb -D data

3) Start the database server:

 postmaster -D data -i
 
4) Create the test database:

 createdb 