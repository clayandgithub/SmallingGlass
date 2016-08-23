package com.game.glassball.layers;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import com.game.glassball.basic.PublicVars;
import com.w_game.basic.W_LayerManager;
import com.w_game.layers.W_Sprite;

public class Ball extends W_Sprite {

	private final Point mStartPoint;

	private boolean mIsUsed;

	private Body mJBoxBody;

	private float mAngle;

	public Ball(final int width, final int height, final int level,
			final Bitmap bmp) {
		super(width, height, level, bmp);
		mVisible = false;
		mStartPoint = new Point();
		setIsUsed(false);
	}

	public void setStartPoint(final int x, final int y) {
		mStartPoint.x = x;
		mStartPoint.y = y;
		setCenterPosition(x, y);
		mVisible = true;
	}

	public void releaseBall(final int x, final int y) {
		setIsUsed(true);
		mJBoxBody = createBallInJBoxWorld(mX, mY, mDstWidth / 2, false);// 创建一个圆球
		mJBoxBody.m_userData = this;
		final float k = 0.6f;
		mJBoxBody.setLinearVelocity(new Vec2(k * (mStartPoint.x - x), k
				* (mStartPoint.y - y)));
		// PublicVars.mStaticPublicVars.mMessageBars.addNewMessage(
		// "vx = " + mJBoxBody.getLinearVelocity().x + "vy = "
		// + mJBoxBody.getLinearVelocity().y, Color.GREEN);
	}

	@Override
	public void paint(final Canvas canvas, final W_LayerManager lm) {
		canvas.save();
		canvas.rotate(mAngle, mX + mDstWidth / 2, mY + mDstHeight / 2);
		super.paint(canvas, lm);
		canvas.restore();
	}

	@Override
	public void tick() {
		if (!mVisible) {
			return;
		}
		super.tick();
		if (mJBoxBody != null) {
			setCenterPosition(
					(int) (mJBoxBody.getPosition().x * J2DBoxWorld.RATE),
					(int) (mJBoxBody.getPosition().y * J2DBoxWorld.RATE));
			// setAngle((float) (mJBoxBody.getAngle() * 180 / Math.PI));
		}
		if (mY > PublicVars.mStaticMainActivity.getmVirtualScreenHeight()) {
			PublicVars.mStaticPublicVars.mJ2DBoxWorld.removeBody(mJBoxBody);
			mJBoxBody = null;
			mVisible = false;
		}
	}

	public Body getJBoxBody() {
		return mJBoxBody;
	}

	public void setJBoxBody(final Body jBoxBody) {
		this.mJBoxBody = jBoxBody;
	}

	public float getAngle() {
		return mAngle;
	}

	public void setAngle(final float angle) {
		this.mAngle = angle;
	}

	public boolean isIsUsed() {
		return mIsUsed;
	}

	public void setIsUsed(final boolean isUsed) {
		this.mIsUsed = isUsed;
	}

	/**
	 * 创建一个圆形body
	 */
	private Body createBallInJBoxWorld(final float x, final float y,
			final float radius, final boolean isStatic) {
		final float centerX = x + radius;
		final float centerY = y + radius;
		/** 设置BodyDef */
		final BodyDef bodyDef = new BodyDef();
		/**
		 * 此处一定要设置，即使density不为0， 若此处不将body.type设置为BodyType.DYNAMIC,物体亦会静止
		 * */
		bodyDef.type = isStatic ? BodyType.STATIC : BodyType.DYNAMIC;
		/** 设置body位置，要将屏幕的参数转化到物理世界中 */
		bodyDef.position.set(centerX / J2DBoxWorld.RATE, centerY
				/ J2DBoxWorld.RATE);
		bodyDef.bullet = true;
		/** 创建body */
		Body body = null;
		while (body == null) {
			body = PublicVars.mStaticPublicVars.mJ2DBoxWorld.mWorld
					.createBody(bodyDef);
		}
		PublicVars.mStaticPublicVars.mJ2DBoxWorld.addBodyIntoWorld(body);
		// 设置body形状
		final CircleShape circle = new CircleShape();
		/** 半径，要将屏幕的参数转化到物理世界中 */
		circle.m_radius = radius / J2DBoxWorld.RATE;
		/** 设置FixtureDef */
		final FixtureDef fDef = new FixtureDef();
		if (isStatic) {
			/** 密度为0时，在物理世界中不受外力影响，为静止的 */
			fDef.density = 0;
		} else {
			/** 密度不为0时，在物理世界中会受外力影响 */
			fDef.density = 10;
		}
		/** 设置摩擦力，范围为 0～1 */
		fDef.friction = 0.2f;
		/** 设置物体碰撞的回复力，值越大，物体越有弹性 */
		fDef.restitution = 0.8f;
		/** 添加形状 */
		fDef.shape = circle;
		/** 为body创建Fixture */
		body.createFixture(fDef);
		/** 添加 m_userData */
		// body.m_userData = this;
		return body;
	}
}
