package frc.robot.Drive;

import java.util.function.Supplier;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;

public class SwerveJoystickCmd extends Command {
    
    private final SwerveSubsystem swerveSubsystem;
    private final Supplier<Double> xSupplier, ySupplier, zSupplier;
    private final Supplier<Boolean> isFieldOrientedSupplier;
    private final SlewRateLimiter xLimiter, yLimiter, zLimiter;

    public SwerveJoystickCmd(
            SwerveSubsystem swerveSubsystem,
            Supplier<Double> xSupplier, 
            Supplier<Double> ySupplier, 
            Supplier<Double> zSupplier,
            Supplier<Boolean> isFieldOrientedSupplier)
        {
        this.swerveSubsystem = swerveSubsystem;
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
        this.zSupplier = zSupplier;
        this.isFieldOrientedSupplier = isFieldOrientedSupplier;
        this.xLimiter = new SlewRateLimiter(DriveConstants.kTeleDriveMaxAccelerationUnitsPerSecond);
        this.yLimiter = new SlewRateLimiter(DriveConstants.kTeleDriveMaxAccelerationUnitsPerSecond);
        this.zLimiter = new SlewRateLimiter(DriveConstants.kTeleDriveMaxAngularAccelerationUnitsPerSecond);
        addRequirements(swerveSubsystem);
        
        
    }
    
    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        // 1. Get real-time joystick inputs
        double xSpeed = -ySupplier.get();
        double ySpeed = xSupplier.get();
        double turningSpeed = zSupplier.get();
        //double turningSpeed = 0;

        // 2. Apply deadband
        xSpeed = Math.abs(xSpeed) > OIConstants.kDeadband ? xSpeed : 0.0;
        ySpeed = Math.abs(ySpeed) > OIConstants.kDeadband ? ySpeed : 0.0;
        turningSpeed = Math.abs(turningSpeed) > OIConstants.kDeadband ? turningSpeed : 0.0;

        // 3. Make the driving smoother
        xSpeed = xLimiter.calculate(xSpeed) * DriveConstants.kTeleDriveMaxSpeedMetersPerSecond;
        ySpeed = yLimiter.calculate(ySpeed) * DriveConstants.kTeleDriveMaxSpeedMetersPerSecond;
        turningSpeed = zLimiter.calculate(turningSpeed) * DriveConstants.kTeleDriveMaxAngularSpeedRadiansPerSecond;
        
        // 4. Construct desired chassis speeds
        ChassisSpeeds chassisSpeeds = isFieldOrientedSupplier.get()
            ? new ChassisSpeeds(xSpeed,ySpeed, turningSpeed)
            : ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, turningSpeed, swerveSubsystem.getRotation2d());
        
        SwerveModuleState[] moduleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(chassisSpeeds);
        swerveSubsystem.setModuleStates(moduleStates);

        
        // 5. Output each module states to wheels
    
    }

    @Override
    public void end(boolean interrupted) {
        swerveSubsystem.stopModules();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
