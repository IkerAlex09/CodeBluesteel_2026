package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.Swerve;
import edu.wpi.first.math.filter.SlewRateLimiter;

public class SwerveJoystickCmd extends Command {
    private final XboxController controller;
    private final SlewRateLimiter xLimiter, yLimiter, rotLimiter;

    public SwerveJoystickCmd(Swerve swerveSubsystem, XboxController controller) {

        this.controller = controller;
        this.xLimiter = new SlewRateLimiter(3);
        this.yLimiter = new SlewRateLimiter(3);
        this.rotLimiter = new SlewRateLimiter(3);
        addRequirements(swerveSubsystem);
    }

    
    @Override
    public void end(boolean interrupted) {
        
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}