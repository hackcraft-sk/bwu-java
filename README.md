# BWU-Java (JNIBWAPI Utility Layer)

**BWU-Java** uses [JNIBWAPI](https://code.google.com/p/jnibwapi/) (which uses
[BWAPI](https://code.google.com/p/bwapi/)) to create a simple API for controlling 
StarCraft: Broodwar mainly for purposes of creating and testing artificial intelligence
algorithms. 

In the future, I believe in creating a very simple and comfortable API for accesing SC:BW
and possibly a framework to support unit control and management. I honestly hope it does many
things from the goal right now.

### Current version

This project is currently under heavy development. It can be used to write simple micro bots,
but it's not recommended for huge full game bot development yet. It has been used in
[StarCraft Micro AI Tournament 2](http://scmai.hackcraft.sk) to build fully functional bot
that achieved 2nd place in the Experienced part of the tournament.

## What is where

This repository contains few other things apart from pure bwu-java (package ``sk.hackcraft.bwu``).

*	Sample bot written in pure bwu-java as a demonstration in [StarCraft Micro AI Tournament 2](http://scmai.hackcraft.sk),
	package ``sk.hackcraft.bwu.sample``
*	Sample bot written in pure JNIBWAPI that took 2nd place in
	[StarCraft Micro AI Tournament 1](http://hackcraft.sk/article/default/starcraft-micro-ai-tournament-2013-report),
	package ``sk.nixone.microqueen``
*	Pure JNIBWAPI which bwu-java uses as a backbone, package ``javabot``

## Code samples

Is my unit of a certain type? (Really crappy in pure JNIBWAPI)

```java
return myUnit.getType() == game.getUnitTypes().Terran_Marine; // that's the way i like it!
```

You want to get a center of all visible enemy units? Easy!

```java
Vector2D enemyCenter = game.getEnemyUnits().where(UnitSelector.IS_VISIBLE).getArithmeticCenter(); // yes, this easy
```

Are there more than 3 units in range of my sieged tank?

```java
// just, say it...
return game.getEnemyUnits()
		.whereLessOrEqual(
			new DistanceSelector(unit), 
			game.getWeaponTypes().Arclite_Shock_Cannon.getMaxRange()
		)
		.size() > 3;
```

We want first enemy unit in range of our ghost to lock down, prioritized by: Battlecruiser, than Tank

```java
// simple, but really useful
Unit lockdownTarget = game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(ghost), range).firstOf(
	game.getUnitTypes().Terran_Battlecruiser,
	game.getUnitTypes().Terran_Siege_Tank_Siege_Mode,
	game.getUnitTypes().Terran_Siege_Tank_Tank_Mode
);
```

Are my units at a certain specified position? Decides, if the average distance of all units from certain position
is less or equal than tolerance.

```java
// obvious, but clean
game.getMyUnits().areAt(certainPosition, tolerance);
```

## Sample bot

```java
package sk.hackcraft.bwu.sample;

import java.util.Random;

import javabot.model.Player;
import sk.hackcraft.bwu.Bot;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.Graphics;

public class SampleBot extends Bot {	
	static public void main(String [] arguments) {
		Bot bot = new SampleBot();
		bot.start();
	}
	
	private Random random = new Random();
	private Game game = null;
	
	@Override
	public void onGameStarted(Game game) {
		this.game = game;
	}

	@Override
	public void onGameEnded() {
		game = null;
	}
	
	@Override
	public void onGameUpdate() {
		for(Unit unit : game.getMyUnits()) {
			if(unit.isIdle()) {
				unit.attack(Vector2D.random().scale(
					game.getMap().getWidth(),
					game.getMap().getHeight()
				));
			}
		}
	}
	
	@Override
	public void onDraw(Graphics graphics) {}
	
	public void onConnected() {}
	public void onDisconnected() {}
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
}

```

### License

This software is released under BSD license (as seen in LICENSE.md file). It uses parts of LGPL licensed software (JNIBWAPI).
