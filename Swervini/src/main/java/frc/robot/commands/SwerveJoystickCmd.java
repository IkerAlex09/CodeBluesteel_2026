package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.Constants.OIConstants;
import frc.robot.Constants.DriveConstants;
import edu.wpi.first.math.filter.SlewRateLimiter;

public class SwerveJoystickCmd extends Command {
    private final SwerveSubsystem swerveSubsystem;
    private final XboxController controller;
    private final SlewRateLimiter xLimiter, yLimiter, rotLimiter;

    public SwerveJoystickCmd(SwerveSubsystem swerveSubsystem, XboxController controller) {
        this.swerveSubsystem = swerveSubsystem;
        this.controller = controller;
        this.xLimiter = new SlewRateLimiter(3);
        this.yLimiter = new SlewRateLimiter(3);
        this.rotLimiter = new SlewRateLimiter(3);
        addRequirements(swerveSubsystem);
    }

    @Override
    public void execute() {
        double xSpeed = -controller.getLeftY();
        double ySpeed = -controller.getLeftX();
        double rotSpeed = -controller.getRightX();

        xSpeed = Math.abs(xSpeed) > OIConstants.DEADBAND ? xSpeed : 0.0;
        ySpeed = Math.abs(ySpeed) > OIConstants.DEADBAND ? ySpeed : 0.0;
        rotSpeed = Math.abs(rotSpeed) > OIConstants.DEADBAND ? rotSpeed : 0.0;

        xSpeed = xLimiter.calculate(xSpeed) * DriveConstants.MAX_SPEED_METERS_PER_SECOND;
        ySpeed = yLimiter.calculate(ySpeed) * DriveConstants.MAX_SPEED_METERS_PER_SECOND;
        rotSpeed = rotLimiter.calculate(rotSpeed) * DriveConstants.MAX_ANGULAR_SPEED_RADIANS_PER_SECOND;

        // ✅ AQUÍ estaba el “hueco”
        swerveSubsystem.drive(xSpeed, ySpeed, rotSpeed, true);
        
    }

    @Override
    public void end(boolean interrupted) {
        
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}