package frc.robot.subsystems;

import com.studica.frc.AHRS;  // ← Import correcto para 2026
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class SwerveSubsystem extends SubsystemBase {
    private final SwerveModule[] modules;
    private final AHRS gyro;  // La clase sigue llamándose AHRS
    private final SwerveDriveOdometry odometry;

    public SwerveSubsystem() {
        modules = new SwerveModule[] {
            new SwerveModule(DriveConstants.FRONT_LEFT_DRIVE, DriveConstants.FRONT_LEFT_TURN, DriveConstants.FRONT_LEFT_ENCODER_OFFSET),
            new SwerveModule(DriveConstants.FRONT_RIGHT_DRIVE, DriveConstants.FRONT_RIGHT_TURN, DriveConstants.FRONT_RIGHT_ENCODER_OFFSET),
            new SwerveModule(DriveConstants.BACK_LEFT_DRIVE, DriveConstants.BACK_LEFT_TURN, DriveConstants.BACK_LEFT_ENCODER_OFFSET),
            new SwerveModule(DriveConstants.BACK_RIGHT_DRIVE, DriveConstants.BACK_RIGHT_TURN, DriveConstants.BACK_RIGHT_ENCODER_OFFSET)
        };

        // ✅ NUEVA FORMA CORRECTA para 2026
        // Usa NavXComType.kMXP_SPI en lugar de SPI.Port.kMXP
        try {
            gyro = new AHRS(AHRS.NavXComType.kMXP_SPI);
            SmartDashboard.putString("NavX Status", "Conectado correctamente por SPI");
        } catch (RuntimeException ex) {
            SmartDashboard.putString("NavX Error", "Error instantiating navX: " + ex.getMessage());
            throw ex;
        }
        
        odometry = new SwerveDriveOdometry(
            DriveConstants.DRIVE_KINEMATICS, 
            getHeading(), 
            getModulePositions(),
            new Pose2d()
        );
    }

    public Rotation2d getHeading() {
        // El método getRotation2d() ya existe en la nueva librería
        // Pero si prefieres, puedes seguir usando getYaw()
        return Rotation2d.fromDegrees(-gyro.getYaw()); // Negativo para convención estándar
    }

    public void zeroHeading() {
        gyro.reset(); // O zeroYaw() - ambos funcionan
    }

    // ... resto del código igual ...

    public SwerveModulePosition[] getModulePositions() {
        SwerveModulePosition[] positions = new SwerveModulePosition[modules.length];
        for (int i = 0; i < modules.length; i++) {
            positions[i] = new SwerveModulePosition(modules[i].getState().speedMetersPerSecond, modules[i].getState().angle);
        }
        return positions;
    }

    @Override
    public void periodic() {
        odometry.update(getHeading(), getModulePositions());
    }
}