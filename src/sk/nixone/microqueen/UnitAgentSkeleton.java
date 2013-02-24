package sk.nixone.microqueen;

import java.util.LinkedList;

import javabot.model.Unit;

abstract public class UnitAgentSkeleton {
	static abstract public class Order {
		abstract public int getTimeout();
		abstract public void run(MicroQueen queen, Unit unit);
		abstract public boolean isCompleted();
		
		private boolean finished = false;
		
		public boolean isFinished() {
			return finished;
		}
		
		private void finish() {
			finished = true;
		}
	}
	
	
	final protected MicroQueen queen;
	final protected Unit unit;
	
	private LinkedList<Order> orders = new LinkedList<UnitAgentSkeleton.Order>();
	private Order currentOrder = null;
	private Order lastOrder = null;
	private int lastOrderFinishedFrame = 0;
	
	public UnitAgentSkeleton(MicroQueen queen, Unit unit) {
		this.queen = queen;
		this.unit = unit;
	}
	
	public void printUnder(int line, String text) {
		queen.BWAPI.drawText(unit.getX(), unit.getY()+line*10, text, false);
	}
	
	public void order(Order order) {
		orders.addLast(order);
	}
	
	public void onNextFrame(int frameNumber) {
		if(currentOrder != null) {
			if(currentOrder.isCompleted()) {
				lastOrder = currentOrder;
				currentOrder = null;
				lastOrderFinishedFrame = frameNumber;
			}
		}
		if(lastOrder != null) {
			if(frameNumber - lastOrderFinishedFrame > lastOrder.getTimeout()) {
				lastOrder.finish();
				lastOrder = null;
			}
		}
		
		if(currentOrder == null && lastOrder == null) {
			currentOrder = orders.pollFirst();
			if(currentOrder != null) {
				currentOrder.run(queen, unit);
			}
		}
		
		printOrders();
	}
	
	public boolean hasRunningOrder() {
		return currentOrder != null;
	}
	
	public boolean isReadyForOrders() {
		return currentOrder == null && lastOrder == null && orders.size() == 0;
	}
	
	private void printOrders() {
		/*queen.BWAPI.drawText(unit.getX(), unit.getY(), "Orders: "+orders.size(), false);
		queen.BWAPI.drawText(unit.getX(), unit.getY()+10, "Empty: "+(isReadyForOrders() ? "true" : "false"), false);
		queen.BWAPI.drawText(unit.getX(), unit.getY()+20, "Current: "+currentOrder, false);
		queen.BWAPI.drawText(unit.getX(), unit.getY()+30, "Last: "+lastOrder, false);*/
	}
}
