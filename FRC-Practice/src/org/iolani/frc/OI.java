package org.iolani.frc;

import org.iolani.frc.commands.DescendLift;
import org.iolani.frc.commands.OperateLift;
import org.iolani.frc.commands.RunIntake;
import org.iolani.frc.commands.RunOutake;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	private final Joystick _lStick = new Joystick(1);
	private final Joystick _rStick = new Joystick(2);
	private final JoystickButton _intakeButton = new JoystickButton(_lStick, 3); //TODO change button ids
	private final JoystickButton _outakeButton = new JoystickButton(_rStick, 3);
	private final JoystickButton _liftUpButton = new JoystickButton(_rStick, 5);
	private final JoystickButton _liftDownButton = new JoystickButton(_rStick, 4);
	
	public OI() {
		_intakeButton.whileHeld(new RunIntake());
		_outakeButton.whileHeld(new RunOutake());
		_liftUpButton.whileHeld(new OperateLift());
		_liftDownButton.whileHeld(new DescendLift());
	}

	public Joystick getLeftStick() {
		return _lStick;
	}

    public Joystick getRightStick() {
        return _rStick;
    }
}
