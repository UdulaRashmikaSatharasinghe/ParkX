# ParkX Eclipse Project

This is a self-contained Eclipse Java project. It includes the complete ParkX
source code and MySQL Connector/J under `lib`.

## Import and run

1. Start Eclipse using a workspace outside this project directory.
2. Select **File > Import > General > Existing Projects into Workspace**.
3. Select this `ParkX-Eclipse` directory as the root directory.
4. Make sure **Copy projects into workspace** is cleared, then select **Finish**.
5. Open `Main.launch` and select **Run**, or run `src/Main.java` as a Java
   application.

The project uses Eclipse's default installed JRE and compiles at Java 17
compatibility. MySQL must be running on `localhost:3306`. Database credentials
can be supplied through the `PARKX_DB_USER` and `PARKX_DB_PASSWORD` environment
variables. Their defaults are `root` and an empty password.

The application creates the `parkx_db` database and its tables automatically.
The initial application login is `admin` / `1234`.
