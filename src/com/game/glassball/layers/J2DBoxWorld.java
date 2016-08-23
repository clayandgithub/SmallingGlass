package com.game.glassball.layers;

import java.util.Vector;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import android.graphics.Canvas;

import com.game.glassball.basic.PublicVars;
import com.game.glassball.basic.R;
import com.w_game.basic.W_Layer;
import com.w_game.basic.W_LayerManager;

public class J2DBoxWorld extends W_Layer implements ContactListener {

	public final static float GRAVITY = 30f;

	public final static int RATE = 20;// 屏幕到现实世界的比例 10px：1m;

	public World mWorld;// 创建一个物理世界

	private final float mTimeStep = 20f / 1000;// 时间步长

	private final int mVelocityIterations = 10;// 迭代越大，模拟越精确，但性能越低

	private final int mPositionIterations = 8;

	private final Vector<Body> mAllBodies;

	private final Vector<Body> mNeedToDestroyBodies;

	public J2DBoxWorld(final int width, final int height, final int level) {
		super(width, height, level);
		mAllBodies = new Vector<Body>(20);
		mNeedToDestroyBodies = new Vector<Body>(20);
		clearWorld();
	}

	public void clearWorld() {
		mWorld = new World(new Vec2(0f, GRAVITY), true);// 创建世界
		mWorld.setContactListener(this);// 碰撞监听
		// int count = 0;
		// for (Body body : mAllBodies) {
		// mWorld.destroyBody(body);
		// ++count;
		// }
		// Log.i("count", "" + count);
		mAllBodies.clear();
		mNeedToDestroyBodies.clear();
		createBorders();
	}

	public void createBorders() {
		createPolygon(160, -16, 320, 32, 0.8f, true);// 创建上边界
		// createPolygon(160, 576, 320, 32, 0.8f, true);// 创建下边界
		createPolygon(-16, 280, 32, 560, 0.8f, true);// 创建左边界
		createPolygon(336, 280, 32, 560, 0.8f, true);// 创建右边界
	}

	public void addBodyIntoWorld(final Body body) {
		mAllBodies.add(body);
	}

	public void removeBody(final Body body) {
		// mAllBodies.remove(body);
		mNeedToDestroyBodies.add(body);
	}

	@Override
	public void paint(final Canvas canvas, final W_LayerManager lm) {
	}

	@Override
	public void tick() {
		for (final Body body : mNeedToDestroyBodies) {
			body.m_userData = null;
			mWorld.destroyBody(body);
		}
		mNeedToDestroyBodies.clear();
		mWorld.step(mTimeStep, mVelocityIterations, mPositionIterations);// 物理世界进行模拟
	}

	public Body createPolygon(final float centerX, final float centerY,
			final float width, final float height, final float restitution,
			final boolean isStatic) {
		final BodyDef bodyDef = new BodyDef();

		bodyDef.type = isStatic ? BodyType.STATIC : BodyType.DYNAMIC;// new

		bodyDef.position.set(centerX / RATE, centerY / RATE);

		Body body = null;
		while (body == null) {
			body = mWorld.createBody(bodyDef);
		}
		addBodyIntoWorld(body);
		final PolygonShape polygon = new PolygonShape();

		polygon.setAsBox((width / 2) / RATE, (height / 2) / RATE);

		final FixtureDef fDef = new FixtureDef();
		if (isStatic) {
			fDef.density = 0;
		} else {
			fDef.density = 1;
		}
		fDef.friction = 0.2f;
		fDef.restitution = restitution;
		fDef.shape = polygon;
		body.createFixture(fDef);

		return body;
	}

	@Override
	public void beginContact(final Contact arg0) {

	}

	@Override
	public void endContact(final Contact arg0) {

	}

	@Override
	public void postSolve(final Contact arg0, final ContactImpulse arg1) {
		// /**碰撞事件的检测，参数是调试出来的 */
		final Body body = arg0.m_fixtureA.getBody();
		if (body.m_userData instanceof Glass) {
			if (PublicVars.mStaticPublicVars.mGameBoard
					.destoryGlass((Glass) body.m_userData)) {
				removeBody(body);
				PublicVars.mStaticSound.playSound(R.raw.glass, 1.0f, 1.0f);
				// PublicVars.mStaticPublicVars.mMessageBars.addNewMessage("peng!",
				// Color.GREEN);
			}
		}
	}

	@Override
	public void preSolve(final Contact arg0, final Manifold arg1) {
		PublicVars.mStaticSound.playSound(R.raw.ding, 0.5f, 0f);
	}
}
