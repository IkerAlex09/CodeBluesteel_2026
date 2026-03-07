package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class ShooterSubsystem extends SubsystemBase {
    private final SparkMax leftShooter;
    private final SparkMax rightShooter;

    public ShooterSubsystem() {
        leftShooter = new SparkMax(ShooterConstants.SHOOTER_LEFT, MotorType.kBrushless);
        rightShooter = new SparkMax(ShooterConstants.SHOOTER_RIGHT, MotorType.kBrushless);
    }

    public void shoot() {
        leftShooter.set(ShooterConstants.SHOOTER_SPEED);
        rightShooter.set(ShooterConstants.SHOOTER_SPEED);
    }
    public void vacum() {
        leftShooter.set(ShooterConstants.SHOOTER_SPEED);
        rightShooter.set(-ShooterConstants.SHOOTER_SPEED);
    }

    public void stop() {
        leftShooter.set(0);
        rightShooter.set(0);
    }
}