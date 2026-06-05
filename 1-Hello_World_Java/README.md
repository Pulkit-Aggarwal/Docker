So what does docker and java file do?


### The Java program is a simple program that prints hello world. 

### The Docker file does the following- 
#### FROM eclipse-temurin:21-jdk 
- This is the base image of the container. 
- If the eclipse-temurin:21-jdk is cached then it uses that otherwise it will download the jdk
- This base image contains a linux distribution (usually Ubuntu or Debian) and the full JDK 21 installed and configured
- Every instruction after this will create a new layer on top of this base layer.

What Docker actually does 
- Checks local cache → if missing, downloads the image layers.
- Creates a new image layer on top of it for the next instructions.


#### WORKDIR /app 
- Sets the currect working directory for all the subsequent instructions 
- If /app does not exist then Docker creates it automatically inside the filesystem.
- This is the equivalent of running mkdir /app and cd /app inside the container.

What Docker actually does internally 
- Creates a new layer taht records “Whenever a command runs, treat /app as the current directory.”


#### COPY HelloWorld.java . 
- Copies HelloWorld.java from your host machine (the build context) into the container’s /app directory.
- The . means “copy into the current working directory”, which is /app because of WORKDIR.
- This creates a new immutable layer containing the file.

Important detail:  
If you change HelloWorld.java, Docker will invalidate the cache for this layer and all layers after it — forcing recompilation. This is why COPY placement matters for build speed.


#### RUN javac HelloWorld.java 
- Executes the command inside the container at build time, not at runtime.
- javac compiles the Java source file into a .class file.
- The compiled output (HelloWorld.class) is written into the same /app directory. 

- What Docker actually does internally:
- Creates a temporary container from the previous layer.
- Runs javac HelloWorld.java inside it.
- Captures the resulting filesystem changes (the .class file).
- Saves those changes as a new image layer.
- Deletes the temporary container.
- This means your final image contains the compiled Java program.


#### CMD ["java", "HelloWorld"]

- Defines the default command that runs when the container starts.
- Uses JSON array syntax so Docker executes it directly (no shell).
- Runs the Java program using the fully‑qualified class name.

Important distinction:
- RUN happens during build (compile time).
- CMD happens during container start (runtime).

What Docker actually does internally:
- Stores this command as metadata in the final image.

When you run: docker run my-image
Docker executes: java HelloWorld




### Demonstration - 

#### Build the image 

docker build -t java-demo .

It will take longer the first time as there is no cache but it will be faster all the subsequent times till the cache is invalidated. 

Each instruction becomes a layer. 
Docker prints cached or executes the step
Base image is pulled if missing 


#### Inspect the image layers 

docker history java-demo

This will show the size of each layer 
Which instruction created it
That RUN javac creates a layer containing the .class file 

#### Inspect the filesystem inside each layer 

Pick the image ID from docker history and run

docker run --rm -it <image-id> sh

Then inside the shell run 

ls -R /
ls /app


You’ll see:

- /app exists because of WORKDIR
- HelloWorld.java exists because of COPY
- HelloWorld.class exists because of RUN javac

#### Run the container normally (demonstrates CMD)

docker run --rm java-demo
This executes: java HelloWorld
because of your CMD.

