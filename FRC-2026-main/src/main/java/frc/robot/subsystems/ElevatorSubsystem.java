package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ElevatorConstants;

public class ElevatorSubsystem extends SubsystemBase {
    private final SparkMax leftElevator;
    private final SparkMax rightElevator;

    public ElevatorSubsystem() {
        leftElevator = new SparkMax(ElevatorConstants.ELEVATOR_LEFT, MotorType.kBrushless);
        rightElevator = new SparkMax(ElevatorConstants.ELEVATOR_RIGHT, MotorType.kBrushless);
    }

    public void Updown() {
        leftElevator.set(ElevatorConstants.ELEVATOR_SPEED);
        rightElevator.set(ElevatorConstants.ELEVATOR_SPEED);
    }
    public void Downup() {
        leftElevator.set(-ElevatorConstants.ELEVATOR_SPEED);
        rightElevator.set(-ElevatorConstants.ELEVATOR_SPEED);
    }

    public void stop() {
        leftElevator.set(0);
        rightElevator.set(0);
    }
}