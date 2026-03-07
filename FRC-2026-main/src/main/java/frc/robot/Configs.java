package frc.robot;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.Constants.ModuleConstants;

public class Configs {
    public static final class MAXSwerveModule {
        public static final SparkMaxConfig drivingConfig = new SparkMaxConfig();
            public static final SparkMaxConfig turningConfig = new SparkMaxConfig();
                public static final CANcoderConfiguration swerveCANcoderconfig = new CANcoderConfiguration();
            static {
                double drivingFactor = ModuleConstants.kWheelDiameterMeters * Math.PI / ModuleConstants.kDrivingMotorReduction;
                double turningFactor = 2 * Math.PI;
                double drivingVelocityFeedForward = 1/ModuleConstants.kDriveWheelFreeSpeedRps;

                drivingConfig
                        .idleMode(IdleMode.kBrake)
                        .smartCurrentLimit(40);
                drivingConfig.encoder
                        .positionConversionFactor(drivingFactor)
                        .velocityConversionFactor(drivingFactor/ 60.0);
                drivingConfig.closedLoop
                        .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                        .pid(0.04, 0, 0)
                        .velocityFF(drivingVelocityFeedForward)
                        .outputRange(-1, 1);
                
                turningConfig
                        .idleMode(IdleMode.kBrake)
                        .smartCurrentLimit(20)
                        .inverted(true);
                turningConfig.encoder
                        .positionConversionFactor((2 * Math.PI ) / Constants.angleGearRatio)
                        .velocityConversionFactor((2 * Math.PI) / Constants.angleGearRatio/60);
                turningConfig.closedLoop
                        .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                        .pid(1, 0, 0)
                        .outputRange(-1, 1)
                        .positionWrappingEnabled(true)
                        .positionWrappingInputRange(0, turningFactor);
            }
    }
    
}