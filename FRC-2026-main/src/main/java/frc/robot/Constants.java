package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;
import frc.lib.util.swerveUtil.COTSNeoSwerveConstants;
import frc.lib.util.swerveUtil.COTSNeoSwerveConstants.driveGearRatios;

public class Constants {

    //0.0238 PathPlanner Units per inch

    public static final COTSNeoSwerveConstants chosenModule = COTSNeoSwerveConstants.SDSMK4i(driveGearRatios.SDSMK4i_L2);
    public static final double angleGearRatio = chosenModule.angleGearRatio;

    public static final double  kMaxSpeedMetersPerSecond = 4.8;
    public static final double kMaxAngularSpeed = 2 * Math.PI; // radians per second

    public static final double stickDeadband = 0.5;

    public static final boolean invertGyro = true; // Always ensure Gyro is CCW+ CW-
    
    public static final double kTrackWidth = Units.inchesToMeters(19);
    public static final double kWheelBase = Units.inchesToMeters(20.75);
    public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
        new Translation2d(kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, -kTrackWidth / 2)
    );
    public static final class ModuleConstants{
        public static final int kDrivingMotorPinionTeeth = 14;

        public static final double kDrivingMotorFreeSpeedRps = NeoMotorConstants.kFreeSpeedRpm / 60;
        public static final double kWheelDiameterMeters = Units.inchesToMeters(4);
        public static final double kWheelCircumferenceMeters = kWheelDiameterMeters * Math.PI;

        public static final double kDrivingMotorReduction = (45.0*16.0*50.0) / (kDrivingMotorPinionTeeth * 15.0 * 28.0);
        public static final double kDriveWheelFreeSpeedRps = (kDrivingMotorFreeSpeedRps * kWheelCircumferenceMeters) / kDrivingMotorReduction;
    }

    public static final class Mod0 {
        
        public static final int drivermotorID0 = 7;
        public static final int anglemotorID0 = 8;
        public static final int camcoderID = 15;
        public static final double offset0 = 0.418701;

    }

    public static final class Mod1 {
        
        public static final int drivermotorID1 = 5;
        public static final int anglemotorID1 = 6;
        public static final int camcoderID1 = 16;
        public static final double offset1 = 0.289551;
    }

    public static final class Mod2 {
        
        public static final int drivermotorID2 = 3;
        public static final int anglemotorID2 = 4;
        public static final int camcoderID2 = 15;
        public static final double offset2 = -0.084717;
    }
    public static final class Mod3 {

        public static final int drivermotorID3 = 1;
        public static final int anglemotorID3 = 2;
        public static final int camcoderID3 = 17;
        public static final double offset3 = 0.343750;
    }
    public static final class NeoMotorConstants {
        public static final double kFreeSpeedRpm = 5676;
    }
    public static final class ShooterConstants {
        public static final int SHOOTER_LEFT = 9;
        public static final int SHOOTER_RIGHT = 10;
        public static final double SHOOTER_SPEED = 3.0158;
    }

    public static final class ElevatorConstants {
        public static final int ELEVATOR_LEFT = 11;
        public static final int ELEVATOR_RIGHT = 12;
        public static final double ELEVATOR_SPEED = 0.2;
    }

    public static final class OIConstants {
        public static final int DRIVER_CONTROLLER_PORT = 0;
        public static final double DEADBAND = 0.05;
    }
}