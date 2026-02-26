package frc.robot.Drive;
/*import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;*/
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry3d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ModuleConstants;

public class SwerveSubsystem extends SubsystemBase {

    boolean isFieldOriented;
    
    private final SwerveModule FrontLeft = new SwerveModule(
        DriveConstants.kFrontLeftSteeringMotorPort,
        DriveConstants.kFrontLeftDriveMotorPort,
        DriveConstants.kFrontLeftDriveAbsoluteEncoderPort,
        DriveConstants.kFrontLeftSteeringEncoderReversed,
        DriveConstants.kFrontLeftDriveEncoderReversed,
        DriveConstants.kFrontLeftDriveAbsoluteEncoderReversed,
        DriveConstants.kFrontLeftDriveAbsoluteEncoderOffsetRad,
        ModuleConstants.CANivore
    );
    
    private final SwerveModule FrontRight = new SwerveModule(
        DriveConstants.kFrontRightSteeringMotorPort,
        DriveConstants.kFrontRightDriveMotorPort,
        DriveConstants.kFrontRightDriveAbsoluteEncoderPort,
        DriveConstants.kFrontRightSteeringEncoderReversed,
        DriveConstants.kFrontRightDriveEncoderReversed,
        DriveConstants.kFrontRightDriveAbsoluteEncoderReversed,
        DriveConstants.kFrontRightDriveAbsoluteEncoderOffsetRad,
        ModuleConstants.CANivore
    );
    
    private final SwerveModule BackLeft = new SwerveModule(
        DriveConstants.kBackLeftSteeringMotorPort,
        DriveConstants.kBackLeftDriveMotorPort,
        DriveConstants.kBackLeftDriveAbsoluteEncoderPort,
        DriveConstants.kBackLeftSteeringEncoderReversed,
        DriveConstants.kBackLeftDriveEncoderReversed,
        DriveConstants.kBackLeftDriveAbsoluteEncoderReversed,
        DriveConstants.kBackLeftDriveAbsoluteEncoderOffsetRad,
        ModuleConstants.CANivore
    );
    
    private final SwerveModule BackRight = new SwerveModule(
        DriveConstants.kBackRightSteeringMotorPort,
        DriveConstants.kBackRightDriveMotorPort,
        DriveConstants.kBackRightDriveAbsoluteEncoderPort,
        DriveConstants.kBackRightSteeringEncoderReversed,
        DriveConstants.kBackRightDriveEncoderReversed,
        DriveConstants.kBackRightDriveAbsoluteEncoderReversed,
        DriveConstants.kBackRightDriveAbsoluteEncoderOffsetRad,
        ModuleConstants.CANivore
    );

    private ADXRS450_Gyro gyro = new ADXRS450_Gyro(SPI.Port.kMXP);
    private double gyroAngleAdjustment = 0.0;

    private Rotation3d getRotation3dFromGyro(){
        return new Rotation3d(0.0, 0.0, Math.toRadians(getHeading()));
    }

    private SwerveModulePosition[] getModulePositions(){
        SwerveModulePosition[] modulesPosition = {
            FrontLeft.getPosition(),
            FrontRight.getPosition(),
            BackLeft.getPosition(),
            BackRight.getPosition()
        };

        return modulesPosition;
    }

    private SwerveModuleState[] getModuleState(){

        SwerveModuleState[] moduleStates = new SwerveModuleState[] {
            FrontLeft.getState(),
            FrontRight.getState(),
            BackLeft.getState(),
            BackRight.getState()
        };

        return moduleStates;

    }

    private ChassisSpeeds getChassisSpeeds (){
        
        return DriveConstants.kDriveKinematics.toChassisSpeeds( getModuleState() );

    }

    private final SwerveDriveOdometry Odometer = new SwerveDriveOdometry(

        DriveConstants.kDriveKinematics,
        new Rotation2d(0),
        getModulePositions()

    );

    private final SwerveDriveOdometry3d odometry3d = new SwerveDriveOdometry3d(
            DriveConstants.kDriveKinematics, 
            new Rotation3d(), 
            getModulePositions()
        ); 

    public SwerveSubsystem(){

        this.isFieldOriented = true;

        new Thread(
            ()->{
                try{
                    Thread.sleep(1000);
                    ZeroHeading();

                }catch(Exception e){
                    System.out.println(e);
                    SmartDashboard.putString("Error: ", e.toString());
                }

            }
        ).start();;
/* 
        RobotConfig config;
        try {
            config = RobotConfig.fromGUISettings();
        } catch (Exception e) {
            e.printStackTrace();
            
            config = new RobotConfig(
                0
                ,0
                , new ModuleConfig(0, 0, 0, DCMotor.getNEO(8), 0, 0)
                ,  DriveConstants.modulesOffSet 
            );
        }


        // Configure AutoBuilder last
        AutoBuilder.configure(
                this::getPose2d, // Robot pose supplier
                this::resetOdometry, // Method to reset odometry (will be called if your auto has a starting pose)
                this::getChassisSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
                (speeds, feedforwards) -> relativeDrive(speeds), // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds. Also optionally outputs individual module feedforwards
                new PPHolonomicDriveController( // PPHolonomicController is the built in path following controller for holonomic drive trains
                        new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
                        new PIDConstants(5.0, 0.0, 0.0) // Rotation PID constants
                ),
                config, // The robot configuration
                // Boolean supplier that controls when the path will be mirrored for the red alliance
                // This will flip the path being followed to the red side of the field.
                // THE ORIGIN WILL REMAIN ON THE BLUE SIDE
                () -> DriverStation.getAlliance().map(a -> a == DriverStation.Alliance.Red).orElse(false),
                
                this // Reference to this subsystem to set requirements
        );*/

    }

    public boolean isFieldOriented(){

        return this.isFieldOriented;

    }

    public void changeIsFieldOriented( boolean newState ){

        this.isFieldOriented = newState;

    }

    public void ZeroHeading(){

        gyro.reset();
        resetEncoders();

    }

    public void resetEncoders(){
        FrontLeft.ResetEncoders();
    public void ChangeHeading(double angle){
    public void resetEncoders(){
        FrontLeft.ResetEncoders();
        FrontRight.ResetEncoders();
        BackLeft.ResetEncoders();
        BackRight.ResetEncoders();
    }

    public void ChangeHeading(double angle){

        gyro.reset();
        gyroAngleAdjustment = angle;

    }

    public double getHeading(){

        double heading = Math.IEEEremainder(gyro.getAngle() + gyroAngleAdjustment, 360);

        return heading * (DriveConstants.isGyroReversed ? -1.0 : 1.0);

    }
        return Rotation2d.fromDegrees(getHeading());

    }

    public Pose2d getPose2d(){

        return poseEstimator != null ? poseEstimator.getEstimatedPosition() : Odometer.getPoseMeters();

    }

    public Pose3d getPose3d(){

        return odometry3d.getPoseMeters();
    public Pose3d getPose3d(){

        return odometry3d.getPoseMeters();

    }

    public void resetOdometry( Pose2d pose ){
        // Reset 2D odometry and pose estimator, and align 3D odometry with the same X,Y (Z=0)
        Odometer.resetPosition(getRotation2d(), getModulePositions(), pose);
        poseEstimator.resetPosition(getRotation2d(), getModulePositions(), pose);
        odometry3d.resetPosition(getRotation3dFromGyro(), getModulePositions(), new Pose3d(pose.getX(), pose.getY(), 0.0, getRotation3dFromGyro()));
    }

    public void reset3dOdometry (Pose3d pose ){

        odometry3d.resetPosition(getRotation3dFromGyro(), getModulePositions(), pose);

    }
            DriveConstants.kDriveKinematics
            , getRotation2d()
            , getModulePositions()
            , getPose2d()
        );

    public void AddVisionMeasurment ( Pose2d Measurement, double timestampSeconds, Matrix<N3,N1> StdDevs ){

        poseEstimator.addVisionMeasurement(Measurement, timestampSeconds, StdDevs);

    }

    private LinearFilter yawRateFilter = LinearFilter.singlePoleIIR(
        2 * Math.PI * DriveConstants.YAW_RATE_LDP_CUTOFF_HZ,
        0.02
    );

    private double yawRateFilteredDegPerSec = 0.0;

    public double getYawRateDegPerSecRaw(){

        double rate = gyro.getRate();

        return rate * (DriveConstants.isGyroReversed ? -1.0 : 1.0);

    }

    public double getYawRateDegPerSec(){

        return yawRateFilteredDegPerSec;

    }

    StructArrayPublisher<SwerveModuleState> StatePublisher = NetworkTableInstance.getDefault()
        odometry3d.update(getRotation3dFromGyro(), getModulePositions());
    public void periodic(){

        odometry3d.update(gyro.getRotation3d(), getModulePositions());
    @Override
    public void periodic(){

        odometry3d.update(getRotation3dFromGyro(), getModulePositions());

        Odometer.update(getRotation2d(), getModulePositions());

        poseEstimator.update(getRotation2d(), getModulePositions());
        yawRateFilteredDegPerSec = yawRateFilter.calculate(getYawRateDegPerSecRaw());

        SmartDashboard.putNumber("RobotHeading: 2d Odometry ", getHeading());
        SmartDashboard.putString("Robot Location: 2d Odometry", Odometer.toString());

        SmartDashboard.putString("Robot Location: 3d Odometry", getPose3d().getTranslation().toString());

        SmartDashboard.putString("Robot Location Pose Estimation", poseEstimator.getEstimatedPosition().getTranslation().toString());

        SmartDashboard.putNumber("Yaw Rate deg/seg raw", getYawRateDegPerSecRaw());
        SmartDashboard.putNumber("Yaw Rate deg/seg filtered", getYawRateDegPerSec());

        SmartDashboard.putString("Swerve State", java.util.Arrays.toString(getModuleState()));

        SmartDashboard.putNumber("Right Front Abs Encoder", FrontRight.getAbsolutePositionDegrees());
        SmartDashboard.putNumber("Left Back Abs Encoder", BackLeft.getAbsolutePositionDegrees());
        SmartDashboard.putNumber("Right Back Abs Encoder", BackRight.getAbsolutePositionDegrees());

    }
        FrontRight.Stop();
        BackLeft.Stop();
        BackRight.Stop();

    }

    public void setModuleStates( SwerveModuleState[] desireState ){

        SwerveDriveKinematics.desaturateWheelSpeeds(desireState, DriveConstants.kPhysicalMaxSpeedMetersPerSecond);

        FrontLeft.SetDesireState(desireState[0]);
        FrontRight.SetDesireState(desireState[1]);
        BackLeft.SetDesireState(desireState[2]);
        BackRight.SetDesireState(desireState[3]);

    }

    public void relativeDrive( ChassisSpeeds speeds ){

        SwerveModuleState[] states = DriveConstants.kDriveKinematics.toSwerveModuleStates(speeds);

        setModuleStates(states);

    }

}