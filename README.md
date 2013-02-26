# JNIBWAPI Utility Layer (bwu-java)

In the future, bwu-java layer is supposed to support developers by providing nice and comfortable API and utility classes to JNIBWAPI so that they can focus on artificial intelligence rather than coding in an uncomfortable and messy environment.

### Current version

This project is currently under heavy development. It is far from finished and also far from alpha version.

## Overview

Why to write

```java
int hitpoints = bwapi.getUnitType(unit.getTypeID()).getMaxHitPoints();
```
if you could write

```java
int hitpoints = unit.getType().getMaxHitPoints();
```

Or why to write

```java
Set<Unit> myFlyers = new HashSet<Unit>();
for(Unit unit : bwapi.getMyUnits()) {
  if(bwapi.getUnitType(unit.getTypeID()).isFlyer()) {
    myFlyers.add(unit);
  }
}
```

if you can write

```java
Set<Unit> myFlyers = bot.getMyUnits().isFlyer();
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
				unit.attack(new Vector2D(
					random.nextDouble()*game.getMap().getWidth(),
					random.nextDouble()*game.getMap().getHeight()
				));
			}
		}
	}
	
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
