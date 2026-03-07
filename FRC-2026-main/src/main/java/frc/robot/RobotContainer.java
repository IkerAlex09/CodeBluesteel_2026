package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;

import java.util.Optional;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.commands.TeleopSwerve;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.ShooterSubsystem;

public class RobotContainer {

private final XboxController driverController = new XboxController(0);

//These three variables represent the id's of the axis' on the Xbox Controller's sticks.
private final int translationAxis = XboxController.Axis.kLeftY.value;
private final int strafeAxis = XboxController.Axis.kLeftX.value;
private final int rotationAxis = XboxController.Axis.kRightX.value;

private final JoystickButton setHeading = new JoystickButton(driverController, XboxController.Button.kY.value);
private final Trigger zeroGyro = new POVButton(driverController, 180); // Marked for removal

private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();

private final Swerve s_swerve = new Swerve();

public RobotContainer() {

    configureBindings();

    //Sets our s_swerve to always be following the TeleopSwerve command when not being used by any other systems
    s_swerve.setDefaultCommand(
        new TeleopSwerve(
            s_swerve,
            driverController, 
            () -> driverController.getRawAxis(translationAxis), 
            () -> driverController.getRawAxis(strafeAxis), 
            () -> driverController.getRawAxis(rotationAxis), 
            () -> false)
    );
    NamedCommands.registerCommand("zeroGyro", new InstantCommand(() -> s_swerve.zeroGyro()));
    NamedCommands.registerCommand("setHeading", Commands.runOnce(() -> {
        double angle = s_swerve.isBlueAlliance() ? Math.PI : 0;
        s_swerve.adjustGyro(angle);
        Pose2d current_pose = s_swerve.getPose();
        Pose2d pose = new Pose2d(current_pose.getX(), current_pose.getY(), new Rotation2d(angle));
        s_swerve.resetOdometry(pose);
    }, s_swerve));
    configureButtonBindings();
}

private void configureBindings() {
        new JoystickButton(driverController, XboxController.Button.kRightBumper.value)
            .whileTrue(new Command() {
                @Override
                public void execute() {
                    shooterSubsystem.shoot();
                }
                @Override
                public void end(boolean interrupted) {
                    shooterSubsystem.stop();
                }
            });

        new JoystickButton(driverController, XboxController.Button.kLeftBumper.value)
        .whileTrue(new Command() {
            @Override
            public void execute() {
                shooterSubsystem.vacum();
            }
            @Override
            public void end(boolean interrupted) {
                shooterSubsystem.stop();
            }
        }
        );
    }

private void configureButtonBindings() {
    //Can be removed
    zeroGyro.onTrue(new InstantCommand(() -> s_swerve.zeroHeading(), s_swerve));

    //Sets the heading
    setHeading.onTrue(Commands.runOnce(() -> {
        double angle = s_swerve.isBlueAlliance() ? Math.PI : 0;
        s_swerve.adjustGyro(angle);
        Pose2d current_pose = s_swerve.getPose();
        Pose2d pose = new Pose2d(current_pose.getX(), current_pose.getY(), new Rotation2d(angle));
        s_swerve.resetOdometry(pose);
    }, s_swerve));
     
    //Switches to RobotCentric/AimAssist

    //Aligns to point on the field using parameters (swerve, vision, x inches from tag, y inches from tag)

}}
