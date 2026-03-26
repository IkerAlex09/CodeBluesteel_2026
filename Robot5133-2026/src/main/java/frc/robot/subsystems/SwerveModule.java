package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ModuleConstants;

public class SwerveModule {

    private final SparkMax driveMotor;
    private final SparkMax turningMotor;
    private final RelativeEncoder driveEncoder;
    private final RelativeEncoder turningEncoder;
    private final PIDController turningPidController;
    private final CANcoder absoluteEncoder;
    
    private final boolean absoluteEncoderReversed;
    private final double absoluteEncoderOffsetRad;
    private final int absoluteEncoderId;
    private static final String CAN_BUS_NAME = "5133BlueSteel";

    public SwerveModule(int driveMotorId, int turningMotorId, boolean driveMotorReversed, 
                        boolean turningMotorReversed, int absoluteEncoderId, 
                        double absoluteEncoderOffset, boolean absoluteEncoderReversed) {

        this.absoluteEncoderOffsetRad = absoluteEncoderOffset;
        this.absoluteEncoderReversed = absoluteEncoderReversed;
        this.absoluteEncoderId = absoluteEncoderId;
        
        // Inicializar CANcoder
        absoluteEncoder = new CANcoder(absoluteEncoderId, CAN_BUS_NAME);

        // Inicializar motores SparkMax
        driveMotor = new SparkMax(driveMotorId, MotorType.kBrushless);
        turningMotor = new SparkMax(turningMotorId, MotorType.kBrushless);

        // Configurar inversiones
        driveMotor.setInverted(driveMotorReversed);
        turningMotor.setInverted(turningMotorReversed);

        // Obtener encoders
        driveEncoder = driveMotor.getEncoder();
        turningEncoder = turningMotor.getEncoder();

        // NOTA: En REVLib 2026, NO existen setPositionConversionFactor
        // Los valores los convertiremos manualmente en los getters

        // Configurar PID Controller
        turningPidController = new PIDController(ModuleConstants.kPTurning, 0, 0);
        turningPidController.enableContinuousInput(-Math.PI, Math.PI);

        // Resetear encoders
        resetEncoders();

        System.out.println("Módulo CANcoder " + absoluteEncoderId + " inicializado");
    }

    public SwerveModulePosition getPosition() {
        // Aplicar conversión manualmente
        double drivePosition = driveEncoder.getPosition() * ModuleConstants.kDriveEncoderRot2Meter;
        double turningPosition = turningEncoder.getPosition() * ModuleConstants.kTurningEncoderRot2Rad;
        
        return new SwerveModulePosition(
            drivePosition, 
            new Rotation2d(turningPosition)
        );
    }

    public double getDrivePosition() {
        return driveEncoder.getPosition() * ModuleConstants.kDriveEncoderRot2Meter;
    }

    public double getTurningPosition() {
        return turningEncoder.getPosition() * ModuleConstants.kTurningEncoderRot2Rad;
    }

    public double getDriveVelocity() {
        return driveEncoder.getVelocity() * ModuleConstants.kDriveEncoderRPM2MeterPerSec;
    }

    public double getTurningVelocity() {
        return turningEncoder.getVelocity() * ModuleConstants.kTurningEncoderRPM2RadPerSec;
    }

    public double getAbsoluteEncoderRad() {
        double angle = absoluteEncoder.getAbsolutePosition().getValueAsDouble();
        angle *= 2.0 * Math.PI;
        angle -= absoluteEncoderOffsetRad * 2.0 * Math.PI;
        return angle * (absoluteEncoderReversed ? -1.0 : 1.0);
    }

    public void resetEncoders() {
        driveEncoder.setPosition(0);
        // El encoder de giro se resetea al valor absoluto (convertido)
        turningEncoder.setPosition(getAbsoluteEncoderRad() / ModuleConstants.kTurningEncoderRot2Rad);
    }

    public SwerveModuleState getState() {
        return new SwerveModuleState(
            getDriveVelocity(), 
            new Rotation2d(getTurningPosition())
        );
    }

    public void setDesiredState(SwerveModuleState state) {
    if (Math.abs(state.speedMetersPerSecond) < 0.001) {
        stop();
        return;
    }
    
    // Debug para ver qué recibe cada módulo
    if (Math.abs(state.speedMetersPerSecond) > 0.01) {
        System.out.println("Módulo " + absoluteEncoderId + 
                          " recibe: Vel=" + state.speedMetersPerSecond + 
                          " Ángulo=" + state.angle.getDegrees() + "°");
    }
    
    state = SwerveModuleState.optimize(state, getState().angle);
    
    driveMotor.set(state.speedMetersPerSecond / DriveConstants.kPhysicalMaxSpeedMetersPerSecond);
    
    double pidOutput = turningPidController.calculate(getTurningPosition(), state.angle.getRadians());
    turningMotor.set(pidOutput);
}
    /*
    public void setDesiredState(SwerveModuleState state) {
        if (Math.abs(state.speedMetersPerSecond) < 0.001) {
            stop();
            return;
        }
        
        state = SwerveModuleState.optimize(state, getState().angle);
        
        driveMotor.set(state.speedMetersPerSecond / DriveConstants.kPhysicalMaxSpeedMetersPerSecond);
        
        double pidOutput = turningPidController.calculate(getTurningPosition(), state.angle.getRadians());
        turningMotor.set(pidOutput);

        SmartDashboard.putNumber("Speed " + absoluteEncoderId, state.speedMetersPerSecond);
        SmartDashboard.putNumber("Swerve[" + absoluteEncoderId + "] target", state.angle.getDegrees());
        SmartDashboard.putNumber("Swerve[" + absoluteEncoderId + "] current", Math.toDegrees(getTurningPosition()));
    }*/

    public void stop() {
        driveMotor.set(0);
        turningMotor.set(0);
    }
}