package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import frc.robot.Constants.DriveConstants;

public class ClawSubsystem extends SubsystemBase {
    private final SparkMax clawLeft;
    private final SparkMax clawRight;
    private boolean isRunning = false;
    private String currentAction = "STOP";
    private double currentSpeed = 0;

    public ClawSubsystem() {
        System.out.println("=== INICIALIZANDO GARRAS (SPARK 11 y 12) ===");
        
        clawLeft = new SparkMax(DriveConstants.kClawLeftMotorPort, MotorType.kBrushless);
        clawRight = new SparkMax(DriveConstants.kClawRightMotorPort, MotorType.kBrushless);
        
        configMotors();
        
        System.out.println("✅ Garras inicializadas");
    }

    private void configMotors() {
        // Configuración motor izquierdo
        SparkMaxConfig leftConfig = new SparkMaxConfig();
        leftConfig.inverted(DriveConstants.kClawLeftReversed)
                  .idleMode(IdleMode.kBrake)
                  .smartCurrentLimit(30)
                  .openLoopRampRate(0.5);
        clawLeft.configure(leftConfig, SparkMax.ResetMode.kResetSafeParameters,
                SparkMax.PersistMode.kNoPersistParameters);

        // Configuración motor derecho
        SparkMaxConfig rightConfig = new SparkMaxConfig();
        rightConfig.inverted(DriveConstants.kClawRightReversed)
                  .idleMode(IdleMode.kBrake)
                  .smartCurrentLimit(30)
                  .openLoopRampRate(0.5);
        clawRight.configure(rightConfig, SparkMax.ResetMode.kResetSafeParameters,
                SparkMax.PersistMode.kNoPersistParameters);
    }

    /**
     * AGARRAR/SUBIR - Ambos motores giran en dirección positiva
     * Velocidad: +30%
     */
    public void grab() {
        double speed = DriveConstants.kClawGrabSpeed;
        System.out.println("🔧 AGARRANDO/SUBIR al " + (speed * 100) + "%");
        clawLeft.set(speed);
        clawRight.set(speed);
        isRunning = true;
        currentAction = "GRAB";
        currentSpeed = speed;
    }

    /**
     * SOLTAR/BAJAR - Ambos motores giran en dirección negativa
     * Velocidad: -30%
     */
    public void release() {
        double speed = DriveConstants.kClawReleaseSpeed;
        System.out.println("🔧 SOLTANDO/BAJAR al " + (Math.abs(speed) * 100) + "%");
        clawLeft.set(speed);
        clawRight.set(speed);
        isRunning = true;
        currentAction = "RELEASE";
        currentSpeed = speed;
    }

    /**
     * Detiene las garras
     */
    public void stop() {
        System.out.println("⏹️ GARRAS DETENIDAS");
        clawLeft.set(0);
        clawRight.set(0);
        isRunning = false;
        currentAction = "STOP";
        currentSpeed = 0;
    }

    /**
     * Prueba individual (para diagnóstico)
     */
    public void testLeft(double speed) {
        clawLeft.set(speed);
        clawRight.set(0);
        System.out.println("Probando garra izquierda: " + speed);
    }

    public void testRight(double speed) {
        clawLeft.set(0);
        clawRight.set(speed);
        System.out.println("Probando garra derecha: " + speed);
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Claw/Running", isRunning);
        SmartDashboard.putString("Claw/Action", currentAction);
        SmartDashboard.putNumber("Claw/Speed", currentSpeed);
    }
}