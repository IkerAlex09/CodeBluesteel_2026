package frc.robot.subsystems;


import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.lib.util.swerveUtil.CTREModuleState;


import com.ctre.phoenix6.hardware.CANcoder;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import frc.lib.math.Conversions;

import frc.robot.Configs;

import frc.robot.Configs;
import frc.robot.Constants;

//This code is run four times, one for each swerve module on the base
public class MAXSwerveModule {

    //Creates our SPARKMAX objects
    private SparkMax m_drivingSpark;
    private SparkMax m_turningSpark;

    //Creates the encoders from the SPARKMAX objects
    private RelativeEncoder m_drivingEncoder;
    private RelativeEncoder m_turningEncoder;

    //Creates the ClosedLoopControllers from the SPARKMAX objects
    private final SparkClosedLoopController m_drivingClosedLoopController;
    private final SparkClosedLoopController m_turningClosedLoopController;
    
    //Creates a CANCODER object
    private CANcoder angleEncoder;

    //Angular offset
    private double m_chassisAngularOffset = 0;

    //Desired state of the module
    private SwerveModuleState m_desiredState = new SwerveModuleState(0.0, new Rotation2d());

    //Value output for testing, not essential.
    private double rotval = 0;

    //Constructor taking arguments for ids and the offset of the module
    public MAXSwerveModule(int drivingCANId, int turningCANId, int cancoderId, double chassisAngularOffset) {
        //Officially creates the sparkmax objects with real references
        m_drivingSpark = new SparkMax(drivingCANId, MotorType.kBrushless);
        m_turningSpark = new SparkMax(turningCANId, MotorType.kBrushless);
        
        //Same with the CLC
        m_drivingClosedLoopController = m_drivingSpark.getClosedLoopController();
        m_turningClosedLoopController = m_turningSpark.getClosedLoopController();

        //Same with the Cancoder
        angleEncoder = new CANcoder(cancoderId);

        //Configures the encoders, method can be found below
        configEncoders();

        //Configures the sparkmaxes using configurations found in Config.java
        m_turningSpark.configure(Configs.MAXSwerveModule.turningConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
        m_drivingSpark.configure(Configs.MAXSwerveModule.drivingConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    
       //Officially sets the angular offset (In Radians)
        m_chassisAngularOffset = chassisAngularOffset;
        
        //Resets the value of the angle encoder to the value of the cancoder upon initialization
        resetToAbsolute();

        
        //Initializes the desiredState angle as a new Rotation2d based on the current position of the module
        m_desiredState.angle = new Rotation2d(m_turningEncoder.getPosition());
    }

    private void configEncoders(){
        //Applies configs to the Cancoder from Config.java
        angleEncoder.getConfigurator().apply(Configs.MAXSwerveModule.swerveCANcoderconfig);
        
        //Officially gives a reference to the driving encoder object and sets it's position to 0
        m_drivingEncoder = m_drivingSpark.getEncoder();
        m_drivingEncoder.setPosition(0);

        //Officially gives a reference to the angle encoder object
        m_turningEncoder = m_turningSpark.getEncoder();

        
    }


    public void resetToAbsolute(){
        //Sets the angle encoder to a position based on the current position of the Cancoder (in radians) and the angular offset
        double absolutePosition = getCanCoder().getRadians() - m_chassisAngularOffset;
        m_turningEncoder.setPosition(absolutePosition); 
    }
    

    //gets the position of the module
    public SwerveModulePosition getPosition() { 
        return new SwerveModulePosition(m_drivingEncoder.getPosition(), getAngle());
    }

    //gets the position of the encoder in radians
    public double getEncoderPosition(){
        return m_turningEncoder.getPosition();
    }
    
    
    public SwerveModuleState getState(){
        return new SwerveModuleState(m_drivingEncoder.getVelocity(), getAngle());
    }


    //Called every time the position or speed of the wheels need to be updated. Essential for drive
    public void setDesiredState(SwerveModuleState desiredState) {
        //Outputs the original argument for testing purposes
        SmartDashboard.putNumber("Original Desired Angle", desiredState.angle.getDegrees());

        //Creates a new corrected and optimized desired state based off of the original argument and outputs it
        SwerveModuleState correctedDesiredState = new SwerveModuleState();
        correctedDesiredState.speedMetersPerSecond = desiredState.speedMetersPerSecond;
        correctedDesiredState.angle = desiredState.angle;
        correctedDesiredState.optimize(new Rotation2d(m_turningEncoder.getPosition()));
        SmartDashboard.putNumber("Desired Angle", correctedDesiredState.angle.getDegrees());

        //Methods are called to apply the desired state to the angular and drive sparkmaxes
        setAngle(correctedDesiredState);
        setSpeed(correctedDesiredState, true);

        //Updates the desired state variable to the argument passed by the method
        m_desiredState = desiredState;
        
    }

    private Rotation2d getAngle(){
        return Rotation2d.fromDegrees(m_turningEncoder.getPosition());
    }

    public Rotation2d getCanCoder(){
        return Rotation2d.fromRotations(angleEncoder.getAbsolutePosition().getValueAsDouble());
    }

    private void setAngle(SwerveModuleState desiredState)
    {
        //Code to stop the angular motor if the input given is very small (Eliminates stick drift or jitter)
        if(Math.abs(desiredState.speedMetersPerSecond) <= (Constants.kMaxAngularSpeed*0.01))
       {
        m_turningSpark.stopMotor();
        return;
       }
        
        //obtains the angle value of the desired state in radians
        rotval = desiredState.angle.getRadians();
        
        //controller updates sparkmax to the specific radian position
        m_turningClosedLoopController.setReference(rotval, ControlType.kPosition);
            
    }

    private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop)
    {
       //If our system is openLoop (which it is), it will set the motor to a certain percent output based on factors like the max speed and the desired state output
        if(isOpenLoop)
        {
            double percentOutput = desiredState.speedMetersPerSecond / Constants.kMaxSpeedMetersPerSecond;
            m_drivingSpark.set(percentOutput);
            return;
        }
 
        
        
        
        //Extraneous code
        m_drivingClosedLoopController.setReference(desiredState.speedMetersPerSecond, ControlType.kVelocity);
        
    }

    public double getRotVal(){
        return rotval;
    }
    



}
