# Urban-Oasis 🌱

<div style="display: block; margin: 0 auto;">
  <img src="https://github.com/Nj0rdd/Urban-Oasis/blob/main/18bc3ebf-3f1f-4779-a11b-896c8ff31936.png" alt="Image description">
</div>

## Project Setup and Running Instructions

First:
```
git clone https://github.com/marcusmpersson/Urban-Oasis.git
```

### Java 
Navigate to the Main.java file and run the project using a maven configuration of your choice, run mvn package in order to convert the client into a runnable Jar file if desired.

If you choose to run a server in tandem with the client then navigate to: **src/main/java/Client/src/controller/ClientConnection.java** and set the **server_url** to your own ip that it's being hosted on.

### Rust
Make sure that you have cargo installed in order to compile Rust.

For the rust server first run:
``` cd Server ```

then edit private_cons.rs using editor of choice and populate the three variables named:
JWT_SECRET
REFRESH_SECRET
URL_DB
The URL_DB is the SRV link to your mongoDB instance.

When that's all done run:
```cargo run```
And you should see a confirmation that rocket has fired up binding to your ipv4 or localhost adress automatically.
