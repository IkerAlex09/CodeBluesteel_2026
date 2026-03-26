package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

public final class Constants {
    public static final class ModuleConstants {
        public static final double kWheelDiameterMeters = Units.inchesToMeters(4);
        public static final double kDriveMotorGearRatio = 6.75;
        public static final double kTurningMotorGearRatio = 150.0 / 7.0;
        public static final double kDriveEncoderRot2Meter = (Math.PI * kWheelDiameterMeters) / kDriveMotorGearRatio;
        public static final double kTurningEncoderRot2Rad = (2 * Math.PI) / kTurningMotorGearRatio;
        public static final double kDriveEncoderRPM2MeterPerSec = kDriveEncoderRot2Meter / 60.0;
        public static final double kTurningEncoderRPM2RadPerSec = kTurningEncoderRot2Rad / 60.0;
        public static final double kPTurning = 0.5;
    }

    public static final class ShooterConstants {
            public static final double kShooterSpeedHigh = 2;
            public static final double kIntakeSpeed = 1;
            public static final double kShooterRampTime = 0.5;
        }

    public static final class DriveConstants {
        public static final double kTrackWidth = Units.inchesToMeters(27.62);
        public static final double kWheelBase = Units.inchesToMeters(27.62);

        public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
                new Translation2d(-kWheelBase / 2, kTrackWidth / 2), // Nuevo orden
                new Translation2d(-kWheelBase / 2, -kTrackWidth / 2),
                new Translation2d(kWheelBase / 2, kTrackWidth / 2),
                new Translation2d(kWheelBase / 2, -kTrackWidth / 2));
        /*
         * public static final SwerveDriveKinematics kDriveKinematics = new
         * SwerveDriveKinematics(
         * new Translation2d(kWheelBase / 2, kTrackWidth / 2), // Front Left
         * new Translation2d(kWheelBase / 2, -kTrackWidth / 2), // Front Right
         * new Translation2d(-kWheelBase / 2, kTrackWidth / 2), // Back Left
         * new Translation2d(-kWheelBase / 2, -kTrackWidth / 2) // Back Right
         * );
         */

         // Garras (SPARK 11 y 12)
        public static final int kClawLeftMotorPort = 11;
        public static final int kClawRightMotorPort = 12;

        // Motor IDs - Drive (Tracción)
        public static final int kFrontLeftDriveMotorPort = 1;
        public static final int kBackLeftDriveMotorPort = 3;
        public static final int kFrontRightDriveMotorPort = 5;
        public static final int kBackRightDriveMotorPort = 7;

        // Motor IDs - Turning (Giro)
        public static final int kFrontLeftTurningMotorPort = 2;
        public static final int kBackLeftTurningMotorPort = 4;
        public static final int kFrontRightTurningMotorPort = 6;
        public static final int kBackRightTurningMotorPort = 8;

        // Disparador (SPARK MAX 9 y 10)
        public static final int kShooterMotorLeftPort = 9;
        public static final int kShooterMotorRightPort = 10;

        // Inversión de motores de las garras
        public static final boolean kClawLeftReversed = false;
        public static final boolean kClawRightReversed = true; // Normalmente uno va invertido

        // Inversión de motores del disparador
        public static final boolean kShooterLeftReversed = false;
        public static final boolean kShooterRightReversed = true;

        // Inversión de motores de tracción
        public static final boolean kFrontLeftDriveEncoderReversed = false;
        public static final boolean kBackLeftDriveEncoderReversed = false;
        public static final boolean kFrontRightDriveEncoderReversed = true;
        public static final boolean kBackRightDriveEncoderReversed = true;

        // Inversión de motores de giro
        public static final boolean kFrontLeftTurningEncoderReversed = false;
        public static final boolean kBackLeftTurningEncoderReversed = false;
        public static final boolean kFrontRightTurningEncoderReversed = false;
        public static final boolean kBackRightTurningEncoderReversed = false;

        // CANcoder IDs
        public static final int kFrontLeftDriveAbsoluteEncoderPort = 14;
        public static final int kBackLeftDriveAbsoluteEncoderPort = 15;
        public static final int kFrontRightDriveAbsoluteEncoderPort = 16;
        public static final int kBackRightDriveAbsoluteEncoderPort = 17;

        // Inversión CANcoders
        public static final boolean kFrontLeftDriveAbsoluteEncoderReversed = false;
        public static final boolean kBackLeftDriveAbsoluteEncoderReversed = false;
        public static final boolean kFrontRightDriveAbsoluteEncoderReversed = false;
        public static final boolean kBackRightDriveAbsoluteEncoderReversed = false;

        // Offsets - AJUSTAR SEGÚN CALIBRACIÓN
        public static final double kFrontLeftDriveAbsoluteEncoderOffsetRad = -0.083984;
        public static final double kBackLeftDriveAbsoluteEncoderOffsetRad = 0.288574;
        public static final double kFrontRightDriveAbsoluteEncoderOffsetRad = 0.415039;
        public static final double kBackRightDriveAbsoluteEncoderOffsetRad = 0.324951;

        // Límites físicos
        public static final double kPhysicalMaxSpeedMetersPerSecond = 4.5;
        public static final double kPhysicalMaxAngularSpeedRadiansPerSecond = 2 * Math.PI;

        // Velocidades para teleoperado
        public static final double kMaxSpeedMetersPerSecond = 4;      
        public static final double kMaxAngularSpeedRadiansPerSecond = 1.5*Math.PI;
    }

    /*public static final class AutoConstants {
        public static final Pose2d START_POSE = new Pose2d(0, 0, Rotation2d.fromDegrees(0));    
    }*/

    public static final class OIConstants {
        public static final int kDriverControllerPort = 0;
        public static final double kDeadband = 0.1;
    }

    public static final class AutoConstants {
        public static final Pose2d START_POSE = new Pose2d(0, 0, Rotation2d.fromDegrees(0));

        // Constantes para el autómata
        public static final double ALIGN_TIME = 1.0; // Tiempo para alinear llantas (segundos)

        public static final double MOVE_FORWARD_TIME = 1; // Tiempo para avanzar 1 metro (a 0.67 m/s)
        public static final double MOVE_BACKWARD_TIME = 1; // Tiempo para retroceder 1 metro (a 0.67 m/s)
        public static final double HARD_MOVEMENT_TIME = 0.2; // Tiempo para acomodar las pelotas
       
        public static final double SHOOT_TIME = 10.0; // Tiempo disparando (segundos)
        public static final double FINAL_ALIGN_TIME = 1.0; // Tiempo para alinear al final

        // Velocidades
        public static final double AUTO_SPEED_FORWARD = 0.67; // m/s (para avanzar 1m en 1.5s)
        public static final double AUTO_SPEED_BACKWARD = -0.67; // m/s (para retroceder 1m en 1.5s)
        public static final double AUTO_SPEED_HARD_MOVEMENT = 1; // m/s (para acomodar las pelotas)
    }
}

/*
 * public static final double kFrontLeftDriveAbsoluteEncoderOffsetRad =
 * -0.083984;
 * public static final double kBackLeftDriveAbsoluteEncoderOffsetRad = 0.288574;
 * public static final double kFrontRightDriveAbsoluteEncoderOffsetRad =
 * 0.415039;
 * public static final double kBackRightDriveAbsoluteEncoderOffsetRad =
 * 0.324951;
 */