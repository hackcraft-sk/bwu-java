# Building your first bot
 
Before we start to write code, you need to understand few things about *BWU-Java*. It's structure is very similar to JNIBWAPI (if you already worked with JNIBWAPI, it will seem very similar but much more comfortable, if you didn't, don't worry, everyhing will be explained later).
 
## Basic classes and their meaning
 
There are few classes that you will meet on daily basis developing with *BWU-Java*.
 
* ``sk.hackcraft.bwu.Bot`` is the class your bot will be extending. You create main function, in which you create your ``SampleBot`` instance (for example). Then you call ``bot.start()`` and everything else is handled inside your ``SampleBot`` object in abstract methods that ``Bot`` provides. It also provides few other funcionality to configure your bot how they behave to the StarCraft environment.
 
* ``sk.hackcraft.bwu.Game`` represents a single game/match that occurs. It contains references about the map you are playing on, players you are against and also methods to retrieve your or enemy units. Most of the things you will need, you will find in this class.
 
* ``sk.hackcraft.bwu.Unit`` represents a single unit in a game. It has many methods for unit manipulation and will be covered in special tutorial. Building are also considered as units in *BWU-Java*.
 
* ``sk.hackcraft.bwu.selection.UnitSet`` represents collection of many units. It is very powerfull class and has a whole framework built around it. For now you just need to know that it implements ``Set<Unit>`` so we can treat it like a traditional Set. Also, the ``Game`` object returns a ``UnitSet`` when you retrieve your or enemy units.


These are the few important classes you will encounter building your sample bot. There are many other utilities, but we will cover them in next tutorials, so hold on.

## Bot
 
Create a new class, for example, ``SampleBot``, that extends ``sk.hackcraft.bwu.Bot``, add unimplemented methods (your IDE should do this for you). Your class should look something like this:
 
```java
public class SampleBot extends Bot {
    public void onConnected() {}
    public void onGameStarted(Game game) {}
        public void onGameEnded() {}
        public void onDisconnected() {}
        public void onGameUpdate() {}
        public void onKeyPressed(int keyCode) {}
        public void onMatchEnded(boolean isWinner) {}
        public void onPlayerLeft(Player player) {}
        public void onNukeDetected(Vector2D position) {}
        public void onUnitDiscovered(Unit unit) {}
        public void onUnitDestroyed(Unit unit) {}
        public void onUnitEvaded(Unit unit) {}
        public void onUnitCreated(Unit unit) {}
        public void onUnitMorphed(Unit unit) {}
        public void onUnitShown(Unit unit) {}
        public void onUnitHidden(Unit unit) {}
        public void onDraw(Graphics graphics) {}
}
```
 
As you can see, there are many empty methods that don't do a thing. Why? There are called *callbacks* (or handlers). The code inside these methods will run, whenever corresponding event occurs in a game. You should never call these methods directly. For example, code inside ``onUnitShown`` is ran whenever some unit appears in a game. The parameter is automatically set to the ``Unit`` object corresponding to the unit that appeared. Because we don't know what we want to do when something happens, every method is empty.
 
However, even if we placed some code to these methods, they would never run, because *we didn't start* this bot yet. But don't worry, it's simple. At the beginning of this class (or any class, but we will do this here) we need to create a ``main`` method that creates a ``SampleBot`` instance and starts it:
 
```java
public class SampleBot extends Bot {
        static public void main(String [] arguments) {
                SampleBot bot = new SampleBot();
                bot.start();
        }
       
        public void onConnected() {}
        public void onGameStarted(Game game) {}
        // ... rest of the empty methods ...
}
```
 
Now your bot is ready. It doesn't do a thing, but the handlers (code inside these empty methods, which will not stay empty as we build our bot) are run every time corresponding events occur in a game.

## Basic flow of the application
 
1. Your ``main`` function is started, you created and started your ``SampleBot``.
2. **BWU-Java** waits for.
 
TBF
 
### Updating the game state
 
There is one method among all of them, which is called more often than any other one. It's ``onGameUpdate()``. This method is called
every single *frame* of the game. *Frame* in the game is the smallest time unit that you can encounter while developing
your bot. In the game, you can see a frame as a updating and re-drawing a game state.
 
In my own experience, I call almost all of my bot logic from this ``onGameUpdate()`` method. There are many other
very useful methods, but if you need to re-evaluate something from the game as often as is possible, you need
to place the evaluation code into this method.
 
```java
public void onGameUpdate() {
        // your game state evaluation code that needs to execute very often
}
```

