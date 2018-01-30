package org.iolani.frc.commands.auto;

import org.iolani.frc.commands.CommandBase;
/**
 *
 */
public class AutoLiftUp extends CommandBase {

	private final double HEIGHT = 17.5;
	private boolean finish = false;
	
    public AutoLiftUp() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	this.requires(elevator);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	elevator.resetLift();
    	finish = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	elevator.liftToPosition(HEIGHT);
    	finish = true;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return finish;
    }

    // Called once after isFinished returns true
    protected void end() {
    	elevator.liftToPosition(0);
    	elevator.resetLift();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	this.end();
    }
}
