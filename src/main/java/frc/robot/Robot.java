// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.math.proto.Controller;
// import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
// import edu.wpi.first.wpilibj.motorcontrol.Talon;
//import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
// import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.WaitCommand;

//motor libraries
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */

 public class Robot extends TimedRobot {
  TalonFX leftFlywheel = new TalonFX(5);
  TalonFX rightFlywheel = new TalonFX(10);
  TalonFX leftArm = new TalonFX(2);
  TalonFX rightArm = new TalonFX(1);
  Spark index1 = new Spark(0);
  Spark index2 = new Spark(1);
  Spark intake1 = new Spark(2);
  Spark intake2 = new Spark(3);
  private Timer time1 = new Timer();
  //private final Joystick driver = new Joystick(0);
  public static final CTREConfigs ctreConfigs = new CTREConfigs();

  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  private final Joystick operator = new Joystick(1);
  private final Joystick driver = new Joystick(0);
  // private final JoystickButton flywheelOn = new JoystickButton(operator,XboxController.Button.kA.value);
  //private final JoystickButton armMovement = new JoystickButton(driver,XboxController.Button.kRightBumper.value);
  
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    time1.start();
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    time1.delay(1.2);

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {

    leftFlywheel.set(.65);
    rightFlywheel.set(.65);
    intake1.set(1);
    intake2.set(-1);
    if (time1.get() > .5 ) {
      index1.set(.75);
      index2.set(.75); 
    } else if(time1.get()>.6 && time1.get()<2){
      index1.stopMotor();
      index2.stopMotor();
    }else{
      index1.set(1);
      index2.set(1);
      
    }
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if (driver.getRawButton(PS4Controller.Button.kCross.value)) {
      m_robotContainer.changeSpeed(.5);
    }
    if (driver.getRawButton(PS4Controller.Button.kCircle.value)) {
      m_robotContainer.changeSpeed(1);
    }
    leftArm.setNeutralMode(NeutralModeValue.Brake);
    rightArm.setNeutralMode(NeutralModeValue.Brake);
    if (driver.getRawButton(PS4Controller.Button.kL1.value)) {
      index1.set(-.75);
      index2.set(-.75);
    } else if (driver.getRawButton(PS4Controller.Button.kR1.value)) {
      index1.set(.75);
      index2.set(.75);
    } else {
      index1.stopMotor();
      index2.stopMotor();
    }
    if (driver.getRawAxis(PS4Controller.Axis.kL2.value) > .1) {
      intake1.set(1);
      intake2.set(1);
    } else if (driver.getRawAxis(PS4Controller.Axis.kR2.value) > .1) {
      intake1.set(-1);
      intake2.set(-1);
    }
    else {
      intake1.stopMotor();
      intake2.stopMotor();
    }
    if (operator.getRawButton(XboxController.Button.kA.value)) {
      leftFlywheel.set(.7);//.65
      rightFlywheel.set(.7);//.65
    } else if (operator.getRawButton(XboxController.Button.kB.value)) {
      leftFlywheel.set(-.4);
      rightFlywheel.set(-.4);
    } else { 
      leftFlywheel.stopMotor();
      rightFlywheel.stopMotor();
    }
    
    if (operator.getRawAxis(XboxController.Axis.kLeftTrigger.value) > .1) {
      leftArm.set(operator.getRawAxis(XboxController.Axis.kLeftTrigger.value)*.6);
      rightArm.set(-operator.getRawAxis(XboxController.Axis.kLeftTrigger.value)*.6);
    } else if (operator.getRawAxis(XboxController.Axis.kRightTrigger.value) > .1) {
      leftArm.set(-operator.getRawAxis(XboxController.Axis.kRightTrigger.value)*.6);
      rightArm.set(operator.getRawAxis(XboxController.Axis.kRightTrigger.value)*.6);
    } else { 
      leftArm.stopMotor();
      rightArm.stopMotor();
    }

  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}