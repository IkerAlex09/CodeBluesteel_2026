package frc.robot.subsystems;


import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

import java.util.Optional;

import com.ctre.phoenix6.swerve.SwerveModule;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
public class Swerve extends SubsystemBase{

    //Creates MaxSwerveModule objects for each of the four modules on the base
    private final MAXSwerveModule SwerveMod0 = new MAXSwerveModule(
        Constants.Mod0.drivermotorID0,
        Constants.Mod0.anglemotorID0,
        Constants.Mod0.camcoderID,
        Constants.Mod0.offset0
    );

    private final MAXSwerveModule SwerveMod1 = new MAXSwerveModule(
        Constants.Mod1.drivermotorID1,
        Constants.Mod1.anglemotorID1,
        Constants.Mod1.camcoderID1,
        Constants.Mod1.offset1
    );

    private final MAXSwerveModule SwerveMod2 = new MAXSwerveModule(
        Constants.Mod2.drivermotorID2,
        Constants.Mod2.anglemotorID2,
        Constants.Mod2.camcoderID2,
        Constants.Mod2.offset2
    );

    private final MAXSwerveModule SwerveMod3 = new MAXSwerveModule(
        Constants.Mod3.drivermotorID3,
        Constants.Mod3.anglemotorID3,
        Constants.Mod3.camcoderID3,
        Constants.Mod3.offset3
    );

    //Creates a field for the smart dashboard to visualize
    Field2d m_field = new Field2d();

    //Creates a gyro (NavX)
    public AHRS m_gyro;

    //Creates an odometry object (Estimation of our position based on motion data from our gyro)
    SwerveDriveOdometry m_Odometry;
    
    //I was trying to test if the auton was working or not, not important
    public boolean autonsuccess = false;

    public Swerve(){

        //Gyro setup
        m_gyro = new AHRS(NavXComType.kUSB1);
        m_gyro.reset();
        Timer.delay(2);
        zeroGyro();

        //Creates odometry from the current positions of the swervemodules
        m_Odometry = new SwerveDriveOdometry(
            Constants.kDriveKinematics, 
            getYaw(),
            new SwerveModulePosition[] {
                SwerveMod0.getPosition(),
                SwerveMod1.getPosition(),
                SwerveMod2.getPosition(),
                SwerveMod3.getPosition()
            }
        );

        //Autonomous setup -- This comes from Path Planner and is NOT my code.
        RobotConfig config;
        try{
        config = RobotConfig.fromGUISettings();
        AutoBuilder.configure(
                this::getPose, // Robot pose supplier
                this::resetPose, // Method to reset odometry (will be called if your auto has a starting pose)
                this::getRobotRelativeSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
                (speeds, feedforwards) -> driveRobotRelative(speeds), // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds. Also optionally outputs individual module feedforwards
                new PPHolonomicDriveController( // PPHolonomicController is the built in path following controller for holonomic drive trains
                        new PIDConstants(0.04, 0.0, 0.0), // Translation PID constants
                        new PIDConstants(0.1, 0.0, 0.0) // Rotation PID constants
                ),
                config, // The robot configuration
                () -> {
                // Boolean supplier that controls when the path will be mirrored for the red alliance
                // This will flip the path being followed to the red side of the field.
                // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

                var alliance = DriverStation.getAlliance();
                if (alliance.isPresent()) {
                    return alliance.get() == DriverStation.Alliance.Red;
                }
                return false;
                },
                this // Reference to this subsystem to set requirements
        );
        autonsuccess = true;
        } catch (Exception e) {
        // Handle exception as needed
        e.printStackTrace();
        }  
        //End of Autonomous setup -- This is where the path planner code ends.
    }

    //Returns whether we are on the blue or red alliance
    public boolean isBlueAlliance() {
    Optional<DriverStation.Alliance> alliance = DriverStation.getAlliance();
    if (alliance.isEmpty() || alliance.get().equals(DriverStation.Alliance.Blue)) {
      return true;
    } else {
      return false;
    }
    }

    //This periodic method updates the pose and odometry of the robot and outputs signifigant values to smartDashboard
    @Override
    public void periodic(){
        m_field.setRobotPose(getPose());
        SmartDashboard.putData("Field", m_field);
        SmartDashboard.putString("Angle", m_gyro.getRotation2d().toString());

        m_Odometry.update(
            getYaw(),
            new SwerveModulePosition[] {
                SwerveMod0.getPosition(),
                SwerveMod1.getPosition(),
                SwerveMod2.getPosition(),
                SwerveMod3.getPosition()
            }

        );
        SmartDashboard.putNumber("Mod 0 Cancoder", SwerveMod0.getCanCoder().getDegrees());
        SmartDashboard.putNumber("Mod 0 Encoder", SwerveMod0.getEncoderPosition());
        SmartDashboard.putNumber("Mod 1 Cancoder", SwerveMod1.getCanCoder().getDegrees());
        SmartDashboard.putNumber("Mod 1 Encoder", SwerveMod1.getEncoderPosition());
        SmartDashboard.putNumber("Mod 2 Cancoder", SwerveMod2.getCanCoder().getDegrees());
        SmartDashboard.putNumber("Mod 2 Encoder", SwerveMod2.getEncoderPosition());
        SmartDashboard.putNumber("Mod 3 Cancoder", SwerveMod3.getCanCoder().getDegrees());
        SmartDashboard.putNumber("Mod 3 Encoder", SwerveMod3.getEncoderPosition());
        SmartDashboard.putNumber("Mod 0 RotVal", SwerveMod0.getRotVal());
        SmartDashboard.putNumber("Mod 0 EncoderPosition", SwerveMod0.getEncoderPosition());
        SmartDashboard.putNumber("Gyro", getHeading().getDegrees());
        SmartDashboard.putBoolean("AutonSuccess", autonsuccess);
        
    }


    public void zeroGyro(){
        m_gyro.zeroYaw();
        
    }

    public void resetPose(Pose2d pose) {
        m_Odometry.resetPosition(getGyroYaw(), getModulePositions(), pose);
    }

    public Rotation2d getYaw(){
        return (Constants.invertGyro) ? Rotation2d.fromDegrees(360 - m_gyro.getFusedHeading()) : Rotation2d.fromDegrees(m_gyro.getFusedHeading());
    }

    public void driveRobotRelative(ChassisSpeeds speeds){
        SwerveModuleState[] states = Constants.kDriveKinematics.toSwerveModuleStates(speeds);
        SwerveDriveKinematics.desaturateWheelSpeeds(states, Constants.kMaxSpeedMetersPerSecond);
        SwerveMod0.setDesiredState(states[0]);
        SwerveMod1.setDesiredState(states[1]);
        SwerveMod2.setDesiredState(states[2]);
        SwerveMod3.setDesiredState(states[3]);
    }

    public Pose2d getPose(){
        return m_Odometry.getPoseMeters ();
    }

    public void adjustGyro(double angle){
        m_gyro.setAngleAdjustment(angle);
    }

    public void resetOdometry(Pose2d pose){
        m_Odometry.resetPosition(
            getYaw(),
            new SwerveModulePosition[] {
                SwerveMod0.getPosition(),
                SwerveMod1.getPosition(),
                SwerveMod2.getPosition(),
                SwerveMod3.getPosition()
            },
            pose);
    }

    //Commands the swerve drive to set its modules to specific states based on arguments usually passed from TeleopSwerve.java
    public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
        SwerveModuleState[] swerveModuleStates =
            Constants.kDriveKinematics.toSwerveModuleStates(
                fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
                                    translation.getX(), 
                                    translation.getY(), 
                                    rotation, 
                                    getHeading()
                                )
                                : new ChassisSpeeds(
                                    translation.getX(), 
                                    translation.getY(), 
                                    rotation)
                                );
    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.kMaxSpeedMetersPerSecond);
    
    SmartDashboard.putNumber("SwerveModState0", swerveModuleStates[0].angle.getDegrees());
    SmartDashboard.putNumber("SwerveModState1", swerveModuleStates[1].angle.getDegrees());
    SmartDashboard.putNumber("SwerveModState2", swerveModuleStates[2].angle.getDegrees());
    SmartDashboard.putNumber("SwerveModState3", swerveModuleStates[3].angle.getDegrees());

    SwerveMod0.setDesiredState(swerveModuleStates[0]);
    SwerveMod1.setDesiredState(swerveModuleStates[1]);
    SwerveMod2.setDesiredState(swerveModuleStates[2]);
    SwerveMod3.setDesiredState(swerveModuleStates[3]);
    }

    public void zeroHeading() {
        m_gyro.reset();
    }

    public ChassisSpeeds getRobotRelativeSpeeds(){
        return Constants.kDriveKinematics.toChassisSpeeds(getModuleStates());
    }

    public SwerveModuleState[] getModuleStates(){
        SwerveModuleState[] states = new SwerveModuleState[4];
        states[0] = SwerveMod0.getState();
        states[1] = SwerveMod1.getState();
        states[2] = SwerveMod2.getState();
        states[3] = SwerveMod3.getState();
        return states;
    }

    public Rotation2d getGyroYaw() {
        return (Constants.invertGyro) ? Rotation2d.fromDegrees(360 - m_gyro.getFusedHeading()) : Rotation2d.fromDegrees(m_gyro.getFusedHeading());
    }

    public SwerveModulePosition[] getModulePositions(){
        SwerveModulePosition[] positions = new SwerveModulePosition[4];
        positions[0] = SwerveMod0.getPosition();
        positions[1] = SwerveMod1.getPosition();
        positions[2] = SwerveMod2.getPosition();
        positions[3] = SwerveMod3.getPosition();

        return positions;
    }

    public Rotation2d getHeading() {
        return getPose().getRotation();
    }
}
