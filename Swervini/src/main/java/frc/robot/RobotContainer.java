package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.SwerveJoystickCmd;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.Constants.OIConstants;

public class RobotContainer {
    private final SwerveSubsystem swerveSubsystem = new SwerveSubsystem();
    private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
    private final XboxController driverController = new XboxController(OIConstants.DRIVER_CONTROLLER_PORT);

    public RobotContainer() {
        configureBindings();
        swerveSubsystem.setDefaultCommand(new SwerveJoystickCmd(swerveSubsystem, driverController));
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

    public Command getAutonomousCommand() {
        return null; // Aquí iría tu comando autónomo
    }
}