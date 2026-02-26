package frc.robot.Elevator;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.Constants.OIConstants;



public class ElevatorCmd extends Command {

    private final ElevatorSubsystem elevatorSubsystem ;
    private final Supplier<Double> elevatorSupplier, pivotSupplier;


    public ElevatorCmd ( Supplier<Double> elevatorSupplier, Supplier<Double> pivotSupplier, ElevatorSubsystem elevatorSubsystem){

        this.elevatorSubsystem = elevatorSubsystem;

        this.elevatorSupplier = elevatorSupplier;
        this.pivotSupplier = pivotSupplier;
        addRequirements(elevatorSubsystem);
    }



    @Override
    public void initialize(){

       // elevatorSubsystem.stopAll();

    }

    @Override
    public void execute(){


        double elevatorSpeed = -elevatorSupplier.get();

        double pivotSpeed = -pivotSupplier.get();

        double elevatorDeadbandSpeed = Math.abs(elevatorSpeed) > OIConstants.kDeadband ? elevatorSpeed : 0;

        double pivotDeadbandSpeed = Math.abs(pivotSpeed) > OIConstants.kDeadband ? pivotSpeed : 0;

        double elevatorLimitSpeed = elevatorDeadbandSpeed * ElevatorConstants.ElevatorMaxSpeed;

        double pivotLimitSpeed = pivotDeadbandSpeed * ElevatorConstants.PivotMaxSpeed;

        elevatorSubsystem.setElevator(elevatorLimitSpeed);

        elevatorSubsystem.setPivot(pivotLimitSpeed);

    }

    @Override 
    public void end(boolean interrupted){

        elevatorSubsystem.stopAll();

    }

    @Override
    public boolean isFinished(){

        return false;

    }

}