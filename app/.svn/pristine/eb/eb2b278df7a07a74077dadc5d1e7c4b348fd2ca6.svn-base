package com.efficientsciences.cowsandbulls.wordwars.graphics;
/**
 * @author Nicholas Bilyk - April 2009
 */



/**
 * To use via mxml
 * <nbilykKineticScrollManager target="{targetContainer}"/>
 *
 * To use via actionscript
 * create a non-temporary instance of KineticScrollManager, passing the UIComponent you wish to
 * use kinetic scrolling on.
 *
 * Example
 * private var ksmKineticScrollManager;
 * ksm = new KineticScrollManager(component);
 */
public class KineticScrollManager 
//implements IScrollDetectorListener, IClickDetectorListener
{
	/*private static final int HISTORY_LENGTH = 5; // The amount of mouse move events to keep track of

	private final ArrayList<Shape> targetShapes = new ArrayList<Shape>();
	private ArrayList<Point> previousPoints;
	private float[] previousTimes;
	private Point velocity= new Point();
	private Boolean _enabled = true;

	public Number dampening = .8;
	public Boolean horizontalScrollEnabled = true;
	public Boolean verticalScrollEnabled = true;

	public  KineticScrollManager(ArrayList<Shape> targetVal) {
		this.targetShapes = targetVal;
		previousPoints=new ArrayList<Point>();
	}

	public ArrayList<Shape> getTargetShapes() {
		return this.targetShapes;
	}

	public void setTargetShapes(ArrayList<Shape> value){
		if (null!=this.targetShapes) {
			removeAllListeners();
		}
		this.targetShapes = value;
		if (null!=value) {
			iterateOverShapesAndCallIndividualShapeLogic(this.targetShapes);
			
		}
	}

	private void iterateOverShapesAndCallIndividualShapeLogic(
			ArrayList<Shape> targetShapes) {
		for (Shape shape : targetShapes) {
			setTargetIndividual(target);
		}
	}
	
	void setTargetIndividual(Shape target){
		target.re(TouchEvent.ACTION_DOWN, mouseDownHandler, false, 0, true);
		target.addEventListener(TouchEvent.ACTION_UP, mouseClickHandler, true, 100, true);
	}

	private void mouseDownHandler(TouchEvent event) {

		stop();
		previousPoints.add(new Point((int)event.getX(), (int)event.getY()));
		previousTimes = getTimer();
		target.stage.addEventListener(TouchEvent.ACTION_MOVE, mouseMoveHandler, false, 0, true);
		target.stage.addEventListener(TouchEvent.ACTION_UP, mouseUpHandler, false, 0, true);
	}

	private Boolean hasTouchEventListeners(DisplayObject displayTarget) {
		if (displayTarget == target) return false;
		if (displayTarget.hasEventListener(TouchEvent.ACTION_DOWN) || displayTarget.hasEventListener(TouchEvent.ACTION_UP)) return true;
		if (displayTarget.parent) return hasTouchEventListeners(displayTarget.parent);
		return false;
	}

	private void mouseMoveHandler(TouchEvent event){
		if (!enabled) return;
		if (!event.buttonDown) {
			mouseUpHandler();
			return;
		}
		Point currPoint = new Point((int)event.getX(), (int)event.getY());
		int currTime = getTimer();
		Point previousPoint =null;
		if(null!=previousPoints && previousPoints.size()>=1){
		 previousPoint = (Point)previousPoints.get(previousPoints.size()-1);
		}
		Point diff = currPoint.subtract(previousPoint);
		diff = transformPointToLocal(diff);
		moveScrollPosition(diff);

		// Keep track of a set amount of positions and times so that on release, we can always look back a consistant amount.
		previousPoints.push(currPoint);
		previousTimes.push(currTime);
		if (previousPoints.length >= HISTORY_LENGTH) {
			previousPoints.shift();
			previousTimes.shift();
		}
	}

	private void mouseUpHandler(TouchEvent event) {
		target.stage.removeEventListener(TouchEvent.ACTION_UP, mouseUpHandler);
		target.stage.removeEventListener(TouchEvent.ACTION_MOVE, mouseMoveHandler);
		if (!enabled) return;
		target.addEventListener(Event.ENTER_FRAME, enterFrameHandler, false, 0, true);

		Point currPoint = new Point((int)event.getX(), (int)event.getY());
		long currTime = Calendar.getInstance().getTimeInMillis();
		Point firstPoint = (Point)(previousPoints.get(0));
		long firstTime = (long)(previousTimes[0]);
		Point diff = new Point(currPoint.x-firstPoint.x, currPoint.y-firstPoint.y);
		float time = (currTime - firstTime) / (1000 / 60);
		velocity = new Point((int)(diff.x / time), (int)(diff.y / time));
	}

	private void enterFrameHandler(Event event) {
		velocity = new Point(velocity.x * dampening, velocity.y * dampening);
		var localVelocityPoint = transformPointToLocal(velocity);
		if (Math.abs(localVelocity.x) < .1) localVelocity.x = 0;
		if (Math.abs(localVelocity.y) < .1) localVelocity.y = 0;
		if (!localVelocity.x && !localVelocity.y) stop();
		moveScrollPosition(localVelocity);
	}

	private void mouseClickHandler(TouchEvent event) {
		if (velocity.length > 5) {
			event.stopImmediatePropagation();
		}
	}
	
	@Override
	public void onClick(ClickDetector pClickDetector, int pPointerID,
			float pSceneX, float pSceneY) {
		//See if the user clicked any of the contents
				Shape mShape;
				for (int i = 0; i < mShapesManaged.size(); i++)//For every shape
				{
					mShape = mShapesManaged.get(i);
					if (mShape.contains(pSceneX, pSceneY))
					{
						mShapeScrollContainerTouchListener.OnContentClicked(mShape);
						return;
					}
				}
	}
	
	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		// TODO Auto-generated method stub
				float XDis;
				float YDis;
				
				if (horizontalScrollEnabled) XDis = pDistanceX;
				else XDis = 0.0f;
				if (verticalScrollEnabled) YDis = pDistanceY;
				else YDis = 0.0f;
				
				//If scroll lock is enabled then prevent the 'camera' view from
				//moving into an area where there are no further contents to show
				if (ScrollLock)
				{
					if ((XDis > 0.0f) && ((mCameraX - XDis) < (mContentMinX - ScrollLockHorizontal))) XDis = 0.0f;
					if ((XDis < 0.0f) && ((mCameraX2 - XDis) > (mContentMaxX + ScrollLockHorizontal))) XDis = 0.0f;
					
					if ((YDis > 0.0f) && ((mCameraY - YDis) < (mContentMinY - ScrollLockVertical))) YDis = 0.0f;
					if ((YDis < 0.0f) && ((mCameraY2 - XDis) > (mContentMaxY + ScrollLockVertical))) YDis = 0.0f;
						
					
				}
				
				MoveContents(XDis, YDis);
		
	}

	private void MoveContents(float XDis, float YDis)
	{
		Shape mShape;
		for (int i = 0; i < mShapesManaged.size(); i++)//For every shape
		{

			
			mShape = mShapesManaged.get(i);
			mShape.setPosition(mShape.getX() + XDis, mShape.getY() + YDis);
			SetShapeAlpha(mShape);
			
		}
		
		//Record the fact that our contents have moved by updating
		//the 'camera' view coordinates
		mCameraX -= XDis;
		mCameraY -= YDis;
		mCameraX2 -= XDis;
		mCameraY2 -= YDis;
		
		//As the camera view is moved the scroll bars need updating
		UpdateScrollBars();
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		// TODO Auto-generated method stub
		
	}
	

	public  void stop() {
		target.removeEventListener(Event.ENTER_FRAME, enterFrameHandler);
		velocity = new Point();
	}

	public void setVelocity(Point value) {
		target.stage.removeEventListener(TouchEvent.ACTION_UP, mouseUpHandler);
		target.stage.removeEventListener(TouchEvent.ACTION_MOVE, mouseMoveHandler);
		target.addEventListener(Event.ENTER_FRAME, enterFrameHandler, false, 0, true);

		velocity = value;
	}

	private  void removeAllListeners() {
		target.removeEventListener(TouchEvent.ACTION_DOWN, mouseDownHandler);
		target.removeEventListener(TouchEvent.ACTION_UP, mouseClickHandler, true);
		target.removeEventListener(Event.ENTER_FRAME, enterFrameHandler);
		if (target.stage) {
			target.stage.removeEventListener(TouchEvent.ACTION_UP, mouseUpHandler);
			target.stage.removeEventListener(TouchEvent.ACTION_MOVE, mouseMoveHandler);
		}
	}

	public Boolean getEnabled() {
		return _enabled;
	}

	public  void setEnabled(Boolean value) {
		if (value == _enabled) return; // no-op
		_enabled = value;
		if (!value) {
			stop();
		}
	}

	protected void moveScrollPosition(Point diff){
		if (horizontalScrollEnabled) {
			Object(target).horizontalScrollPosition -= diff.x;
		}
		if (verticalScrollEnabled) {
			Object(target).verticalScrollPosition -= diff.y;
		}
	}

	private Point transformPointToLocal(Point p) {
		Matrix cM = target.transform.concatenatedMatrix.clone();
	cM.tx = 0;
	cM.ty = 0;
	cM.invert();
	return cM.transformPoint(p);
	}
}*/
}

