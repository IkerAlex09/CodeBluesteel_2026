package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.DriveConstants;

public class SwerveModule {
    private final SparkMax driveMotor;
    private final SparkMax turnMotor;
    private final PIDController turnPIDController;
    private final double encoderOffset;

    public SwerveModule(int driveCANId, int turnCANId, double encoderOffset) {
        driveMotor = new SparkMax(driveCANId, MotorType.kBrushless);
        turnMotor = new SparkMax(turnCANId, MotorType.kBrushless);

        // Configuración de los motores
        SparkMaxConfig driveConfig = new SparkMaxConfig();
        driveConfig.idleMode(IdleMode.kBrake);
        driveMotor.configure(driveConfig, (com.revrobotics.spark.SparkBase.ResetMode) null, (com.revrobotics.spark.SparkBase.PersistMode) null);

        SparkMaxConfig turnConfig = new SparkMaxConfig();
        turnConfig.idleMode(IdleMode.kBrake);
        turnMotor.configure(turnConfig, (com.revrobotics.spark.SparkBase.ResetMode) null, (com.revrobotics.spark.SparkBase.PersistMode) null);

        this.encoderOffset = encoderOffset;
        this.turnPIDController = new PIDController(0.01, 0, 0);
        this.turnPIDController.enableContinuousInput(-Math.PI, Math.PI);
    }

    public SwerveModuleState getState() {
        // Aquí deberías leer el encoder absoluto (CANCoder) para obtener el ángulo
        double angle = getAbsoluteEncoderAngle(); // Implementa esto con tu encoder CTRE
        return new SwerveModuleState(driveMotor.getEncoder().getVelocity(), new Rotation2d(angle));
    }

    public void setDesiredState(SwerveModuleState desiredState) {
        SwerveModuleState optimizedState = SwerveModuleState.optimize(desiredState, getState().angle);

        driveMotor.set(optimizedState.speedMetersPerSecond / DriveConstants.MAX_SPEED_METERS_PER_SECOND);

        double turnOutput = turnPIDController.calculate(getState().angle.getRadians(), optimizedState.angle.getRadians());
        turnMotor.set(turnOutput);

        SmartDashboard.putNumber("Modulo " + driveMotor.getDeviceId() + " target angle", optimizedState.angle.getDegrees());
    }

    private double getAbsoluteEncoderAngle() {
        // Implementa aquí la lectura del encoder CANCoder (CTRE)
        // Retorna el ángulo en radianes, restando el offset
        return 0.0;
    }
}