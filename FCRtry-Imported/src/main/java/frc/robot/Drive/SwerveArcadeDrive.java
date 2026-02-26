package frc.robot.Drive;

import java.util.function.Supplier;

// CTRE library not available in the classpath; use a local CANcoder stub defined below.

import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ModuleConstants;
import frc.robot.Constants.OIConstants;


public class SwerveArcadeDrive extends SubsystemBase {

    // Local CANcoder stub to avoid requiring the CTRE library at compile time.
    // This provides just the minimal API used in this file; replace with the real CTRE CANcoder
    // class by restoring the import and removing this stub when the CTRE library is added to the project.
// Minimal local stubs for the small subset of REV classes used in this file.
// These allow compiling without the com.revrobotics library; replace with the real library when available.
enum PersistMode { kPersistParameters }
enum ResetMode { kNoResetSafeParameters }
enum MotorType { kBrushless }

class SparkMaxConfig {
    private boolean inverted = false;
    public void inverted(boolean inv) { this.inverted = inv; }
}

class SparkMax {
    public SparkMax(int id, MotorType type) { }
    public void set(double value) { }
    public void configure(SparkMaxConfig config, ResetMode reset, PersistMode persist) { }
}

class CANcoder {
        private double absolutePos = 0.0;
        public CANcoder(int port, Object canivore) { }
        public AbsolutePosition getAbsolutePosition() { return new AbsolutePosition(absolutePos); }
        public static class AbsolutePosition {
            private final double value;
            public AbsolutePosition(double v) { this.value = v; }
            public double getValueAsDouble() { return value; }
        }
    }

    private SparkMax frontLeft, frontRight, backLeft, backRight;

    private SparkMaxConfig frontLeftConfig, frontRightConfig, backLeftConfig, backRightConfig;
    
    private SparkMax frontLeftSteering, frontRightSteering, backRightSteering, backLeftSteering;

    private Supplier<Double> speed, turn ;

    private CANcoder frontLeftEncoder, frontRightEncoder, backLeftEncoder, backRightEncoder;

    public SwerveArcadeDrive(
        int FrontLeftdriveMotorID,
        int FrontRightdriveMotorID,
        int BackLeftdriveMotorID,
        int BackRightdriveMotorID,
        Supplier<Double> speed,
        Supplier<Double> turn
    ) {

        this.backLeft = new SparkMax(BackLeftdriveMotorID, MotorType.kBrushless);
        this.backRight = new SparkMax(BackRightdriveMotorID, MotorType.kBrushless);
        this.frontLeft = new SparkMax(FrontLeftdriveMotorID, MotorType.kBrushless);
        this.frontRight = new SparkMax(FrontRightdriveMotorID, MotorType.kBrushless);
        
        this.backLeftSteering = new SparkMax(DriveConstants.kBackLeftSteeringMotorPort, MotorType.kBrushless);
        this.backRightSteering = new SparkMax(DriveConstants.kBackRightSteeringMotorPort, MotorType.kBrushless);
        this.frontLeftSteering = new SparkMax(DriveConstants.kFrontLeftSteeringMotorPort, MotorType.kBrushless);
        this.frontRightSteering = new SparkMax(DriveConstants.kFrontRightSteeringMotorPort, MotorType.kBrushless);

        this.speed = speed;
        this.turn = turn;

        this.backLeftConfig = new SparkMaxConfig();
        this.backRightConfig = new SparkMaxConfig();
        this.frontLeftConfig = new SparkMaxConfig();
        this.frontRightConfig = new SparkMaxConfig();

        this.backLeftConfig.inverted(DriveConstants.kBackLeftDriveEncoderReversed);
        this.backRightConfig.inverted(DriveConstants.kBackRightDriveEncoderReversed);
        this.frontLeftConfig.inverted(DriveConstants.kFrontLeftDriveEncoderReversed);
        this.frontRightConfig.inverted(DriveConstants.kFrontRightDriveEncoderReversed);

        this.backLeft.configure(backLeftConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
        this.backRight.configure(backRightConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
        this.frontLeft.configure(frontLeftConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
        this.frontRight.configure(frontRightConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);

        this.backLeftEncoder = new CANcoder(DriveConstants.kBackLeftDriveAbsoluteEncoderPort, ModuleConstants.CANivore);
        this.backRightEncoder = new CANcoder(DriveConstants.kBackRightDriveAbsoluteEncoderPort, ModuleConstants.CANivore);
        this.frontLeftEncoder = new CANcoder(DriveConstants.kFrontLeftDriveAbsoluteEncoderPort, ModuleConstants.CANivore);
        this.frontRightEncoder = new CANcoder(DriveConstants.kFrontRightDriveAbsoluteEncoderPort, ModuleConstants.CANivore);

    }

    public void updateSpeed(){

        double limitSpeed = Math.abs(speed.get()) > OIConstants.kDeadband ? speed.get() * 1 : 0;
        double limitTurn = Math.abs(turn.get()) > OIConstants.kDeadband ? -turn.get() * 0.6 : 0;

        double left = limitSpeed + limitTurn;

        double right = limitSpeed - limitTurn ;

        frontLeft.set(left);

        backLeft.set(left);

        frontRight.set(right);

        backRight.set(right);

        frontLeftSteering.set(getError(DriveConstants.kFrontLeftDriveAbsoluteEncoderOffsetRad, 0, frontRightEncoder, 0.1));
        frontRightSteering.set(getError(DriveConstants.kFrontRightDriveAbsoluteEncoderOffsetRad, 0, frontLeftEncoder, 0.1));
        backLeftSteering.set(getError(DriveConstants.kBackLeftDriveAbsoluteEncoderOffsetRad, 0, backRightEncoder, 0.1));
        backRightSteering.set(getError(DriveConstants.kBackRightDriveAbsoluteEncoderOffsetRad, 0, backLeftEncoder, 0.1));

    }

    public double getAbsPositionDegrees(double offset, CANcoder encoder){

        double encoderdeg = encoder.getAbsolutePosition().getValueAsDouble() * 360;

        double offsetDeg = offset * 360;

        encoderdeg -= offsetDeg;

        return encoderdeg;

    }

    public double getError( double offset, double setpoint, CANcoder encoder, double proportional){

        double absPosition = getAbsPositionDegrees(offset, encoder);

        double error = setpoint - absPosition;

        error *= (1/360);

        error *= proportional;

        return error;

    }

}

