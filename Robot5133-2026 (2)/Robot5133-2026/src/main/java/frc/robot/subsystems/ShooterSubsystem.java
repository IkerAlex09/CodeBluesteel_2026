package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ShooterConstants;

public class ShooterSubsystem extends SubsystemBase {
    private final SparkMax shooterLeft;
    private final SparkMax shooterRight;
    private double currentSpeed = 0;
    private boolean isRunning = false;

    public ShooterSubsystem() {
        System.out.println("=== INICIALIZANDO DISPARADOR ===");
        
        shooterLeft = new SparkMax(DriveConstants.kShooterMotorLeftPort, MotorType.kBrushless);
        shooterRight = new SparkMax(DriveConstants.kShooterMotorRightPort, MotorType.kBrushless);
        configMotors();
        
        System.out.println("✅ Disparador inicializado");
    }

    private void configMotors() {
        SparkMaxConfig leftConfig = new SparkMaxConfig();
        leftConfig.inverted(DriveConstants.kShooterLeftReversed)
                  .idleMode(IdleMode.kCoast)
                  .smartCurrentLimit(40)
                  .openLoopRampRate(ShooterConstants.kShooterRampTime);
        shooterLeft.configure(leftConfig, SparkMax.ResetMode.kResetSafeParameters,
                SparkMax.PersistMode.kNoPersistParameters);

        SparkMaxConfig rightConfig = new SparkMaxConfig();
        rightConfig.inverted(DriveConstants.kShooterRightReversed)
                  .idleMode(IdleMode.kCoast)
                  .smartCurrentLimit(40)
                  .openLoopRampRate(ShooterConstants.kShooterRampTime);
        shooterRight.configure(rightConfig, SparkMax.ResetMode.kResetSafeParameters,
                SparkMax.PersistMode.kNoPersistParameters);
    }

    public void shootHigh() {
        System.out.println("🎯 DISPARANDO 100%");
        shooterLeft.set(ShooterConstants.kShooterSpeedHigh);
        shooterRight.set(ShooterConstants.kShooterSpeedHigh);
        currentSpeed = ShooterConstants.kShooterSpeedHigh;
        isRunning = true;
    }

    public void intake() {
        System.out.println("📥 ABSORBIENDO 76%");
        shooterLeft.set(ShooterConstants.kIntakeSpeed);
        shooterRight.set(-ShooterConstants.kIntakeSpeed);
        currentSpeed = ShooterConstants.kIntakeSpeed;
        isRunning = true;
    }

    public void reverseIntake() {
        System.out.println("📤 EXPULSANDO 76%");
        shooterLeft.set(-ShooterConstants.kIntakeSpeed);
        shooterRight.set(ShooterConstants.kIntakeSpeed);
        currentSpeed = ShooterConstants.kIntakeSpeed;
        isRunning = true;
    }

    public void stop() {
        shooterLeft.set(0);
        shooterRight.set(0);
        currentSpeed = 0;
        isRunning = false;
        System.out.println("⏹️ Disparador detenido");
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Shooter/Running", isRunning);
        SmartDashboard.putNumber("Shooter/Speed", currentSpeed);
    }
}

/*package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ShooterConstants;

public class ShooterSubsystem extends SubsystemBase {
    private final SparkMax shooterLeft;
    private final SparkMax shooterRight;

    private double currentSpeed = 0;
    private boolean isRunning = false;

    public ShooterSubsystem() {
        System.out.println("=== INICIALIZANDO DISPARADOR (SPARK 9 y 10) ===");

        // Inicializar motores
        shooterLeft = new SparkMax(DriveConstants.kShooterMotorLeftPort, MotorType.kBrushless);
        shooterRight = new SparkMax(DriveConstants.kShooterMotorRightPort, MotorType.kBrushless);

        // Configurar motores
        configMotors();

        System.out.println("✅ Disparador inicializado correctamente");
    }

    private void configMotors() {
        // Configuración motor izquierdo
        SparkMaxConfig leftConfig = new SparkMaxConfig();
        leftConfig
                .inverted(DriveConstants.kShooterLeftReversed)
                .idleMode(IdleMode.kCoast) // Coast para que gire libremente
                .smartCurrentLimit(40)
                .openLoopRampRate(ShooterConstants.kShooterRampTime);

        shooterLeft.configure(leftConfig, SparkMax.ResetMode.kResetSafeParameters,
                SparkMax.PersistMode.kNoPersistParameters);

        // Configuración motor derecho (normalmente invertido)
        SparkMaxConfig rightConfig = new SparkMaxConfig();
        rightConfig
                .inverted(DriveConstants.kShooterRightReversed)
                .idleMode(IdleMode.kCoast)
                .smartCurrentLimit(40)
                .openLoopRampRate(ShooterConstants.kShooterRampTime);

        shooterRight.configure(rightConfig, SparkMax.ResetMode.kResetSafeParameters,
                SparkMax.PersistMode.kNoPersistParameters);

        System.out.println("Motores del disparador configurados");
    }

    // Métodos de control

    public void shootLow() {
        setSpeed(ShooterConstants.kShooterSpeedLow);
        System.out.println("🔫 Disparador LOW: " + (ShooterConstants.kShooterSpeedLow * 100) + "%");
    }

    public void shootMedium() {
        setSpeed(ShooterConstants.kShooterSpeedMedium);
        System.out.println("🔫 Disparador MEDIUM: " + (ShooterConstants.kShooterSpeedMedium * 100) + "%");
    }

    public void shootHigh() {
        setSpeed(ShooterConstants.kShooterSpeedHigh);
        System.out.println("🔫 Disparador HIGH: " + (ShooterConstants.kShooterSpeedHigh * 100) + "%");
    }

    public void setSpeed(double speed) {
        currentSpeed = speed;
        shooterLeft.set(speed);
        shooterRight.set(speed);
        isRunning = (speed != 0);
    }

    public void stop() {
        setSpeed(0);
        System.out.println("⏹️ Disparador detenido");
    }

    public boolean isRunning() {
        return isRunning;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    // Prueba individual de motores
    public void testLeftMotor(double speed) {
        shooterLeft.set(speed);
        shooterRight.set(0);
        System.out.println("Probando motor izquierdo: " + speed);
    }

    public void testRightMotor(double speed) {
        shooterLeft.set(0);
        shooterRight.set(speed);
        System.out.println("Probando motor derecho: " + speed);
    }

    // Método para que ambos motores giren en la MISMA dirección
    // Método para que ambos motores giren en la MISMA dirección
public void shootSameDirection(double speed) {
    shootHigh();
    System.out.println("🔫 Ambos motores misma dirección: " + (speed * 100) + "%");
    
    // Para que ambos vayan en la misma dirección, ignoramos la inversión
    // y aplicamos la misma potencia a ambos
    shooterLeft.set(speed);
    shooterRight.set(-speed);
    
    currentSpeed = speed;
    isRunning = (speed != 0);
}

// También puedes agregar una versión para que ambos vayan hacia atrás
public void shootSameDirectionReverse(double speed) {
    shootHigh();
    System.out.println("🔫 Ambos motores hacia atrás: " + (speed * 100) + "%");
    shooterLeft.set(-speed);
    shooterRight.set(-speed);
    
    currentSpeed = -speed;
    isRunning = (speed != 0);
}

    @Override
    public void periodic() {
        // Mostrar estado en SmartDashboard
        SmartDashboard.putBoolean("Shooter/Running", isRunning);
        SmartDashboard.putNumber("Shooter/Speed", currentSpeed);
        SmartDashboard.putNumber("Shooter/Left RPM", shooterLeft.getEncoder().getVelocity());
        SmartDashboard.putNumber("Shooter/Right RPM", shooterRight.getEncoder().getVelocity());
    }
}*/