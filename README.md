# BWU-Java (JNIBWAPI Utility Layer)

In the future, I believe in creating a very simple and comfortable API for accesing SC:BW
and possibly a framework to support unit control and management. I honestly hope it does many
things from the goal right now.

**BWU-Java** uses [JNIBWAPI](https://code.google.com/p/jnibwapi/) (which uses
[BWAPI](https://code.google.com/p/bwapi/)) to create a simple API for controlling 
StarCraft: Broodwar mainly for purposes of creating and testing artificial intelligence
algorithms.

### Current version

This project is currently under heavy development. It can be used to write simple micro bots,
but it's not recommended for huge full game bot development yet. It has been used in
[StarCraft Micro AI Tournament 2 & 3](http://scmai.hackcraft.sk) to build fully functional bot
that achieved 2nd place in the Experienced part of the tournament.

## Where to start (TODO)

If you are new to **BWU-Java**, best start for you is to look in the ``tutorials`` folder in the repository. You will
find an example to build a simple sample bot capable of playing simple micro match from the scratch.

## What is where

This repository contains few other things apart from pure bwu-java (package ``sk.hackcraft.bwu``).

*	Sample bot written in pure bwu-java as a demonstration in [StarCraft Micro AI Tournament 3](http://scmai.hackcraft.sk),
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

### License

This software is released under BSD license (as seen in LICENSE.md file). It uses parts of LGPL licensed software (JNIBWAPI).
