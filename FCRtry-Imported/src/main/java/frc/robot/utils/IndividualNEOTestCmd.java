package frc.robot.utils;

// REV Robotics library not available here; local stub implementations are provided at the end of this file.

// Using a local CommandBase stub below instead of importing WPILib's CommandBase.

public class IndividualNEOTestCmd extends CommandBase {

    private CANSparkMax testMotor;

    private double velocity;

    public IndividualNEOTestCmd(
        int id,
        CANSparkMaxLowLevel.MotorType motorType,
        double velocity
    ){
        this.testMotor = new CANSparkMax(id, motorType);
        this.velocity = velocity;
    }

    @Override
    public void initialize(){

    }

    @Override
    public void execute(){

        testMotor.set(velocity);

    }

    @Override 
    public void end(boolean interrupted){

        testMotor.set(0);

    }

    @Override
    public boolean isFinished(){

        return false;

    }
    
}

class CommandBase {
    public void initialize() {}
    public void execute() {}
    public void end(boolean interrupted) {}
    public boolean isFinished() { return false; }
}

class CANSparkMax {
    private int id;
    private CANSparkMaxLowLevel.MotorType motorType;
    public CANSparkMax(int id, CANSparkMaxLowLevel.MotorType motorType) {
        this.id = id;
        this.motorType = motorType;
    }
    public void set(double value) { /* no-op stub */ }
}

class CANSparkMaxLowLevel {
    public enum MotorType { kBrushless, kBrushed }
}
