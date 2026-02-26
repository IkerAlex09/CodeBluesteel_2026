// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.List;

//import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.VisionConstants;
import frc.robot.Drive.ServeArcadeDriveCmd;
import frc.robot.Drive.SwerveArcadeDrive;
import frc.robot.Drive.SwerveJoystickCmd;
import frc.robot.Drive.SwerveSubsystem;
import frc.robot.Elevator.ElevatorCmd;
import frc.robot.Elevator.ElevatorSubsystem;

public class RobotContainer {

  //private final SwerveSubsystem swerveSubsystem = new SwerveSubsystem();
  
  private final ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(9, 10);


  @SuppressWarnings("unused")
  //private final visionSubsystem visionSubsystem = new visionSubsystem( ()-> swerveSubsystem.getYawRateDegPerSec() , cameraSpecsList, swerveSubsystem );

  private final CommandXboxController driverController = new CommandXboxController(0); //chasis
  private final CommandXboxController addOnsController = new CommandXboxController(1);//parte superior

  private final SwerveArcadeDrive swerveArcadeDrive = new SwerveArcadeDrive(
    DriveConstants.kFrontLeftDriveMotorPort,
    DriveConstants.kFrontRightDriveMotorPort,
    DriveConstants.kBackLeftDriveMotorPort,
    DriveConstants.kBackRightDriveMotorPort,
      ()-> driverController.getLeftY(), 
      ()-> driverController.getRightX());

  //private final SendableChooser<Command> autoChooser;
  
    /**
     * 
     */
    public RobotContainer() {
  
      // Build an auto chooser. This will use Commands.none() as the default option.
      //autoChooser = AutoBuilder.buildAutoChooser();

     //Another option that allows you to specify the default auto by its name
      //autoChooser = AutoBuilder.buildAutoChooser("My Default Auto");

     //SmartDashboard.putData("Auto Chooser", autoChooser);

      swerveArcadeDrive.setDefaultCommand(
    
      new ServeArcadeDriveCmd(swerveArcadeDrive)

      );

      /*swerveSubsystem.setDefaultCommand(
        new SwerveJoystickCmd(
          swerveSubsystem
          , () -> driverController.getLeftX()
          , () -> driverController.getLeftY()
          , () -> driverController.getRightX()
          , () -> driverController.leftBumper().getAsBoolean()

          )
      );
      */
       
      elevatorSubsystem.setDefaultCommand(
        new ElevatorCmd(
           () -> addOnsController.getLeftX()
          , () -> addOnsController.getRightX()
          , elevatorSubsystem
          )
      );
    

    configureBindings();
  }

  private void configureBindings() {

    //driverController.b().onTrue(new InstantCommand( ()-> swerveSubsystem.ZeroHeading() ));

  }

  public void mResetEncoders(){

  }

  public Command getAutonomousCommand() {
    //return autoChooser.getSelected();
    return Commands.print("No autonomous command configured");
  }
}