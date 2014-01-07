package org.tomcurran.finity.tool;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.util.Animatable;

public class Animator extends Thread {

	private DrawingView fView;
	private Animatable fAnimatable;

	private boolean fIsRunning;
	private int fDelay;

	public Animator(Animatable animatable, DrawingView view, int delay) {
		super("Animator");
		fView = view;
		fAnimatable = animatable;
		fDelay = delay;
	}

	public void end() {
		fIsRunning = false;
	}

	@Override
	public void run() {
		while (fIsRunning) {
			long tm = System.currentTimeMillis();
			fView.freezeView();
			fAnimatable.animationStep();
			fView.checkDamage();
			fView.unfreezeView();
			try {
				tm += fDelay;
				Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	@Override
	public void start() {
		super.start();
		fIsRunning = true;
	}

}