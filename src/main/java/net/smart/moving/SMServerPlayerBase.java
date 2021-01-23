// ==================================================================
// This file is part of Smart Moving.
//
// Smart Moving is free software: you can redistribute it and/or
// modify it under the terms of the GNU General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// Smart Moving is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Smart Moving. If not, see <http://www.gnu.org/licenses/>.
// ==================================================================

package net.smart.moving;

import java.util.List;

import api.player.server.IServerPlayerAPI;
import api.player.server.ServerPlayerAPI;
import api.player.server.ServerPlayerBase;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.smart.utilities.Reflect;
import net.smart.utilities.SoundUtil;

public class SMServerPlayerBase extends ServerPlayerBase implements IEntityPlayerMP {
	public final SMServer moving;

	public static void registerPlayerBase() {
		ServerPlayerAPI.register(SMInfo.ModName, SMServerPlayerBase.class);
	}

	public static SMServerPlayerBase getPlayerBase(Object player) {
		return (SMServerPlayerBase) ((IServerPlayerAPI) player).getServerPlayerBase(SMInfo.ModName);
	}

	public SMServerPlayerBase(ServerPlayerAPI playerApi) {
		super(playerApi);
		moving = new SMServer(this, false);
	}

	@Override
	public float getHeight() {
		return player.height;
	}

	@Override
	public double getMinY() {
		return player.getEntityBoundingBox().minY;
	}

	@Override
	public void setMaxY(double maxY) {
		AxisAlignedBB box = player.getEntityBoundingBox();
		player.setEntityBoundingBox(new AxisAlignedBB(box.minX, box.minY, box.minZ, box.maxX, maxY, box.maxZ));
	}

	@Override
	public void afterSetPosition(double d, double d1, double d2) {
		moving.afterSetPosition(d, d1, d2);
	}

	@Override
	public void beforeIsPlayerSleeping() {
		moving.beforeIsPlayerSleeping();
	}

	@Override
	public void beforeOnUpdate() {
		moving.beforeOnUpdate();
	}

	@Override
	public void afterOnUpdate() {
		moving.afterOnUpdate();
	}

	@Override
	public void afterOnLivingUpdate() {
		moving.afterOnLivingUpdate();
	}

	@Override
	public float doGetHealth() {
		return player.getHealth();
	}

	@Override
	public AxisAlignedBB getBox() {
		return player.getEntityBoundingBox();
	}

	@Override
	public AxisAlignedBB expandBox(AxisAlignedBB box, double x, double y, double z) {
		return box.expand(x, y, z);
	}

	@Override
	public List<?> getEntitiesExcludingPlayer(AxisAlignedBB box) {
		return player.world.getEntitiesWithinAABBExcludingEntity(player, box);
	}

	@Override
	public boolean isDeadEntity(Entity entity) {
		return entity.isDead;
	}

	@Override
	public void onCollideWithPlayer(Entity entity) {
		entity.onCollideWithPlayer(player);
	}

	@Override
	public float getEyeHeight() {
		return player.height - 0.18F;
	}

	@Override
	public boolean isEntityInsideOpaqueBlock() {
		return moving.isEntityInsideOpaqueBlock();
	}

	@Override
	public boolean localIsEntityInsideOpaqueBlock() {
		return super.isEntityInsideOpaqueBlock();
	}

	@Override
	public void addExhaustion(float exhaustion) {
		moving.addExhaustion(exhaustion);
	}

	@Override
	public void localAddExhaustion(float exhaustion) {
		player.getFoodStats().addExhaustion(exhaustion);
	}

	@Override
	public void addMovementStat(double x, double y, double z) {
		moving.addMovementStat(x, y, z);
	}

	@Override
	public void localAddMovementStat(double x, double y, double z) {
		super.addMovementStat(x, y, z);
	}

	@Override
	public void localPlaySound(String soundId, float volume, float pitch) {
		SoundEvent soundEvent = SoundUtil.getSoundEvent(soundId);
		if (soundEvent != null)
			player.playSound(soundEvent, volume, pitch);
	}

	@Override
	public boolean isSneaking() {
		return moving.isSneaking();
	}

	@Override
	public boolean localIsSneaking() {
		return playerAPI.localIsSneaking();
	}

	@Override
	public void setHeight(float height) {
		player.height = height;
	}

	@Override
	public void sendPacket(IMessage message) {
		SMPacketHandler.INSTANCE.sendTo(message, player);
	}

	@Override
	public String getUsername() {
		return player.getGameProfile().getName();
	}

	@Override
	public void resetFallDistance() {
		player.fallDistance = 0;
		player.motionY = 0.08;
	}

	@Override
	public void resetTicksForFloatKick() {
		Reflect.SetField(net.minecraft.network.NetHandlerPlayServer.class, playerAPI.getPlayerNetServerHandlerField(),
				SMInstall.NetServerHandler_ticksForFloatKick, 0);
	}

	@Override
	public void sendPacketToTrackedPlayers(FMLProxyPacket packet) {
		player.mcServer.getWorld(player.dimension).getEntityTracker().sendToTracking(player, packet);
	}

	@Override
	public SMServer getMoving() {
		return moving;
	}

	@Override
	public int getItemInUseCount() {
		return player.getItemInUseCount();
	}

	@Override
	public EntityPlayerMP getEntityPlayerMP() {
		return player;
	}
}
