package org.iolani.frc.commands.auto;

import org.iolani.frc.commands.CommandBase;

/**
 *
 */
public class IntakeAuto extends CommandBase {

    public IntakeAuto(double timeout) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(intake);
    	
    	if(timeout > 0) {
    		this.setTimeout(timeout);
    	}
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	intake.intakeBlock(.75);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return this.isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	intake.intakeBlock(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	this.end();
    }
}
