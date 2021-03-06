package com.minecolonies.coremod.entity.ai.minimal;

import com.minecolonies.api.entity.ai.DesiredActivity;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

/**
 * Entity action to wander randomly around.
 */
public class EntityAICitizenWander extends Goal
{
    protected final AbstractEntityCitizen citizen;
    protected final double                speed;
    private final   double                randomModifier;

    /**
     * Instantiates this task.
     *
     * @param citizen        the citizen.
     * @param speed          the speed.
     * @param randomModifier the random modifier for the movement.
     */
    public EntityAICitizenWander(final AbstractEntityCitizen citizen, final double speed, final double randomModifier)
    {
        super();
        this.citizen = citizen;
        this.speed = speed;
        this.randomModifier = randomModifier;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    /**
     * {@inheritDoc} Returns whether the Goal should begin execution. True when age less than 100, when a random (120) is chosen correctly, and when a citizen is nearby.
     */
    @Override
    public boolean shouldExecute()
    {
        return !isTooOld() && !checkForRandom() && citizen.getDesiredActivity() != DesiredActivity.SLEEP && citizen.getNavigator().noPath();
    }

    /**
     * Returns whether or not the citizen is too old to wander. True when age >= 100.
     *
     * @return True when age => 100, otherwise false.
     */
    private boolean isTooOld()
    {
        return citizen.getGrowingAge() >= 100;
    }

    private boolean checkForRandom()
    {
        return citizen.getRNG().nextInt((int) (randomModifier * 120.0D)) != 0;
    }

    /**
     * {@inheritDoc} Returns whether an in-progress Goal should continue executing.
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        return !citizen.getNavigator().noPath();
    }

    /**
     * {@inheritDoc} Execute a one shot task or start executing a continuous task.
     */
    @Override
    public void startExecuting()
    {
        citizen.getNavigator().moveToRandomPos(10, this.speed);
    }

    @Override
    public void resetTask()
    {
        citizen.getCitizenData().setVisibleStatus(null);
    }
}
