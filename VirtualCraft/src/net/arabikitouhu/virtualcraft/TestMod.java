package net.arabikitouhu.virtualcraft;

import net.virtualcraft.client.IModEntryPoint;

public class TestMod implements IModEntryPoint {

	public static int BASE_ID;

	@Override
	public String getModName() { return "TestMod"; }

	@Override
	public String getModVersion() { return "1.0"; }

	@Override
	public void setIDStartPosition(int start) { BASE_ID = start; }

	@Override
	public void preInitialize() {
		System.out.println("TestMod.preInitialize()");
	}

	@Override
	public void Initialize() {
		System.out.println("TestMod.Initialize()");
	}

	@Override
	public void postInitialize() {
		System.out.println("TestMod.postInitialize()");
	}

}
