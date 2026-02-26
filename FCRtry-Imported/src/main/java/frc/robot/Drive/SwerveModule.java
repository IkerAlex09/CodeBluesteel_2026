package frc.robot.Drive;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ModuleConstants;

/*
 Minimal local stubs for REV Robotics classes used by this file so the
 code can compile without the REV library. These stubs implement only
 the small subset of the API that SwerveModule uses and do nothing on hardware.
 Replace these with the real REV classes by adding the REV dependency
 when you are building for actual robot hardware.
*/

class RelativeEncoder {
    private double position = 0.0;
    private double velocity = 0.0;

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public double getVelocity() {
        return velocity;
    }

    // Optional setter if ever needed
    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }
}

class SparkLowLevel {
    public enum MotorType {
        kBrushless
    }
}

class SparkBase {
    public enum PersistMode {
        kPersistParameters
    }

    public enum ResetMode {
        kNoResetSafeParameters
    }
}

class SparkBaseConfig {
    public enum IdleMode {
        kBrake
    }
}

class SparkMaxConfig {
    public final AbsoluteEncoderConfig absoluteEncoder = new AbsoluteEncoderConfig();

    public SparkMaxConfig() {
    }

    public void inverted(boolean inv) {
        // no-op stub
    }

    public void idleMode(SparkBaseConfig.IdleMode mode) {
        // no-op stub
    }

    static class AbsoluteEncoderConfig {
        public void positionConversionFactor(double v) {
            // no-op stub
        }

        public void velocityConversionFactor(double v) {
            // no-op stub
        }
    }
}

class SparkMax {
    private final int id;
    private final SparkLowLevel.MotorType motorType;
    private final RelativeEncoder encoder = new RelativeEncoder();

    public SparkMax(int id, SparkLowLevel.MotorType motorType) {
        this.id = id;
        this.motorType = motorType;
    }

    public RelativeEncoder getEncoder() {
        return encoder;
    }

    public void set(double output) {
        // no-op stub
    }

    public void configure(SparkMaxConfig config, SparkBase.ResetMode resetMode, SparkBase.PersistMode persistMode) {
        // no-op stub
    }
}

public class SwerveModule {
 
    //Declaracion de motores
    private final SparkMax SteeringMotor, DriveMotor;

    //declaracion de los encoders reltivos
    private final RelativeEncoder SteeringMotorEncoder, DriveMotorEncoder;

    //Declaracion de la configuracion de los motores
    private SparkMaxConfig SteeringMotorConfig, DriveMotorConfig;

    //declaracion del encoder absoluto
    private final CANcoder AbsoluteEncoder ;

    //declaracfion del PID
    private final PIDController steeringPidController;

    //declaracion del offset
    private double AbsoluteEncoderOffSet;

    //declaracion para saber si el encoder tiene que estar en reversa
    private boolean AbsoluteEncoderReversed;

    //declaracion para saber el ID del encoder
    private int absoluteEncoderID;

    //funcion para crear a los modulos
    public SwerveModule( 

        //Requisitos
        int SteeringMotorID, 
        int DriveMotorID, 
        int EncoderID, 
        boolean SteeringMotorReversed, 
        boolean DriveMotorReversed, 
        boolean EncoderReversed, 
        double offSet,
        String CANivore

     ){

        //Asignamos todos los valores que queremos conservar a lo largo del codigo
        this.AbsoluteEncoderOffSet = offSet;
        this.AbsoluteEncoderReversed = EncoderReversed;
        this.absoluteEncoderID = EncoderID;

        //Creamos los motores
        DriveMotor = new SparkMax(DriveMotorID, SparkLowLevel.MotorType.kBrushless);
        SteeringMotor = new SparkMax(SteeringMotorID, SparkLowLevel.MotorType.kBrushless);

        //Conseguimos los encoders
        DriveMotorEncoder = DriveMotor.getEncoder();
        SteeringMotorEncoder = SteeringMotor.getEncoder();

        //Creamos el encoder absoluto
        AbsoluteEncoder = new CANcoder(EncoderID, CANivore);

        //creamos las configuraciones 
        DriveMotorConfig = new SparkMaxConfig();
        SteeringMotorConfig = new SparkMaxConfig();

        //hacemos que los motores esten en brake
        DriveMotorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        SteeringMotorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        
        //invertimos si es necesario los motores
        DriveMotorConfig.inverted(DriveMotorReversed);
        SteeringMotorConfig.inverted(SteeringMotorReversed);

        // Sacamos convesion de posicion para los encoders
        DriveMotorConfig.absoluteEncoder.positionConversionFactor(ModuleConstants.kDriveEncoderRot2Meter);
        SteeringMotorConfig.absoluteEncoder.positionConversionFactor(ModuleConstants.kSteeringEncoderRot2Rad);

        //sacamos la conversion de velocidad para los motores
        DriveMotorConfig.absoluteEncoder.velocityConversionFactor(ModuleConstants.kDriveEncoderRPM2MeterPerSec);
        SteeringMotorConfig.absoluteEncoder.velocityConversionFactor(ModuleConstants.kSteeringEncoderRPM2RadPerSec);

        //aplciamos la configuracion a los motores
        DriveMotor.configure(DriveMotorConfig, SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kPersistParameters);
        SteeringMotor.configure(SteeringMotorConfig, SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kPersistParameters);

        //creamos el PID
        steeringPidController = new PIDController(ModuleConstants.kPSteering, ModuleConstants.kISteering, ModuleConstants.KDSteering);

        //Activamos que el PID pueda continuar con un flujo constante
        steeringPidController.enableContinuousInput(-Math.PI, Math.PI);

    }

    public double getDrivePosition(){

        return DriveMotorEncoder.getPosition();

    }

    public double getSteeringPosition(){

        return SteeringMotorEncoder.getPosition();

    }

    public double getDriveVelocity(){

        return DriveMotorEncoder.getVelocity();

    }

    public double getSteeringVelocity(){

        return SteeringMotorEncoder.getVelocity();

    }

    public double getAbsolutePositionDegrees(){
        double angle = AbsoluteEncoder.getAbsolutePosition().getValueAsDouble();
        
        double radOffSet = AbsoluteEncoderOffSet;

        angle *= 360;

        radOffSet *= 360;

        angle -= radOffSet;

        return angle * (AbsoluteEncoderReversed ? -1.0 : 1.0);
    }


    public double getAbsolutePosition(){

        double angle = AbsoluteEncoder.getAbsolutePosition().getValueAsDouble();
        
        double radOffSet = AbsoluteEncoderOffSet;

        angle *= 2;
        angle *= Math.PI;

        radOffSet *= 2;
        radOffSet *= Math.PI;

        angle -= radOffSet;

        return angle * (AbsoluteEncoderReversed ? -1.0 : 1.0);

    }

    public void ResetEncoders(){

        double encoderPosition = getAbsolutePosition();

        DriveMotorEncoder.setPosition(0);
        SteeringMotorEncoder.setPosition(encoderPosition);

        SmartDashboard.putNumber("Encoder: "+ absoluteEncoderID +" ", encoderPosition);

    }

    public SwerveModuleState getState(){

        SwerveModuleState State = new SwerveModuleState(getDriveVelocity(), new Rotation2d(getSteeringPosition()));

        return State;

    }

    public SwerveModulePosition getPosition(){

        SwerveModulePosition Position = new SwerveModulePosition(getDrivePosition(), new Rotation2d(getSteeringPosition()));

        return Position;

    }

    public void Stop(){

        DriveMotor.set(0);
        SteeringMotor.set(0);

    }

    public void SetDesireState( SwerveModuleState state ){

        double PhysicalMaxSpeed = DriveConstants.kPhysicalMaxSpeedMetersPerSecond;

        if ( Math.abs( state.speedMetersPerSecond ) < 0.001 ){

            Stop();
            return;

        }

        // state.optimize(getState().angle);

        //double StateAngleRad = state.angle.getRadians();
        double StateAngleRad = 0;

        double StateVelocity = state.speedMetersPerSecond;

        double DriveMotorState = StateVelocity / PhysicalMaxSpeed;

        DriveMotor.set( DriveMotorState );
        SteeringMotor.set(steeringPidController.calculate(getSteeringPosition(), StateAngleRad ));

    }
}

/**
 * Minimal local stub for CANcoder to allow compilation when the CTRE library is not present.
 * This stub implements only the small subset of the API used by SwerveModule.
 * Replace this with the real CTRE CANcoder class by adding the appropriate CTRE dependency
 * when you want actual hardware behavior.
 */
class CANcoder {
    private final int id;
    private final String canivore;

    public CANcoder(int id, String canivore) {
        this.id = id;
        this.canivore = canivore;
    }

    public AbsolutePosition getAbsolutePosition() {
        return new AbsolutePosition(0.0);
    }

    public static class AbsolutePosition {
        private final double value;

        public AbsolutePosition(double value) {
            this.value = value;
        }

        public double getValueAsDouble() {
            return value;
        }
    }
}
