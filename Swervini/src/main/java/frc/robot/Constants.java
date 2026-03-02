package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

public final class Constants {
    public static final class DriveConstants {
        // Chasis dimensions (ajusta según tu robot)
        public static final double TRACK_WIDTH = Units.inchesToMeters(20.5);
        public static final double WHEEL_BASE = Units.inchesToMeters(20.5);
        
        public static final SwerveDriveKinematics DRIVE_KINEMATICS = new SwerveDriveKinematics(
            new Translation2d(WHEEL_BASE / 2, TRACK_WIDTH / 2),
            new Translation2d(WHEEL_BASE / 2, -TRACK_WIDTH / 2),
            new Translation2d(-WHEEL_BASE / 2, TRACK_WIDTH / 2),
            new Translation2d(-WHEEL_BASE / 2, -TRACK_WIDTH / 2)
        );

        // ID de CAN de los motores (ajusta según tu robot)
        public static final int FRONT_LEFT_DRIVE = 1;
        public static final int FRONT_LEFT_TURN = 2;
        public static final int FRONT_RIGHT_DRIVE = 3;
        public static final int FRONT_RIGHT_TURN = 4;
        public static final int BACK_LEFT_DRIVE = 5;
        public static final int BACK_LEFT_TURN = 6;
        public static final int BACK_RIGHT_DRIVE = 7;
        public static final int BACK_RIGHT_TURN = 8;

        // Encoder offsets (ajusta con tus valores)
        public static final double FRONT_LEFT_ENCODER_OFFSET = 0.0;
        public static final double FRONT_RIGHT_ENCODER_OFFSET = 0.0;
        public static final double BACK_LEFT_ENCODER_OFFSET = 0.0;
        public static final double BACK_RIGHT_ENCODER_OFFSET = 0.0;

        // Velocidades máximas
        public static final double MAX_SPEED_METERS_PER_SECOND = 4.5;
        public static final double MAX_ANGULAR_SPEED_RADIANS_PER_SECOND = 2 * Math.PI;
    }

    public static final class ShooterConstants {
        public static final int SHOOTER_LEFT = 9;
        public static final int SHOOTER_RIGHT = 10;
        public static final double SHOOTER_SPEED = 0.8;
    }

    public static final class OIConstants {
        public static final int DRIVER_CONTROLLER_PORT = 0;
        public static final double DEADBAND = 0.05;
    }
}
