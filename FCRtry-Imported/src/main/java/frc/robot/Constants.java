package frc.robot;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
//import edu.wpi.first.apriltag.AprilTagFieldLayout; //por ahora no son necesarios
//import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

public final class Constants {


    public static final class ModuleConstants {//constantes
        public static final double kWheelDiameterMeters = Units.inchesToMeters(4); //(MODIFY) Diametro de la rueda
        public static final double kDriveMotorGearRatio = 1 / 6.75; //(MODIFY) Relacion de engranajes del motor de traccion
        public static final double kSteeringMotorGearRatio = 1 / 21.4285714286; //(MODIFY) Relacion de engranajes del motor de direccion
        public static final double kDriveEncoderRot2Meter = kDriveMotorGearRatio * Math.PI * kWheelDiameterMeters; // Conversion de vueltas del encoder a metros
        public static final double kSteeringEncoderRot2Rad = kSteeringMotorGearRatio * 2 * Math.PI; // Conversion de vueltas del encoder a radianes
        public static final double kDriveEncoderRPM2MeterPerSec = kDriveEncoderRot2Meter / 60; // Conversion de RPM del encoder a metros por segundo
        public static final double kSteeringEncoderRPM2RadPerSec = kSteeringEncoderRot2Rad / 60; // Conversion de RPM del encoder a radianes por segundo
        public static final double kPSteering = 0.32; // Control del posicion (PID)
        public static final double kISteering = 0;
        public static final double KDSteering = 0.05;
        public static final String CANivore = "5133BlueSteel"; //Nombre del CANivore 

    }

    public static final class DriveConstants {//la cinematica del robot

        public static final double kTrackWidth = Units.inchesToMeters(27); //(MODIFY)
        // Distance between right and left wheels
        public static final double kWheelBase = Units.inchesToMeters(27); //(MODIFY)
        // Distance between front and back wheels
        public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics( 
                new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
                new Translation2d(kWheelBase / 2, kTrackWidth / 2),
                new Translation2d(-kWheelBase / 2, -kTrackWidth / 2),
                new Translation2d(-kWheelBase / 2, kTrackWidth / 2));
        
        public static final Translation2d[] modulesOffSet = new Translation2d[] {
            new Translation2d(DriveConstants.kWheelBase / 2, -DriveConstants.kTrackWidth / 2),
            new Translation2d(DriveConstants.kWheelBase / 2, DriveConstants.kTrackWidth / 2),
            new Translation2d(-DriveConstants.kWheelBase / 2, -DriveConstants.kTrackWidth / 2),
            new Translation2d(-DriveConstants.kWheelBase / 2, DriveConstants.kTrackWidth / 2)
        };


        public static final int kFrontLeftDriveMotorPort = 1; //posiciones de los sparks
        public static final int kBackLeftDriveMotorPort = 3 ;
        public static final int kFrontRightDriveMotorPort = 5;
        public static final int kBackRightDriveMotorPort = 7 ;
    
        public static final int kFrontLeftSteeringMotorPort = 2;
        public static final int kBackLeftSteeringMotorPort = 4 ;
        public static final int kFrontRightSteeringMotorPort = 6 ;
        public static final int kBackRightSteeringMotorPort = 8 ;
    
        public static final boolean kFrontLeftSteeringEncoderReversed = false; //si el encoder esta invertido o no
        public static final boolean kBackLeftSteeringEncoderReversed = false ; //util para ver hacia donde gira o si esta conectado al reves
        public static final boolean kFrontRightSteeringEncoderReversed = false ;
        public static final boolean kBackRightSteeringEncoderReversed = false ; 
    
        public static final boolean kFrontLeftDriveEncoderReversed = true;
        public static final boolean kBackLeftDriveEncoderReversed = true ;
        public static final boolean kFrontRightDriveEncoderReversed = false ;
        public static final boolean kBackRightDriveEncoderReversed = false ;
    
        public static final int kBackLeftDriveAbsoluteEncoderPort = 15 ;
        
        public static final int kFrontRightDriveAbsoluteEncoderPort = 16 ;
        public static final int kBackRightDriveAbsoluteEncoderPort = 17 ;
        public static final int kFrontLeftDriveAbsoluteEncoderPort = 14 ;
    
        public static final boolean kFrontLeftDriveAbsoluteEncoderReversed = true;
        public static final boolean kBackLeftDriveAbsoluteEncoderReversed = false;
        public static final boolean kFrontRightDriveAbsoluteEncoderReversed = false;
        public static final boolean kBackRightDriveAbsoluteEncoderReversed = false;
    
        public static final double kBackLeftDriveAbsoluteEncoderOffsetRad = 0.293701; //BL
        public static final double kFrontRightDriveAbsoluteEncoderOffsetRad = 0.416992; //FR
        public static final double kBackRightDriveAbsoluteEncoderOffsetRad = 0.325928  ;  //BR
        public static final double kFrontLeftDriveAbsoluteEncoderOffsetRad = -0.084961 ;  //Fl
    

        public static final double kPhysicalMaxSpeedMetersPerSecond = 4.60248; //limitar la v de la llanta
        public static final double kPhysicalMaxAngularSpeedRadiansPerSecond = 4 * Math.PI; //limitar la v direccion de la llanta

        public static final double kTeleDriveMaxSpeedMetersPerSecond = kPhysicalMaxSpeedMetersPerSecond * 0.80; // Velocidad lineal maxima
        public static final double kTeleDriveMaxAngularSpeedRadiansPerSecond = kPhysicalMaxAngularSpeedRadiansPerSecond / 2; // Velocidad angular maxima
        public static final double kTeleDriveMaxAccelerationUnitsPerSecond = 3; //aceleracion maxima por segundo
        public static final double kTeleDriveMaxAngularAccelerationUnitsPerSecond = 3; //aceleracion rotativa maxima por segundo
        
        public static final double YAW_RATE_LDP_CUTOFF_HZ = 0.0; //no se usa, filtración de señales

        public static final boolean isGyroReversed = false; 
    }

    public static final class OIConstants { //constante comunicacion con el operador
        public static final double kDeadband = 0.15;
    }

    public static final class FieldCosntants{ //QR
        
        public static final AprilTagFieldLayout kTagLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeWelded);

    }

    public static final class VisionConstants{ //vision del QR

        public static final String cameraName = "";

        public static final Transform3d kRobotToCam = new Transform3d( new Translation3d( 0 , 0 , 0 ), new Rotation3d( 0 , 0 , 0 ));

        public static final double maxAmbiguity = 0.0;

        public static final double MAX_DIST_SINGLE = 10;

        public static final double MAX_DIST_MULTIPLE = 20;

        public static final double MAX_YAW_RATE_DEG_SEC = 0.5;

    }

    public static final class ElevatorConstants{ //elevador y brazo (Modificar)

        public static final int ElevatorID = 9 ;

        public static final int PivotID = 10 ;

        public static final double ElevatorMaxSpeed = 0.5;

        public static final double PivotMaxSpeed = 0.3;

    }

}