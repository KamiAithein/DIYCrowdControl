### DIYCrowdControl MC

#### Purpose
Be able to implement different interfaces for a Crowd Control like minecraft experience. This serves as the backend that connects to a REST server on localhost.
Interaction with the REST API would allow any interface to interact with any game implementing the REST API in real time.

### Sections:
   0. Structure
   1. Mod
   2. REST
   3. Websocket
   
### Structure
A REST endpoint on localhost which is for this instance as an example integrated with a Forge Mod Loader 1.14.4 mod that actually modifies the game. The REST endpoint will act as an interface that can be used generically for any game, and the game must figure out a way to implement the features specified in the REST endpoint on its own. 
   
#### Mod
To actually accomplish modifying the game you need to be able to _access_ the game which I've done through the Forge Mod Loader API, specifically 1.14.4. All this does is initalize the REST endpoint and pass the Server object to the REST endpoint when a server is initialized.

#### REST
I use Spark to create a REST endpoint that allows GET on /command/:command and parse the command to interact with the mod.

#### Mod
Using the Forge Mod Loader API I implement the REST API so that calling GET on the REST will modify the game appropriately; i.e. when the command is SPAWN it needs to take in an argument and the argument will correspond to a mob/entity name in Minecraft which needs to be spawned. This is true for all resources in REST
