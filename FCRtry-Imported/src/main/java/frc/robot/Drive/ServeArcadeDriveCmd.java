package frc.robot.Drive;

import edu.wpi.first.wpilibj2.command.Command;

public class ServeArcadeDriveCmd extends Command {

    private SwerveArcadeDrive swerveArcadeDrive;

    public ServeArcadeDriveCmd ( SwerveArcadeDrive swerveArcadeDrive) {
        this.swerveArcadeDrive = swerveArcadeDrive;
        addRequirements(swerveArcadeDrive);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        
        swerveArcadeDrive.updateSpeed();
    
    }

    @Override
    public void end(boolean interrupted) {
        
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
