package net.arabikitouhu.virtualcraft;

import net.virtualcraft.client.IModEntryPoint;

public class TestMod implements IModEntryPoint {

	public static int BASE_ItemID;

	@Override
	public String getModName() { return "TestMod"; }

	@Override
	public String getModVersion() { return "1.0"; }

	@Override
	public void setItemIDStartPosition(int start) { BASE_ItemID = start; }

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
