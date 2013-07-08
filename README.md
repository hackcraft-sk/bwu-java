# JNIBWAPI Utility Layer (bwu-java)

In the future, bwu-java layer is supposed to support developers by providing nice and comfortable API and utility classes to JNIBWAPI so that they can focus on artificial intelligence rather than coding in an uncomfortable and messy environment.

### Current version

This project is currently under heavy development. It can be used to write simple micro bots, but it's not recommended for huge full game bot
development yet. It has been used in StarCraft Micro AI Tournament 2 (5.-7. July 2013) to build fully functional bot that achieved 2nd place in the 
Experienced part of the tournament.

## Code samples

Is my unit of a certain type? (Really crappy in pure JNIBWAPI)

```java
return myUnits.getType() == game.getUnitTypes().Terran_Marine; // that's the way i like it!
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

Are my units at a certain specified position? Decides, if the average distance of all units from certain position is less or equal than tolerance.

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
