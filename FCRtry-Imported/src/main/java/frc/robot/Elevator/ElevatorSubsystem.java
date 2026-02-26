package frc.robot.Elevator;

// Removed external REV dependency; added lightweight local stubs below to allow compilation without the REV library.

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ElevatorSubsystem extends SubsystemBase {

    private final CANSparkMax elevatorSpark, pivotspark;

    public ElevatorSubsystem ( int elevatorId, int pivotId) {

        this.elevatorSpark = new CANSparkMax(elevatorId, MotorType.kBrushed);

        this.pivotspark = new CANSparkMax(pivotId, MotorType.kBrushed);

    }

    public void setElevator( double speed ){

        this.elevatorSpark.set(speed);

    }

    public void stopElevator () {

        this.elevatorSpark.set(0);

    }

    public void setPivot ( double speed ){

        this.pivotspark.set(speed);

    }

    public void stopPivot(){

        this.pivotspark.set(0);

    }

    public void stopAll(){

        this.elevatorSpark.set(0);
        this.pivotspark.set(0);

    }

}

// Lightweight local stubs for REV CANSparkMax API (compile-only).
class CANSparkMax {
    private final int deviceId;
    private final MotorType motorType;

    public CANSparkMax(int deviceId, MotorType motorType) {
        this.deviceId = deviceId;
        this.motorType = motorType;
    }

    public void set(double speed) {
        // no-op stub for compilation; replace with REV library for real hardware
    }
}

enum MotorType {
    kBrushed,
    kBrushless
}
     