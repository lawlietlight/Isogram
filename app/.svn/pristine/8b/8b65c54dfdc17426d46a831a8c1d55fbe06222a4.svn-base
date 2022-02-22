package com.efficientsciences.cowsandbulls.wordwars.graphics;


import java.util.ArrayList;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.Shape;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;

import com.efficientsciences.cowsandbulls.wordwars.domain.PageChunk;
import com.efficientsciences.cowsandbulls.wordwars.managers.EntityTagManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;


/**Container based upon a Rectangle that allows a developer to add object instances based
 * upon the Shape class, e.g., Sprites, Animated Sprites, Text e.t.c. This container then
 * allows a user to scroll through these items within the confines of the ShapeScrollContainer.
 * 
 * @author Steven
 *
 */
public class OriginalShapeScrollContainer extends Rectangle implements IScrollDetectorListener, IClickDetectorListener{

	public final ArrayList<Shape> mShapesManaged = new ArrayList<Shape>();

	final ScrollDetector mScrollDetector = new ScrollDetector(this);
	final ClickDetector mClickDetector = new ClickDetector(this);

	private boolean ScrollHorizontalEnabled = true;
	private boolean ScrollVerticalEnabled = false;

	private boolean ScrollLock = true;
	private boolean ContentVisibilityControl = true;
	private boolean ContentAlphaControl = true;


	//To record where the minimum and maximum points are for the contents in our
	//scroll container that the user has added so we can work out when to lock the
	//scrolling mechanism, (because there is nothing else to see from further scrolling).
	private float mContentMinX;
	private float mContentMinY;
	private float mContentMaxX;
	private float mContentMaxY;

	//To record how much all of the contents have moved by
	private float mCameraX;
	private float mCameraY;
	private float mCameraX2;
	private float mCameraY2;

	private Rectangle mRectVertical;
	private Rectangle mRectHorizontal;

	/**
	 * Container based upon a Rectangle that allows a developer to add object instances based
	 * upon the Shape class, e.g., Sprites, Animated Sprites, Text e.t.c. This container then
	 * allows a user to scroll through these items within the confines of the ShapeScrollContainer.
	 * 
	 * @param pX the x position of the ShapeScrollContainer
	 * @param pY the y position of the ShapeScrollContainer
	 * @param pWidth the width of the ShapeScrollContainer
	 * @param pHeight the height of the ShapeScrollContainer
	 * @param pShapeScrollContainerTouchListener the event listener of the ShapeScrollContainer
	 */



	public OriginalShapeScrollContainer(float pX, float pY, float pWidth, float pHeight, IShapeScrollContainerTouchListener pShapeScrollContainerTouchListener) 
	{
		super(pX, pY, pWidth, pHeight,ResourcesManager.getInstance().vbom);
		this.setAnchorCenter(0, 0);
		this.setTag(EntityTagManager.shapeScrollContainerTag);

		mRectVertical = new Rectangle(this.getWidth(), 0.0f, (this.getWidth() * .025f), (this.getHeight() * .05f),ResourcesManager.getInstance().vbom);
		mRectVertical.setColor(0.0f, 0.0f, 0.0f, 0.5f);
		this.attachChild(mRectVertical);



		mRectHorizontal = new Rectangle(0.0f, 20, (this.getWidth() * .05f), (this.getHeight() * .025f),ResourcesManager.getInstance().vbom);
		mRectHorizontal.setColor(0.0f, 0.0f, 0.0f, 0.5f);
		mRectHorizontal.setAnchorCenter(0, 1);
		this.attachChild(mRectHorizontal);



		this.setColor(0.0f, 0.0f, 0.0f, 0.0f);
		mShapeScrollContainerTouchListener = pShapeScrollContainerTouchListener;

		//By default our 'camera' view will use the location of our container within the scene
		ResetCameraView();

		//Container padded coordinates, (top left and bottom right)
		//for application of alpha on shapes contained
		SetAlphaPadding(0.0f,0.0f);

		ResetContentArea();

		UpdateScrollBars();
	}


	@Override
	public void setPosition(final float pX, final float pY) {

		//Record the distance we are to move the ShapeScrollContainer
		float dX = pX - this.getX();
		float dY = pY - this.getY();

		//Move the ShapeScrollContainer
		super.setPosition(pX, pY);

		//Move all of the contents with respect to this
		Shape mShape;
		for (int i = 0; i < mShapesManaged.size(); i++)//For every shape
		{
			mShape = mShapesManaged.get(i);
			mShape.setPosition(mShape.getX() + dX, mShape.getY() + dY);
			SetShapeAlpha(mShape);
		}

		//Move the content area
		mContentMinX += dX;
		mContentMaxX += dX;
		mContentMinY += dY;
		mContentMaxY += dY;

		//Move the 'camera' view
		mCameraX += dX;
		mCameraX2 += dX;
		mCameraY += dY;
		mCameraY2 += dY;

		//Ensure the scroll bars are positioned with respect to
		//the ShapeScrollContainer
		UpdateScrollBars();
	}

	public void ResetCameraPosition()
	{
		MoveContents(mCameraX - this.getX(), mCameraY - this.getY());
	}
	/**
	 * Sets the height and width of the horizontal and vertical scroll bars respectively.
	 * 
	 * @param HorizontalHeight the horizontal scroll bar height
	 * @param VerticalWidth the vertical scroll bar width
	 */
	public void SetScrollBarThickness(float HorizontalHeight, float VerticalWidth)
	{
		mRectHorizontal.setHeight(HorizontalHeight);
		mRectVertical.setWidth(VerticalWidth);
	}

	/**
	 * Dictates whether the scroll bars lose visibility when the user removes
	 * touch contact with the ShapeScrollContainer.
	 * 
	 * @param VisibleOnTouch whether or not to hide scroll bars when user 
	 * touch contact is removed
	 */
	public void SetScrollBarVisibleOnlyOnTouch(boolean VisibleOnTouch)
	{
		ScrollBarVisibleOnlyOnTouch = VisibleOnTouch;
	}

	/**
	 * Sets the visibility of the horizontal and vertical scroll bars independently.
	 * Note that if there is no where to scroll to in a direction, that scroll
	 * bar will not be visible regardless.
	 * 
	 * @param Horizontal whether to display the horizontal scroll bar
	 * @param Vertical whether to display the vertical scroll bar
	 */
	public void SetScrollBarVisibitlity(boolean Horizontal, boolean Vertical)
	{

		ScrollBarHorizontalVisible = Horizontal;
		ScrollBarVerticalVisible = Vertical;
		UpdateScrollBars();
	}

	private boolean ScrollBarVerticalVisible = true;
	private boolean ScrollBarHorizontalVisible = true;

	private boolean ScrollBarVerticalFull= false;
	private boolean ScrollBarHorizontalFull = false;


	private boolean ScrollBarVisibleOnlyOnTouch = true;

	private void UpdateScrollBars()
	{
		//Vertical Scroll Bar Setup
		float TotalContentLengthY = mContentMaxY - mContentMinY + (2.0f * ScrollLockVertical);
		float TotalCameraLengthY = (mCameraY2 - mCameraY);

		float CameraPerContentLengthY = TotalCameraLengthY/TotalContentLengthY;

		//Calculate the maximum height as the scroll bar will appear
		float maxVerticalHeight = CameraPerContentLengthY * this.getHeight();
		if (maxVerticalHeight > this.getHeight()) maxVerticalHeight = this.getHeight();

		float ActualVerticalHeight;
		float ActualVerticalPosY = 0.0f;

		if ((mCameraY >= (mContentMinY - ScrollLockVertical))
				&& (mCameraY2 <= (mContentMaxY + ScrollLockVertical)))
		{
			//We are within bounds of the content are so we will use the
			//maximum vertical scroll bar height

			ActualVerticalHeight = maxVerticalHeight;
			if (CameraPerContentLengthY >= 1.0f)
			{
				//In this circumstance the content area must be the same as
				//the camera area so there is little point in showing a scroll
				//bar
				mRectVertical.setVisible(false);
				ScrollBarVerticalFull = true;
			}
			else
			{
				float ScrollBarYRatio;
				float TotalCameraTravelY = TotalContentLengthY - TotalCameraLengthY;
				float CurrentCameraY = mCameraY - (mContentMinY - ScrollLockVertical);

				if (TotalCameraTravelY > 0.0f) ScrollBarYRatio = (CurrentCameraY/TotalCameraTravelY);
				else ScrollBarYRatio = 0.0f;
				if (ScrollBarYRatio > 1.0f) ScrollBarYRatio = 1.0f;
				ActualVerticalPosY = (ScrollBarYRatio * (this.getHeight() - mRectVertical.getHeight()));
				ScrollBarVerticalFull = false;
				mRectVertical.setVisible(ScrollBarVerticalVisible);
			}
		}
		else
		{
			//We are out of bounds of the content area. Calculate the proportion
			//of the maximum scroll bar height to display based upon how far out
			//of the content area we are

			float ProportionActualVerticalHeight;
			ScrollBarVerticalFull = false;
			mRectVertical.setVisible(ScrollBarVerticalVisible);
			//mRectVertical.setVisible(true);

			if (mCameraY < (mContentMinY - ScrollLockVertical))
				ProportionActualVerticalHeight = 1.0f + (((mContentMinY - ScrollLockVertical) - mCameraY)/ this.getHeight());
			else ProportionActualVerticalHeight = 1.0f + ((mCameraY2 - (mContentMaxY + ScrollLockVertical))/ this.getHeight());

			ActualVerticalHeight = maxVerticalHeight / ProportionActualVerticalHeight;

			if (ActualVerticalHeight < 0.1f) ActualVerticalHeight = 0.1f;

			if (mCameraY < (mContentMinY - ScrollLockVertical)) ActualVerticalPosY = 0.0f;
			else ActualVerticalPosY = this.getHeight() - ActualVerticalHeight;

		}

		mRectVertical.setHeight(ActualVerticalHeight);
		mRectVertical.setPosition( this.getWidth(), 
				ActualVerticalPosY);

		//


		//Horizontal Scroll Bar Setup
		float TotalContentLengthX = mContentMaxX - mContentMinX + (2.0f * ScrollLockHorizontal);
		float TotalCameraLengthX = (mCameraX2 - mCameraX);

		float CameraPerContentLengthX = TotalCameraLengthX/TotalContentLengthX;

		//Calculate the maximum width as the scroll bar will appear
		float maxHorizontalWidth = CameraPerContentLengthX * this.getWidth();
		if (maxHorizontalWidth > this.getWidth()) maxHorizontalWidth = this.getWidth();

		float ActualHorizontalWidth;
		float ActualHorizontalPosX = 0.0f;

		if ((mCameraX >= (mContentMinX - ScrollLockHorizontal))
				&& (mCameraX2 <= (mContentMaxX + ScrollLockHorizontal)))
		{
			//We are within bounds of the content are so we will use the
			//maximum horizontal scroll bar height

			ActualHorizontalWidth = maxHorizontalWidth;
			if (CameraPerContentLengthX >= 1.0f)
			{
				//In this circumstance the content area must be the same as
				//the camera area so there is little point in showing a scroll
				//bar
				mRectHorizontal.setVisible(false);
				ScrollBarHorizontalFull = true;
			}
			else
			{
				float ScrollBarXRatio;
				float TotalCameraTravelX = TotalContentLengthX - TotalCameraLengthX;
				float CurrentCameraX = mCameraX - (mContentMinX - ScrollLockHorizontal);

				if (TotalCameraTravelX > 0.0f) ScrollBarXRatio = (CurrentCameraX/TotalCameraTravelX);
				else ScrollBarXRatio = 0.0f;
				if (ScrollBarXRatio > 1.0f) ScrollBarXRatio = 1.0f;
				ActualHorizontalPosX = (ScrollBarXRatio * (this.getWidth() - mRectHorizontal.getWidth()));
				ScrollBarHorizontalFull = false;
				mRectHorizontal.setVisible(ScrollBarHorizontalVisible);
				//mRectHorizontal.setVisible(true);
			}
		}
		else
		{
			//We are out of bounds of the content area. Calculate the proportion
			//of the maximum scroll bar width to display based upon how far out
			//of the content area we are

			float ProportionActualHorizontalWidth;
			ScrollBarHorizontalFull = false;
			mRectHorizontal.setVisible(ScrollBarHorizontalVisible);
			//mRectHorizontal.setVisible(true);

			if (mCameraX < (mContentMinX - ScrollLockHorizontal))
				ProportionActualHorizontalWidth = 1.0f + (((mContentMinX - ScrollLockHorizontal) - mCameraX)/ this.getWidth());
			else ProportionActualHorizontalWidth = 1.0f + ((mCameraX2 - (mContentMaxX + ScrollLockHorizontal))/ this.getWidth());

			ActualHorizontalWidth = maxHorizontalWidth / ProportionActualHorizontalWidth;

			if (ActualHorizontalWidth < 0.1f) ActualHorizontalWidth = 0.1f;

			if (mCameraX < (mContentMinX - ScrollLockHorizontal)) ActualHorizontalPosX = 0.0f;
			else ActualHorizontalPosX = this.getWidth() - ActualHorizontalWidth;

		}

		mRectHorizontal.setWidth(ActualHorizontalWidth);
		mRectHorizontal.setPosition( ActualHorizontalPosX, this.getHeight());


	}



	@Override 
	public boolean onAreaTouched(final TouchEvent pAreaTouchEvent,final float pTouchAreaLocalX, final float pTouchAreaLocalY) 
	{ 

		if(null!=this.getChildByTag(EntityTagManager.keyClearExpand) && this.getChildByTag(EntityTagManager.keyClearExpand).contains( pAreaTouchEvent.getX(), pAreaTouchEvent.getY())){
			boolean handled = this.getChildByTag(EntityTagManager.keyClearExpand).onAreaTouched(pAreaTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			if(handled){
				return handled;
			}
		}
		mScrollDetector.onTouchEvent(pAreaTouchEvent);
		mClickDetector.onTouchEvent(pAreaTouchEvent);

		if (pAreaTouchEvent.isActionDown())
		{
			if (ScrollBarVisibleOnlyOnTouch)
			{
				//Only display if they were displayed originally
				if (!ScrollBarVerticalFull) mRectVertical.setVisible(ScrollBarVerticalVisible);
				if (!ScrollBarHorizontalFull) mRectHorizontal.setVisible(ScrollBarHorizontalVisible);
			}
		}
		if (pAreaTouchEvent.isActionUp())
		{
			if (ScrollBarVisibleOnlyOnTouch)
			{
				mRectVertical.setVisible(false);
				mRectHorizontal.setVisible(false);
			}
		}





		return true; 
	} 

	/**
	 * Sets whether or not the scroll lock is engaged.
	 * See also the method:
	 * {@link #SetScrollLockPadding(float mPercentageVertical, float mPercentageHorizontal)}.
	 * @param Lock whether the scroll lock is engaged or not
	 */
	public void SetScrollLock(boolean Lock)
	{
		ScrollLock = Lock;
	}

	private float AlphaPaddingHorizontal = 0.0f;
	private float AlphaPaddingVertical = 0.0f;
	/**
	 * Sets up an inner rectangular area within the ShapeScrollContainer. As contents
	 * move outside this boundary they will increase in alpha. At the point where they are
	 * in contact with the ShapeScrollContainer edges itself, they will be completely
	 * transparent
	 * 
	 * @param mPercentageVertical the percentage of the height of the ShapeScrollContainer
	 * with which the vertical alpha boundary will start inside. Practically this will be between 0
	 * and 50%, (as this is half the height). e.g., if the height of the ShapeScrollContainer
	 * is 400 and you set this argument to 25% any contents below 100 from the top or above
	 * 300 will start to become alpha. At 0 and 400, they will become completely alpha.
	 * @param mPercentageHorizontal the percentage of the width of the ShapeScrollContainer
	 * with which the horizontal alpha boundary will start.
	 */
	public void SetAlphaPadding(float mPercentageVertical, float mPercentageHorizontal)
	{
		AlphaPaddingHorizontal = ((this.getWidth() / 100.0f)  * mPercentageHorizontal);
		AlphaPaddingVertical = ((this.getHeight() / 100.0f)  * mPercentageVertical);

		SetShapeAlpha();

	}

	private float ScrollLockHorizontal = 0.0f;
	private float ScrollLockVertical = 0.0f;

	/**
	 * Sets up an outer rectangle around the ShapeScrollContainer. Calling
	 * <code>SetScrollLock(true)</code> will engage the scroll lock. The user can now
	 * no longer scroll to an area where no further contents are present. This function
	 * provides the means to define how far past the last content in any direction the
	 * user is allowed to scroll. (i.e., you may wish for the user to scroll so that the
	 * last content in that direction can travel to the centre of the ShapeScrollContainer,
	 * in which case you would call <code>SetScrollLockPadding(50.0f, 50.0f)</code>.
	 * 
	 * @param mPercentageVertical the percentage of the height of the ShapeScrollContainer
	 * with which the vertical scroll lock will start outside. e.g., if you want it so the user can
	 * not scroll past the last content in any given direction then set this to 0%. If you
	 * want it so the user can scroll past this content up to the centre of the ShapeScrollContainer
	 * then set this to 50%.
	 * 
	 * @param mPercentageHorizontal the percentage of the width of the ShapeScrollContainer
	 * with which the horizontal scroll lock will start outside.
	 */
	public void SetScrollLockPadding(float mPercentageVertical, float mPercentageHorizontal)
	{
		ScrollLockHorizontal = ((this.getWidth() / 100.0f)  * mPercentageHorizontal);
		ScrollLockVertical = ((this.getHeight() / 100.0f)  * mPercentageVertical);

		//As this affects the scroll area we need to reconfigure the scroll bars
		UpdateScrollBars();
	}

	/**
	 * Sets whether the user is permitted to scroll horizontally and/or vertically.
	 * 
	 * @param Horizontal whether the user may scroll horizontally
	 * @param Vertical whether the user may scroll vertically
	 */
	public void SetScrollableDirections(boolean Horizontal, boolean Vertical)
	{
		ScrollHorizontalEnabled = Horizontal;
		ScrollVerticalEnabled = Vertical;
	}

	/**
	 * Defines whether the ShapeScrollContainer is permitted to set the visibility of
	 * content to false when the user has scrolled so that the content is outside of the
	 * bounds of the ShapeScrollContainer.
	 * The content visibility will then be set to true in the opposite case.
	 * 
	 * @param HasControl whether the ShapeScrollContainer has control of content visibility
	 */
	public void SetContentVisiblitiyControl(boolean HasControl)
	{
		ContentVisibilityControl = HasControl;
	}

	/**
	 * Defines whether the ShapeScrollContainer is permitted to set the alpha of
	 * content when the user has scrolled so that the content is outside of the
	 * bounds as defined in {@link #SetAlphaPadding(float mPercentageVertical, float mPercentageHorizontal)}.
	 * 
	 * @param HasControl whether the ShapeScrollContainer has control of content alpha
	 */
	public void SetAlphaVisiblitiyControl(boolean HasControl)
	{
		ContentAlphaControl = HasControl;
	}

	private void ResetContentArea()
	{
		mContentMinX = this.getX();
		mContentMinY = this.getY();
		mContentMaxX = (this.getX() + this.getWidth());
		mContentMaxY = (this.getY() + this.getHeight());


	}

	private void ResetCameraView()
	{
		mCameraX = this.getX();
		mCameraY = this.getY();
		mCameraX2 = (this.getX() + this.getWidth());
		mCameraY2 = (this.getY() + this.getHeight());
	}

	/**
	 * Clears internal list off all the pointers to contents added via the Add(Shape mShape) method.
	 */
	public void Clear()
	{
		mShapesManaged.clear();
		ResetContentArea();
		ResetCameraView();

		//As this affects the scroll area and the camera,
		//we need to reconfigure the scroll bars
		UpdateScrollBars();
	}

	/**
	 * Adds a shape to the ShapeScrollContainer.
	 * 
	 * @param mShape the shape to add
	 */
	public void Add(Shape mShape)
	{
		mShapesManaged.add(mShape);
		if (mShape.getX() < mContentMinX) mContentMinX = mShape.getX();
		if ((mShape.getX() + mShape.getWidth()) > mContentMaxX) mContentMaxX = mShape.getX() + mShape.getWidth();
		if (mShape.getY() < mContentMinY) mContentMinY = mShape.getY();
		if ((mShape.getY() + mShape.getHeight()) > mContentMaxY) mContentMaxY = mShape.getY() + mShape.getHeight();
		SetShapeAlpha(mShape);

		//Recalculate the area in which our contents cover
		if (mShape.getX() < mContentMinX) mContentMinX = mShape.getX();
		if ((mShape.getX() + mShape.getWidth()) > mContentMaxX) mContentMaxX = mShape.getX() + mShape.getWidth();
		if (mShape.getY() < mContentMinY) mContentMinY = mShape.getY();
		if ((mShape.getY() + mShape.getHeight()) > mContentMaxY) mContentMaxY = mShape.getY() + mShape.getHeight();

		//As this affects the scrollable area we need to reconfigure the scroll bars
		UpdateScrollBars();
	}

	public void Remove(Shape mShape)
	{
		if (mShapesManaged.contains(mShape)) 
		{
			mShapesManaged.remove(mShape);

			//Recalculate the area in which our contents now cover
			RecalculateContentArea();

			//As this affects the scrollable area we need to reconfigure the scroll bars
			UpdateScrollBars();
		}

	}

	private void RecalculateContentArea()
	{
		ResetContentArea();
		for (int i = 0; i < mShapesManaged.size(); i++)//For every shape
		{
			Shape mShape = mShapesManaged.get(i);

			//Recalculate the area in which our contents cover
			if (mShape.getX() < mContentMinX) mContentMinX = mShape.getX();
			if ((mShape.getX() + mShape.getWidth()) > mContentMaxX) mContentMaxX = mShape.getX() + mShape.getWidth();
			if (mShape.getY() < mContentMinY) mContentMinY = mShape.getY();
			if ((mShape.getY() + mShape.getHeight()) > mContentMaxY) mContentMaxY = mShape.getY() + mShape.getHeight();

		}
	}

	public void MoveScrollCamera(float pX, float pY)
	{
		float dX = pX - mCameraX;
		float dY = pY - mCameraY;
		MoveContents(mCameraX, mCameraY);
		UpdateScrollBars();

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

	private void SetShapeAlpha()
	{
		for (int i = 0; i < mShapesManaged.size(); i++)//For every shape
		{
			SetShapeAlpha(mShapesManaged.get(i));
		}
	}

	private void SetShapeAlpha(Shape mShape)
	{

		float newAlphaValue = 1.0f;
		float finalAlphaValue = 1.0f;

		//Container padded coordinates where shapes will start to have
		//alpha, (top left and bottom right - more over this border the more alpha they have)
		float mPadX = this.getX() + AlphaPaddingHorizontal;
		float mPadY = this.getY() + AlphaPaddingVertical;

		float mPadX2 = (this.getX() + this.getWidth()) - AlphaPaddingHorizontal;
		float mPadY2 = (this.getY() + this.getHeight()) - AlphaPaddingVertical;


		if ((mShape.getX() < this.getX()) || ((mShape.getX() + mShape.getWidth()) > (this.getX() + this.getWidth())) 
				|| (mShape.getY() < this.getY()) || ((mShape.getY()) > (this.getY() + this.getHeight())))
		{
			//Shape is out of bounds of our container so it is completely transparent
			if (ContentAlphaControl) finalAlphaValue = 0.5f;
			//if (ContentVisibilityControl) mShape.setVisible(false);
			//Log.w("SetShapeAlpha","Out of bounds!");
		}


		else
		{
			if (ContentVisibilityControl) mShape.setVisible(true);

			//If the shape is out of bounds of the padding then calculate
			//the percentage alpha that will result, (the further outside the
			//padding, the more alpha it will have)
			if (mShape.getX() < mPadX)
			{
				newAlphaValue = 1 - ((mPadX - mShape.getX())/(mPadX - this.getX()));
				if (newAlphaValue < finalAlphaValue) finalAlphaValue = newAlphaValue;

			}
			if ((mShape.getX() + mShape.getWidth()) > mPadX2)
			{
				newAlphaValue = 1 - ((mPadX2 - mShape.getX())/(mPadX2 - (this.getX() + this.getWidth())));
				if (newAlphaValue < finalAlphaValue) finalAlphaValue = newAlphaValue;
			}
			if (mShape.getY() < mPadY)
			{
				newAlphaValue = 1 - ((mPadY - mShape.getY())/(mPadY - this.getY()));
				if (newAlphaValue < finalAlphaValue) finalAlphaValue = newAlphaValue;

			}
			if ((mShape.getY()) > mPadY2)
			{
				newAlphaValue = 1 - ((mPadY2 - mShape.getY())/(mPadY2 - (this.getY() + this.getHeight())));
				if (newAlphaValue < finalAlphaValue) finalAlphaValue = newAlphaValue;
			}
		}





		if (ContentAlphaControl) {

			mShape.setAlpha(0.2f);
			((PageChunk)mShape).getWordText().setAlpha(0.8f);;
		}

	}
	private IShapeScrollContainerTouchListener mShapeScrollContainerTouchListener;

	/**
	 * Handles events fired during user touch interaction within the ShapeScrollContainer.
	 * 
	 * @author Steven
	 *
	 */
	public interface IShapeScrollContainerTouchListener
	{
		/**
		 * Called when a user clicks an item within the ShapeScrollContainer.
		 * 
		 * @param pShape the shape that was clicked by the user within the ShapeScrollContainer
		 */
		public void OnContentClicked(Shape pShape);

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

		if (ScrollHorizontalEnabled) XDis = pDistanceX;
		else XDis = 0.0f;
		if (ScrollVerticalEnabled) YDis = pDistanceY;
		else YDis = 0.0f;

		//If scroll lock is enabled then prevent the 'camera' view from
		//moving into an area where there are no further contents to show
		if (ScrollLock)
		{
			if ((XDis > 0.0f) && ((mCameraX - XDis) < (mContentMinX - ScrollLockHorizontal))) XDis = 0.0f;
			if ((XDis < 0.0f) && ((mCameraX2 - XDis) > (mContentMaxX + ScrollLockHorizontal))) XDis = 0.0f;
			/*if(null!=mShapesManaged && !mShapesManaged.isEmpty() && null!=mShapesManaged.get(mShapesManaged.size()-1))
			{
				Shape lastPosition = mShapesManaged.get(mShapesManaged.size()-1);
				if(XDis < 0.0f && (lastPosition.getX()+lastPosition.getWidth() < ResourcesManager.getInstance().windowDimensions.x - 100)){
					XDis = 0.0f;
				}
			}*/
			
			if ((YDis > 0.0f) && ((mCameraY - YDis) < (mContentMinY - ScrollLockVertical))) YDis = 0.0f;
			if ((YDis < 0.0f) && ((mCameraY2 - XDis) > (mContentMaxY + ScrollLockVertical))) YDis = 0.0f;


		}

		MoveContents(XDis, YDis);

	}


	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		// TODO Auto-generated method stub

	}


}


