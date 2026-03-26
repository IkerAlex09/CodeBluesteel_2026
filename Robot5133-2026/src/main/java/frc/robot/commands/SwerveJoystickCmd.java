package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;

public class SwerveJoystickCmd extends Command {
    @SuppressWarnings("unused")
    private final SwerveSubsystem swerveSubsystem;
    
    public SwerveJoystickCmd(SwerveSubsystem swerveSubsystem) {
        this.swerveSubsystem = swerveSubsystem;
        // Este comando ya no se usa - el control está en RobotContainer
        addRequirements(swerveSubsystem);
    }
    
    @Override
    public void execute() {
        // Vacío - el control ahora es directo en RobotContainer
    }
    
    @Override
    public void end(boolean interrupted) {}
    
    @Override
    public boolean isFinished() {
        return false;
    }
}