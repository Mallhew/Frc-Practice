package org.iolani.frc.subsystems;

import org.iolani.frc.RobotMap;
import org.iolani.frc.commands.OperateArcadeDrive;
import org.iolani.frc.commands.OperateTankDrive;
//import org.usfirst.frc.team2438.robot.commands.OperateTankDrive;
import org.iolani.util.Utility;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.ControlMode;
/**
 * @author koluke
 */
public class DriveTrain extends Subsystem {
	// hardware //
    private TalonSRX      _left;
    private TalonSRX      _leftSlave1;
    private TalonSRX      _leftSlave2;
    private TalonSRX      _right;
    private TalonSRX      _rightSlave1;
    private TalonSRX      _rightSlave2;
    
    private final double POWER_LIMIT = 0.85;
    
	// physical constants //
	private static final double WHEEL_DIAMETER_INCHES  = 8.0;
	private static final double WHEEL_INCHES_PER_REV   = Math.PI * WHEEL_DIAMETER_INCHES;
	private static final int    ENCODER_PINION_TEETH   = 12;
	private static final int    WHEEL_GEAR_TEETH       = 132;
	private static final double ENCODER_INCHES_PER_REV = WHEEL_INCHES_PER_REV * ENCODER_PINION_TEETH / WHEEL_GEAR_TEETH;
	
    public void init()  {
        // configure left //
    	_left = new TalonSRX(RobotMap.driveLeftMain);
    	_left.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
    	_left.setInverted(true);
    	_left.setSelectedSensorPosition(0, 0, 0);
        _leftSlave1  = new TalonSRX(RobotMap.driveLeftSlave1);
        _leftSlave1.setInverted(true);
        _leftSlave1.follow(_left);
        _leftSlave2  = new TalonSRX(RobotMap.driveLeftSlave2);
        _leftSlave2.setInverted(true);
        _leftSlave2.follow(_left);
        
        // configure right //
        _right = new TalonSRX(RobotMap.driveRightMain);
        _right.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        _right.setSelectedSensorPosition(0, 0, 0);
        _rightSlave1 = new TalonSRX(RobotMap.driveRightSlave1);
        _rightSlave1.follow(_right);
        _rightSlave2 = new TalonSRX(RobotMap.driveRightSlave2);
        _rightSlave2.follow(_right);
        
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //this.setDefaultCommand(new OperateTankDrive());
        this.setDefaultCommand(new OperateTankDrive());
    }

    /**
     * Set Tank drive powers (independent left / right side).
     * 
     * @param left  - positive is forward
     * @param right - positive is forward
     */
    public void setTank(double left, double right) {
    	this.setTank(left, right, false);
    }
 
    /**
     * Set Tank drive powers (independent left / right side) with optional scaling.
     * 
     * @param left  - positive is forward
     * @param right - positive is forward
     * @param squaredInputs - square the input (-1.0 < i < 1.0) for parabolic sensitivity
     */
    public void setTank(double left, double right, boolean squaredInputs) {
    	
    	if (squaredInputs) {
            // square the inputs (while preserving the sign) to increase fine control while permitting full power
            if (left >= 0.0) {
                left = (left * left);
            } else {
                left = -(left * left);
            }
            if (right >= 0.0) {
                right = (right * right);
            } else {
                right = -(right * right);
            }
        }
    	
    	SmartDashboard.putNumber("drive-right-tank", right);
    	SmartDashboard.putNumber("drive-left-tank", left);
    	
    	_left.set(ControlMode.PercentOutput, left * POWER_LIMIT);
    	_right.set(ControlMode.PercentOutput, right * POWER_LIMIT);
    }
    
    /**
     * Arcade drive implements throttle and rudder style steering, either with one stick or two.
     * 
     * @param moveValue   - positive is forward
     * @param rotateValue - positive is counterclockwise
     */
    public void setArcade(double moveValue, double rotateValue) {
        this.setArcade(moveValue, rotateValue, false);
    }
    
    /**
     * Arcade drive implements throttle and rudder style steering, either with one stick or two
     * with optional input scaling.
     * 
     * @param moveValue     - positive is forward
     * @param rotateValue   - positive is counterclockwise
     * @param squaredInputs - square the input (-1.0 < i < 1.0) for parabolic sensitivity
     */
    public void setArcade(double moveValue, double rotateValue, boolean squaredInputs) {
        double leftMotorSpeed;
        double rightMotorSpeed;

        moveValue   = Utility.limit(moveValue);
        rotateValue = Utility.limit(rotateValue);

        if (squaredInputs) {
            // square the inputs (while preserving the sign) to increase fine control while permitting full power
            if (moveValue >= 0.0) {
                moveValue = (moveValue * moveValue);
            } else {
                moveValue = -(moveValue * moveValue);
            }
            if (rotateValue >= 0.0) {
                rotateValue = (rotateValue * rotateValue);
            } else {
                rotateValue = -(rotateValue * rotateValue);
            }
        }

        if (moveValue > 0.0) {
            if (rotateValue > 0.0) {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } else {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } else {
            if (rotateValue > 0.0) {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } else {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }

        this.setTank(leftMotorSpeed, rightMotorSpeed);
    }
    
    public double getLeftEncoderDistance() {
    	return _left.getSelectedSensorPosition(0) * ENCODER_INCHES_PER_REV;
    }
    
    public void setLeftEncoderDistance(int value) {
    	_left.setSelectedSensorPosition((int) (value / ENCODER_INCHES_PER_REV),0 ,0);
    }
    
    public double getRightEncoderDistance() {
    	return _right.getSelectedSensorPosition(0) * ENCODER_INCHES_PER_REV;
    }
    
    public void setRightEncoderDistance(int value) {
    	_right.setSelectedSensorPosition((int) (value / ENCODER_INCHES_PER_REV), 0, 0);
    }
    
    public void debug() {
    	SmartDashboard.putNumber("drive-left-power", _left.getMotorOutputPercent());
    	SmartDashboard.putNumber("drive-right-power", _right.getMotorOutputPercent());
    	
    	SmartDashboard.putNumber("drive-left-distance", this.getLeftEncoderDistance());
    	SmartDashboard.putNumber("drive-right-distance", this.getRightEncoderDistance());
    }    
}