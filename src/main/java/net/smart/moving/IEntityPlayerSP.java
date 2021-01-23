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

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IEntityPlayerSP {
	SMBase getMoving();

	boolean getSleepingField();

	boolean getIsJumpingField();

	boolean getIsInWebField();

	void setIsInWebField(boolean b);

	Minecraft getMcField();

	void setMoveForwardField(float f);

	void setMoveStrafingField(float f);

	void setIsJumpingField(boolean flag);

	void localMoveEntity(MoverType mover, double d, double d1, double d2);

	EntityPlayer.SleepResult localSleepInBedAt(int i, int j, int k);

	float localGetBrightness();

	int localGetBrightnessForRender();

	void localUpdateEntityActionState();

	boolean localIsInsideOfMaterial(Material material);

	void localWriteEntityToNBT(NBTTagCompound nBTTagCompound);

	boolean localIsSneaking();

	float localGetFOVMultiplier();
}
